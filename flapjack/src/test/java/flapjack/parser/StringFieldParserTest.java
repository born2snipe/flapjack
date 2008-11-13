package flapjack.parser;

import flapjack.layout.SimpleFieldDefinition;
import junit.framework.TestCase;


public class StringFieldParserTest extends TestCase {

    public void test_parse() {
        StringFieldParser parser = new StringFieldParser();
        assertEquals("@1", parser.parse("@132", new SimpleFieldDefinition("", 0, 2)));
    }

    public void test_parse_DoNotTrim() {
        StringFieldParser parser = new StringFieldParser();
        assertEquals("32 ", parser.parse("@132  ", new SimpleFieldDefinition("", 2, 3), false));
    }

    public void test_parse_Trim() {
        StringFieldParser parser = new StringFieldParser();
        assertEquals("32", parser.parse("@132  ", new SimpleFieldDefinition("", 2, 3), true));
    }
}
