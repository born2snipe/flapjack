package flapjack.parser;

import junit.framework.TestCase;


public class DefaultParseResultTest extends TestCase {
    private DefaultParseResult result;

    public void setUp() {
        result = new DefaultParseResult();
    }

    public void test_hadErrors_NoBadRecords() {
        assertFalse(result.hadErrors());
    }

    public void test_hadErrors_PartialRecord() {
        result.addPartialRecord(new BadRecord(null));

        assertTrue(result.hadErrors());
    }

    public void test_hadErrors_UnresolvedRecord() {
        result.addUnresolvedRecord(new BadRecord(null));

        assertTrue(result.hadErrors());
    }
    
    public void test_hadErrors_UnparseableRecord() {
        result.addUnparseableRecord(new BadRecord(null));

        assertTrue(result.hadErrors());
    }
}
