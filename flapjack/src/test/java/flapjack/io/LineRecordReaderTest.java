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

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class LineRecordReaderTest extends TestCase {

    public void test_SingleLineWithoutLineEndings() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
        assertNull(reader.readRecord());
    }

    public void test_SingleLineWithCRLF() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1\r\n".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
    }

    public void test_SingleLineWithLF() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1\n".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
    }

    public void test_TwoLinesWithLF() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1\nline2".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
        assertEquals("line2", new String(reader.readRecord()));
    }

    public void test_TwoLinesWithCRLF() throws IOException {
        LineRecordReader reader = new LineRecordReader(new ByteArrayInputStream("line1\r\nline2".getBytes()));

        assertEquals("line1", new String(reader.readRecord()));
        assertEquals("line2", new String(reader.readRecord()));
    }
}
