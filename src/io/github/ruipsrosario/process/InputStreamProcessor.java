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
public interface InputStreamProcessor<T> extends InputStreamConsumer {
    /**
     * Retrieves the results of processing the data in the underlying {@link java.io.InputStream InputStream}.
     *
     * @return The processing results.
     *
     * @throws IllegalStateException If the data in the underlying stream hasn't been processed yet.
     */
    T getResults() throws IllegalStateException;

    /**
     * Processes the data in the underlying {@link java.io.InputStream InputStream} and retrieves the results.
     *
     * @return The processing results.
     *
     * @throws IOException If an error occurred while processing the data.
     */
    default T processAndGetResults() throws IOException {
        consume();
        return getResults();
    }

    /**
     * Retrieves the results of processing the data in the underlying {@link java.io.InputStream InputStream} sometime
     * in the future.
     *
     * @return The future processing results.
     */
    default Future<T> getResultsAsync() {
        return new FutureTask<T>(this::processAndGetResults);
    }
}
