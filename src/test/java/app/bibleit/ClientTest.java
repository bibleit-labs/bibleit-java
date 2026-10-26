package app.bibleit;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UncheckedIOException;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientTest {
    static Response response(int status, String json, Map<String, List<String>> headers) {
        return new Response(status, HttpHeaders.of(headers, (k,v) -> true), ProtocolTest.wire(json));
    }
    @Test void everyOperationHasMatchingMethodAndPath() throws Exception {
        var spec = Json.MAPPER.readValue(Files.readString(Path.of("contract/openapi.json")), TestFixtures.ApiDocument.class);
        var calls = new AtomicInteger();
        var client = new BibleitClient("bt_fixture", request -> {
            calls.incrementAndGet(); assertEquals("api.bibleit.app", request.uri().getHost());
            assertEquals("Bearer bt_fixture", request.headers().firstValue("Authorization").orElseThrow());
            return response(200, "{}", Map.of());
        });
        for (var path : spec.paths().entrySet()) for (var verb : path.getValue().operations().entrySet()) {
            var op = verb.getValue(); if (op.operationId() == null) continue;
            String name = op.operationId();
            var params = java.util.regex.Pattern.compile("\\{([^}]+)}").matcher(path.getKey());
            var values = new ArrayList<Object>(); var types = new ArrayList<Class<?>>();
            while (params.find()) { values.add("fixture"); types.add(String.class); }
            types.add(Options.class); values.add(Options.defaults());
            var method = BibleitClient.class.getMethod(name, types.toArray(Class[]::new));
            try (var res = (Response) method.invoke(client, values.toArray())) { assertEquals(200, res.statusCode()); }
            assertEquals(verb.getKey().toUpperCase(Locale.ROOT), BibleitClient.operations().get(name).method());
        }
        assertEquals(95, calls.get()); assertEquals(95, BibleitClient.operationNames().size());
    }
    @Test void validatesAndEncodesPathsAndOptions() throws Exception {
        var captured = new AtomicReference<HttpRequest>();
        var client = new BibleitClient("", r -> { captured.set(r); return response(200, "{}", Map.of()); });
        try (var res = client.getLive("space?#%", Options.builder().ifMatch("\"v1\"").destinationToken("svc_destination").build())) {
            assertEquals("https://api.bibleit.app/v1/lives/space%3F%23%25", captured.get().uri().toString());
            assertEquals("\"v1\"", captured.get().headers().firstValue("If-Match").orElseThrow());
            assertEquals("svc_destination", captured.get().headers().firstValue("X-Destination-Service-Token").orElseThrow());
            assertTrue(captured.get().headers().firstValue("Authorization").isEmpty());
        }
        for (String id : List.of("", "..", ".", "a/b", "a\\b", "a\n")) assertThrows(IllegalArgumentException.class, () -> client.getLive(id));
        assertThrows(IllegalArgumentException.class, () -> new BibleitClient(" token"));
        assertThrows(IllegalArgumentException.class, () -> Options.builder().json(Map.of()).body(ProtocolTest.wire("x"),1,"text/plain").build());
        assertThrows(IllegalArgumentException.class, () -> Options.builder().body(ProtocolTest.wire("x"),-1,"text/plain").build());
    }
    @Test void typedQueriesEncodeAndValidateScalarValues() throws Exception {
        var captured = new AtomicReference<HttpRequest>();
        var client = new BibleitClient("", request -> { captured.set(request); return response(200,"{}",Map.of()); });
        var query = Queries.SearchPersonalScriptureQuery.builder().query("a &b").limit(2).build();
        try (var ignored = client.searchPersonalScripture("account","web",query)) {
            assertEquals("q=a%20%26b&limit=2",captured.get().uri().getRawQuery());
        }
        assertEquals("a &b",query.getQuery());
        assertThrows(IllegalArgumentException.class, () -> Queries.ReadPersonalBookQuery.builder().limit(501).build());
        assertThrows(IllegalArgumentException.class, () -> Queries.GetLiveEventsQuery.builder().waitSeconds(26).build());
        var upload = Queries.UploadPresentationAssetQuery.builder().kind(Queries.UploadPresentationAssetQuery.Kind.IMAGE).build();
        assertEquals("image",upload.parameters().getFirst().value());
    }
    @Test void failuresCloseBodiesPreserveMetadataAndDoNotRetry() {
        var closed = new AtomicBoolean(); var calls = new AtomicInteger();
        var client = new BibleitClient("bt_fixture", r -> {
            calls.incrementAndGet();
            return new Response(429, HttpHeaders.of(Map.of("Retry-After",List.of("3"),"X-Request-Id",List.of("req")),(k,v)->true),
                new ByteArrayInputStream("{\"code\":\"future_code\",\"detail\":\"private\"}".getBytes(StandardCharsets.UTF_8)) {
                    @Override public void close() { closed.set(true); }
                });
        });
        var e = assertThrows(ProblemException.class, client::getPersonalIdentity);
        assertEquals(429,e.status()); assertEquals("future_code",e.code()); assertEquals("req",e.requestId());
        assertEquals(3,e.retryAfter().toSeconds()); assertFalse(e.getMessage().contains("private"));
        assertTrue(closed.get()); assertEquals(1,calls.get());
    }
    @Test void responseLimitsAndTrailingJson() {
        assertThrows(IOException.class, () -> response(200,"{} {}",Map.of()).decode(Models.EmptyObject.class));
        var body = new ByteArrayInputStream(new byte[Response.MAX_JSON_BYTES + 1]);
        assertThrows(IOException.class, () -> new Response(200,HttpHeaders.of(Map.of(),(k,v)->true),body).decode(Models.EmptyObject.class));
    }
    @Test void typedModelsPreservePresenceNullUnknownAndLargeIntegers() throws Exception {
        var json = "{\"id\":\"identity\",\"future\":9007199254740993,\"display_name\":null}";
        var identity = response(200,json,Map.of()).decode(Models.PersonalIdentity.class);
        assertEquals("identity",identity.getId());
        assertTrue(identity.hasField("display_name")); assertFalse(identity.hasField("missing"));
        assertEquals("9007199254740993",identity.additionalProperties().get("future").toString());
        assertEquals(Json.MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {}),
            Json.MAPPER.convertValue(identity, new TypeReference<Map<String, Object>>() {}));
        var create = new Models.CreateLive();
        assertEquals("{}",Json.MAPPER.writeValueAsString(create));
        create.setName("Service"); assertEquals("Service",create.getName());
        create.setName(null); assertTrue(create.hasField("name")); assertNull(create.getName());
        assertFalse(create.toString().contains("Service"));
    }
    static String page(String cursor, int verse) {
        return "{\"translation\":\"web\",\"rights\":{},\"verses\":[{\"book_id\":43,\"chapter\":3,\"verse\":"+verse+"}],\"next_cursor\":"+cursor+"}";
    }
    @Test void bookPagesAreLazyBoundedAndCloseBeforeYield() throws Exception {
        var calls = new AtomicInteger();
        var client = new BibleitClient("", r -> response(200,page(calls.getAndIncrement()==0 ? "\"next\"" : "null",calls.get()),Map.of()));
        var options = Options.defaults();
        var query = Queries.ReadPersonalBookQuery.builder().limit(1).build();
        var pages = client.readPersonalBookPages("account","web","43",query,options).iterator();
        assertEquals(0,calls.get()); assertNotNull(pages.next()); assertEquals(1,calls.get());
        assertNotNull(pages.next()); assertFalse(pages.hasNext()); assertEquals(2,calls.get());
        assertNull(query.getAfter());
    }
    @Test void paginationRejectsLoopsAndStopsAfterFailure() throws Exception {
        var client = new BibleitClient("",r -> response(200,page("\"same\"",1),Map.of()));
        var pages = client.readPersonalBookPages("account","web","43",Options.defaults()).iterator();
        assertNotNull(pages.next()); assertThrows(UncheckedIOException.class,pages::hasNext); assertFalse(pages.hasNext());
    }
    @Test void enhancedForLoopsAreLazyAndRepeatable() throws Exception {
        var calls = new AtomicInteger();
        var closed = new AtomicInteger();
        var client = new BibleitClient("", request -> {
            calls.incrementAndGet();
            boolean second = request.uri().getQuery().contains("after=");
            return new Response(200, HttpHeaders.of(Map.of(), (k,v) -> true),
                new ByteArrayInputStream(page(second ? "null" : "\"next\"", second ? 2 : 1).getBytes(StandardCharsets.UTF_8)) {
                    @Override public void close() { closed.incrementAndGet(); }
                });
        });
        var pages = client.readPersonalBookPages("account","web","43",Options.defaults());
        var iterator = pages.iterator();
        assertEquals(0,calls.get());
        assertTrue(iterator.hasNext()); assertTrue(iterator.hasNext());
        assertEquals(1,calls.get()); assertEquals(1,closed.get());
        var first = iterator.next();
        assertEquals("web",first.getTranslation());
        assertEquals(1,first.getVerses().getFirst().getVerse());
        for (var page : pages) { assertNotNull(page); break; }
        assertEquals(2,calls.get());
        int count = 0;
        for (var page : pages) { count++; assertEquals("web",page.getTranslation()); assertEquals(count,page.getVerses().getFirst().getVerse()); }
        assertEquals(2,count); assertEquals(4,calls.get()); assertEquals(4,closed.get());
        var empty = client.readPersonalBookPages("account","web","43",Options.defaults()).iterator();
        assertNotNull(empty.next()); assertNotNull(empty.next());
        assertFalse(empty.hasNext());
        assertThrows(NoSuchElementException.class,empty::next);
    }
    @Test void iteratorInterruptionPreservesInterruptFlag() {
        var client = new BibleitClient("",request -> { throw new InterruptedException("cancelled"); });
        var iterator = client.readPersonalBookPages("account","web","43",Options.defaults()).iterator();
        try {
            var failure = assertThrows(UncheckedIOException.class,iterator::hasNext);
            assertInstanceOf(InterruptedIOException.class,failure.getCause());
            assertTrue(Thread.currentThread().isInterrupted());
            assertFalse(iterator.hasNext());
        } finally { Thread.interrupted(); }
    }
    @Test void oauthHasSeparateOriginAndValidatedPkce() throws Exception {
        var requests = new ArrayList<HttpRequest>();
        var oauth = new OAuthClient(r -> { requests.add(r); return response(400,"{\"error\":\"invalid_grant\",\"error_description\":\"private\"}",Map.of()); });
        var e = assertThrows(OAuthException.class, () -> oauth.exchangeCode("client","code","https://example.com/callback","a".repeat(43)));
        assertEquals("invalid_grant",e.code()); assertEquals("private",e.description()); assertEquals(1,requests.size());
        assertEquals("bibleit.app",requests.getFirst().uri().getHost()); assertTrue(requests.getFirst().headers().firstValue("Authorization").isEmpty());
        var url = OAuthClient.authorizationUrl("client","https://example.com/callback","state","a".repeat(43),List.of("scripture.read"));
        assertTrue(url.toString().contains("code_challenge_method=S256"));
        assertThrows(IllegalArgumentException.class, () -> OAuthClient.authorizationUrl("client","callback","state","bad",List.of("scope")));
    }
    @Test void eventValidation() throws Exception {
        var valid = "{\"schema_version\":1,\"type\":\"stream.heartbeat\",\"resource_type\":\"live\",\"resource_id\":\"live\",\"cursor\":\"opaque\",\"data\":null}";
        assertEquals("opaque",new BibleitClient("",r -> response(200,valid,Map.of())).pollLiveEvents("live").getCursor());
        assertThrows(IOException.class, () -> new BibleitClient("",r -> response(200,valid,Map.of())).pollLiveEvents("other","",0));
        assertThrows(IllegalArgumentException.class, () -> new BibleitClient("").pollLiveEvents("live","",26));
    }
}
