package flapjack.example;

import flapjack.example.model.User;
import flapjack.io.LineRecordReader;
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.RecordFactory;
import flapjack.model.RecordFactoryResolver;
import flapjack.parser.*;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


public class StopProcessingAfterARecordIsFoundWithAProblemTest extends TestCase {

    public void test() throws Exception {
        // this record is not the proper length to match the RecordLayout
        String records = "Joe        Schmoe     jschmoe111";

        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new BasicRecordLayoutResolver());
        recordParser.setRecordFactoryResolver(new BasicRecordFactoryResolver());
        recordParser.setRecordFieldParser(new StringRecordFieldParser());
        recordParser.setParseResultFactory(new ExplodindParseResultFactory());

        /**
         * Actually call the parser with our RecordReader
         */
        LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(records.getBytes()));
        try {
            DefaultParseResult result = (DefaultParseResult) recordParser.parse(recordReader);
            fail();
        } catch (BadRecordException err) {

        }
    }

    /**
     * This class is a different implementation of the ParseResult that will
     *  throw an exception when a BadRecord is found.
     */
    private static class ExplodingParseResult implements ParseResult {
        private List records = new ArrayList();

        public void addRecord(Object obj) {
            records.add(obj);
        }

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
     * This class is responsible for determining what RecordLayout should be used based on the current record being processed.
     */
    private static class BasicRecordLayoutResolver implements RecordLayoutResolver {
        public RecordLayout resolve(byte[] bytes) {
            return new UserRecordLayout();
        }
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
