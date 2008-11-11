package flapjack.io;

import java.io.File;

public class MappedRecordReaderFactory implements RecordReaderFactory {
    private int recordLength;

    public MappedRecordReaderFactory(int recordLength) {
        this.recordLength = recordLength;
    }

    public RecordReader build(File file) {
        MappedRecordReader recordReader = new MappedRecordReader(file);
        recordReader.setRecordLength(recordLength);
        return recordReader;
    }

}
