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

import flapjack.io.LineRecordReader;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.model.RecordFactory;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import flapjack.util.TypeConverter;
import flapjack.util.ValueConverter;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;


public class BeanFieldSpecificValueConverterTest extends TestCase {
    public void test() throws Exception {
        String records = "Joe        Schmoe     Y";

        /**
         * Configure the ObjectMapping from the record data to the domain objects
         */
        ObjectMapping userMapping = new ObjectMapping(Person.class);
        userMapping.field("First Name", "firstName");
        userMapping.field("Last Name", "lastName");
        userMapping.field("Parent", "parent", YesNoValueConverter.class);

        ObjectMappingStore objectMappingStore = new ObjectMappingStore();
        objectMappingStore.add(userMapping);

        /**
         * Create a TypeConverter with our custom ValueConverter
         */
        TypeConverter typeConverter = new TypeConverter();
        typeConverter.registerConverter(new YesNoValueConverter());


        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(new PersonRecordLayout()));
        recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(PersonFactory.class));
        recordParser.setObjectMappingStore(objectMappingStore);
        recordParser.setTypeConverter(typeConverter);

        /**
         * Actually call the parser with our RecordReader
         */
        LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(records.getBytes()));
        ParseResult result = recordParser.parse(recordReader);

        /**
         * Verify the contents read from the records have not been altered
         */
        assertNotNull(result);
        assertEquals(0, result.getPartialRecords().size());
        assertEquals(0, result.getUnparseableRecords().size());
        assertEquals(0, result.getUnresolvedRecords().size());
        assertEquals(1, result.getRecords().size());

        Person person = (Person) result.getRecords().get(0);
        assertEquals("Joe        ", person.getFirstName());
        assertEquals("Schmoe     ", person.getLastName());
        assertTrue(person.isParent());
    }

    /**
     * These RecordLayouts represent the different possible record types that should be encounted in out data
     */
    private static class PersonRecordLayout extends SimpleRecordLayout {
        private PersonRecordLayout() {
            super("person");
            field("First Name", 11);
            field("Last Name", 11);
            field("Parent", 1);
        }
    }

    /**
     * This class is responsible for creating the POJO that represents the given record.
     */
    private static class PersonFactory implements RecordFactory {
        public Object build() {
            return new Person();
        }
    }

    /**
     * Custom ValueConverter to handle Y/N as a Boolean
     */
    private static class YesNoValueConverter implements ValueConverter {
        public Object toDomain(byte[] bytes) {
            String value = new String(bytes);
            if (value.equalsIgnoreCase("Y")) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        public byte[] toBytes(Object domain) {
            return null;
        }
    }

    private static class Person {
        private String firstName;
        private String lastName;
        private boolean parent;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public boolean isParent() {
            return parent;
        }
    }
}
