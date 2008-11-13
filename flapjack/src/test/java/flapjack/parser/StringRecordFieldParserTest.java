package flapjack.parser;

import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import junit.framework.TestCase;

import java.util.List;


public class StringRecordFieldParserTest extends TestCase {
    private StringRecordFieldParser parser;
    private SimpleRecordLayout recordLayout;

    protected void setUp() throws Exception {
        super.setUp();
        parser = new StringRecordFieldParser();
        recordLayout = new SimpleRecordLayout();
        recordLayout.addFieldDefinition(new SimpleFieldDefinition("", 0, 1));
        recordLayout.addFieldDefinition(new SimpleFieldDefinition("", 1, 4));
    }

    public void test_parse_ThrowsException() {
        try {
            parser.parse("1".getBytes(), recordLayout);
            fail();
        } catch (ParseException err) {
            assertNotNull(err.getCause());
        }
    }

    public void test_parse() throws ParseException {
        List fields = (List) parser.parse("@1234".getBytes(), recordLayout);

        assertEquals(2, fields.size());
        assertEquals("@", fields.get(0));
        assertEquals("1234", fields.get(1));
    }

    public void test_parse_EmptyLine() throws ParseException {
        List fields = (List) parser.parse("".getBytes(), recordLayout);

        assertNotNull(fields);
        assertEquals(0, fields.size());
    }

}
