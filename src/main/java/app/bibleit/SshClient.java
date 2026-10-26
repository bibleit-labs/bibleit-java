package app.bibleit;

import java.io.IOException;
import java.util.Objects;
import app.bibleit.SshModels.AccountInfo;
import app.bibleit.SshModels.HelpResult;
import app.bibleit.SshModels.IdentityInfo;
import app.bibleit.SshModels.LiveActionResult;
import app.bibleit.SshModels.LiveList;
import app.bibleit.SshModels.LiveResult;
import app.bibleit.SshModels.LiveStack;
import app.bibleit.SshModels.LiveStatistics;
import app.bibleit.SshModels.LiveVerseResult;
import app.bibleit.SshModels.OrganizationApplicationResult;
import app.bibleit.SshModels.OrganizationDirectory;
import app.bibleit.SshModels.QuotaResult;
import app.bibleit.SshModels.Reading;
import app.bibleit.SshModels.ServerInfo;
import app.bibleit.SshModels.TokenList;
import app.bibleit.SshModels.TranslationCatalog;
import app.bibleit.SshModels.TranslationInfo;

/**
 * SSH helpers. The application owns authentication, host-key verification and connection lifetime.
 */
public final class SshClient {
    /**
     * Typed executor helpers.
     */
    @FunctionalInterface
    public interface Executor {
        /**
         * Executes a validated command on the application-owned SSH connection.
         * @param command the finite command
         * @return the parsed finite reply
         * @throws IOException if SSH execution or decoding fails
         * @throws InterruptedException if execution is interrupted
         */
        CommandResponse execute(Command command) throws IOException, InterruptedException;
    }
    private final Executor executor;
    /**
     * Binds SSH command helpers to an application-owned executor.
     * @param executor the caller-owned finite SSH command executor
     */
    public SshClient(Executor executor) { this.executor = Objects.requireNonNull(executor); }
    /**
     * Executes one validated finite command through the caller-owned SSH connection.
     * @param command the validated command to execute or decode
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public CommandResponse execute(Command command) throws IOException, InterruptedException {
        if (command == null || command.streaming()) throw new IllegalArgumentException("Finite command required");
        if (Thread.currentThread().isInterrupted()) throw new InterruptedException("SSH execution interrupted");
        return Objects.requireNonNull(executor.execute(command));
    }
    /**
     * Executes ping through the caller-owned finite SSH executor.
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public CommandResponse ping() throws IOException, InterruptedException { return execute(Commands.pingCommand()); }
    /**
     * Executes identity through the caller-owned finite SSH executor.
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public CommandResponse identity() throws IOException, InterruptedException { return execute(Commands.identityCommand()); }
    /**
     * Executes read through the caller-owned finite SSH executor.
     * @param ref the Scripture reference
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public CommandResponse read(Reference ref) throws IOException, InterruptedException { return execute(Commands.readCommand(ref)); }
    /**
     * Executes search through the caller-owned finite SSH executor.
     * @param translation the translation slug
     * @param query the search query
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public CommandResponse search(String translation, String query) throws IOException, InterruptedException { return execute(Commands.searchCommand(translation, query)); }
    /**
     * Executes list translations through the caller-owned finite SSH executor.
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public CommandResponse listTranslations() throws IOException, InterruptedException { return execute(Commands.translationListCommand()); }
    /**
     * Executes add translation through the caller-owned finite SSH executor.
     * @param slug the translation slug
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public CommandResponse addTranslation(String slug) throws IOException, InterruptedException { return execute(Commands.addTranslationCommand(slug)); }
    /**
     * Executes remove translation through the caller-owned finite SSH executor.
     * @param slug the translation slug
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public CommandResponse removeTranslation(String slug) throws IOException, InterruptedException { return execute(Commands.removeTranslationCommand(slug)); }
    /**
     * Executes get identity through the caller-owned finite SSH executor.
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public IdentityInfo getIdentity() throws IOException, InterruptedException { return SshDecoders.identity(execute(Commands.identityCommand())); }
    /**
     * Executes read verses through the caller-owned finite SSH executor.
     * @param ref the Scripture reference
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public Reading readVerses(Reference ref) throws IOException, InterruptedException { return SshDecoders.reading(execute(Commands.readCommand(ref)), false); }
    /**
     * Executes search verses through the caller-owned finite SSH executor.
     * @param translation the translation slug
     * @param query the search query
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public Reading searchVerses(String translation, String query) throws IOException, InterruptedException { return SshDecoders.reading(execute(Commands.searchCommand(translation, query)), true); }
    /**
     * Executes get server info through the caller-owned finite SSH executor.
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public ServerInfo getServerInfo() throws IOException, InterruptedException { return SshDecoders.serverInfo(execute(Commands.serverInfoCommand())); }
    /**
     * Executes help through the caller-owned finite SSH executor.
     * @param topic the help topic, or an empty string for root help
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public HelpResult help(String topic) throws IOException, InterruptedException { return SshDecoders.help(execute(Commands.helpCommand(topic))); }
    /**
     * Executes get account info through the caller-owned finite SSH executor.
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public AccountInfo getAccountInfo() throws IOException, InterruptedException { return SshDecoders.account(execute(Commands.accountInfoCommand())); }
    /**
     * Executes list quotas through the caller-owned finite SSH executor.
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public QuotaResult listQuotas() throws IOException, InterruptedException { return SshDecoders.quotas(execute(Commands.accountQuotasCommand())); }
    /**
     * Executes list tokens through the caller-owned finite SSH executor.
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public TokenList listTokens() throws IOException, InterruptedException { return SshDecoders.tokens(execute(Commands.accountTokensCommand())); }
    /**
     * Executes get translation info through the caller-owned finite SSH executor.
     * @param slug the translation slug
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public TranslationInfo getTranslationInfo(String slug) throws IOException, InterruptedException { return SshDecoders.translationInfo(execute(Commands.translationInfoCommand(slug))); }
    /**
     * Executes get translation catalog through the caller-owned finite SSH executor.
     * @param slug the translation slug
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public TranslationCatalog getTranslationCatalog(String slug) throws IOException, InterruptedException { return SshDecoders.catalog(execute(Commands.translationCatalogCommand(slug))); }
    /**
     * Executes create live through the caller-owned finite SSH executor.
     * @param name the display name
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public LiveResult createLive(String name) throws IOException, InterruptedException { return SshDecoders.live(execute(Commands.createLiveCommand(name))); }
    /**
     * Executes get live through the caller-owned finite SSH executor.
     * @param id the Live resource identifier
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public LiveResult getLive(String id) throws IOException, InterruptedException { return SshDecoders.live(execute(Commands.liveCommand(id, Commands.LiveAction.INFO))); }
    /**
     * Executes set live through the caller-owned finite SSH executor.
     * @param id the Live resource identifier
     * @param field the Live field to replace
     * @param values the field or query values
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public LiveResult setLive(String id, Commands.LiveField field, String... values) throws IOException, InterruptedException { return SshDecoders.live(execute(Commands.setLiveCommand(id, field, values))); }
    /**
     * Executes list lives through the caller-owned finite SSH executor.
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public LiveList listLives() throws IOException, InterruptedException { return SshDecoders.lives(execute(Commands.liveListCommand())); }
    /**
     * Executes get live stats through the caller-owned finite SSH executor.
     * @param id the Live resource identifier
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public LiveStatistics getLiveStats(String id) throws IOException, InterruptedException { return SshDecoders.stats(execute(Commands.liveCommand(id, Commands.LiveAction.STATS))); }
    /**
     * Executes get live stack through the caller-owned finite SSH executor.
     * @param id the Live resource identifier
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public LiveStack getLiveStack(String id) throws IOException, InterruptedException { return SshDecoders.stack(execute(Commands.liveStackCommand(id, Commands.StackAction.INFO))); }
    /**
     * Executes push live through the caller-owned finite SSH executor.
     * @param id the Live resource identifier
     * @param ref the Scripture reference
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public LiveVerseResult pushLive(String id, Reference ref) throws IOException, InterruptedException { return SshDecoders.liveVerses(execute(Commands.pushLiveCommand(id, ref))); }
    /**
     * Executes pop live through the caller-owned finite SSH executor.
     * @param id the Live resource identifier
     * @param count the pop count, or zero for the server default
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public LiveVerseResult popLive(String id, int count) throws IOException, InterruptedException { return SshDecoders.liveVerses(execute(Commands.popLiveCommand(id, count))); }
    /**
     * Executes control live through the caller-owned finite SSH executor.
     * @param id the Live resource identifier
     * @param action the protocol action
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public LiveActionResult controlLive(String id, Commands.LiveAction action) throws IOException, InterruptedException {
        if (!java.util.Set.of(Commands.LiveAction.START, Commands.LiveAction.STOP, Commands.LiveAction.PAUSE, Commands.LiveAction.RESUME, Commands.LiveAction.CLEAR, Commands.LiveAction.DELETE).contains(action)) throw new IllegalArgumentException("Unsupported Live control action");
        return SshDecoders.control(execute(Commands.liveCommand(id, action)), action); }
    /**
     * Executes list organizations through the caller-owned finite SSH executor.
     * @param cursor the opaque directory cursor
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public OrganizationDirectory listOrganizations(String cursor) throws IOException, InterruptedException { return SshDecoders.organizations(execute(Commands.organizationListCommand(cursor))); }
    /**
     * Executes get organization application status through the caller-owned finite SSH executor.
     * @param id the Live resource identifier
     * @return the decoded result, retaining its original finite reply
     * @throws IOException if I/O or response processing fails
     * @throws InterruptedException if execution is interrupted
     */
    public OrganizationApplicationResult getOrganizationApplicationStatus(String id) throws IOException, InterruptedException { return SshDecoders.application(execute(Commands.organizationApplicationStatusCommand(id))); }
}
