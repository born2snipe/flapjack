/**
 * Copyright 2008-2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. 
 */
package flapjack.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains the results of parsing
 */
public class ParseResult {
    private List records = new ArrayList();
    private List partialRecords = new ArrayList();
    private List unresolvedRecords = new ArrayList();
    private List unparseableRecords = new ArrayList();

    public List getRecords() {
        return Collections.unmodifiableList(records);
    }

    /**
     * This method is called when a record was successfully parsed.
     *
     * @param obj - the domain object created from the record of data
     */
    public void addRecord(Object obj) {
        records.add(obj);
    }

    public List getPartialRecords() {
        return Collections.unmodifiableList(partialRecords);
    }

    /**
     * This method is called when a record read from a file does not match lengths with the RecordLayout
     *
     * @param record - the record that was read that caused the problem
     */
    public void addPartialRecord(BadRecord record) {
        partialRecords.add(record);
    }

    public List getUnresolvedRecords() {
        return Collections.unmodifiableList(unresolvedRecords);
    }

    /**
     * This method is called when a record read from a file could not resolve a RecordLayout
     *
     * @param record - the record that was read that caused the problem
     */
    public void addUnresolvedRecord(BadRecord record) {
        unresolvedRecords.add(record);
    }

    /**
     * This method is called when a record read from a file encoutered a ParseException
     *
     * @param record - the record that was read that caused the problem
     */
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
