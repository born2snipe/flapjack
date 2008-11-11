package flapjack.parser;

import flapjack.io.RecordReader;
import flapjack.io.RecordReaderFactory;
import flapjack.io.StubRecordReader;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.io.File;
import java.io.IOException;


public class FlatFileParserTest extends MockObjectTestCase {
    private FlatFileParser parser;
    private Mock recordReaderFactory;
    private RecordReader recordReader;
    private Mock recordParser;

    protected void setUp() throws Exception {
        recordReaderFactory = mock(RecordReaderFactory.class);
        recordParser = mock(RecordParser.class);

        recordReader = new StubRecordReader();

        parser = new FlatFileParser();
        parser.setRecordReaderFactory((RecordReaderFactory) recordReaderFactory.proxy());
        parser.setRecordParser((RecordParser) recordParser.proxy());
    }

    public void test_parse_() throws IOException {
        File file = new File("test.txt");
        ParseResult expectedResult = new DefaultParseResult();

        recordReaderFactory.expects(once()).method("build").with(eq(file)).will(returnValue(recordReader));

        recordParser.expects(once()).method("parse").with(eq(recordReader)).will(returnValue(expectedResult));


        ParseResult result = parser.parse(file);

        assertNotNull(result);
        assertSame(expectedResult, result);
    }
}
