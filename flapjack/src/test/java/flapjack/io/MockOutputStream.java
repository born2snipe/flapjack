/**
 * Copyright 2008-2009 the original author or authors.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MockOutputStream extends OutputStream {
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private int flushCount = 0;
    private boolean closed = false;
    private IOException error;

    public void write(int i) throws IOException {
        if (error != null)
            throw error;
        output.write(i);
    }

    public void flush() throws IOException {
        super.flush();
        flushCount++;
    }

    public void close() throws IOException {
        super.close();
        closed = true;
    }

    public int getFlushCount() {
        return flushCount;
    }

    public boolean isClosed() {
        return closed;
    }

    public byte[] getBytes() {
        return output.toByteArray();
    }

    public void willThrowException(IOException error) {
        this.error = error;
    }
}
