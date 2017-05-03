package io.github.ruipsrosario.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Processor that just aggregates any data read from the underlying {@link InputStream} into a string.
 */
public class AggregatorInputStreamProcessor implements InputStreamProcessor<String> {
    private static final String NEWLINE = String.format("%n");

    /**
     * Default allocation size of the internal buffer for consuming data.
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    private final BufferedReader inputStream;
    private String aggregatedResult;

    /**
     * Constructs a new {@code AggregatorInputStreamProcessor} for the stream supplied. The constructed processor will
     * use the {@link #DEFAULT_BUFFER_SIZE default buffer size} for the internal buffer.
     *
     * @param inputStream The stream to process.
     *
     * @throws NullPointerException If no stream is supplied.
     */
    public AggregatorInputStreamProcessor(InputStream inputStream) {
        this(inputStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Constructs a new {@code AggregatorInputStreamProcessor} for the stream supplied. The constructed processor will
     * use the supplied default buffer size for the internal buffer.
     *
     * @param inputStream The stream to process.
     * @param bufferSize The size of the underlying buffer used to process the data.
     *
     * @throws NullPointerException If no stream is supplied.
     * @throws IllegalArgumentException If the supplied buffer size is negative or zero.
     */
    public AggregatorInputStreamProcessor(InputStream inputStream, int bufferSize) {
        if (inputStream == null)
            throw new NullPointerException();
        if (bufferSize <= 0)
            throw new IllegalArgumentException();

        this.inputStream = new BufferedReader(new InputStreamReader(inputStream), bufferSize);
    }

    @Override
    public String process() throws IOException {
        if (aggregatedResult != null)
            return aggregatedResult;

        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = inputStream.readLine()) != null) {
            if (builder.length() > 0) {
                builder.append(NEWLINE);
            }

            builder.append(line);
        }

        aggregatedResult = builder.toString();
        return aggregatedResult;
    }
}
