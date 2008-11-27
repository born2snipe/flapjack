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
