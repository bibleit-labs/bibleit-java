/**
 * Java clients and protocol types for Bibleit.
 *
 * <p>{@link app.bibleit.BibleitClient} provides REST operations with explicit
 * credentials, {@link app.bibleit.Queries} provides operation-specific query
 * builders, and {@link app.bibleit.Models} contains Jackson schema beans.
 * Successful {@link app.bibleit.Response} instances must be closed by their caller.
 *
 * <p>{@link app.bibleit.SshClient} uses an application-owned SSH executor;
 * applications retain responsibility for authentication and host verification.
 *
 * @since 0.0.1
 */
package app.bibleit;
