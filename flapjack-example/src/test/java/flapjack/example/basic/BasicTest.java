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

        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new BasicRecordLayoutResolver());
        recordParser.setRecordFactoryResolver(new BasicRecordFactoryResolver());
        recordParser.setRecordFieldParser(new StringRecordFieldParser());

        DefaultParseResult result = (DefaultParseResult) recordParser.parse(new LineRecordReader(new ByteArrayInputStream(records.getBytes())));

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

    private static class BasicRecordLayoutResolver implements RecordLayoutResolver {
        public RecordLayout resolve(byte[] bytes) {
            SimpleRecordLayout recordLayout = new SimpleRecordLayout();
            recordLayout.addFieldDefinition(new SimpleFieldDefinition("First Name", 0, 11));
            recordLayout.addFieldDefinition(new SimpleFieldDefinition("Last Name", 11, 11));
            recordLayout.addFieldDefinition(new SimpleFieldDefinition("Username", 22, 11));
            recordLayout.addFieldDefinition(new SimpleFieldDefinition("Record Terminator", 33, 1));
            return recordLayout;
        }
    }

    private static class UserRecordFactory implements RecordFactory {
        public Object build(Object fields, RecordLayout recordLayout) {
            List strings = (List) fields;
            return new User((String)strings.get(0), (String)strings.get(1), (String)strings.get(2));
        }
    }

    private static class BasicRecordFactoryResolver implements RecordFactoryResolver {

        public RecordFactory resolve(RecordLayout layout) {
            return new UserRecordFactory();
        }
    }

    private static class User {
        public final String firstName, lastName, userName;

        private User(String firstName, String lastName, String userName) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.userName = userName;
        }
    }
}
