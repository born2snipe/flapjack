package flapjack.io;

import flapjack.layout.RecordLayout;
import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.SimpleRecord;
import flapjack.parser.*;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;


public class FileTest extends TestCase {
    private RecordParserImpl recordParser;
    private FlatFileParser fileParser;
    private File file;

    protected void setUp() throws Exception {
        super.setUp();
        recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SimpleRecordLayoutResolver());
        recordParser.setRecordFieldParser(new StringRecordFieldParser());
        fileParser = new FlatFileParser();
        fileParser.setRecordParser(recordParser);
        
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        file = new File(loader.getResource("flapjack/io/two_megs.txt").toURI());
    }

    public void test_MappedFile() throws IOException, URISyntaxException {
        verifyRecordReader(new MappedRecordReaderFactory(10));
    }


    public void test_NIOFile() throws IOException, URISyntaxException {
        verifyRecordReader(new NioRecordReaderFactory(10));
    }

    public void test_StreamFile() throws IOException, URISyntaxException {
        verifyRecordReader(new FileStreamRecordReaderFactory(10));
    }

    private void verifyRecordReader(RecordReaderFactory recordReaderFactory) throws IOException {
        fileParser.setRecordReaderFactory(recordReaderFactory);

        DefaultParseResult result = (DefaultParseResult) fileParser.parse(file);

        Iterator it = result.getRecords().iterator();
        while (it.hasNext()) {
            SimpleRecord record = (SimpleRecord) it.next();
            assertEquals("1234567890", record.getField(0));
        }
        assertEquals(231000, result.getRecords().size());
    }

    private static class SimpleRecordLayoutResolver implements RecordLayoutResolver {
        public RecordLayout resolve(byte[] bytes) {
            SimpleRecordLayout layout = new SimpleRecordLayout();
            layout.addFieldDefinition(new SimpleFieldDefinition("data", 0, 10));
            return layout;
        }
    }
}
