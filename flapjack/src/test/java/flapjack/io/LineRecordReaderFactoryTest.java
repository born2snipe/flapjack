package flapjack.io;

import junit.framework.TestCase;

import java.io.File;
import java.net.URL;
import java.net.URISyntaxException;


public class LineRecordReaderFactoryTest extends TestCase {

    public void test() throws URISyntaxException {
        LineRecordReaderFactory factory = new LineRecordReaderFactory();

        URL url = getClass().getClassLoader().getResource("flapjack/io/test.txt");
        RecordReader reader = factory.build(new File(url.toURI()));

        assertNotNull(reader);
        assertTrue(reader instanceof LineRecordReader);
    }
}
