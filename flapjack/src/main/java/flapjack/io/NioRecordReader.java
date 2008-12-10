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
    private byte[] recordData;
    private long fileLength;
    private long totalNumberOfBytesRead;
    private boolean useDirectBuffer = false;

    public NioRecordReader(File file, boolean useDirectBuffer) {
        this.file = file;
        this.useDirectBuffer = useDirectBuffer;
    }

    public byte[] readRecord() throws IOException {
        validateRecordLength();
        initialize();

        if (byteBuffer.remaining() < recordLength) {
            byte[] temp = new byte[byteBuffer.remaining()];
            byteBuffer.get(temp);
            if (fileLength == totalNumberOfBytesRead) {
                if (temp.length == 0) {
                    return null;
                }
                return temp;
            }
            int diff = recordLength - temp.length;
            ByteArrayBuilder builder = new ByteArrayBuilder();
            builder.append(temp);
            while (diff > 0) {
                fillByteBuffer();
                temp = new byte[Math.min(diff, byteBuffer.remaining())];
                byteBuffer.get(temp);
                diff -= temp.length;
                builder.append(temp);
            }
            return builder.toByteArray();
        } else {
            byteBuffer.get(recordData);
        }


        return recordData;
    }


    private void fillByteBuffer() throws IOException {
        byteBuffer.clear();
        int numberOfBytesRead = channel.read(byteBuffer);
        byteBuffer.flip();
        totalNumberOfBytesRead += numberOfBytesRead < 0 ? 0 : numberOfBytesRead;
    }

    private void initialize() throws IOException {
        if (byteBuffer == null) {
            if (useDirectBuffer) {
                byteBuffer = ByteBuffer.allocateDirect(initializeBufferSize());
            } else {
                byteBuffer = ByteBuffer.allocate(initializeBufferSize());
            }
        }
        if (channel == null) {
            fileLength = fileUtil.length(file);
            channel = fileUtil.channel(file);
            fillByteBuffer();
        }
        if (recordData == null) {
            recordData = new byte[recordLength];
        }
    }

    protected int initializeBufferSize() {
        return 1024 * 16;
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

    protected void setFileUtil(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

}
