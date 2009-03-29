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

import flapjack.util.TypeConverter;
import flapjack.util.ValueConverter;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


public class BeanPathObjectMapperTest extends TestCase {
    private BeanPathObjectMapper mapper;
    private Map fields;
    private Person person;
    private ObjectMappingStore mappingStore;

    protected void setUp() throws Exception {
        super.setUp();
        mappingStore = new ObjectMappingStore();
        mapper = new BeanPathObjectMapper();
        mapper.setObjectMappingStore(mappingStore);
        fields = new HashMap();
        person = new Person();
    }

    public void test_mapOnTo_MultipleFields() {
        ObjectMapping objMapping = new ObjectMapping(Person.class);
        objMapping.add("field1", "firstName");
        objMapping.add("field2", "lastName");

        mappingStore.add(objMapping);

        fields.put("field1", "Jim".getBytes());
        fields.put("field2", "Smith".getBytes());

        mapper.mapOnTo(fields, person);

        assertEquals("Jim", person.firstName);
        assertEquals("Smith", person.lastName);
    }

    public void test_mapOnTo_SingleField() {
        ObjectMapping objMapping = new ObjectMapping(Person.class);
        objMapping.add("field1", "firstName");
        mappingStore.add(objMapping);

        fields.put("field1", "Jim".getBytes());

        mapper.mapOnTo(fields, person);

        assertEquals("Jim", person.firstName);
    }

    public void test_mapOnTo_SingleField_UseCustomValueConverter() {
        TypeConverter typeConverter = new TypeConverter();
        typeConverter.registerConverter(new CustomConverter());

        mapper.setTypeConverter(typeConverter);

        ObjectMapping objMapping = new ObjectMapping(Person.class);
        objMapping.add("field1", "firstName", CustomConverter.class);
        mappingStore.add(objMapping);

        fields.put("field1", "Jim".getBytes());

        mapper.mapOnTo(fields, person);

        assertEquals("customValue", person.firstName);
    }

    public void test_mapOnTo_SingleField_UseCustomValueConverter_NotRegisteredInTypeConverter() {
        ObjectMapping objMapping = new ObjectMapping(Person.class);
        objMapping.add("field1", "firstName", CustomConverter.class);
        mappingStore.add(objMapping);

        fields.put("field1", "Jim".getBytes());

        try {
            mapper.mapOnTo(fields, person);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Could not find a flapjack.model.BeanPathObjectMapperTest$CustomConverter in the TypeConverter", err.getMessage());
        }
    }

    public void test_mapOnTo_FieldDoesNotExistOnDomain() {
        ObjectMapping objMapping = new ObjectMapping(Person.class);
        objMapping.add("field1", "address");
        mappingStore.add(objMapping);

        fields.put("field1", "Jim".getBytes());

        try {
            mapper.mapOnTo(fields, person);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Could not map field1 -> address on " + Person.class.getName() + ", \"address\" could NOT be found", err.getMessage());
        }
    }

    public void test_build_FieldMappingNotFoundForField() {
        mappingStore.add(new ObjectMapping(Person.class));

        fields.put("field1", "value".getBytes());

        try {
            mapper.mapOnTo(fields, person);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Could not locate field mapping for field=\"field1\"", err.getMessage());
        }
    }

    public void test_build_NoObjectMappingFound() {
        try {
            mapper.mapOnTo(fields, person);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Could not locate object mapping for class=" + Person.class.getName(), err.getMessage());
        }
    }

    public void test_build_IgnoreUnmappedFields() {
        mappingStore.add(new ObjectMapping(Person.class));

        mapper.setIgnoreUnmappedFields(true);
        fields.put("field1", "value".getBytes());

        mapper.mapOnTo(fields, person);
    }

    private static class Person {
        private String firstName;
        private String lastName;
    }

    private static class CustomConverter implements ValueConverter {
        public Object convert(byte[] bytes) {
            return "customValue";
        }
    }
}
