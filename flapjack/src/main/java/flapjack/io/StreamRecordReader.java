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
        validateRecordLength();
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
        this.recordLength = recordLength;
        validateRecordLength();
    }

    private void validateRecordLength() {
        if (this.recordLength <= 0) {
            throw new IllegalArgumentException("Record length MUST be greater than zero");
        }
    }

    public int getRecordLength() {
        return recordLength;
    }
}
