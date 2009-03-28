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
