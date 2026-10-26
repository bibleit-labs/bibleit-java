package app.bibleit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import app.bibleit.SshModels.AccountInfo;
import app.bibleit.SshModels.CatalogBook;
import app.bibleit.SshModels.CatalogChapter;
import app.bibleit.SshModels.HelpEntry;
import app.bibleit.SshModels.HelpResult;
import app.bibleit.SshModels.IdentityInfo;
import app.bibleit.SshModels.LiveActionResult;
import app.bibleit.SshModels.LiveConnection;
import app.bibleit.SshModels.LiveList;
import app.bibleit.SshModels.LiveResult;
import app.bibleit.SshModels.LiveStack;
import app.bibleit.SshModels.LiveState;
import app.bibleit.SshModels.LiveStatistics;
import app.bibleit.SshModels.LiveVerse;
import app.bibleit.SshModels.LiveVerseResult;
import app.bibleit.SshModels.OrganizationApplicationResult;
import app.bibleit.SshModels.OrganizationDirectory;
import app.bibleit.SshModels.OrganizationDirectoryEntry;
import app.bibleit.SshModels.Quota;
import app.bibleit.SshModels.QuotaLimit;
import app.bibleit.SshModels.QuotaResult;
import app.bibleit.SshModels.Reading;
import app.bibleit.SshModels.ServerInfo;
import app.bibleit.SshModels.StackEntry;
import app.bibleit.SshModels.TokenList;
import app.bibleit.SshModels.TokenMetadata;
import app.bibleit.SshModels.TranslationCatalog;
import app.bibleit.SshModels.TranslationInfo;
import app.bibleit.SshModels.VerseText;

