package flapjack.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StubRecordReader implements RecordReader {
    private List records = new ArrayList();
    private int offset;
    private boolean closed;

    public byte[] readRecord() throws IOException {
        if (offset > records.size() - 1) {
            return null;
        }
        return (byte[]) records.get(offset++);
    }

    public void close() {
        closed = true;
    }

    public void addRecord(byte[] record) {
        records.add(record);
    }

    public boolean isClosed() {
        return closed;
    }
}
