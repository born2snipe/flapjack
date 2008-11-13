package flapjack.example.intermediate;

import flapjack.example.model.Address;
import flapjack.example.model.User;
import flapjack.io.LineRecordReader;
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.RecordFactory;
import flapjack.model.RecordFactoryResolver;
import flapjack.parser.DefaultParseResult;
import flapjack.parser.RecordLayoutResolver;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.StringRecordFieldParser;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;


public class IntermediateTest extends TestCase {

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
        DefaultParseResult result = (DefaultParseResult) recordParser.parse(recordReader);

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
            field("Record Type", 2);
            field("First Name", 10);
            field("Last Name", 10);
            field("Username", 10);
        }
    }

    private static class AddressRecordLayout extends SimpleRecordLayout {
        private AddressRecordLayout() {
            field("Record Type", 2);
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
