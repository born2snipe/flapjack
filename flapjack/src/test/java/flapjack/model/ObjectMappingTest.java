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

import flapjack.util.IntegerTextValueConverter;
import junit.framework.TestCase;


public class ObjectMappingTest extends TestCase {
    private ObjectMapping mapping;

    protected void setUp() throws Exception {
        super.setUp();
        mapping = new ObjectMapping(String.class);
    }

    public void test_add_WithValueConverter() {
        mapping.add("field", "domainField", IntegerTextValueConverter.class);

        FieldMapping fieldMapping = mapping.findRecordField("field");

        assertEquals(IntegerTextValueConverter.class, fieldMapping.getValueConverterClass());
    }

    public void test_findRecordField_noFieldMappings() {
        assertNull(mapping.findRecordField("doesNotExist"));
    }

    public void test_findRecordField_WithMapping() {
        mapping.add("field", "domainField");

        FieldMapping fieldMapping = mapping.findRecordField("field");

        assertNotNull(fieldMapping);
        assertEquals("field", fieldMapping.getRecordFieldName());
        assertEquals("domainField", fieldMapping.getDomainFieldName());
        assertEquals(1, mapping.getFieldCount());
        assertSame(fieldMapping, mapping.findDomainField("domainField"));
    }

    public void test_findDomainField_Null() {
        assertNull(mapping.findDomainField(null));
    }

    public void test_findRecordField_Null() {
        assertNull(mapping.findRecordField(null));
    }

    public void test_findRecordField_Uppercase() {
        mapping.add("field", "domainField");
        assertNotNull(mapping.findRecordField("FIELD"));
    }

    public void test_findRecordField_Mixedcase() {
        mapping.add("field", "domainField");
        assertNotNull(mapping.findRecordField("Field"));
    }

    public void test_hasFieldMappingFor_noFieldMappings() {
        assertFalse(mapping.hasFieldMappingFor("Field"));
    }

    public void test_hasFieldMappingFor_hasFieldMappings() {
        mapping.add("Field", "domainField");

        assertTrue(mapping.hasFieldMappingFor("Field"));
    }
}
