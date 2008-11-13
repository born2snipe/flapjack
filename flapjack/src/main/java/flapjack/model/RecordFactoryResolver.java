package flapjack.model;

import flapjack.layout.RecordLayout;

/**
 * Resolves what RecordFactory should be used based on the RecordLayout
 */
public interface RecordFactoryResolver {

    /**
     * Attempts to resolve a RecordFactory for the given RecordLayout.
     * If no RecordFactory is found for the given RecordLayout return null.
     *
     * @param layout - the RecordLayout of the record currently being processed
     * @return the RecordFactory that is used for the given RecordLayout
     */
    RecordFactory resolve(RecordLayout layout);
}
