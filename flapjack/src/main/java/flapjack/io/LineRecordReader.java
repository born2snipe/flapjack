package flapjack.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LineRecordReader implements RecordReader {
    private BufferedReader reader;

    public LineRecordReader(InputStream inputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public byte[] readRecord() throws IOException {
        String line = reader.readLine();
        return line == null ? null : line.getBytes();
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
