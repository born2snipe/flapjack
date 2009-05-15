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

import java.util.HashMap;
import java.util.Map;


public class FieldNameMassagerTest extends TestCase {
    private FieldNameMassager massager;

    protected void setUp() throws Exception {
        super.setUp();
        massager = new FieldNameMassager();
    }

    public void test_get_ExactMatch() {
        assertEquals("value", massager.get(createMap("Field", "value"), "Field"));
    }

    public void test_get_Lowercase() {
        assertEquals("rabbit", massager.get(createMap("Field", "rabbit"), "field"));
    }

    public void test_get_Uppercase() {
        assertEquals("rabbit", massager.get(createMap("Field", "rabbit"), "FIELD"));
    }

    public void test_get_Mixedcase() {
        assertEquals("rabbit", massager.get(createMap("Field", "rabbit"), "FielD"));
    }

    public void test_get_NoMatch() {
        assertNull(massager.get(createMap("Field", "rabbit"), "DoesNotExist"));
    }

    private Map createMap(String fieldName, String value) {
        Map fields = new HashMap();
        fields.put(fieldName, value);
        return fields;
    }


}
