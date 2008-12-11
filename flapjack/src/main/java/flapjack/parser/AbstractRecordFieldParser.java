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

import java.util.Iterator;


public abstract class AbstractRecordFieldParser implements RecordFieldParser {

    protected abstract Object createObject();

    protected abstract void processField(byte[] field, FieldDefinition definition, Object obj);

    public Object parse(byte[] bytes, RecordLayout recordLayout) throws ParseException {
        final Object obj = createObject();

        if (bytes.length == 0) {
            return obj;
        }

        splitRecord(bytes, recordLayout, new FieldHandler() {
            public void handle(byte[] field, FieldDefinition definition) {
                processField(field, definition, obj);
            }
        });

        return obj;
    }

    protected void splitRecord(byte[] bytes, RecordLayout layout, FieldHandler handler) throws ParseException {
        int offset = 0;
        FieldDefinition fieldDef = null;
        Iterator it = layout.getFieldDefinitions().iterator();
        try {
            while (it.hasNext()) {
                fieldDef = (FieldDefinition) it.next();
                byte temp[] = new byte[fieldDef.getLength()];
                System.arraycopy(bytes, offset, temp, 0, temp.length);
                handler.handle(temp, fieldDef);
                offset += temp.length;
            }
        } catch (Exception err) {
            throw new ParseException(err, layout, fieldDef);
        }
    }

    protected interface FieldHandler {
        public void handle(byte[] field, FieldDefinition definition);
    }
}
