package flapjack.example.basic;

import flapjack.io.LineRecordReaderFactory;
import flapjack.io.StreamRecordReader;
import flapjack.io.LineRecordReader;
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import flapjack.parser.*;
import flapjack.model.RecordFactory;
import flapjack.model.RecordFactoryResolver;
import flapjack.example.model.User;
import junit.framework.TestCase;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Enumeration;
import java.net.URL;


public class BasicTest extends TestCase {

    public void test_success() throws Exception {
        String records = "Joe        Schmoe     jschmoe111 #\n" +
                "Jimmy      Smith      jsmith     #";

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
        DefaultParseResult result = (DefaultParseResult) recordParser.parse(recordReader);

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
            return new User((String)strings.get(0), (String)strings.get(1), (String)strings.get(2));
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
