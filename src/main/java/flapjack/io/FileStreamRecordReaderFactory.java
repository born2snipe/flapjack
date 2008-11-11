package flapjack.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class FileStreamRecordReaderFactory implements RecordReaderFactory {
    private int recordLength;

    public FileStreamRecordReaderFactory(int recordLength) {
        this.recordLength = recordLength;
    }

    public RecordReader build(File file) {
        StreamRecordReader recordReader = null;
        try {
            recordReader = new StreamRecordReader(new FileInputStream(file));

            recordReader.setRecordLength(recordLength);
            return recordReader;
        } catch (IOException err) {
            throw new RuntimeException(err);
        }

    }
}
