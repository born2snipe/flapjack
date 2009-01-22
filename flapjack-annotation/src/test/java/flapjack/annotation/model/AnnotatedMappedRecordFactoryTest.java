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
package flapjack.annotation.model;

import flapjack.annotation.Field;
import flapjack.annotation.Record;
import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import flapjack.util.TypeConverter;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


public class AnnotatedMappedRecordFactoryTest extends TestCase {
    private AnnotatedMappedRecordFactory recordFactory;
    private SimpleRecordLayout layout;

    protected void setUp() throws Exception {
        super.setUp();
        recordFactory = new AnnotatedMappedRecordFactory(Dummy.class, new TypeConverter());
        layout = new SimpleRecordLayout();
        layout.addFieldDefinition(new SimpleFieldDefinition("field1", 0, 1));
    }

    public void test_build_FieldDoesNotExists() {
        Map<String, byte[]> fields = new HashMap<String, byte[]>();
        fields.put("doesNotExist", "1".getBytes());

        Object obj = recordFactory.build(fields, layout);

        assertNotNull(obj);
        assertTrue(obj instanceof Dummy);

        Dummy dummy = (Dummy) obj;
        assertEquals(0, dummy.field1);
    }

    public void test_build_FieldExists() {
        Map<String, byte[]> fields = new HashMap<String, byte[]>();
        fields.put("field1", "1".getBytes());

        Object obj = recordFactory.build(fields, layout);

        assertNotNull(obj);
        assertTrue(obj instanceof Dummy);

        Dummy dummy = (Dummy) obj;
        assertEquals(1, dummy.field1);
    }

    public void test_build_MultipleFieldExists() {
        Map<String, byte[]> fields = new HashMap<String, byte[]>();
        fields.put("field1", "1".getBytes());
        fields.put("field2", "true".getBytes());

        Object obj = recordFactory.build(fields, layout);

        assertNotNull(obj);
        assertTrue(obj instanceof Dummy);

        Dummy dummy = (Dummy) obj;
        assertEquals(1, dummy.field1);
        assertTrue(dummy.field2);
    }


    @Record(SimpleRecordLayout.class)
    public static class Dummy {
        @Field("field1")
        private int field1;
        @Field("field2")
        private boolean field2;

        public void setField1(int field1) {
            this.field1 = field1;
        }

        public int getField1() {
            return field1;
        }

        public boolean isField2() {
            return field2;
        }

        public void setField2(boolean field2) {
            this.field2 = field2;
        }
    }

}
