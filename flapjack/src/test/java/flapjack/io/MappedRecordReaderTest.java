package flapjack.io;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;


public class MappedRecordReaderTest extends TestCase {
    private ShuntMappedRecordReader reader;
    private static final File FILE = new File("/commonline/core/io/test.txt");

    public void setUp() {
        reader = new ShuntMappedRecordReader(FILE);
        reader.setRecordLength(5);
    }


    public void test_readRecord_RecordLengthNotSet() throws IOException {
        try {
            reader = new ShuntMappedRecordReader(FILE);
            reader.buffer = ByteBuffer.wrap("123".getBytes());

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

    public void test_readRecord_EOF() throws IOException {
        reader.buffer = ByteBuffer.wrap("".getBytes());

        assertNull(reader.readRecord());
    }

    public void test_readRecord_PartialRecord() throws IOException {
        reader.buffer = ByteBuffer.wrap("123".getBytes());

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(3, actualRecord.length);
        assertEquals("123", new String(actualRecord));
    }

    public void test_readRecord_SingleRecord() throws IOException {
        reader.buffer = ByteBuffer.wrap("12345".getBytes());

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("12345", new String(actualRecord));
    }

    public void test_readRecord_TwoRecords() throws IOException {
        reader.buffer = ByteBuffer.wrap("1234567890".getBytes());

        reader.readRecord();
        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("67890", new String(actualRecord));
    }

    private static class ShuntMappedRecordReader extends MappedRecordReader {
        private ByteBuffer buffer;

        public ShuntMappedRecordReader(File file) {
            super(file);
        }

        protected ByteBuffer mapSection() {
            return buffer;
        }
    }
}
