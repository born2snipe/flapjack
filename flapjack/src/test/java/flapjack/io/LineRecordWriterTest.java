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

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class LineRecordWriterTest extends TestCase {
    private ByteArrayOutputStream output;
    private LineRecordWriter writer;

    protected void setUp() throws Exception {
        super.setUp();
        output = new ByteArrayOutputStream();
    }

    public void test_close() {
        MockOutputStream output = new MockOutputStream();
        LineRecordWriter writer = new LineRecordWriter(new StreamRecordWriter(output));

        writer.close();

        assertTrue(output.isClosed());
    }

    public void test_write_NullLineEnding() {
        try {
            new LineRecordWriter(new StreamRecordWriter(output), null);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("You may only provide valid line endings '\\n' or '\\r\\n'", err.getMessage());
        }
    }

    public void test_write_EmptyLineEnding() {
        try {
            new LineRecordWriter(new StreamRecordWriter(output), "");
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("You may only provide valid line endings '\\n' or '\\r\\n'", err.getMessage());
        }
    }

    public void test_write_NotLineEnding() {
        try {
            new LineRecordWriter(new StreamRecordWriter(output), "AB");
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("You may only provide valid line endings '\\n' or '\\r\\n'", err.getMessage());
        }
    }

    public void test_write_SpecificLineEnding() throws IOException {
        String lineEnding = System.getProperty("os.name").toLowerCase().indexOf("win") == -1 ? "\r\n" : "\n";

        writer = new LineRecordWriter(new StreamRecordWriter(output), lineEnding);
        writer.write("value".getBytes());

        assertEquals("value" + lineEnding, new String(output.toByteArray()));
    }

    public void test_write_DefaultLineEnding() throws IOException {
        writer = new LineRecordWriter(new StreamRecordWriter(output));
        writer.write("value".getBytes());

        assertEquals("value" + LineRecordWriter.DEFAULT_LINE_ENDING, new String(output.toByteArray()));
    }


}
