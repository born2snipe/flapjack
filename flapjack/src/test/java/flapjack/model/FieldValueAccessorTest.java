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

import junit.framework.TestCase;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;


public class FieldValueAccessorTest extends MockObjectTestCase {
    private FieldValueAccessor accessor;
    private Dummy obj;
    private Mock nullSafeValueFactory;

    protected void setUp() throws Exception {
        super.setUp();
        nullSafeValueFactory = mock(NullSafeValueFactory.class);
        accessor = new FieldValueAccessor((NullSafeValueFactory) nullSafeValueFactory.proxy());
        obj = new Dummy();
    }

    public void test_set_fieldNotFound() {
        try {
            accessor.set("value", obj, "doesNotExist");
            fail();
        } catch (ObjectMappingException err) {
            assertEquals("Could NOT find field \"doesNotExist\" on " + Dummy.class.getName(), err.getMessage());
        }
    }

    public void test_set_fieldOnNestedObject_EncountersANullElement() {
        try {
            accessor.set("value", obj, "nested.nullElement.string");
            fail();
        } catch (ObjectMappingException err) {
            assertEquals("Encountered a NULL element on " + Nested.class.getName() + " field \"nullElement\"", err.getMessage());
        }
    }

    public void test_set_fieldOnNestedObject() {
        accessor.set("value", obj, "nested.string");

        assertEquals("value", obj.nested.string);
    }

    public void test_set_fieldOnRoot() {
        accessor.set("value", obj, "string");

        assertEquals("value", obj.string);
    }

    public void test_set_nullValueGiven() {
        nullSafeValueFactory.expects(once()).method("build").with(eq(int.class)).will(returnValue(0));

        accessor.set(null, obj, "smallInt");

        assertEquals(0, obj.smallInt);
    }

    public void test_get_fieldOnRoot() {
        obj.string = "value";

        assertEquals("value", accessor.get(obj, "string"));
    }

    public void test_get_fieldOnNestedObject() {
        obj.nested.string = "value";

        assertEquals("value", accessor.get(obj, "nested.string"));
    }
    
    public void test_get_fieldOnNestedObject_EncountersANullElement() {
        try {
            accessor.get(obj, "nested.nullElement.string");
            fail();
        } catch (ObjectMappingException err) {
            assertEquals("Encountered a NULL element on " + Nested.class.getName() + " field \"nullElement\"", err.getMessage());
        }
    }


    private static class Dummy {
        private String string;
        private Nested nested = new Nested();
        private Nested nullElement = null;
        private int smallInt;
    }

    private static class Nested {
        private String string;
        private Nested nullElement = null;
    }
}
