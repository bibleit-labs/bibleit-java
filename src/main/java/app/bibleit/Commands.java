package app.bibleit;

import java.util.*;

/** SSH command builders using protocol v1 argument rules. */
public final class Commands {
    private Commands() {}
    public enum LiveAction { INFO, STATS, START, STOP, PAUSE, RESUME, CLEAR, DELETE, SUBSCRIBE }
    public enum LiveField { NAME, REFERENCE, TRANSLATIONS }
    public enum SecretAction { AUTHENTICATE, CREATE, ROTATE, DELETE }
    public enum StackAction { INFO, CLEAR }
    private static Command command(String prefix, String... args) {
        var wire = new StringBuilder(prefix);
        for (String arg : args) {
            if (arg == null || arg.isEmpty() || arg.chars().anyMatch(c -> c == '\r' || c == '\n' || c == 0 || c == '"'))
                throw new IllegalArgumentException("Arguments require nonempty lines without quotes or NUL");
            wire.append(' ');
            if (arg.contains(" ") || arg.contains("\t")) wire.append('"').append(arg).append('"');
            else wire.append(arg);
        }
        if (wire.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 8192)
            throw new IllegalArgumentException("Command exceeds 8 KiB");
        return new Command(wire.toString(), false);
    }
    public static Command pingCommand() { return command("PING"); }
    public static Command serverInfoCommand() { return command("SERVER INFO"); }
    public static Command identityCommand() { return command("AUTH INFO"); }
    public static Command accountInfoCommand() { return command("ACCOUNT INFO"); }
    public static Command accountQuotasCommand() { return command("ACCOUNT QUOTA LIST"); }
    public static Command accountTokensCommand() { return command("ACCOUNT TOKEN LIST"); }
    public static Command translationListCommand() { return command("ACCOUNT TRANSLATION LIST"); }
    public static Command addTranslationCommand(String slug) { return command("ACCOUNT TRANSLATION ADD", slug); }
    public static Command removeTranslationCommand(String slug) { return command("ACCOUNT TRANSLATION REMOVE", slug); }
    public static Command translationInfoCommand(String slug) { return command("TRANSLATION INFO", slug); }
    public static Command translationCatalogCommand(String slug) { return command("TRANSLATION CATALOG", slug); }
    public static Command readCommand(Reference ref) { return command("READ", ref.arguments(true).toArray(String[]::new)); }
    public static Command searchCommand(String translation, String query) { return command("SEARCH", translation, query); }
    public static Command liveListCommand() { return command("LIVE LIST"); }
    public static Command createLiveCommand(String name) { return name.isEmpty() ? command("LIVE CREATE") : command("LIVE CREATE", name); }
    public static Command deleteAllLivesCommand() { return command("LIVE DELETE ALL"); }
    public static Command liveCommand(String id, LiveAction action) {
        String wire = command("LIVE", id).wire() + " " + Objects.requireNonNull(action).name();
        return new Command(wire, action == LiveAction.SUBSCRIBE);
    }
    public static Command setLiveCommand(String id, LiveField field, String... values) {
        Objects.requireNonNull(field);
        if (values.length == 0 || field != LiveField.TRANSLATIONS && values.length != 1) throw new IllegalArgumentException("Invalid Live value count");
        return command(command("LIVE", id).wire() + " SET " + field.name(), values);
    }
    public static Command liveSecretCommand(String id, SecretAction action, String value) {
        String prefix = command("LIVE", id).wire() + " SECRET";
        if (action == SecretAction.AUTHENTICATE) {
            if (Set.of("CREATE", "ROTATE", "DELETE").contains(value.toUpperCase(Locale.ROOT))) throw new IllegalArgumentException("Secret conflicts with keyword");
            return command(prefix, value);
        }
        if (!value.isEmpty()) throw new IllegalArgumentException("Unexpected secret value");
        return command(prefix + " " + Objects.requireNonNull(action).name());
    }
    public static Command liveStackCommand(String id, StackAction action) { return command(command("LIVE", id).wire() + " STACK " + Objects.requireNonNull(action).name()); }
    public static Command pushLiveCommand(String id, Reference ref) { return command(command("LIVE", id).wire() + " STACK PUSH", ref.arguments(false).toArray(String[]::new)); }
    public static Command popLiveCommand(String id, int count) {
        if (count < 0) throw new IllegalArgumentException("Invalid pop count");
        String prefix = command("LIVE", id).wire() + " STACK POP";
        return count == 0 ? command(prefix) : command(prefix, Integer.toString(count));
    }
    public static Command helpCommand(String topic) {
        if (topic.isEmpty()) return command("HELP");
        String upper = topic.toUpperCase(Locale.ROOT);
        if (!Set.of("ROOT", "SERVER", "ACCOUNT", "AUTH", "TRANSLATION", "LIVE", "ORGANIZATION").contains(upper)) throw new IllegalArgumentException("Invalid help topic");
        return command("HELP", upper);
    }
    public static Command organizationListCommand(String cursor) { return cursor.isEmpty() ? command("ORGANIZATION LIST") : command("ORGANIZATION LIST", cursor); }
    public static Command organizationApplicationStatusCommand(String id) { return command("ORGANIZATION APPLICATION", id, "STATUS"); }
}
