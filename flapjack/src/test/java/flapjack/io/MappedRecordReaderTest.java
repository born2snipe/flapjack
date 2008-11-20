package flapjack.io;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;


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
            reader.buffers.add(ByteBuffer.wrap("123".getBytes()));

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
        reader.buffers.add(ByteBuffer.wrap("".getBytes()));

        assertNull(reader.readRecord());
    }

    public void test_readRecord_PartialRecord() throws IOException {
        reader.buffers.add(ByteBuffer.wrap("123".getBytes()));

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(3, actualRecord.length);
        assertEquals("123", new String(actualRecord));
    }

    public void test_readRecord_SingleRecord_MapRegionNotBigEnough() throws IOException {
        reader.buffers.add(ByteBuffer.wrap("123".getBytes()));
        reader.buffers.add(ByteBuffer.wrap("45".getBytes()));

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("12345", new String(actualRecord));
    }

    public void test_readRecord_SingleRecord() throws IOException {
        reader.buffers.add(ByteBuffer.wrap("12345".getBytes()));

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("12345", new String(actualRecord));
    }

    public void test_readRecord_TwoRecords() throws IOException {
        reader.buffers.add(ByteBuffer.wrap("1234567890".getBytes()));

        reader.readRecord();
        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("67890", new String(actualRecord));
    }

    private static class ShuntMappedRecordReader extends MappedRecordReader {
        private List buffers = new ArrayList();
        private int offset = 0;
        private long mappedOffset, mappedLength;

        public ShuntMappedRecordReader(File file) {
            super(file);
        }

        protected ByteBuffer mapSection(long offset, long length) {
            mappedOffset = offset;
            mappedLength = length;
            return (ByteBuffer) buffers.get(this.offset++);
        }

        protected void initializeChannel() throws FileNotFoundException {
        }
    }
}
