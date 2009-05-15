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

import flapjack.example.model.Address;
import flapjack.io.LineRecordReader;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.*;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import flapjack.util.TypeConverter;
import flapjack.util.ValueConverter;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class CompoundFieldTest extends TestCase {
    public void test() throws IOException {
        String record = "Joe  Smith     123 Easy St    City  IA";

        /**
         * Create the record layout
         */
        SimpleRecordLayout recordLayout = new SimpleRecordLayout("user");
        recordLayout.field("First Name", 5);
        recordLayout.field("Last Name", 10);
        recordLayout.field("Address Line", 15);
        recordLayout.field("City", 6);
        recordLayout.field("State", 2);

        /**
         * Create the ObjectMapping for the record
         */
        ObjectMapping objectMapping = new ObjectMapping(Person.class);
        objectMapping.field("First Name", "firstName");
        objectMapping.field("Last Name", "lastName");
        objectMapping.field(new String[]{"Address Line", "City", "State"}, "address", new AddressFactory());

        ObjectMappingStore objectMappingStore = new ObjectMappingStore();
        objectMappingStore.add(objectMapping);


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
    private static class AddressFactory implements DomainFieldFactory {
        public Object build(ListMap fields, Class domainFieldType, TypeConverter typeConverter) {
            Address address = new Address();
            ValueConverter converter = typeConverter.type(String.class);
            address.setLine((String) converter.toDomain((byte[]) fields.get(0)));
            address.setCity((String) converter.toDomain((byte[]) fields.get(1)));
            address.setState((String) converter.toDomain((byte[]) fields.get(2)));
            return address;
        }
    }

    private static class PersonFactory implements RecordFactory {
        public Object build() {
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
