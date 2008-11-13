package flapjack.parser;

import flapjack.layout.RecordLayout;

/**
 * This is used to resolve what RecordLayout should be used based on identity bytes.
 */
public interface RecordLayoutResolver {

    /**
     * Resolves what record layout is represented by the given bytes
     *
     * @param bytes - the bytes containing the identity field
     * @return the RecordLayout found using the bytes given
     */
    RecordLayout resolve(byte[] bytes);

}
