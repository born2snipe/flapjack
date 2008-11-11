package flapjack.io;

import java.io.File;

/**
 * A Factory for RecordReaders
 */
public interface RecordReaderFactory {
    RecordReader build(File file);
}
