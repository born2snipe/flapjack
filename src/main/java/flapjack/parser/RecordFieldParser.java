package flapjack.parser;

import flapjack.layout.RecordLayout;


public interface RecordFieldParser {

    /**
     * Breaks up the data given into the fields specified by the record layout
     *
     * @param bytes        - the data representing a record
     * @param recordLayout - the layout of the record
     * @return a List of the fields
     * @throws ParseException - thrown when an error occurs while parsing the current record
     */
    Object parse(byte[] bytes, RecordLayout recordLayout) throws ParseException;
}
