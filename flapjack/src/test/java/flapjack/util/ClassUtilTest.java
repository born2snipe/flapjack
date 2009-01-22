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
package flapjack.util;

import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import junit.framework.TestCase;

import java.lang.reflect.Field;


public class ClassUtilTest extends TestCase {

    public void test_setBean_NestedElement_EncounterANullElement() {
        NestClass domain = new NestClass();
        domain.fieldDef = null;

        try {
            ClassUtil.setBean(domain, "fieldDef.position", new Integer(10));
            fail();
        } catch (IllegalStateException err) {
            assertEquals("Choked on a null field 'fieldDef' on "+NestClass.class.getName(), err.getMessage());
        }
    }
    
    public void test_setBean_NestedElement() {
        NestClass domain = new NestClass();

        ClassUtil.setBean(domain, "fieldDef.position", new Integer(10));

        assertEquals(10, domain.fieldDef.getPosition());
    }
    
    public void test_setBean() {
        Domain domain = new Domain();

        ClassUtil.setBean(domain, "value", "newValue");

        assertEquals("newValue", domain.value);
    }
    
    public void test_setField_ValidBean() {
        Domain domain = new Domain();

        ClassUtil.setField(domain, "value", "newValue");

        assertEquals("newValue", domain.value);
    }

    public void test_setField_NoSetter() {
        NoSetterDomain domain = new NoSetterDomain();

        ClassUtil.setField(domain, "value", "newValue");

        assertEquals("newValue", domain.value);
    }

    public void test_findField_FieldNotFound_NestElement() {
        assertNull(ClassUtil.findField(NestClass.class, "fieldDef.doesNotExist"));
    }

    public void test_findField_FieldFound_NestElement() {
        Field field = ClassUtil.findField(new NestClass(), "fieldDef.position");

        assertNotNull(field);
        assertEquals("position", field.getName());
    }

    public void test_findField_FieldFound() {
        Field field = ClassUtil.findField(new SimpleFieldDefinition(), "position");

        assertNotNull(field);
        assertEquals("position", field.getName());
    }

    public void test_findField_FieldNotFound() {
        assertNull(ClassUtil.findField(new String(), "doesNotExist"));
    }

    public void test_findDefaultContructor() {
        assertNotNull(ClassUtil.findDefaultConstructor(SimpleRecordLayout.class));
        assertNull(ClassUtil.findDefaultConstructor(NoDefaultConstructor.class));
    }

    public void test_newInstance() {
        Object obj = ClassUtil.newInstance(SimpleRecordLayout.class);

        assertNotNull(obj);
        assertTrue(obj instanceof SimpleRecordLayout);
    }

    public void test_newInstance_NoDefaultConstructor() {
        try {
            ClassUtil.newInstance(NoDefaultConstructor.class);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Cannot construct without a default constructor", err.getMessage());
        }
    }

    private static class NoDefaultConstructor {
        private NoDefaultConstructor(String value) {
        }
    }

    private static class NestClass {
        private SimpleFieldDefinition fieldDef = new SimpleFieldDefinition();
    }

    public static class Domain extends NoSetterDomain {
        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class NoSetterDomain {
        protected String value;
    }
}
