package flapjack.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * RecordReader implementation using the Java NIO Mapped File API
 * <p/>
 * If you choose to use this it is recommended to only use it on large files.
 *
 * Defaults the mapped region size to 10MB.
 */
public class MappedRecordReader implements RecordReader {
    private static final long TEN_MEGABYTES = 10L * 1024L * 1024L;
    private File file;
    private int recordLength;
    private FileChannel channel;
    private ByteBuffer mappedRegion;
    private int offset = 0;
    private long mappedRegionSize = TEN_MEGABYTES;

    public MappedRecordReader(File file) {
        this.file = file;
    }

    public byte[] readRecord() throws IOException {
        validateRecordLength();
        initializeChannel();
        byte[] buffer = new byte[recordLength];

        if (mappedRegion == null) {
            mappedRegion = mapSection(offset, mappedRegionSize);
        }

        if (mappedRegion.remaining() == 0) {
            return null;
        } else if (mappedRegion.remaining() > buffer.length) {
            mappedRegion.get(buffer);
            offset += buffer.length;
        } else {
            byte[] temp = new byte[mappedRegion.remaining()];
            mappedRegion.get(temp);
            offset += temp.length;
            return temp;
        }

        return buffer;
    }

    protected void initializeChannel() throws FileNotFoundException {
        if (channel == null) {
            channel = new FileInputStream(file).getChannel();
        }
    }

    protected ByteBuffer mapSection(long offset, long length) throws IOException {
        return channel.map(FileChannel.MapMode.READ_ONLY, offset, length);
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
        validateRecordLength();
    }

    private void validateRecordLength() {
        if (recordLength <= 0) {
            throw new IllegalArgumentException("Record length MUST be greater than zero");
        }
    }

    public int getRecordLength() {
        return recordLength;
    }

    public void setMappedRegionSize(long mappedRegionSize) {
        this.mappedRegionSize = mappedRegionSize;
    }
}
