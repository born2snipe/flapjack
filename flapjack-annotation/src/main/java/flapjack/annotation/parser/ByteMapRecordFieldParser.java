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

import flapjack.parser.ByteRecordFieldParser;
import flapjack.parser.ParseException;
import flapjack.layout.RecordLayout;
import flapjack.layout.FieldDefinition;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class ByteMapRecordFieldParser extends ByteRecordFieldParser {
    private MappedFieldIdGenerator fieldIdGenerator = new FieldNameGenerator();

    public Object parse(byte[] bytes, RecordLayout recordLayout) throws ParseException {
        List<byte[]> byteArrays = (List) super.parse(bytes, recordLayout);
        Map<Object, byte[]> fields = new HashMap<Object, byte[]>();
        for (int i = 0; i < byteArrays.size(); i++) {
            FieldDefinition fieldDef = (FieldDefinition) recordLayout.getFieldDefinitions().get(i);
            fields.put(fieldIdGenerator.generate(fieldDef), byteArrays.get(i));
        }
        return fields;
    }

    public void setFieldIdGenerator(MappedFieldIdGenerator fieldIdGenerator) {
        this.fieldIdGenerator = fieldIdGenerator;
    }
}
