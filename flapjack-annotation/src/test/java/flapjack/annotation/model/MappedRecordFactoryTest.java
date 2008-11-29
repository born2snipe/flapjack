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
package flapjack.annotation.model;

import flapjack.annotation.Field;
import flapjack.annotation.Record;
import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


public class MappedRecordFactoryTest extends TestCase {
    private MappedRecordFactory recordFactory;
    private SimpleRecordLayout layout;

    protected void setUp() throws Exception {
        super.setUp();
        recordFactory = new MappedRecordFactory(Dummy.class);
        layout = new SimpleRecordLayout();
        layout.addFieldDefinition(new SimpleFieldDefinition("field1", 0, 1));
    }
    
    public void test_build_FieldDoesNotExists() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("field2", "1");

        Object obj = recordFactory.build(fields, layout);

        assertNotNull(obj);
        assertTrue(obj instanceof Dummy);

        Dummy dummy = (Dummy) obj;
        assertEquals(0, dummy.field1);
    }

    public void test_build_FieldExists() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("field1", "1");

        Object obj = recordFactory.build(fields, layout);

        assertNotNull(obj);
        assertTrue(obj instanceof Dummy);

        Dummy dummy = (Dummy) obj;
        assertEquals(1, dummy.field1);
    }


    @Record(SimpleRecordLayout.class)
    public static class Dummy {
        @Field("field1")
        private int field1;

        public void setField1(int field1) {
            this.field1 = field1;
        }

        public int getField1() {
            return field1;
        }
    }
}
