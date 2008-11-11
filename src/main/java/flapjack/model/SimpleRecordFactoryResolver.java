package flapjack.model;

import flapjack.layout.RecordLayout;


public class SimpleRecordFactoryResolver implements RecordFactoryResolver{
    public RecordFactory resolve(RecordLayout layout) {
        return new SimpleRecordFactory();
    }
}
