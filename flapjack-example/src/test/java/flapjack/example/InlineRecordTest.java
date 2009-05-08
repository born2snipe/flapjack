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
import flapjack.example.model.User;
import flapjack.io.RecordReader;
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.model.RecordFactory;
import flapjack.model.RecordFactoryResolver;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordLayoutResolver;
import flapjack.parser.RecordParserImpl;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class InlineRecordTest extends TestCase {
    public void test() throws IOException {
        String records = "#1030Joe       Smith     jsmith    #20371234 Easy St        Chicago        IL";

        /**
         * Configure the ObjectMapping from the record data to the domain objects
         */
        ObjectMapping userMapping = new ObjectMapping(User.class);
        userMapping.field("First Name", "firstName");
        userMapping.field("Last Name", "lastName");
        userMapping.field("Username", "userName");

        ObjectMapping addressMapping = new ObjectMapping(Address.class);
        addressMapping.field("Address Line", "line");
        addressMapping.field("City", "city");
        addressMapping.field("State", "state");

        ObjectMappingStore objectMappingStore = new ObjectMappingStore();
        objectMappingStore.add(userMapping);
        objectMappingStore.add(addressMapping);

        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new BasicRecordLayoutResolver());
        recordParser.setRecordFactoryResolver(new BasicRecordFactoryResolver());
        recordParser.setObjectMappingStore(objectMappingStore);
        recordParser.setIgnoreUnmappedFields(true);

        /**
         * Actually call the parser with our RecordReader
         */
        InlineRecordReader recordReader = new InlineRecordReader(new ByteArrayInputStream(records.getBytes()));
        ParseResult result = recordParser.parse(recordReader);

        /**
         * Verify the contents read from the records have not been altered
         */
        assertNotNull(result);
        assertEquals(0, result.getPartialRecords().size());
        assertEquals(0, result.getUnparseableRecords().size());
        assertEquals(0, result.getUnresolvedRecords().size());
        assertEquals(2, result.getRecords().size());

        User user = (User) result.getRecords().get(0);
        assertEquals("Joe       ", user.getFirstName());
        assertEquals("Smith     ", user.getLastName());
        assertEquals("jsmith    ", user.getUserName());

        Address address = (Address) result.getRecords().get(1);
        assertEquals("1234 Easy St        ", address.getLine());
        assertEquals("Chicago        ", address.getCity());
        assertEquals("IL", address.getState());
    }

    /**
     * Created a new implementation of RecordReader to support the specific record format
     */
    private static class InlineRecordReader implements RecordReader {
        private InputStream input;

        private InlineRecordReader(InputStream input) {
            this.input = input;
        }

        public byte[] readRecord() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] code = new byte[2];
            byte[] length = new byte[3];

            int read = input.read(code);
            if (read == -1) {
                return null;
            }
            input.read(length);

            byte[] contents = new byte[Integer.parseInt(new String(length))];
            input.read(contents);

            baos.write(code, 0, 2);
            baos.write(length, 0, 3);
            baos.write(contents, 0, contents.length);

            return baos.toByteArray();
        }

        public void close() {

        }
    }

    /**
     * These RecordLayouts represent the different possible record types that should be encounted in out data
     */
    private static class UserRecordLayout extends SimpleRecordLayout {
        private UserRecordLayout() {
            super("user");
            field("Record Type", 2);
            field("Record Length", 3);
            field("First Name", 10);
            field("Last Name", 10);
            field("Username", 10);
        }
    }

    private static class AddressRecordLayout extends SimpleRecordLayout {
        private AddressRecordLayout() {
            super("address");
            field("Record Type", 2);
            field("Record Length", 3);
            field("Address Line", 20);
            field("City", 15);
            field("State", 2);
        }
    }

    /**
     * This class is responsible for determining what RecordLayout should be used based on the current record being processed.
     */
    private static class BasicRecordLayoutResolver implements RecordLayoutResolver {
        public RecordLayout resolve(byte[] bytes) {
            String code = new String(new byte[]{bytes[0], bytes[1]});
            if ("#1".equals(code)) {
                return new UserRecordLayout();
            } else if ("#2".equals(code)) {
                return new AddressRecordLayout();
            }
            return null;
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

    private static class AddressRecordFactory implements RecordFactory {
        public Object build() {
            return new Address();
        }
    }

    /**
     * This class is responsible for determining what RecordFactory should be used for the current RecordLayout
     */
    private static class BasicRecordFactoryResolver implements RecordFactoryResolver {
        public RecordFactory resolve(RecordLayout layout) {
            if (layout instanceof UserRecordLayout)
                return new UserRecordFactory();
            else if (layout instanceof AddressRecordLayout)
                return new AddressRecordFactory();
            else
                return null;
        }
    }
}
