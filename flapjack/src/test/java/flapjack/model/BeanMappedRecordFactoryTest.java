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
package flapjack.model;

import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import flapjack.util.TypeConverter;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


public class BeanMappedRecordFactoryTest extends TestCase {
    private BeanMappedRecordFactory factory;
    private SimpleRecordLayout layout;
    private Map fieldMappings;

    public void setUp() {
        fieldMappings = new HashMap();
        fieldMappings.put("field1", "name");

        factory = new BeanMappedRecordFactory(Dummy.class, new TypeConverter());
        factory.setFieldMappings(fieldMappings);

        layout = new SimpleRecordLayout();
        layout.addFieldDefinition(new SimpleFieldDefinition("field1", 0, 1));
    }

    public void test_build_FieldMappingNotFoundForField_IgnoreUnmappedFields() {
        factory.setFieldMappings(new HashMap());
        factory.setIgnoreUnmappedFields(true);

        Map fields = new HashMap();
        fields.put("field1", "value".getBytes());

        Object obj = factory.build(fields, layout);

        assertNotNull(obj);
        assertTrue(obj instanceof Dummy);

        Dummy dummy = (Dummy) obj;
        assertNull(dummy.name);
    }

    public void test_build_FieldMappingNotFoundForField() {
        factory.setFieldMappings(new HashMap());

        Map fields = new HashMap();
        fields.put("field1", "value".getBytes());

        try {
            factory.build(fields, layout);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Could not locate field mapping for field=\"field1\"", err.getMessage());
        }
    }

    public void test_build_FieldOnParent() {
        Map fields = new HashMap();
        fields.put("field1", "value".getBytes());

        Object obj = factory.build(fields, layout);

        assertNotNull(obj);
        assertTrue(obj instanceof Dummy);

        Dummy dummy = (Dummy) obj;
        assertEquals("value", dummy.name);
    }

    public void test_build_FieldNested() {
        fieldMappings.put("field1", "nested.name");

        factory = new BeanMappedRecordFactory(NestedDummy.class, new TypeConverter());
        factory.setFieldMappings(fieldMappings);

        Map fields = new HashMap();
        fields.put("field1", "value".getBytes());

        Object obj = factory.build(fields, layout);

        assertNotNull(obj);
        assertTrue(obj instanceof NestedDummy);

        NestedDummy dummy = (NestedDummy) obj;
        assertEquals("value", dummy.nested.name);
    }

    private static class Dummy {
        private String name;
    }

    private static class NestedDummy {
        private Dummy nested = new Dummy();
    }
}
