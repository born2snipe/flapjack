/**
 * Copyright 2008 Dan Dudley
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
import flapjack.io.LineRecordReader;
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.RecordFactory;
import flapjack.model.RecordFactoryResolver;
import flapjack.parser.*;
import flapjack.util.DataType;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


public class DifferentRecordTypeTest extends TestCase {

    public void test() throws IOException {
        String records = "#1Joe       Smith     jsmith    \n" +
                "#21234 Easy St        Chicago        IL";
        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new BasicRecordLayoutResolver());
        recordParser.setRecordFactoryResolver(new BasicRecordFactoryResolver());
        recordParser.setRecordFieldParser(new StringRecordFieldParser());

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
        assertEquals(2, result.getRecords().size());

        User user = (User) result.getRecords().get(0);
        assertEquals("Joe       ", user.firstName);
        assertEquals("Smith     ", user.lastName);
        assertEquals("jsmith    ", user.userName);

        Address address = (Address) result.getRecords().get(1);
        assertEquals("1234 Easy St        ", address.line);
        assertEquals("Chicago        ", address.city);
        assertEquals("IL", address.state);
    }

    /**
     * These RecordLayouts represent the different possible record types that should be encounted in out data
     */
    private static class UserRecordLayout extends SimpleRecordLayout {
        private UserRecordLayout() {
            field("Record Type", 2, DataType.TEXT);
            field("First Name", 10, DataType.TEXT);
            field("Last Name", 10, DataType.TEXT);
            field("Username", 10, DataType.TEXT);
        }
    }

    private static class AddressRecordLayout extends SimpleRecordLayout {
        private AddressRecordLayout() {
            field("Record Type", 2, DataType.TEXT);
            field("Address Line", 20, DataType.TEXT);
            field("City", 15, DataType.TEXT);
            field("State", 2, DataType.TEXT);
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
        public Object build(Object fields, RecordLayout recordLayout) {
            List strings = (List) fields;
            return new User((String) strings.get(1), (String) strings.get(2), (String) strings.get(3));
        }
    }

    private static class AddressRecordFactory implements RecordFactory {
        public Object build(Object fields, RecordLayout recordLayout) {
            List strings = (List) fields;
            return new Address((String) strings.get(1), (String) strings.get(2), (String) strings.get(3));
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
