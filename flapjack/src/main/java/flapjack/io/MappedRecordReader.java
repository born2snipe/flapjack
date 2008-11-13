package flapjack.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * RecordReader implementation using the Java NIO Mapped File API
 * <p/>
 * If you choose to use this it is recommended to only use it on large files.
 */
public class MappedRecordReader implements RecordReader {
    private File file;
    private int recordLength;
    private FileChannel channel;
    private ByteBuffer fileBuffer;

    public MappedRecordReader(File file) {
        this.file = file;
    }

    public byte[] readRecord() throws IOException {
        byte[] buffer = new byte[recordLength];
        ByteBuffer fileBuffer = mapSection();

        if (fileBuffer.remaining() == 0) {
            return null;
        } else if (fileBuffer.remaining() > buffer.length) {
            fileBuffer.get(buffer);
        } else {
            byte[] temp = new byte[fileBuffer.remaining()];
            fileBuffer.get(temp);
            return temp;
        }

        return buffer;
    }

    protected ByteBuffer mapSection() throws IOException {
        if (channel == null) {
            channel = new FileInputStream(file).getChannel();
            fileBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        }
        return fileBuffer;
    }

    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
    }

    public int getRecordLength() {
        return recordLength;
    }
}
