package flapjack.parser;

import java.util.List;
import java.io.File;
import java.io.IOException;

public interface FileParser {
    List parse(File file) throws IOException;
}
