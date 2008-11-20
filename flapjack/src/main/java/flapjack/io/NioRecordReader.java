package flapjack.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;

/**
 * RecordReader implementation using the Java NIO Api
 */
public class NioRecordReader implements RecordReader {
    private File file;
    private int recordLength;
    private ByteBuffer buffer;
    private ScatteringByteChannel channel;
    private FileUtil fileUtil = new FileUtilImpl();

    public NioRecordReader(File file) {
        this.file = file;
    }

    public byte[] readRecord() throws IOException {
        validateRecordLength();
        if (buffer == null) {
            buffer = ByteBuffer.allocate(recordLength);
        } else {
            buffer.clear();
        }
        if (channel == null) {
            channel = fileUtil.channel(file);
        }
        int lengthRead = channel.read(buffer);

        if (lengthRead == -1) {
            return null;
        } else if (lengthRead < recordLength) {
            byte[] temp = new byte[lengthRead];
            buffer.flip();
            buffer.get(temp);
            return temp;
        }
        return buffer.array();
    }

    public void close() {
        fileUtil.close(channel);
    }

    public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
        validateRecordLength();
    }

    private void validateRecordLength() {
        if (this.recordLength <= 0)
            throw new IllegalArgumentException("Record length MUST be greater than zero");
    }

    public int getRecordLength() {
        return recordLength;
    }

    public void setFileUtil(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }
}
