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
import flapjack.model.RecordFactoryResolver;
import flapjack.parser.*;
import flapjack.util.DataType;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.List;


public class StopProcessingAfterARecordIsFoundWithAProblemTest extends TestCase {

    public void test() throws Exception {
        // this record is not the proper length to match the RecordLayout
        String records = "Joe        Schmoe     jschmoe111";

        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(UserRecordLayout.class));
        recordParser.setRecordFactoryResolver(new BasicRecordFactoryResolver());
        recordParser.setRecordFieldParser(new StringRecordFieldParser());
        recordParser.setParseResultFactory(new ExplodindParseResultFactory());

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

    /**
     * This class is responsible for determining what RecordFactory should be used for the current RecordLayout
     */
    private static class BasicRecordFactoryResolver implements RecordFactoryResolver {
        public RecordFactory resolve(RecordLayout layout) {
            return new UserRecordFactory();
        }
    }
}
