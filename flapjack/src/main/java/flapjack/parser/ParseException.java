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
import flapjack.layout.FieldDefinition;

/**
 * An exception thrown when a problem occurs while parsing a record
 */
public class ParseException extends Exception {
    private FieldDefinition fieldDefinition;
    private RecordLayout recordLayout;

    public ParseException(Throwable cause, RecordLayout recordLayout, FieldDefinition fieldDefinition) {
        super(cause);
        this.recordLayout = recordLayout;
        this.fieldDefinition = fieldDefinition;
    }

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public RecordLayout getRecordLayout() {
        return recordLayout;
    }
}
