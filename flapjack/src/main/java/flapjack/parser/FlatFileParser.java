package flapjack.parser;

import flapjack.io.RecordReaderFactory;

import java.io.File;
import java.io.IOException;

/**
 * Basic FileParser that defaults the RecordParser to RecordParserImpl.
 */
public class FlatFileParser implements FileParser {
    private RecordReaderFactory recordReaderFactory;
    private RecordParser recordParser;

    public FlatFileParser() {
        setRecordParser(new RecordParserImpl());
    }

    public ParseResult parse(File file) throws IOException {
        return recordParser.parse(recordReaderFactory.build(file));
    }

    public void setRecordReaderFactory(RecordReaderFactory recordReader) {
        this.recordReaderFactory = recordReader;
    }

    public void setRecordParser(RecordParser recordParser) {
        this.recordParser = recordParser;
    }
}
