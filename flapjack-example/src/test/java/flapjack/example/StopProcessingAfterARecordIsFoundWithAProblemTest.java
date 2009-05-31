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
import flapjack.model.RecordFactoryResolver;
import flapjack.parser.*;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;


public class StopProcessingAfterARecordIsFoundWithAProblemTest extends TestCase {

    public void test() throws Exception {
        // this record is not the proper length to match the RecordLayout
        String records = "Joe        Schmoe     jschmoe111";

        /**
         * Configure the ObjectMapping from the record data to the domain objects
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
        recordParser.setRecordFactoryResolver(new BasicRecordFactoryResolver());
        recordParser.setParseResultFactory(new ExplodindParseResultFactory());
        recordParser.setObjectMappingStore(objectMappingStore);
        recordParser.setIgnoreUnmappedFields(true);

        /**
         * Actually call the parser with our RecordReader
         */
        LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(records.getBytes()));
        try {
            recordParser.parse(recordReader);
            fail();
        } catch (BadRecordException err) {

        }
    }

    /**
     * This class is a different implementation of the ParseResult that will
     * throw an exception when a BadRecord is found.
     */
    private static class ExplodingParseResult extends ParseResult {
        public void addPartialRecord(BadRecord record) {
            throw new BadRecordException();
        }

        public void addUnresolvedRecord(BadRecord record) {
            throw new BadRecordException();
        }

        public void addUnparseableRecord(BadRecord record) {
            throw new BadRecordException();
        }

    }

    /**
     * Factory for creating our ExplodingParseResults
     */
    private static class ExplodindParseResultFactory implements ParseResultFactory {
        public ParseResult build() {
            return new ExplodingParseResult();
        }
    }

    /**
     * This error will be thrown when a record can not be parsed correctly
     */
    private static class BadRecordException extends RuntimeException {
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

    /**
     * This class is responsible for determining what RecordFactory should be used for the current RecordLayout
     */
    private static class BasicRecordFactoryResolver implements RecordFactoryResolver {
        public RecordFactory resolve(RecordLayout layout) {
            return new UserRecordFactory();
        }
    }
}
