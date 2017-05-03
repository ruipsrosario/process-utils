package io.github.ruipsrosario.process;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Processes the data in an {@link java.io.InputStream InputStream}.
 * <br><br>
 * This interface is useful for declaring processors for the {@link Process#getInputStream() standard output} or {@link
 * Process#getErrorStream() standard error} streams of processes.
 *
 * @param <T> The type of the results.
 *
 * @see InputStreamConsumer
 */
@FunctionalInterface
public interface InputStreamProcessor<T> {
    /**
     * Retrieves the results of processing the data in the underlying {@link java.io.InputStream InputStream}.
     *
     * @return The processing results.
     *
     * @throws IOException If an error occurred while processing the data.
     */
    T process() throws IOException;

    /**
     * Retrieves the results of processing the data in the underlying {@link java.io.InputStream InputStream} sometime
     * in the future.
     *
     * @return The future processing results.
     */
    default Future<T> getResultsAsync() {
        return new FutureTask<T>(this::process);
    }
}
