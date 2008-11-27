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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;


public class ByteArrayChannel implements ScatteringByteChannel {
    private ByteArrayInputStream input;
    private byte[] data;
    private boolean open = true;

    public ByteArrayChannel(byte[] data) {
        if (data != null) {
            this.data = data;
            input = new ByteArrayInputStream(data);
        }
    }

    public int read(ByteBuffer buf) throws IOException {
        if (data.length == 0) {
            return -1;
        }

        int length = -1;
        byte buffer[] = new byte[buf.remaining()];
        length = input.read(buffer);

        for (int i = 0; i < length; i++) {
            buf.put(buffer[i]);
        }

        return length;
    }

    public long read(ByteBuffer[] buffers, int offset, int length) throws IOException {
        long numberOfBytesRead = -1L;
        for (int i = offset; i < length; i++) {
            ByteBuffer buffer = buffers[i];
            int readBytes = read(buffer);
            if (readBytes == -1) {
                return numberOfBytesRead;
            }
            numberOfBytesRead += readBytes;
        }
        return numberOfBytesRead;
    }

    public long read(ByteBuffer[] buffers) throws IOException {
        return read(buffers, 0, buffers.length);
    }

    public boolean isOpen() {
        return open;
    }

    public void close() throws IOException {
        open = false;
    }
}
