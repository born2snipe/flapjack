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
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;

/**
 * RecordReader implementation using the Java NIO Api
 */
public class NioRecordReader implements RecordReader {
    private File file;
    private int recordLength;
    private ByteBuffer byteBuffer;
    private ScatteringByteChannel channel;
    private FileUtil fileUtil = new FileUtilImpl();
    private byte[] buffer;
    private int numberOfBytesRead;
    private int offset;
    private byte[] recordData;
    private long fileLength;
    private long totalNumberOfBytesRead;

    public NioRecordReader(File file) {
        this.file = file;
    }

    public byte[] readRecord() throws IOException {
        validateRecordLength();
        initialize();
        ByteArrayBuilder builder = new ByteArrayBuilder();

        if (numberOfBytesRead == -1) {
            return null;
        } else if (numberOfBytesRead < recordLength && fileLength == numberOfBytesRead) {
            builder.append(buffer, offset, numberOfBytesRead);
            numberOfBytesRead = -1;
            return builder.toByteArray();
        } else {
            int diff = numberOfBytesRead - offset;
            boolean enoughDataRemaining = Math.min(diff, recordLength) >= recordLength;
            if (enoughDataRemaining) {
                builder.append(buffer, offset, recordLength);
                offset += recordLength;
            } else if (fileLength == totalNumberOfBytesRead) {
                return null;
            } else {
                builder.append(buffer, offset, diff);
                int remaining = recordLength - diff;
                while (remaining > 0) {
                    readBytes();
                    int length = Math.min(numberOfBytesRead, remaining);
                    builder.append(buffer, offset, length);
                    remaining -= numberOfBytesRead;
                    offset += length;
                }
            }
        }
        return builder.toByteArray();
    }


    private void readBytes() throws IOException {
        byteBuffer.clear();
        numberOfBytesRead = channel.read(byteBuffer);
        byteBuffer.flip();
        offset = 0;
        totalNumberOfBytesRead += numberOfBytesRead;
    }

    private void initialize() throws IOException {
        if (byteBuffer == null) {
            buffer = initializeBuffer();
            byteBuffer = ByteBuffer.wrap(buffer);
        }
        if (channel == null) {
            fileLength = fileUtil.length(file);
            channel = fileUtil.channel(file);
            readBytes();
        }
        if (recordData == null) {
            recordData = new byte[recordLength];
        }
    }

    protected byte[] initializeBuffer() {
        return new byte[1024 * 64];
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
