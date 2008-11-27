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
import flapjack.layout.RecordLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Breaks up the array of bytes into fields and converts them to Strings
 */
public class StringRecordFieldParser extends ByteRecordFieldParser {
    public Object parse(byte[] bytes, RecordLayout recordLayout) throws ParseException {
        List byteArrays = (List) super.parse(bytes, recordLayout);
        if (byteArrays.size() == 0) {
            return byteArrays;
        }

        List fields = new ArrayList();
        Iterator it = byteArrays.iterator();
        while (it.hasNext()) {
            fields.add(new String((byte[]) it.next()));
        }

        return fields;
    }


}
