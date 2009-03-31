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

import flapjack.example.model.User;
import flapjack.io.LineRecordReader;
import flapjack.layout.FieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.model.RecordFactory;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.*;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;


public class CustomFieldIdentifierTest extends TestCase {
    public void test() throws Exception {
        String records = "Joe        Schmoe     jschmoe111 #";

        /**
         * Configure the ObjectMapping from the record data to the domain objects
         */
        ObjectMapping userMapping = new ObjectMapping(User.class);
        userMapping.add("emaN tsriF", "firstName");
        userMapping.add("emaN tsaL", "lastName");
        userMapping.add("emanresU", "userName");

        ObjectMappingStore objectMappingStore = new ObjectMappingStore();
        objectMappingStore.add(userMapping);

        /**
         *  Configure the RecordFieldParser to use the custom field id generator
         */
        ByteMapRecordFieldParser fieldParser = new ByteMapRecordFieldParser();
        fieldParser.setFieldIdGenerator(new ReverseDescriptionIdGenerator());

        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(UserRecordLayout.class));
        recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(UserRecordFactory.class));
        recordParser.setObjectMappingStore(objectMappingStore);
        recordParser.setIgnoreUnmappedFields(true);
        recordParser.setRecordFieldParser(fieldParser);

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

        User user1 = (User) result.getRecords().get(0);
        assertEquals("Joe        ", user1.getFirstName());
        assertEquals("Schmoe     ", user1.getLastName());
        assertEquals("jschmoe111 ", user1.getUserName());
    }

    /**
     * These RecordLayouts represent the different possible record types that should be encounted in out data
     */
    private static class UserRecordLayout extends SimpleRecordLayout {
        private UserRecordLayout() {
            field("First Name", 11);
            field("Last Name", 11);
            field("Username", 11);
            field("Terminator", 1);
        }
    }

    /**
     * This class is responsible for creating the POJO that represents the given record.
     */
    private static class UserRecordFactory implements RecordFactory {
        public Object build() {
            return new User();
        }
    }

    /**
     * This class creates the field identifier for a specific FieldDefinition
     */
    private static class ReverseDescriptionIdGenerator implements MappedFieldIdGenerator {
        public String generate(FieldDefinition fieldDef) {
            StringBuilder builder = new StringBuilder();
            char[] chars = fieldDef.getName().toCharArray();
            for (int i = chars.length - 1; i >= 0; i--) {
                builder.append(chars[i]);
            }
            return builder.toString();
        }
    }
}
