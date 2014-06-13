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
package flapjack.example;

import flapjack.builder.RecordBuilder;
import flapjack.builder.SameBuilderRecordLayoutResolver;
import flapjack.example.model.Address;
import flapjack.io.LineRecordReader;
import flapjack.io.StreamRecordWriter;
import flapjack.layout.FieldDefinition;
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleRecordLayout;
import flapjack.layout.TextPaddingDescriptor;
import flapjack.model.BinaryFieldFactory;
import flapjack.model.DomainFieldFactory;
import flapjack.model.FieldByteMap;
import flapjack.model.ListMap;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.model.RecordFactory;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.FieldData;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import flapjack.util.TypeConverter;
import flapjack.util.ValueConverter;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class CompoundFieldTest {
    private SimpleRecordLayout recordLayout;
    private ObjectMapping objectMapping;
    private ObjectMappingStore objectMappingStore;

    @Before
    public void setUp() {
        /**
         * Create the record layout
         */
        recordLayout = new SimpleRecordLayout("user");
        recordLayout.field("First Name", 5, new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' '));
        recordLayout.field("Last Name", 10, new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' '));
        recordLayout.field("Address Line", 15, new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' '));
        recordLayout.field("City", 6, new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' '));
        recordLayout.field("State", 2, new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' '));

        /**
         * Create the ObjectMapping for the record
         */
        objectMapping = new ObjectMapping(Person.class);
        objectMapping.field("First Name", "firstName");
        objectMapping.field("Last Name", "lastName");
        objectMapping.field(new String[]{"Address Line", "City", "State"}, "address", new AddressFactory(), new AddressFactory());

        objectMappingStore = new ObjectMappingStore();
        objectMappingStore.add(objectMapping);
    }

    @Test
    public void test_build() {
        RecordBuilder builder = new RecordBuilder();
        builder.setBuilderRecordLayoutResolver(new SameBuilderRecordLayoutResolver(recordLayout));
        builder.setObjectMappingStore(objectMappingStore);

        Person person = new Person();
        person.firstName = "Joe";
        person.lastName = "Smith";
        person.address = new Address();
        person.address.setCity("City");
        person.address.setLine("123 Easy St");
        person.address.setState("IA");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        builder.build(person, new StreamRecordWriter(output));

        assertEquals("Joe  Smith     123 Easy St    City  IA", new String(output.toByteArray()));
    }

    @Test
    public void test_parse() throws IOException {
        String record = "Joe  Smith     123 Easy St    City  IA";

        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(recordLayout));
        recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(PersonFactory.class));
        recordParser.setObjectMappingStore(objectMappingStore);

        /**
         * Actually call the parser with our RecordReader
         */
        LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(record.getBytes()));
        ParseResult result = recordParser.parse(recordReader);

        assertEquals(0, result.getUnparseableRecords().size());
        assertEquals(0, result.getUnresolvedRecords().size());
        assertEquals(0, result.getPartialRecords().size());
        assertEquals(1, result.getRecords().size());

        Person person = (Person) result.getRecords().get(0);
        assertEquals("Joe", person.getFirstName().trim());
        assertEquals("Smith", person.getLastName().trim());
        assertNotNull(person.getAddress());
        assertEquals("123 Easy St", person.getAddress().getLine().trim());
        assertEquals("City", person.getAddress().getCity().trim());
        assertEquals("IA", person.getAddress().getState().trim());
    }

    /**
     * Create the DomainFieldFactory for constructing the Address from the 3 fields
     */
    private static class AddressFactory implements DomainFieldFactory, BinaryFieldFactory {
        public Object build(ListMap fields, Class domainFieldType, TypeConverter typeConverter) {
            Address address = new Address();
            ValueConverter converter = typeConverter.type(String.class);
            address.setLine((String) converter.toDomain(((FieldData) fields.get(0)).data));
            address.setCity((String) converter.toDomain(((FieldData) fields.get(1)).data));
            address.setState((String) converter.toDomain(((FieldData) fields.get(2)).data));
            return address;
        }

        public FieldByteMap build(Object domain, TypeConverter typeConverter, List fieldDefinitions) {
            FieldByteMap byteMap = new FieldByteMap();
            Address address = (Address) domain;
            ValueConverter converter = typeConverter.type(String.class);
            for (Object obj : fieldDefinitions) {
                FieldDefinition definition = (FieldDefinition) obj;
                if (definition.getName().equals("City")) {
                    byteMap.put(definition, converter.toBytes(address.getCity()));
                } else if (definition.getName().equals("State")) {
                    byteMap.put(definition, converter.toBytes(address.getState()));
                } else {
                    byteMap.put(definition, converter.toBytes(address.getLine()));
                }
            }
            return byteMap;
        }
    }

    private static class PersonFactory implements RecordFactory {
        public Object build(RecordLayout recordLayout) {
            return new Person();
        }
    }

    private static class Person {
        private String firstName;
        private String lastName;
        private Address address;

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }


}
