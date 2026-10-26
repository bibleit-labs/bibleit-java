package app.bibleit;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * SSH command builders using protocol v1 argument rules.
 */
public final class Commands {
    private Commands() {}
    /**
     * Supported Live state, control and subscription actions.
     */
    public enum LiveAction {
        /** Info protocol action. */
        INFO,
        /** Stats protocol action. */
        STATS,
        /** Start protocol action. */
        START,
        /** Stop protocol action. */
        STOP,
        /** Pause protocol action. */
        PAUSE,
        /** Resume protocol action. */
        RESUME,
        /** Clear protocol action. */
        CLEAR,
        /** Delete protocol action. */
        DELETE,
        /** Subscribe protocol action. */
        SUBSCRIBE
    }
    /**
     * Fields replaceable through the Live SET command.
     */
    public enum LiveField {
        /** Name protocol action. */
        NAME,
        /** Reference protocol action. */
        REFERENCE,
        /** Translations protocol action. */
        TRANSLATIONS
    }
    /**
     * Connection authentication and audience-secret management actions.
     */
    public enum SecretAction {
        /** Authenticate protocol action. */
        AUTHENTICATE,
        /** Create protocol action. */
        CREATE,
        /** Rotate protocol action. */
        ROTATE,
        /** Delete protocol action. */
        DELETE
    }
    /**
     * Actions on a Live reference stack.
     */
    public enum StackAction {
        /** Info protocol action. */
        INFO,
        /** Clear protocol action. */
        CLEAR
    }
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
    /**
     * Builds a validated ping command.
     * @return the validated command; wire values may contain secrets
     */
    public static Command pingCommand() { return command("PING"); }
    /**
     * Builds a validated server info command.
     * @return the validated command; wire values may contain secrets
     */
    public static Command serverInfoCommand() { return command("SERVER INFO"); }
    /**
     * Builds a validated identity command.
     * @return the validated command; wire values may contain secrets
     */
    public static Command identityCommand() { return command("AUTH INFO"); }
    /**
     * Builds a validated account info command.
     * @return the validated command; wire values may contain secrets
     */
    public static Command accountInfoCommand() { return command("ACCOUNT INFO"); }
    /**
     * Builds a validated account quotas command.
     * @return the validated command; wire values may contain secrets
     */
    public static Command accountQuotasCommand() { return command("ACCOUNT QUOTA LIST"); }
    /**
     * Builds a validated account tokens command.
     * @return the validated command; wire values may contain secrets
     */
    public static Command accountTokensCommand() { return command("ACCOUNT TOKEN LIST"); }
    /**
     * Builds a validated translation list command.
     * @return the validated command; wire values may contain secrets
     */
    public static Command translationListCommand() { return command("ACCOUNT TRANSLATION LIST"); }
    /**
     * Builds a validated add translation command.
     * @param slug the translation slug
     * @return the validated command; wire values may contain secrets
     */
    public static Command addTranslationCommand(String slug) { return command("ACCOUNT TRANSLATION ADD", slug); }
    /**
     * Builds a validated remove translation command.
     * @param slug the translation slug
     * @return the validated command; wire values may contain secrets
     */
    public static Command removeTranslationCommand(String slug) { return command("ACCOUNT TRANSLATION REMOVE", slug); }
    /**
     * Builds a validated translation info command.
     * @param slug the translation slug
     * @return the validated command; wire values may contain secrets
     */
    public static Command translationInfoCommand(String slug) { return command("TRANSLATION INFO", slug); }
    /**
     * Builds a validated translation catalog command.
     * @param slug the translation slug
     * @return the validated command; wire values may contain secrets
     */
    public static Command translationCatalogCommand(String slug) { return command("TRANSLATION CATALOG", slug); }
    /**
     * Builds a validated read command.
     * @param ref the Scripture reference
     * @return the validated command; wire values may contain secrets
     */
    public static Command readCommand(Reference ref) { return command("READ", ref.arguments(true).toArray(String[]::new)); }
    /**
     * Builds a validated search command.
     * @param translation the translation slug
     * @param query the search query
     * @return the validated command; wire values may contain secrets
     */
    public static Command searchCommand(String translation, String query) { return command("SEARCH", translation, query); }
    /**
     * Builds a validated live list command.
     * @return the validated command; wire values may contain secrets
     */
    public static Command liveListCommand() { return command("LIVE LIST"); }
    /**
     * Builds a validated create live command.
     * @param name the display name
     * @return the validated command; wire values may contain secrets
     */
    public static Command createLiveCommand(String name) { return name.isEmpty() ? command("LIVE CREATE") : command("LIVE CREATE", name); }
    /**
     * Builds a validated delete all lives command.
     * @return the validated command; wire values may contain secrets
     */
    public static Command deleteAllLivesCommand() { return command("LIVE DELETE ALL"); }
    /**
     * Builds a validated live command.
     * @param id the Live resource identifier
     * @param action the protocol action
     * @return the validated command; wire values may contain secrets
     */
    public static Command liveCommand(String id, LiveAction action) {
        String wire = command("LIVE", id).wire() + " " + Objects.requireNonNull(action).name();
        return new Command(wire, action == LiveAction.SUBSCRIBE);
    }
    /**
     * Builds a validated set live command.
     * @param id the Live resource identifier
     * @param field the Live field to replace
     * @param values the field or query values
     * @return the validated command; wire values may contain secrets
     */
    public static Command setLiveCommand(String id, LiveField field, String... values) {
        Objects.requireNonNull(field);
        if (values.length == 0 || field != LiveField.TRANSLATIONS && values.length != 1) throw new IllegalArgumentException("Invalid Live value count");
        return command(command("LIVE", id).wire() + " SET " + field.name(), values);
    }
    /**
     * Builds a validated live secret command.
     * @param id the Live resource identifier
     * @param action the protocol action
     * @param value the value to set
     * @return the validated command; wire values may contain secrets
     */
    public static Command liveSecretCommand(String id, SecretAction action, String value) {
        String prefix = command("LIVE", id).wire() + " SECRET";
        if (action == SecretAction.AUTHENTICATE) {
            if (Set.of("CREATE", "ROTATE", "DELETE").contains(value.toUpperCase(Locale.ROOT))) throw new IllegalArgumentException("Secret conflicts with keyword");
            return command(prefix, value);
        }
        if (!value.isEmpty()) throw new IllegalArgumentException("Unexpected secret value");
        return command(prefix + " " + Objects.requireNonNull(action).name());
    }
    /**
     * Builds a validated live stack command.
     * @param id the Live resource identifier
     * @param action the protocol action
     * @return the validated command; wire values may contain secrets
     */
    public static Command liveStackCommand(String id, StackAction action) { return command(command("LIVE", id).wire() + " STACK " + Objects.requireNonNull(action).name()); }
    /**
     * Builds a validated push live command.
     * @param id the Live resource identifier
     * @param ref the Scripture reference
     * @return the validated command; wire values may contain secrets
     */
    public static Command pushLiveCommand(String id, Reference ref) { return command(command("LIVE", id).wire() + " STACK PUSH", ref.arguments(false).toArray(String[]::new)); }
    /**
     * Builds a validated pop live command.
     * @param id the Live resource identifier
     * @param count the pop count, or zero for the server default
     * @return the validated command; wire values may contain secrets
     */
    public static Command popLiveCommand(String id, int count) {
        if (count < 0) throw new IllegalArgumentException("Invalid pop count");
        String prefix = command("LIVE", id).wire() + " STACK POP";
        return count == 0 ? command(prefix) : command(prefix, Integer.toString(count));
    }
    /**
     * Builds a validated help command.
     * @param topic the help topic, or an empty string for root help
     * @return the validated command; wire values may contain secrets
     */
    public static Command helpCommand(String topic) {
        if (topic.isEmpty()) return command("HELP");
        String upper = topic.toUpperCase(Locale.ROOT);
        if (!Set.of("ROOT", "SERVER", "ACCOUNT", "AUTH", "TRANSLATION", "LIVE", "ORGANIZATION").contains(upper)) throw new IllegalArgumentException("Invalid help topic");
        return command("HELP", upper);
    }
    /**
     * Builds a validated organization list command.
     * @param cursor the opaque directory cursor
     * @return the validated command; wire values may contain secrets
     */
    public static Command organizationListCommand(String cursor) { return cursor.isEmpty() ? command("ORGANIZATION LIST") : command("ORGANIZATION LIST", cursor); }
    /**
     * Builds a validated organization application status command.
     * @param id the Live resource identifier
     * @return the validated command; wire values may contain secrets
     */
    public static Command organizationApplicationStatusCommand(String id) { return command("ORGANIZATION APPLICATION", id, "STATUS"); }
    /**
     * Creates a Live using the server default name.
     * @return the validated command
     */
    public static Command createLiveCommand() { return createLiveCommand(""); }
    /**
     * Builds the root help command.
     * @return the validated command
     */
    public static Command helpCommand() { return helpCommand(""); }
    /**
     * Builds the first organization-directory page request.
     * @return the validated command
     */
    public static Command organizationListCommand() { return organizationListCommand(""); }
    /**
     * Pops the default number of Live stack entries.
     * @param id the Live identifier
     * @return the validated command
     */
    public static Command popLiveCommand(String id) { return popLiveCommand(id, 0); }
    /**
     * Builds an audience-secret management command without an unused value argument.
     * @param id the Live identifier
     * @param action the management action
     * @return the validated command
     * @throws IllegalArgumentException if authentication requires a secret value
     */
    public static Command liveSecretCommand(String id, SecretAction action) {
        if (action == SecretAction.AUTHENTICATE) throw new IllegalArgumentException("Authentication requires a secret value");
        return liveSecretCommand(id, action, "");
    }

}
