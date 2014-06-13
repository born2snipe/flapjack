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

import flapjack.annotation.Converter;
import flapjack.annotation.Field;
import flapjack.annotation.FieldLocator;
import flapjack.annotation.Record;
import flapjack.annotation.RecordPackageClassScanner;
import flapjack.annotation.ReflectionField;
import flapjack.model.FieldMapping;
import flapjack.model.ObjectMapping;
import flapjack.util.IntegerTextValueConverter;
import flapjack.util.ValueConverter;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AnnotatedObjectMappingStoreTest {
    private RecordPackageClassScanner classScanner;
    private FieldLocator fieldLocator;
    private AnnotatedObjectMappingStore store;

    @Before
    public void setUp() throws Exception {
        classScanner = mock(RecordPackageClassScanner.class);
        fieldLocator = mock(FieldLocator.class);
        store = new AnnotatedObjectMappingStore(classScanner, fieldLocator);
        store.setPackages(asList("some.package.name"));
    }

    @Test
    public void test_singleAnnotatedFieldWithConverter() {
        ReflectionField field = mock(ReflectionField.class);

        when(classScanner.scan(asList("some.package.name"))).thenReturn(asList(Person.class, Address.class));
        when(fieldLocator.gatherFieldIds(Person.class)).thenReturn(asList("firstName"));
        when(fieldLocator.locateById(Person.class, "firstName")).thenReturn(field);
        when(field.getName()).thenReturn("firstName");
        when(field.hasConverter()).thenReturn(true);
        when(field.getConverterClass()).thenReturn(IntegerTextValueConverter.class);

        ObjectMapping objectMapping = store.find(Person.class);

        FieldMapping fieldMapping = objectMapping.findDomainField("firstName");
        assertNotNull(fieldMapping);
        assertEquals(1, objectMapping.getFieldCount());
    }

    @Test
    public void test_singleAnnotatedField() {
        ReflectionField field = mock(ReflectionField.class);

        when(classScanner.scan(asList("some.package.name"))).thenReturn(asList(Person.class, Address.class));
        when(fieldLocator.gatherFieldIds(Person.class)).thenReturn(asList("firstName"));
        when(fieldLocator.locateById(Person.class, "firstName")).thenReturn(field);
        when(field.getName()).thenReturn("firstName");

        ObjectMapping objectMapping = store.find(Person.class);

        assertNotNull(objectMapping.findDomainField("firstName"));
        assertEquals(1, objectMapping.getFieldCount());
    }

    @Test
    public void test_find_FindMapping() {
        AnnotatedObjectMappingStore objMappingStore = new AnnotatedObjectMappingStore();
        objMappingStore.setPackages(asList("flapjack.annotation.model"));

        ObjectMapping objMapping = objMappingStore.find(Person.class);

        assertNotNull(objMapping);
        assertEquals(Person.class, objMapping.getMappedClass());
        assertEquals(2, objMapping.getFieldCount());
        assertNotNull(objMapping.findDomainField("firstName"));
        assertNotNull(objMapping.findDomainField("lastName"));
        assertNotNull(objMapping.findRecordField("last name"));
        assertNull(objMapping.findDomainField("middleIntial"));
        assertNull(objMapping.findDomainField("surname"));
        ObjectMapping addressMapping = objMappingStore.find(Address.class);
        assertNotNull(addressMapping);
        assertNotNull(addressMapping.findRecordField("line1"));
    }


    @Record("person")
    private static class Person {
        @Field
        private String firstName;
        @Field("last name")
        private String lastName;

        private String middleInitial;

        @Converter(CustomValueConverter.class)
        private String surname;
    }

    @Record("address")
    private static class Address {
        @Field("line1")
        @Converter(CustomValueConverter.class)
        private String line1;
    }

    public static class CustomValueConverter implements ValueConverter {
        public Object toDomain(byte[] bytes) {
            return null;
        }

        public byte[] toBytes(Object domain) {
            return null;
        }
    }
}
