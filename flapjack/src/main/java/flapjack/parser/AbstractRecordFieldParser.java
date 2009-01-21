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

import flapjack.layout.FieldDefinition;
import flapjack.layout.RecordLayout;

import java.util.Iterator;


public abstract class AbstractRecordFieldParser implements RecordFieldParser {

    /**
     * Create your collection object for holding on to your fields
     *
     * @return collection obj
     */
    protected abstract Object createObject();

    /**
     * Process the logical field
     *
     * @param field      - logical field
     * @param definition - the field definition used for parsing the field
     * @param obj        - the collection object
     */
    protected abstract void processField(byte[] field, FieldDefinition definition, Object obj);

    public final Object parse(byte[] bytes, RecordLayout recordLayout) throws ParseException {
        final Object obj = createObject();

        if (bytes.length == 0) {
            return obj;
        }

        int offset = 0;
        FieldDefinition fieldDef = null;
        Iterator it = recordLayout.getFieldDefinitions().iterator();
        try {
            while (it.hasNext()) {
                fieldDef = (FieldDefinition) it.next();
                byte temp[] = new byte[fieldDef.getLength()];
                System.arraycopy(bytes, offset, temp, 0, temp.length);
                processField(temp, fieldDef, obj);
                offset += temp.length;
            }
        } catch (Exception err) {
            throw new ParseException(err, recordLayout, fieldDef);
        }

        return obj;
    }
}
