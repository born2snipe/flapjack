/**
 * Copyright 2008 Dan Dudley
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
