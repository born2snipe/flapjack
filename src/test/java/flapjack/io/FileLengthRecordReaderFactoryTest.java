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

        RecordReader recordReader = factory.build(new File("/commonline/core/io/test.txt"));

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof NioRecordReader);
    }

    public void test_build_LargeFile() {
        factory.fileLength = 2000;

        RecordReader recordReader = factory.build(new File("/commonline/core/io/test.txt"));

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof MappedRecordReader);
    }

    public void test_build_ChangeTheSmallFileLimit() {
        factory.setSmallFileLimit(1000);
        factory.fileLength = 1000;

        RecordReader recordReader = factory.build(new File("/commonline/core/io/test.txt"));

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
