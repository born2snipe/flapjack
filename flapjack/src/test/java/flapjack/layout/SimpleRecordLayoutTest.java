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

import junit.framework.TestCase;
import flapjack.util.DataType;


public class SimpleRecordLayoutTest extends TestCase {
    SimpleRecordLayout layout;

    public void setUp() {
        layout = new SimpleRecordLayout();
    }

    public void test_OneFieldDefinition() {
        layout.addFieldDefinition(new SimpleFieldDefinition("", 0, 2, DataType.TEXT));

        assertEquals(2, layout.getLength());
        assertEquals(1, layout.getFieldDefinitions().size());
    }
    
    public void test_TwoFieldDefinition() {
        layout.addFieldDefinition(new SimpleFieldDefinition("", 0, 2, DataType.TEXT));
        layout.addFieldDefinition(new SimpleFieldDefinition("", 2, 2, DataType.TEXT));

        assertEquals(4, layout.getLength());
        assertEquals(2, layout.getFieldDefinitions().size());
    }
    
    public void test_TwoFieldDefinition_OutOfOrder() {
        layout.addFieldDefinition(new SimpleFieldDefinition("", 2, 2, DataType.TEXT));
        layout.addFieldDefinition(new SimpleFieldDefinition("", 0, 2, DataType.TEXT));

        assertEquals(4, layout.getLength());
        assertEquals(2, layout.getFieldDefinitions().size());
        FieldDefinition fieldDef = (FieldDefinition) layout.getFieldDefinitions().get(0);
        assertEquals(0, fieldDef.getPosition());
    }
}
