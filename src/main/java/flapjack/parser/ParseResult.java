package flapjack.parser;

/**
 * Contains the results of parsing
 */
public interface ParseResult {

    /**
     * This method is called when a record was successfully parsed.
     *
     * @param obj - the domain object created from the record of data
     */
    void addRecord(Object obj);

    /**
     * This method is called when a record read from a file does not match lengths with the RecordLayout
     *
     * @param record - the record that was read that caused the problem
     */
    void addPartialRecord(BadRecord record);

    /**
     * This method is called when a record read from a file could not resolve a RecordLayout
     *
     * @param record - the record that was read that caused the problem
     */
    void addUnresolvedRecord(BadRecord record);

    /**
     * This method is called when a record read from a file encoutered a ParseException
     *
     * @param record - the record that was read that caused the problem
     */
    void addUnparseableRecord(BadRecord record);
}
