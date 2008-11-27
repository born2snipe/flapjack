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

import java.io.File;


public class FileLengthRecordReaderFactoryTest extends TestCase {
    private ShuntFileLengthRecordReaderFactory factory;

    public void setUp() {
        factory = new ShuntFileLengthRecordReaderFactory(10);
    }

    public void test_build_SmallFile() {
        factory.fileLength = 5;

        RecordReader recordReader = factory.build(new File("test.txt"));

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof NioRecordReader);
    }

    public void test_build_LargeFile() {
        factory.fileLength = 2000;

        RecordReader recordReader = factory.build(new File("test.txt"));

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof MappedRecordReader);
    }

    public void test_build_ChangeTheSmallFileLimit() {
        factory.setSmallFileLimit(1000);
        factory.fileLength = 1000;

        RecordReader recordReader = factory.build(new File("test.txt"));

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof NioRecordReader);
    }

    private static class ShuntFileLengthRecordReaderFactory extends FileLengthRecordReaderFactory {
        private int fileLength;

        public ShuntFileLengthRecordReaderFactory(int recordLength) {
            super(recordLength);
        }

        protected int getFileLength(File file) {
            return fileLength;
        }

    }
}
