package flapjack.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;

/**
 * RecordReader implementation using the Java NIO Mapped File API
 * <p/>
 * If you choose to use this it is recommended to only use it on large files.
 * <p/>
 * Defaults the mapped region size to 10MB.
 */
public class MappedRecordReader implements RecordReader {
    public static final long TEN_MEGABYTES = 10L * 1024L * 1024L;
    private File file;
    private int recordLength;
    private ScatteringByteChannel channel;
    private ByteBuffer mappedRegion;
    private long offset = 0L;
    private long mappedRegionSize = TEN_MEGABYTES;
    private FileUtil fileUtil;

    public MappedRecordReader(File file) {
        this.file = file;
    }

    public byte[] readRecord() throws IOException {
        validateRecordLength();
        initializeChannel();
        byte[] buffer = new byte[recordLength];
        long fileLength = fileUtil.length(file);

        if (mappedRegion == null) {
            mapNextRegion(fileLength);
        }

        if (mappedRegion.remaining() == 0) {
            return null;
        } else if (mappedRegion.remaining() < buffer.length) {
            buffer = new byte[mappedRegion.remaining()];
        }
        readBytes(buffer);

        if (buffer.length != recordLength) {
            if (offset == fileLength) {
                return buffer;
            }
            
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(buffer);
            byte[] temp = new byte[recordLength - buffer.length];
            mapNextRegion(fileLength);
            readBytes(temp);
            output.write(temp);
            return output.toByteArray();            
        }

        return buffer;
    }

    private void mapNextRegion(long fileLength) {
        long diff = fileLength - offset;
        mappedRegion = fileUtil.map(channel, offset, Math.min(mappedRegionSize, diff));
    }

    private void readBytes(byte[] buffer) {
        mappedRegion.get(buffer);
        offset += buffer.length;
    }

    private void initializeChannel() throws FileNotFoundException {
        if (channel == null) {
            channel = fileUtil.channel(file);
        }
    }

    public void close() {
        fileUtil.close(channel);
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

    protected void setFileUtil(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }
}
