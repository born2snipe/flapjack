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

import flapjack.example.model.User;
import flapjack.io.LineRecordReader;
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.RecordFactory;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import flapjack.parser.StringRecordFieldParser;
import flapjack.util.DataType;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.List;


public class SameRecordTypeTest extends TestCase {

    public void test_success() throws Exception {
        String records = "Joe        Schmoe     jschmoe111 #\n" +
                "Jimmy      Smith      jsmith     #";

        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(UserRecordLayout.class));
        recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(UserRecordFactory.class));
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

        User user1 = (User) result.getRecords().get(0);
        assertEquals("Joe        ", user1.firstName);
        assertEquals("Schmoe     ", user1.lastName);
        assertEquals("jschmoe111 ", user1.userName);

        User user2 = (User) result.getRecords().get(1);
        assertEquals("Jimmy      ", user2.firstName);
        assertEquals("Smith      ", user2.lastName);
        assertEquals("jsmith     ", user2.userName);
    }

    /**
     * These RecordLayouts represent the different possible record types that should be encounted in out data
     */
    private static class UserRecordLayout extends SimpleRecordLayout {
        private UserRecordLayout() {
            field("First Name", 11, DataType.TEXT);
            field("Last Name", 11, DataType.TEXT);
            field("Username", 11, DataType.TEXT);
            field("Terminator", 1, DataType.TEXT);
        }
    }

    /**
     * This class is responsible for creating the POJO that represents the given record.
     */
    private static class UserRecordFactory implements RecordFactory {
        public Object build(Object fields, RecordLayout recordLayout) {
            List strings = (List) fields;
            return new User((String) strings.get(0), (String) strings.get(1), (String) strings.get(2));
        }
    }


}
