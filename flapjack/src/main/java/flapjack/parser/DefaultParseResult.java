package flapjack.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A basic ParseResult that holds on to a collection of records, partial records, unresolvable records, and unparseable records.
 */
public class DefaultParseResult implements ParseResult {
    private List records = new ArrayList();
    private List partialRecords = new ArrayList();
    private List unresolvedRecords = new ArrayList();
    private List unparseableRecords = new ArrayList();

    public List getRecords() {
        return Collections.unmodifiableList(records);
    }

    public void addRecord(Object obj) {
        records.add(obj);
    }

    public List getPartialRecords() {
        return Collections.unmodifiableList(partialRecords);
    }

    public void addPartialRecord(BadRecord record) {
        partialRecords.add(record);
    }

    public List getUnresolvedRecords() {
        return Collections.unmodifiableList(unresolvedRecords);
    }

    public void addUnresolvedRecord(BadRecord record) {
        unresolvedRecords.add(record);
    }

    public void addUnparseableRecord(BadRecord record) {
        unparseableRecords.add(record);
    }

    public boolean hadErrors() {
        return partialRecords.size() > 0 || unresolvedRecords.size() > 0 || unparseableRecords.size() > 0;
    }

    public List getUnparseableRecords() {
        return Collections.unmodifiableList(unparseableRecords);
    }
}
