package flapjack.parser;

import java.io.File;
import java.io.IOException;

public interface FileParser {
    ParseResult parse(File file) throws IOException;
}
