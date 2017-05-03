package io.github.ruipsrosario.process;

import java.io.IOException;
import java.io.InputStream;

/**
 * Consumer that just discards any data being read from the underlying {@link InputStream}.
 */
public class DiscardInputStreamConsumer implements InputStreamConsumer {
    /**
     * Default allocation size of the internal buffer for consuming data.
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    private final InputStream inputStream;
    private final int bufferSize;

    /**
     * Constructs a new {@code DiscardInputStreamConsumer} for the stream supplied. The constructed consumer will use
     * the {@link #DEFAULT_BUFFER_SIZE default buffer size} for the internal buffer.
     *
     * @param inputStream The stream to consume.
     *
     * @throws NullPointerException If no stream is supplied.
     */
    public DiscardInputStreamConsumer(InputStream inputStream) {
        this(inputStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Constructs a new {@code DiscardInputStreamConsumer} for the stream supplied. The constructed consumer will use
     * the supplied buffer size for the internal buffer.
     *
     * @param inputStream The stream to consume.
     * @param bufferSize The size of the underlying buffer used to consume the data.
     *
     * @throws NullPointerException If no stream is supplied.
     * @throws IllegalArgumentException If the supplied buffer size is negative or zero.
     */
    public DiscardInputStreamConsumer(InputStream inputStream, int bufferSize) {
        if (inputStream == null)
            throw new NullPointerException();
        if (bufferSize <= 0)
            throw new IllegalArgumentException();

        this.inputStream = inputStream;
        this.bufferSize = bufferSize;
    }

    @Override
    public void consume() throws IOException {
        byte[] buffer = new byte[bufferSize];
        while (inputStream.read(buffer) != -1) {
            // Empty body - We just discard any data being read
        }
    }
}
