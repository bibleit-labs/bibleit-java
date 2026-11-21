package app.bibleit;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/** Conditional overlay operations over a caller-owned SSH connection. No method retries or starts playback.
     */
public final class LiveOverlaySshClient {
    private final SshClient client;
    /** Reuses the supplied finite executor.
     * @param client authenticated SSH client
     */
    public LiveOverlaySshClient(SshClient client) { this.client=Objects.requireNonNull(client); }
    /** Immutable selected design metadata.
     * @param designId design ID
     * @param designRevision selected revision
     * @param name display name
     * @param youtubeUrl authorized live URL, or null when unavailable
     */
    public record Selection(String designId,long designRevision,String name,String youtubeUrl) {}
    /** Response retaining unknown protocol fields.
     * @param liveId Live ID
     * @param sequence conditional sequence
     * @param overlay selected design, or null for normal output
     * @param fields future protocol fields
     * @param raw original reply
     */
    public record Result(String liveId,long sequence,Selection overlay,Map<String,String> fields,CommandResponse raw) {
        @Override public String toString() { return "LiveOverlayResult[redacted]"; }
    }
    private Result call(Command command) throws IOException,InterruptedException {
        var raw=client.execute(command);var f=SshDecoders.single(raw);
        String id=SshDecoders.required(f,"live_id");long sequence=SshDecoders.number(f,"sequence");
        Selection selection=null;
        if(SshDecoders.bool(f,"active")) selection=new Selection(SshDecoders.required(f,"design_id"),SshDecoders.number(f,"design_revision"),SshDecoders.present(f,"name"),f.get("youtube_url"));
        return new Result(id,sequence,selection,Map.copyOf(f),raw);
    }
    /** Reads the current overlay.
     * @param id Live ID
     * @return selection and sequence
     * @throws IOException for transport/protocol errors
     * @throws InterruptedException if interrupted
     */
    public Result get(String id) throws IOException,InterruptedException { return call(Commands.getLiveOverlayCommand(id)); }
    /** Selects an overlay without starting a stopped Live.
     * @param id Live ID
     * @param designId saved or built-in design
     * @param sequence expected sequence
     * @return updated selection
     * @throws IOException for transport/protocol errors
     * @throws InterruptedException if interrupted
     */
    public Result set(String id,String designId,long sequence) throws IOException,InterruptedException { return call(Commands.setLiveOverlayCommand(id,designId,sequence)); }
    /** Restores normal output without changing the queue.
     * @param id Live ID
     * @param sequence expected sequence
     * @return updated selection
     * @throws IOException for transport/protocol errors
     * @throws InterruptedException if interrupted
     */
    public Result clear(String id,long sequence) throws IOException,InterruptedException { return call(Commands.clearLiveOverlayCommand(id,sequence)); }
}
