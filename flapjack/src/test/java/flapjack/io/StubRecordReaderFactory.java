package flapjack.io;

import java.io.File;


public class StubRecordReaderFactory implements RecordReaderFactory {
    private RecordReader reader;

    public StubRecordReaderFactory(RecordReader reader) {
        this.reader = reader;
    }

    public RecordReader build(File file) {
        return reader;
    }
}