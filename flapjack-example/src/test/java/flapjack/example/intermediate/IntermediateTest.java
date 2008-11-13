package flapjack.example.intermediate;

import junit.framework.TestCase;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.StringRecordFieldParser;
import flapjack.parser.DefaultParseResult;
import flapjack.parser.RecordLayoutResolver;
import flapjack.io.LineRecordReader;
import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleRecordLayout;
import flapjack.layout.SimpleFieldDefinition;
import flapjack.model.RecordFactory;
import flapjack.model.RecordFactoryResolver;

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
            addFieldDefinition(new SimpleFieldDefinition("Record Type", 0, 2));
            addFieldDefinition(new SimpleFieldDefinition("First Name", 2, 10));
            addFieldDefinition(new SimpleFieldDefinition("Last Name", 12, 10));
            addFieldDefinition(new SimpleFieldDefinition("Username", 22, 10));
        }
    }

    private static class AddressRecordLayout extends SimpleRecordLayout {
        private AddressRecordLayout() {
            addFieldDefinition(new SimpleFieldDefinition("Record Type", 0, 2));
            addFieldDefinition(new SimpleFieldDefinition("Address Line", 2, 20));
            addFieldDefinition(new SimpleFieldDefinition("City", 22, 15));
            addFieldDefinition(new SimpleFieldDefinition("State", 37, 2));
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
            return new User((String)strings.get(1), (String)strings.get(2), (String)strings.get(3));
        }
    }

    private static class AddressRecordFactory implements RecordFactory {
        public Object build(Object fields, RecordLayout recordLayout) {
            List strings = (List) fields;
            return new Address((String)strings.get(1), (String)strings.get(2), (String)strings.get(3));
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

    /**
     * My simple POJO representing a record
     */
    private static class User {
        public final String firstName, lastName, userName;

        private User(String firstName, String lastName, String userName) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.userName = userName;
        }
    }

    private static class Address {
        public final String line, city, state;

        private Address(String line, String city, String state) {
            this.line = line;
            this.city = city;
            this.state = state;
        }
    }
}
