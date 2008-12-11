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

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;


public class MappedRecordReaderTest extends MockObjectTestCase {
    private MappedRecordReader reader;
    private static final File FILE = new File("test.txt");
    private Mock fileUtil;

    public void setUp() {
        fileUtil = mock(FileUtil.class);

        reader = new MappedRecordReader(FILE);
        reader.setRecordLength(5);
        reader.setFileUtil((FileUtil) fileUtil.proxy());
    }

    public void test_close() throws IOException {
        ByteArrayChannel channel = new ByteArrayChannel(null);

        expect_channel(channel);
        expect_map(channel, 0L, 0L, "");
        fileUtil.expects(once()).method("close").with(isA(ByteArrayChannel.class));
        expect_length(0L);

        reader.readRecord();

        reader.close();
    }

    public void test_readRecord_RecordLengthNotSet() throws IOException {
        try {
            reader = new MappedRecordReader(FILE);

            reader.readRecord();
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Record length MUST be greater than zero", err.getMessage());
        }
    }


    public void test_setRecordLength_NegativeRecordLength() {
        try {
            reader.setRecordLength(-1);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Record length MUST be greater than zero", err.getMessage());
        }
    }

    public void test_setRecordLength_ZeroRecordLength() {
        try {
            reader.setRecordLength(0);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Record length MUST be greater than zero", err.getMessage());
        }
    }

    public void test_readRecord_EOF() throws IOException {
        ByteArrayChannel channel = new ByteArrayChannel(null);

        expect_channel(channel);
        expect_map(channel, 0L, 0L, "");
        expect_length(0L);

        assertNull(reader.readRecord());
    }

    public void test_readRecord_PartialRecord() throws IOException {
        ByteArrayChannel channel = new ByteArrayChannel(null);

        expect_channel(channel);
        expect_map(channel, 0L, 3L, "123");
        expect_length(3L);

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(3, actualRecord.length);
        assertEquals("123", new String(actualRecord));
    }

    public void test_readRecord_SingleRecord_MapRegionNotBigEnough() throws IOException {
        ByteArrayChannel channel = new ByteArrayChannel(null);

        expect_channel(channel);
        expect_map(channel, 0L, 5L, "123");
        expect_map(channel, 3L, 2L, "45");
        expect_length(5L);

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("12345", new String(actualRecord));
    }

    public void test_readRecord_SingleRecord() throws IOException {
        ByteArrayChannel channel = new ByteArrayChannel(null);

        expect_channel(channel);
        expect_map(channel, 0L, 5L, "12345");
        expect_length(5L);

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("12345", new String(actualRecord));
    }

    public void test_readRecord_TwoRecords() throws IOException {
        ByteArrayChannel channel = new ByteArrayChannel(null);

        expect_channel(channel);
        expect_map(channel, 0L, 10L, "1234567890");
        expect_length(10L);

        reader.readRecord();
        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("67890", new String(actualRecord));
    }

    private void expect_map(ByteArrayChannel channel, long expectedOffset, long expectedLength, String data) {
        fileUtil.expects(once()).method("map").with(eq(channel), eq(expectedOffset), eq(expectedLength)).will(returnValue(ByteBuffer.wrap(data.getBytes())));
    }

    private void expect_channel(ByteArrayChannel channel) {
        fileUtil.expects(once()).method("channel").with(eq(FILE)).will(returnValue(channel));
    }

    private void expect_length(long length) {
        fileUtil.expects(once()).method("length").with(eq(FILE)).will(returnValue(length));
    }

}
