package flapjack.io;

import java.io.File;


public class NioRecordReaderFactory implements RecordReaderFactory {
    private int recordLength;

    public NioRecordReaderFactory(int recordLength) {
        this.recordLength = recordLength;
    }

    public RecordReader build(File file) {
        NioRecordReader recordReader = new NioRecordReader(file);
        recordReader.setRecordLength(recordLength);
        return recordReader;
    }
}
