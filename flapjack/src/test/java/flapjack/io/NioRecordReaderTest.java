package flapjack.io;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ScatteringByteChannel;


public class NioRecordReaderTest extends TestCase {
    private ShuntNioRecordReader reader;
    private static final File FILE = new File("/commonline/core/io/test.txt");

    protected void setUp() throws Exception {
        super.setUp();

        reader = new ShuntNioRecordReader(FILE);
        reader.setRecordLength(5);
    }

    public void test_readRecord_RecordLengthNeverSet() throws IOException {
        try {
            reader = new ShuntNioRecordReader(FILE);
            reader.channel = new ByteArrayChannel("".getBytes());

            reader.readRecord();
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Record length MUST be greater than zero", err.getMessage());
        }
    }

    public void test_setRecordLength_NegativeRecordLength() {
        try {
            reader.setRecordLength(-1);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Record length MUST be greater than zero", err.getMessage());
        }
    }

    public void test_setRecordLength_ZeroRecordLength() {
        try {
            reader.setRecordLength(0);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Record length MUST be greater than zero", err.getMessage());
        }
    }

    public void test_readRecord_ReachedEndOfFile() throws IOException {
        reader.channel = new ByteArrayChannel("".getBytes());

        assertNull(reader.readRecord());
    }

    public void test_readRecord_HasPartialRecord() throws IOException {
        reader.channel = new ByteArrayChannel("123".getBytes());

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(3, actualRecord.length);
        assertEquals("123", new String(actualRecord));
    }

    public void test_readRecord_HasOneRecord() throws IOException {
        reader.channel = new ByteArrayChannel("12345".getBytes());

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("12345", new String(actualRecord));
    }

    public void test_readRecord_HasTwoRecords() throws IOException {
        reader.channel = new ByteArrayChannel("1234567890".getBytes());

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("12345", new String(actualRecord));

        byte[] actualRecord2 = reader.readRecord();

        assertNotNull(actualRecord2);
        assertEquals(5, actualRecord2.length);
        assertEquals("67890", new String(actualRecord));
    }

    private static class ShuntNioRecordReader extends NioRecordReader {
        private ScatteringByteChannel channel;

        public ShuntNioRecordReader(File file) {
            super(file);
        }

        protected ScatteringByteChannel createChannel(File file) {
            return channel;
        }
    }

}
