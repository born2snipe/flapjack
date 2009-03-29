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
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.FieldMapping;
import flapjack.model.ObjectMapping;
import flapjack.util.ValueConverter;
import junit.framework.TestCase;

import java.util.Arrays;


public class AnnotatedObjectMappingStoreTest extends TestCase {

    public void test_find_FindMapping() {
        AnnotatedObjectMappingStore objMappingStore = new AnnotatedObjectMappingStore();
        objMappingStore.setPackages(Arrays.asList("flapjack.annotation.model"));

        ObjectMapping objMapping = objMappingStore.find(Person.class);

        assertNotNull(objMapping);
        assertEquals(Person.class, objMapping.getMappedClass());
        assertEquals(2, objMapping.getFieldCount());
        assertNotNull(objMapping.findDomainField("firstName"));
        assertNotNull(objMapping.findDomainField("lastName"));
        assertNotNull(objMapping.find("last name"));
        ObjectMapping addressMapping = objMappingStore.find(Address.class);
        assertNotNull(addressMapping);
        FieldMapping line1Mapping = addressMapping.find("line1");
        assertTrue(line1Mapping.getValueConverter().getClass().equals(CustomValueConverter.class));
    }

    @Record(SimpleRecordLayout.class)
    private static class Person {
        @Field
        private String firstName;
        @Field("last name")
        private String lastName;
    }

    @Record(SimpleRecordLayout.class)
    private static class Address {
        @Field(value = "line1", converter = CustomValueConverter.class)
        private String line1;
    }

    public static class CustomValueConverter implements ValueConverter {
        public Object convert(byte[] bytes) {
            return null;
        }

        public Class[] types() {
            return new Class[0];
        }
    }
}