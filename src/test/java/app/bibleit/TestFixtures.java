package app.bibleit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class TestFixtures {
    private TestFixtures() {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ExpectedRecord(String type, Map<String, String> fields) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ProtocolFixture(String name, String wire, List<ExpectedRecord> records,
        @JsonProperty("error_code") String errorCode,
        @JsonProperty("error_fields") Map<String, String> errorFields, boolean invalid) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ApiDocument(Map<String, ApiPathItem> paths) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ApiPathItem(ApiOperation get, ApiOperation post, ApiOperation put,
        ApiOperation patch, ApiOperation delete, ApiOperation head, ApiOperation options, ApiOperation trace) {
        Map<String, ApiOperation> operations() {
            var operations = new LinkedHashMap<String, ApiOperation>();
            if (get != null) operations.put("get", get);
            if (post != null) operations.put("post", post);
            if (put != null) operations.put("put", put);
            if (patch != null) operations.put("patch", patch);
            if (delete != null) operations.put("delete", delete);
            if (head != null) operations.put("head", head);
            if (options != null) operations.put("options", options);
            if (trace != null) operations.put("trace", trace);
            return operations;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ApiOperation(String operationId) {}

    static List<ProtocolFixture> responses() throws IOException {
        return Json.MAPPER.readValue(Files.readString(Path.of("contract/fixtures/responses-v1.json")), new TypeReference<>() {});
    }
}
