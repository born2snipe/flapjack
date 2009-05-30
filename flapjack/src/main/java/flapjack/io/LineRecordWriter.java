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

import java.io.IOException;


public class LineRecordWriter implements RecordWriter {
    public static final String DEFAULT_LINE_ENDING = System.getProperty("line.separator");
    private RecordWriter writer;
    private String lineEnding;

    public LineRecordWriter(RecordWriter writer) {
        this(writer, DEFAULT_LINE_ENDING);
    }

    public LineRecordWriter(RecordWriter writer, String lineEnding) {
        if (lineEnding == null || (!lineEnding.equals("\r\n") && !lineEnding.equals("\n"))) {
            throw new IllegalArgumentException("You may only provide valid line endings '\\n' or '\\r\\n'");
        }

        this.writer = writer;
        this.lineEnding = lineEnding;
    }

    public void write(byte[] data) throws IOException {
        writer.write(data);
        writer.write(lineEnding.getBytes());
    }

    public void close() {
        writer.close();
    }
}
