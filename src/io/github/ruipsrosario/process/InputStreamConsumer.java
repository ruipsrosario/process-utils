package io.github.ruipsrosario.process;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Consumes the data in an {@link java.io.InputStream InputStream}.
 * <br><br>
 * This interface is useful for declaring consumers for the {@link Process#getInputStream() standard output} or {@link
 * Process#getErrorStream() standard error} streams of processes.
 */
@FunctionalInterface
public interface InputStreamConsumer {
    /**
     *  Consumes the data in the underlying {@link java.io.InputStream InputStream}.
     *
     *  @throws IOException If an error occurred while consuming the data.
     */
    void consume() throws IOException;

    /**
     * Consumes the data in the underlying {@link java.io.InputStream InputStream} asynchronously.
     *
     * @return The thread responsible for consuming the data.
     *
     * @throws UncheckedIOException If an error occurred while consuming the data.
     */
    default Thread consumeAsync() {
        Thread thread = new Thread(() -> {
            try {
                consume();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        thread.start();
        return thread;
    }
}
