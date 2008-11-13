package flapjack.io;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class StreamRecordReaderTest extends TestCase {

    private StreamRecordReader createRecordReader(String content) {
        StreamRecordReader reader = new StreamRecordReader(new ByteArrayInputStream(content.getBytes()));
        reader.setRecordLength(5);
        return reader;
    }

    public void test_readRecord_EOF() throws IOException {
        StreamRecordReader reader = createRecordReader("");

        assertNull(reader.readRecord());
    }

    public void test_readRecord_PartialRecord() throws IOException {
        StreamRecordReader reader = createRecordReader("123");

        byte[] actual = reader.readRecord();

        assertNotNull(actual);
        assertEquals(3, actual.length);
        assertEquals("123", new String(actual));
    }

    public void test_readRecord_TwoRecords() throws IOException {
        StreamRecordReader reader = createRecordReader("1234567890");

        byte[] actual = reader.readRecord();

        assertNotNull(actual);
        assertEquals(5, actual.length);
        assertEquals("12345", new String(actual));

        byte[] actual2 = reader.readRecord();

        assertNotNull(actual2);
        assertEquals(5, actual2.length);
        assertEquals("67890", new String(actual2));
    }

    public void test_readRecord_SingleRecord() throws IOException {
        StreamRecordReader reader = createRecordReader("12345");

        byte[] actual = reader.readRecord();

        assertNotNull(actual);
        assertEquals(5, actual.length);
        assertEquals("12345", new String(actual));
    }

    private ByteArrayInputStream createInputStream(String contents) {
        return new ByteArrayInputStream(contents.getBytes());
    }

}
