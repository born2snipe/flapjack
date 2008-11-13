package flapjack.io;

import java.io.*;

/**
 * RecordReader implementation using the Java IO Stream API
 */
public class StreamRecordReader implements RecordReader {
    private int recordLength;
    private InputStream input;

    public StreamRecordReader(InputStream inputStream) {
        this.input = inputStream;
    }

    public byte[] readRecord() throws IOException {
        byte[] buffer = new byte[recordLength];
        int lengthRead = input.read(buffer);
        if (lengthRead == -1) {
            return null;
        } else if (lengthRead < recordLength) {
            byte[] temp = new byte[lengthRead];
            System.arraycopy(buffer, 0, temp, 0, temp.length);
            return temp;
        }
        return buffer;
    }

    public void close() {
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRecordLength(int recordLength) {
        if (recordLength <= 0) {
            throw new IllegalArgumentException("Record length MUST be greater than zero");
        }
        this.recordLength = recordLength;
    }

    public int getRecordLength() {
        return recordLength;
    }
}
