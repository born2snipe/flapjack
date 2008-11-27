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


public class NioRecordReaderFactoryTest extends TestCase {

    public void test_build() {
        RecordReader reader = new NioRecordReaderFactory(10).build(new File("test.txt"));
        assertNotNull(reader);
        assertTrue(reader instanceof NioRecordReader);

        NioRecordReader nioReader = (NioRecordReader) reader;
        assertEquals(10, nioReader.getRecordLength());
    }
}
