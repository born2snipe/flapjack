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
        IntegerTextValueConverter valueConverter = new IntegerTextValueConverter();
        mapping.add("field", "domainField", valueConverter);

        FieldMapping fieldMapping = mapping.find("field");

        assertEquals(valueConverter, fieldMapping.getValueConverter());
    }

    public void test_findField_noFieldMappings() {
        assertNull(mapping.find("doesNotExist"));
    }

    public void test_findField_WithMapping() {
        mapping.add("field", "domainField");

        FieldMapping fieldMapping = mapping.find("field");

        assertNotNull(fieldMapping);
        assertEquals("field", fieldMapping.getRecordFieldName());
        assertEquals("domainField", fieldMapping.getDomainFieldName());
        assertEquals(1, mapping.getFieldCount());
        assertSame(fieldMapping, mapping.findDomainField("domainField"));
    }

    public void test_findField_Uppercase() {
        mapping.add("field", "domainField");
        assertNotNull(mapping.find("FIELD"));
    }

    public void test_findField_Mixedcase() {
        mapping.add("field", "domainField");
        assertNotNull(mapping.find("Field"));
    }

}
