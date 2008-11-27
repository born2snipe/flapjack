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


public class FileLengthRecordReaderFactoryTest extends MockObjectTestCase {
    private FileLengthRecordReaderFactory factory;
    private Mock fileUtil;
    private File file;

    public void setUp() {
        fileUtil = mock(FileUtil.class);

        factory = new FileLengthRecordReaderFactory(10);
        factory.setFileUtil((FileUtil) fileUtil.proxy());
        file = new File("test.txt");
    }

    public void test_build_SmallFile() {
        fileUtil.expects(once()).method("length").with(eq(file)).will(returnValue(5L));

        RecordReader recordReader = factory.build(file);

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof NioRecordReader);
    }

    public void test_build_LargeFile() {
        fileUtil.expects(once()).method("length").with(eq(file)).will(returnValue(Long.MAX_VALUE));

        RecordReader recordReader = factory.build(file);

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof MappedRecordReader);
    }

    public void test_build_ChangeTheSmallFileLimit() {
        factory.setSmallFileLimit(1000);
        fileUtil.expects(once()).method("length").with(eq(file)).will(returnValue(1000L));

        RecordReader recordReader = factory.build(file);

        assertNotNull(recordReader);
        assertTrue(recordReader instanceof NioRecordReader);
    }

}
