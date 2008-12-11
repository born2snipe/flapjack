/**
 * Copyright 2008 Dan Dudley
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. 
 */
package flapjack.io;

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
 * Defaults the max mapped region size to 10MB.  The mapped region size is determined on how big the file
 * is that is being processed. So if the file size is less than the max mapped region size it will you the file size
 * and if the max mapped region size is less than the file size it will you use the max mapped region size.
 * <p/>
 * For the visual folks:
 * <p/>
 * file size = 100MB
 * max mapped region size = 10MB
 * mapped region size = 10MB
 * <p/>
 * file size = 10MB
 * max mapped region size = 100MB
 * mapped region size = 10MB
 */
public class MappedRecordReader implements RecordReader {
    public static final long TEN_MEGABYTES = 10L * 1024L * 1024L;
    private File file;
    private int recordLength;
    private ScatteringByteChannel channel;
    private ByteBuffer mappedRegion;
    private long offset = 0L;
    private long mappedRegionSize = TEN_MEGABYTES;
    private FileUtil fileUtil = new FileUtilImpl();
    private long fileLength;
    private byte[] recordData;

    public MappedRecordReader(File file) {
        this.file = file;
    }

    public byte[] readRecord() throws IOException {
        validateRecordLength();
        initialize();


        if (mappedRegion.remaining() == 0) {
            return null;
        } else if (mappedRegion.remaining() < recordData.length) {
            recordData = new byte[mappedRegion.remaining()];
        }

        readBytes(recordData);

        if (recordData.length != recordLength) {
            if (offset == fileLength) {
                return recordData;
            }

            ByteArrayBuilder builder = new ByteArrayBuilder();
            builder.append(recordData);

            byte[] temp = new byte[recordLength - recordData.length];
            mapNextRegion(fileLength);
            readBytes(temp);

            builder.append(temp);
            return builder.toByteArray();
        }

        return recordData;
    }

    private void mapNextRegion(long fileLength) {
        long diff = fileLength - offset;
        mappedRegion = fileUtil.map(channel, offset, Math.min(mappedRegionSize, diff));
    }

    private void readBytes(byte[] buffer) {
        mappedRegion.get(buffer);
        offset += buffer.length;
    }

    private void initialize() throws FileNotFoundException {
        if (channel == null) {
            channel = fileUtil.channel(file);
            fileLength = fileUtil.length(file);
        }

        if (mappedRegion == null) {
            mapNextRegion(fileLength);
        }
        if (recordData == null) {
            recordData = new byte[recordLength];
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
