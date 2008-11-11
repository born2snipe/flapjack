package flapjack.model;

import flapjack.layout.RecordLayout;

/**
 * Resolves what RecordFactory should be used based on the RecordLayout
 */
public interface RecordFactoryResolver {
    RecordFactory resolve(RecordLayout layout);
}
