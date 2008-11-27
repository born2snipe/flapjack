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

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class StreamRecordReaderTest extends TestCase {

    private StreamRecordReader createRecordReader(String content) {
        StreamRecordReader reader = new StreamRecordReader(new ByteArrayInputStream(content.getBytes()));
        reader.setRecordLength(5);
        return reader;
    }

    public void test_readRecord_RecordLengthNotSet() throws IOException {
        StreamRecordReader reader = new StreamRecordReader(new ByteArrayInputStream("".getBytes()));
        try {
            reader.readRecord();
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Record length MUST be greater than zero", err.getMessage());
        }
    }

    public void test_setRecordLength_NegativeRecordLength() {
        StreamRecordReader reader = createRecordReader("");
        try {
            reader.setRecordLength(-1);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Record length MUST be greater than zero", err.getMessage());
        }
    }

    public void test_setRecordLength_ZeroRecordLength() {
        StreamRecordReader reader = createRecordReader("");
        try {
            reader.setRecordLength(0);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Record length MUST be greater than zero", err.getMessage());
        }
    }

    public void test_readRecord_EOF() throws IOException {
        StreamRecordReader reader = createRecordReader("");

        assertNull(reader.readRecord());
    }

    public void test_readRecord_PartialRecord() throws IOException {
        StreamRecordReader reader = createRecordReader("123");

        byte[] actual = reader.readRecord();

        assertNotNull(actual);
        assertEquals(3, actual.length);
        assertEquals("123", new String(actual));
    }

    public void test_readRecord_TwoRecords() throws IOException {
        StreamRecordReader reader = createRecordReader("1234567890");

        byte[] actual = reader.readRecord();

        assertNotNull(actual);
        assertEquals(5, actual.length);
        assertEquals("12345", new String(actual));

        byte[] actual2 = reader.readRecord();

        assertNotNull(actual2);
        assertEquals(5, actual2.length);
        assertEquals("67890", new String(actual2));
    }

    public void test_readRecord_SingleRecord() throws IOException {
        StreamRecordReader reader = createRecordReader("12345");

        byte[] actual = reader.readRecord();

        assertNotNull(actual);
        assertEquals(5, actual.length);
        assertEquals("12345", new String(actual));
    }

    private ByteArrayInputStream createInputStream(String contents) {
        return new ByteArrayInputStream(contents.getBytes());
    }

}
