package flapjack.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class LineRecordReaderFactory implements RecordReaderFactory {
    public RecordReader build(File file) {
        try {
            return new LineRecordReader(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
