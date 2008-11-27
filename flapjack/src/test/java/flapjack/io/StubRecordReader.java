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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StubRecordReader implements RecordReader {
    private List records = new ArrayList();
    private int offset;
    private boolean closed;

    public byte[] readRecord() throws IOException {
        if (offset > records.size() - 1) {
            return null;
        }
        return (byte[]) records.get(offset++);
    }

    public void close() {
        closed = true;
    }

    public void addRecord(byte[] record) {
        records.add(record);
    }

    public boolean isClosed() {
        return closed;
    }
}
