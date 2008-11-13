package flapjack.parser;

import flapjack.io.RecordReader;

import java.io.IOException;


public interface RecordParser {
    ParseResult parse(RecordReader recordReader) throws IOException;
}