final class SshDecoders {
    private SshDecoders() {}
    static IOException invalid() { return new IOException("Invalid typed SSH response"); }
    static String text(Map<String, String> f, String k) { return f.getOrDefault(k, ""); }
    static String present(Map<String, String> f, String k) throws IOException { if (!f.containsKey(k)) throw invalid(); return f.get(k); }
    static String required(Map<String, String> f, String k) throws IOException { String s = present(f,k); if (s.isEmpty()) throw invalid(); return s; }
    static long number(Map<String, String> f, String k) throws IOException {
        try { long n = Long.parseLong(required(f,k)); if (n < 0) throw invalid(); return n; }
        catch (NumberFormatException e) { throw invalid(); }
    }
    static long positive(Map<String, String> f, String k) throws IOException { long n = number(f,k); if (n == 0) throw invalid(); return n; }
    static Long optionalNumber(Map<String, String> f, String k) throws IOException { return f.containsKey(k) ? number(f,k) : null; }
    static boolean bool(Map<String, String> f, String k) throws IOException {
        return switch (required(f,k)) { case "true" -> true; case "false" -> false; default -> throw invalid(); };
    }
    static Boolean optionalBool(Map<String, String> f, String k) throws IOException { return f.containsKey(k) ? bool(f,k) : null; }
    static List<String> csv(String s) { return s == null || s.isEmpty() ? List.of() : List.of(s.split(",",-1)); }
    static Map<String, String> header(CommandResponse r) throws IOException {
        if (r.records().isEmpty() || !r.records().getFirst().type().equals("ok")) throw invalid();
        return r.records().getFirst().fields();
    }
    static Map<String, String> single(CommandResponse r) throws IOException { if (r.records().size() != 1) throw invalid(); return header(r); }
    static Map<String, String> counted(CommandResponse r, String key, String... kinds) throws IOException {
        var h = header(r); if (number(h,key) != r.records().size()-1) throw invalid();
        var allowed = Set.of(kinds);
        for (var record : r.records().subList(1,r.records().size())) if (!allowed.contains(record.type())) throw invalid();
        return h;
    }
    static ServerInfo serverInfo(CommandResponse r) throws IOException {
        var f = single(r); return new ServerInfo(number(f,"protocol_version"),required(f,"version"),csv(f.get("capabilities")),r);
    }
    static IdentityInfo identity(CommandResponse r) throws IOException {
        var f = single(r); return new IdentityInfo(required(f,"actor"),text(f,"display_name"),text(f,"identity_name"),text(f,"auth_provider"),text(f,"key_fingerprint"),bool(f,"auth"),csv(f.get("roles")),csv(f.get("permissions")),r);
    }
    static HelpResult help(CommandResponse r) throws IOException {
        var h = counted(r,"commands","command"); var entries = new ArrayList<HelpEntry>();
        for (var record : r.records().subList(1,r.records().size())) {
            var f = record.fields(); entries.add(new HelpEntry(required(f,"usage"),required(f,"summary"),f.getOrDefault("auth","none"),f));
        }
        return new HelpResult(bool(h,"auth"),List.copyOf(entries),r);
    }
    static AccountInfo account(CommandResponse r) throws IOException {
        var f = single(r); return new AccountInfo(required(f,"actor"),text(f,"display_name"),text(f,"handle"),text(f,"avatar_url"),text(f,"locale"),text(f,"theme"),required(f,"plan"),text(f,"plan_name"),required(f,"plan_source"),number(f,"plan_activated_at"),number(f,"lives"),number(f,"translations"),number(f,"tokens"),number(f,"keys"),r);
    }
    static QuotaResult quotas(CommandResponse r) throws IOException {
        counted(r,"count","quota"); var entries = new ArrayList<Quota>();
        for (var record : r.records().subList(1,r.records().size())) {
            var f = record.fields(); boolean unlimited = text(f,"limit").equals("unlimited");
            entries.add(new Quota(required(f,"permission"),new QuotaLimit(unlimited,unlimited?0:number(f,"limit")),number(f,"used"),f));
        }
        return new QuotaResult(List.copyOf(entries),r);
    }
    static VerseText verseText(Map<String, String> f) throws IOException {
        Long b = optionalNumber(f,"book"), c = optionalNumber(f,"chapter"), v = optionalNumber(f,"verse");
        int count = (b==null?0:1)+(c==null?0:1)+(v==null?0:1);
        if (count != 0 && (count != 3 || b == 0 || c == 0 || v == 0)) throw invalid();
        return new VerseText(present(f,"text"),f.get("content"),text(f,"translation"),text(f,"book_name"),text(f,"reference"),b,c,v,f);
    }
    static Reading reading(CommandResponse r, boolean search) throws IOException {
        var h = header(r); String translation = required(h,"translation");
        Long b = optionalNumber(h,"book"), c = optionalNumber(h,"chapter"), v = optionalNumber(h,"verse");
        if (b != null && b == 0 || c != null && c == 0 || v != null && v == 0 || !search && b == null) throw invalid();
        var verses = new ArrayList<VerseText>();
        if (!search && v != null) { if (c == null) throw invalid(); verses.add(verseText(single(r))); }
        else { counted(r,search?"results":"verses","verse"); for (var record : r.records().subList(1,r.records().size())) verses.add(verseText(record.fields())); }
        return new Reading(translation,b,c,v,List.copyOf(verses),r);
    }
    static TranslationInfo translationInfo(CommandResponse r) throws IOException {
        var f = single(r); return new TranslationInfo(required(f,"short_name"),required(f,"full_name"),text(f,"edition"),text(f,"updated"),text(f,"rights_status"),text(f,"rights_label"),text(f,"rights_url"),text(f,"source_url"),text(f,"attribution"),text(f,"trademark_notice"),r);
    }
    static TranslationCatalog catalog(CommandResponse r) throws IOException {
        var h = header(r); String slug = required(h,"translation"); long count = number(h,"books");
        var books = new ArrayList<CatalogBook>(); var seen = new HashSet<Long>();
        var chapters = new ArrayList<CatalogChapter>(); var seenChapters = new HashSet<Long>();
        Map<String,String> current = null; long id = 0, expected = 0;
        for (var record : r.records().subList(1,r.records().size())) {
            var f = record.fields(); long book = positive(f,"book");
            if (record.type().equals("book")) {
                if (current != null) {
                    if (chapters.size() != expected) throw invalid();
                    books.add(new CatalogBook(id,required(current,"name"),List.copyOf(chapters),current));
                }
                if (!seen.add(book)) throw invalid();
                current = f; id = book; expected = number(f,"chapters"); chapters.clear(); seenChapters.clear();
            } else if (record.type().equals("chapter")) {
                if (current == null || book != id) throw invalid();
                long n = positive(f,"chapter"); if (!seenChapters.add(n)) throw invalid();
                chapters.add(new CatalogChapter(n,positive(f,"verses"),f));
            } else throw invalid();
        }
        if (current != null) {
            if (chapters.size() != expected) throw invalid();
            books.add(new CatalogBook(id,required(current,"name"),List.copyOf(chapters),current));
        }
        if (books.size() != count) throw invalid();
        return new TranslationCatalog(slug,List.copyOf(books),r);
    }
    static TokenList tokens(CommandResponse r) throws IOException {
        var h = counted(r,"count","token"); var tokens = new ArrayList<TokenMetadata>(); var seen = new HashSet<String>();
        for (var record : r.records().subList(1,r.records().size())) {
            var f = record.fields(); String id = required(f,"id"); if (!seen.add(id)) throw invalid();
            tokens.add(new TokenMetadata(id,required(f,"source"),number(f,"issued_at"),f.get("issued_by"),f.get("label"),f.containsKey("scopes")?csv(f.get("scopes")):null,optionalNumber(f,"last_used_at"),optionalNumber(f,"expires_at"),optionalBool(f,"retiring"),f));
        }
        return new TokenList(required(h,"actor"),List.copyOf(tokens),r);
    }
    static LiveState liveState(Map<String,String> f) throws IOException {
        return new LiveState(required(f,"id"),present(f,"name"),required(f,"status"),bool(f,"paused"),f.get("reference"),f.get("owner"),f.get("created_by"),f.get("organization_id"),optionalBool(f,"owned"),optionalBool(f,"protected"),optionalNumber(f,"created_at"),csv(f.get("translations")),f);
    }
    static LiveResult live(CommandResponse r) throws IOException { return new LiveResult(liveState(single(r)),r); }
    static LiveList lives(CommandResponse r) throws IOException {
        counted(r,"count","live"); var entries = new ArrayList<LiveState>(); var seen = new HashSet<String>();
        for (var record : r.records().subList(1,r.records().size())) { var state = liveState(record.fields()); if (!seen.add(state.id())) throw invalid(); entries.add(state); }
        return new LiveList(List.copyOf(entries),r);
    }
    static LiveStatistics stats(CommandResponse r) throws IOException {
        var h = counted(r,"connections","connection"); var entries = new ArrayList<LiveConnection>();
        long actor = number(h,"actor_connections"), anonymous = number(h,"anonymous_connections");
        if (actor > r.records().size()-1 || anonymous != r.records().size()-1-actor) throw invalid();
        for (var record : r.records().subList(1,r.records().size())) {
            var f = record.fields(); entries.add(new LiveConnection(required(f,"actor"),required(f,"access"),number(f,"connected_at"),number(f,"connected_for_seconds"),f));
        }
        return new LiveStatistics(required(h,"id"),number(h,"running_for_seconds"),number(h,"revision"),number(h,"stack_entries"),actor,anonymous,List.copyOf(entries),r);
    }
    static LiveVerse liveVerse(Map<String,String> f) throws IOException { return new LiveVerse(required(f,"translation"),text(f,"reference"),present(f,"text"),f); }
    static LiveStack stack(CommandResponse r) throws IOException {
        var h = counted(r,"count","stack"); var entries = new ArrayList<StackEntry>();
        for (var record : r.records().subList(1,r.records().size())) {
            long position = positive(record.fields(),"position"); if (position != entries.size()+1) throw invalid();
            entries.add(new StackEntry(position,liveVerse(record.fields())));
        }
        return new LiveStack(required(h,"id"),List.copyOf(entries),r);
    }
    static LiveVerseResult liveVerses(CommandResponse r) throws IOException {
        String event = text(header(r),"event"); var verses = new ArrayList<LiveVerse>();
        if (event.equals("clear")) single(r);
        else {
            if (!event.equals("verse")) throw invalid(); counted(r,"translations","verse");
            for (var record : r.records().subList(1,r.records().size())) verses.add(liveVerse(record.fields()));
        }
        return new LiveVerseResult(event,List.copyOf(verses),r);
    }
    static LiveActionResult control(CommandResponse r, Commands.LiveAction action) throws IOException {
        var f = single(r);
        return switch (action) {
            case START, STOP -> new LiveActionResult(liveState(f),null,"",r);
            case PAUSE, RESUME -> new LiveActionResult(null,bool(f,"paused"),"",r);
            case CLEAR, DELETE -> {
                String expected = action == Commands.LiveAction.CLEAR?"clear":"deleted";
                if (!text(f,"event").equals(expected)) throw invalid();
                yield new LiveActionResult(null,null,expected,r);
            }
            default -> throw new IllegalArgumentException("Unsupported Live control action");
        };
    }
    static OrganizationDirectoryEntry organization(String kind, Map<String,String> f) throws IOException {
        for (String key : (kind.equals("application")?List.of("application_id","name","domain","state","next_action","verification_url"):List.of("id","handle","name","role","verification","status"))) required(f,key);
        return new OrganizationDirectoryEntry(kind,text(f,"id"),text(f,"application_id"),text(f,"handle"),text(f,"name"),text(f,"domain"),text(f,"role"),text(f,"verification"),text(f,"status"),text(f,"state"),text(f,"next_action"),text(f,"verification_url"),f.get("organization_id"),f);
    }
    static OrganizationDirectory organizations(CommandResponse r) throws IOException {
        var h = counted(r,"count","organization","application"); var entries = new ArrayList<OrganizationDirectoryEntry>();
        for (var record : r.records().subList(1,r.records().size())) entries.add(organization(record.type(),record.fields()));
        return new OrganizationDirectory(List.copyOf(entries),h.get("next"),r);
    }
    static OrganizationApplicationResult application(CommandResponse r) throws IOException { return new OrganizationApplicationResult(organization("application",single(r)),r); }
}
