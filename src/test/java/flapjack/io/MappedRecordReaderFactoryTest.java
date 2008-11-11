package flapjack.io;

import junit.framework.TestCase;

import java.io.File;


public class MappedRecordReaderFactoryTest extends TestCase {

    public void test_build() {
        MappedRecordReaderFactory factory = new MappedRecordReaderFactory(10);

        RecordReader recordReader = factory.build(new File("/commonline/core/io/test.txt"));

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof MappedRecordReader);
        assertEquals(10, ((MappedRecordReader) recordReader).getRecordLength());
    }
}
