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
package flapjack.annotation.parser;

import flapjack.layout.FieldDefinition;
import flapjack.layout.RecordLayout;
import flapjack.parser.AbstractRecordFieldParser;
import flapjack.parser.ParseException;

import java.util.HashMap;
import java.util.Map;


public class ByteMapRecordFieldParser extends AbstractRecordFieldParser {
    private MappedFieldIdGenerator fieldIdGenerator = new FieldNameGenerator();

    public Object parse(byte[] bytes, RecordLayout recordLayout) throws ParseException {
        final Map<Object, byte[]> fields = new HashMap<Object, byte[]>();
        splitRecord(bytes, recordLayout, new FieldHandler() {
            public void handle(byte[] field, FieldDefinition definition) {
                fields.put(fieldIdGenerator.generate(definition), field);
            }
        });
        return fields;
    }

    public void setFieldIdGenerator(MappedFieldIdGenerator fieldIdGenerator) {
        this.fieldIdGenerator = fieldIdGenerator;
    }
}
