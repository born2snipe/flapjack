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

import flapjack.layout.FieldDefinition;


public class StringFieldParser {

    /**
     * Parses the field out of the line based on the position and length given
     *
     * @param line            - the current record line
     * @param fieldDefinition - the field to be parsed out of the given line
     * @return the field value
     */
    public String parse(String line, FieldDefinition fieldDefinition) {
        return parse(line, fieldDefinition, false);
    }

    /**
     * Parses the field out of the line based on the position and length given
     *
     * @param line            - the current record line
     * @param fieldDefinition - the field to be parsed out of the given line
     * @param trim            - should trim the String
     * @return the field value
     */
    public String parse(String line, FieldDefinition fieldDefinition, boolean trim) {
        int position = fieldDefinition.getPosition();
        int endPosition = position + fieldDefinition.getLength();
        String value = line.substring(position, endPosition);
        if (trim)
            return value.trim();
        else
            return value;
    }
}
