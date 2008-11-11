package flapjack.io;

import junit.framework.TestCase;

import java.io.File;


public class NioRecordReaderFactoryTest extends TestCase {

    public void test_build() {
        RecordReader reader = new NioRecordReaderFactory(10).build(new File("/commonline/core/io/test.txt"));
        assertNotNull(reader);
        assertTrue(reader instanceof NioRecordReader);

        NioRecordReader nioReader = (NioRecordReader) reader;
        assertEquals(10, nioReader.getRecordLength());
    }
}
