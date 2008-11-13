package flapjack.parser;

import junit.framework.TestCase;


public class DefaultBadRecordFactoryTest extends TestCase {
    private DefaultBadRecordFactory factory;

    public void setUp() {
        factory = new DefaultBadRecordFactory();
    }

    public void test_ByteArray() {
        byte[] data = new byte[0];

        BadRecord badRecord = factory.build(data);

        assertSame(data, badRecord.getContents());
        assertNull(badRecord.getException());
    }
    
    public void test_ByteArrayWithException() {
        byte[] data = new byte[0];
        ParseException err = new ParseException(null, null, null);

        BadRecord badRecord = factory.build(data, err);

        assertSame(data, badRecord.getContents());
        assertSame(err, badRecord.getException());
    }
}
