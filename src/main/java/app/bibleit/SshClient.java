package app.bibleit;

import java.io.IOException;
import java.util.Objects;
import static app.bibleit.SshModels.*;

/** SSH helpers. The application owns authentication, host-key verification and connection lifetime. */
public final class SshClient {
    @FunctionalInterface public interface Executor {
        CommandResponse execute(Command command) throws IOException, InterruptedException;
    }
    private final Executor executor;
    public SshClient(Executor executor) { this.executor = Objects.requireNonNull(executor); }
    public CommandResponse execute(Command command) throws IOException, InterruptedException {
        if (command == null || command.streaming()) throw new IllegalArgumentException("Finite command required");
        if (Thread.currentThread().isInterrupted()) throw new InterruptedException("SSH execution interrupted");
        return Objects.requireNonNull(executor.execute(command));
    }
    public CommandResponse ping() throws IOException, InterruptedException { return execute(Commands.pingCommand()); }
    public CommandResponse identity() throws IOException, InterruptedException { return execute(Commands.identityCommand()); }
    public CommandResponse read(Reference ref) throws IOException, InterruptedException { return execute(Commands.readCommand(ref)); }
    public CommandResponse search(String translation, String query) throws IOException, InterruptedException { return execute(Commands.searchCommand(translation, query)); }
    public CommandResponse listTranslations() throws IOException, InterruptedException { return execute(Commands.translationListCommand()); }
    public CommandResponse addTranslation(String slug) throws IOException, InterruptedException { return execute(Commands.addTranslationCommand(slug)); }
    public CommandResponse removeTranslation(String slug) throws IOException, InterruptedException { return execute(Commands.removeTranslationCommand(slug)); }
    public IdentityInfo getIdentity() throws IOException, InterruptedException { return SshDecoders.identity(execute(Commands.identityCommand())); }
    public Reading readVerses(Reference ref) throws IOException, InterruptedException { return SshDecoders.reading(execute(Commands.readCommand(ref)), false); }
    public Reading searchVerses(String translation, String query) throws IOException, InterruptedException { return SshDecoders.reading(execute(Commands.searchCommand(translation, query)), true); }
    public ServerInfo getServerInfo() throws IOException, InterruptedException { return SshDecoders.serverInfo(execute(Commands.serverInfoCommand())); }
    public HelpResult help(String topic) throws IOException, InterruptedException { return SshDecoders.help(execute(Commands.helpCommand(topic))); }
    public AccountInfo getAccountInfo() throws IOException, InterruptedException { return SshDecoders.account(execute(Commands.accountInfoCommand())); }
    public QuotaResult listQuotas() throws IOException, InterruptedException { return SshDecoders.quotas(execute(Commands.accountQuotasCommand())); }
    public TokenList listTokens() throws IOException, InterruptedException { return SshDecoders.tokens(execute(Commands.accountTokensCommand())); }
    public TranslationInfo getTranslationInfo(String slug) throws IOException, InterruptedException { return SshDecoders.translationInfo(execute(Commands.translationInfoCommand(slug))); }
    public TranslationCatalog getTranslationCatalog(String slug) throws IOException, InterruptedException { return SshDecoders.catalog(execute(Commands.translationCatalogCommand(slug))); }
    public LiveResult createLive(String name) throws IOException, InterruptedException { return SshDecoders.live(execute(Commands.createLiveCommand(name))); }
    public LiveResult getLive(String id) throws IOException, InterruptedException { return SshDecoders.live(execute(Commands.liveCommand(id, Commands.LiveAction.INFO))); }
    public LiveResult setLive(String id, Commands.LiveField field, String... values) throws IOException, InterruptedException { return SshDecoders.live(execute(Commands.setLiveCommand(id, field, values))); }
    public LiveList listLives() throws IOException, InterruptedException { return SshDecoders.lives(execute(Commands.liveListCommand())); }
    public LiveStatistics getLiveStats(String id) throws IOException, InterruptedException { return SshDecoders.stats(execute(Commands.liveCommand(id, Commands.LiveAction.STATS))); }
    public LiveStack getLiveStack(String id) throws IOException, InterruptedException { return SshDecoders.stack(execute(Commands.liveStackCommand(id, Commands.StackAction.INFO))); }
    public LiveVerseResult pushLive(String id, Reference ref) throws IOException, InterruptedException { return SshDecoders.liveVerses(execute(Commands.pushLiveCommand(id, ref))); }
    public LiveVerseResult popLive(String id, int count) throws IOException, InterruptedException { return SshDecoders.liveVerses(execute(Commands.popLiveCommand(id, count))); }
    public LiveActionResult controlLive(String id, Commands.LiveAction action) throws IOException, InterruptedException {
        if (!java.util.Set.of(Commands.LiveAction.START, Commands.LiveAction.STOP, Commands.LiveAction.PAUSE, Commands.LiveAction.RESUME, Commands.LiveAction.CLEAR, Commands.LiveAction.DELETE).contains(action)) throw new IllegalArgumentException("Unsupported Live control action");
        return SshDecoders.control(execute(Commands.liveCommand(id, action)), action); }
    public OrganizationDirectory listOrganizations(String cursor) throws IOException, InterruptedException { return SshDecoders.organizations(execute(Commands.organizationListCommand(cursor))); }
    public OrganizationApplicationResult getOrganizationApplicationStatus(String id) throws IOException, InterruptedException { return SshDecoders.application(execute(Commands.organizationApplicationStatusCommand(id))); }
}
