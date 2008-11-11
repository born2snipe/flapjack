package flapjack.io;

import junit.framework.TestCase;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URI;


public class FileStreamRecordReaderFactoryTest extends TestCase {
    public void test_build() throws URISyntaxException {
        FileStreamRecordReaderFactory factory = new FileStreamRecordReaderFactory(10);

        URI uri = getClass().getClassLoader().getResource("flapjack/io/test.txt").toURI();

        RecordReader recordReader = factory.build(new File(uri));

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof StreamRecordReader);
        assertEquals(10, ((StreamRecordReader) recordReader).getRecordLength());
    }
}
