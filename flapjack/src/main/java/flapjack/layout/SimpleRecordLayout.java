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
package flapjack.layout;

import java.util.*;

/**
 * Basic implementation of the RecordLayout
 */
public class SimpleRecordLayout implements RecordLayout {
    private List fieldDefinitions = new ArrayList();
    private int offset = 0;

    public void addFieldDefinition(FieldDefinition fieldDef) {
        fieldDefinitions.add(fieldDef);
        Collections.sort(fieldDefinitions, new FieldDefinitionComparator());
    }

    protected void field(String name, int length) {
        addFieldDefinition(new SimpleFieldDefinition(name, offset, length));
        offset += length;
    }

    public List getFieldDefinitions() {
        return Collections.unmodifiableList(fieldDefinitions);
    }

    public int getLength() {
        int len = 0;
        Iterator it = fieldDefinitions.iterator();
        while (it.hasNext()) {
            FieldDefinition fieldDef = (FieldDefinition) it.next();
            len += fieldDef.getLength();
        }
        return len;
    }

    private static class FieldDefinitionComparator implements Comparator {

        public int compare(Object o, Object o1) {
            FieldDefinition fieldDef1 = (FieldDefinition) o;
            FieldDefinition fieldDef2 = (FieldDefinition) o1;
            return new Integer(fieldDef1.getPosition()).compareTo(new Integer(fieldDef2.getPosition()));
        }
    }
}
