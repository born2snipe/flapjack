package flapjack.model;

import flapjack.layout.RecordLayout;


public interface RecordFactory {

    /**
     * Creates the domain object the represents the record that contains the fields given
     *
     * @param fields       - the fields of the record just read
     * @param recordLayout - the RecordLayout used to parse the record
     * @return the domain object that represent the record
     */
    Object build(Object fields, RecordLayout recordLayout);
}
