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


public class NioRecordReaderTest extends MockObjectTestCase {
    private ShuntNioRecordReader reader;
    private static final File FILE = new File("test.txt");
    private Mock fileUtil;

    protected void setUp() throws Exception {
        super.setUp();

        fileUtil = mock(FileUtil.class);

        reader = new ShuntNioRecordReader(FILE);
        reader.setRecordLength(5);
        reader.setFileUtil((FileUtil) fileUtil.proxy());
    }

    public void test_readRecord_RecordLengthNeverSet() throws IOException {
        try {
            reader = new ShuntNioRecordReader(FILE);

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

    public void test_readRecord_FileIsEmpty() throws IOException {
        fileUtil.expects(once()).method("channel").with(eq(FILE)).will(returnValue(new ByteArrayChannel("".getBytes())));
        fileUtil.expects(once()).method("length").with(eq(FILE)).will(returnValue(0L));

        assertNull(reader.readRecord());
    }

    public void test_readRecord_HasPartialRecord() throws IOException {
        fileUtil.expects(once()).method("channel").with(eq(FILE)).will(returnValue(new ByteArrayChannel("123".getBytes())));
        fileUtil.expects(once()).method("length").with(eq(FILE)).will(returnValue(3L));

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(3, actualRecord.length);
        assertEquals("123", new String(actualRecord));
        assertNull(reader.readRecord());
    }

    public void test_readRecord_HasOneRecord() throws IOException {
        fileUtil.expects(once()).method("channel").with(eq(FILE)).will(returnValue(new ByteArrayChannel("12345".getBytes())));
        fileUtil.expects(once()).method("length").with(eq(FILE)).will(returnValue(5L));

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("12345", new String(actualRecord));
        assertNull(reader.readRecord());
    }

    public void test_readRecord_HasTwoRecords() throws IOException {
        fileUtil.expects(once()).method("channel").with(eq(FILE)).will(returnValue(new ByteArrayChannel("1234567890".getBytes())));
        fileUtil.expects(once()).method("length").with(eq(FILE)).will(returnValue(10L));

        byte[] actualRecord = reader.readRecord();

        assertNotNull(actualRecord);
        assertEquals(5, actualRecord.length);
        assertEquals("12345", new String(actualRecord));

        byte[] actualRecord2 = reader.readRecord();

        assertNotNull(actualRecord2);
        assertEquals(5, actualRecord2.length);
        assertEquals("67890", new String(actualRecord2));

        assertNull(reader.readRecord());
    }

    public void test_close() throws IOException {
        ByteArrayChannel channel = new ByteArrayChannel("12345".getBytes());
        fileUtil.expects(once()).method("channel").with(eq(FILE)).will(returnValue(channel));
        fileUtil.expects(once()).method("length").with(eq(FILE)).will(returnValue(5L));

        reader.readRecord();

        fileUtil.expects(once()).method("close").with(eq(channel));

        reader.close();
    }

    public void test_readRecord_BufferSizeNotLargeEnoughForRecord_MustMakeTwoReads() throws IOException {
        reader.bufferSize = 3;

        fileUtil.expects(once()).method("channel").with(eq(FILE)).will(returnValue(new ByteArrayChannel("12345".getBytes())));
        fileUtil.expects(once()).method("length").with(eq(FILE)).will(returnValue(5L));

        assertEquals("12345", new String(reader.readRecord()));
        assertNull(reader.readRecord());
    }

    public void test_readRecord_BufferSizeNotLargeEnoughForRecord_MustMakeMultipleReads() throws IOException {
        reader.bufferSize = 2;

        fileUtil.expects(once()).method("channel").with(eq(FILE)).will(returnValue(new ByteArrayChannel("12345".getBytes())));
        fileUtil.expects(once()).method("length").with(eq(FILE)).will(returnValue(5L));

        assertEquals("12345", new String(reader.readRecord()));
        assertNull(reader.readRecord());
    }

    public void test_readRecord_BufferSizeLargeEnoughForMultipleRecords_MustMakeMultipleFills() throws IOException {
        reader.bufferSize = 10;

        fileUtil.expects(once()).method("channel").with(eq(FILE)).will(returnValue(new ByteArrayChannel("12345678901234567890".getBytes())));
        fileUtil.expects(once()).method("length").with(eq(FILE)).will(returnValue(20L));

        assertEquals("12345", new String(reader.readRecord()));
        assertEquals("67890", new String(reader.readRecord()));
        assertEquals("12345", new String(reader.readRecord()));
        assertEquals("67890", new String(reader.readRecord()));
        assertNull(reader.readRecord());
    }

    private static class ShuntNioRecordReader extends NioRecordReader {
        private int bufferSize = 1024;

        public ShuntNioRecordReader(File file) {
            super(file);
        }

        protected int initializeBufferSize() {
            return bufferSize;
        }
    }

}
