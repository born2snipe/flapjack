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
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.model.RecordFactory;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.BadRecord;
import flapjack.parser.NotifyingParseResultFactory;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EventDrivenParsingTest extends TestCase {
    public void test() throws IOException {
        String data = "Joe        Schmoe     jschmoe111 #\n" +
                "Jimmy      Smith      jsmith     #";

        final List records = new ArrayList();

        /**
         * Create a NotifyingParseResultFactory then register you Notifier to be notified of any parsing events
         */
        NotifyingParseResultFactory resultFactory = new NotifyingParseResultFactory();
        resultFactory.addNotifier(new NotifyingParseResultFactory.Notifier() {
            public void addRecord(Object obj) {
                records.add(obj);
            }

            public void addPartialRecord(BadRecord record) {

            }

            public void addUnparseableRecord(BadRecord record) {

            }

            public void addUnresolvedRecord(BadRecord record) {

            }
        });

        /**
         * Configure the ObjectMapping from the record data to the User domain object
         */
        ObjectMapping userMapping = new ObjectMapping(User.class);
        userMapping.field("First Name", "firstName");
        userMapping.field("Last Name", "lastName");
        userMapping.field("Username", "userName");

        ObjectMappingStore objectMappingStore = new ObjectMappingStore();
        objectMappingStore.add(userMapping);

        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(new UserRecordLayout()));
        recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(UserRecordFactory.class));
        recordParser.setParseResultFactory(resultFactory);
        recordParser.setObjectMappingStore(objectMappingStore);
        recordParser.setIgnoreUnmappedFields(true);

        /**
         * Actually call the parser with our RecordReader
         */
        LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(data.getBytes()));
        recordParser.parse(recordReader);

        /**
         * Verify the contents read from the records have not been altered
         */
        assertEquals(2, records.size());

        User user1 = (User) records.get(0);
        assertEquals("Joe        ", user1.getFirstName());
        assertEquals("Schmoe     ", user1.getLastName());
        assertEquals("jschmoe111 ", user1.getUserName());

        User user2 = (User) records.get(1);
        assertEquals("Jimmy      ", user2.getFirstName());
        assertEquals("Smith      ", user2.getLastName());
        assertEquals("jsmith     ", user2.getUserName());
    }

    /**
     * These RecordLayouts represent the different possible record types that should be encounted in out data
     */
    private static class UserRecordLayout extends SimpleRecordLayout {
        private UserRecordLayout() {
            super("user");
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
        public Object build(RecordLayout recordLayout) {
            return new User();
        }
    }

}
