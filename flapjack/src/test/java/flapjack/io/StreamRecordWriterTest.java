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

import java.io.IOException;
import java.util.Arrays;


public class StreamRecordWriterTest extends TestCase {
    private MockOutputStream output;
    private StreamRecordWriter writer;

    protected void setUp() throws Exception {
        super.setUp();
        output = new MockOutputStream();
        writer = new StreamRecordWriter(output);
    }

    public void test_close() {
        writer.close();

        assertTrue(output.isClosed());
    }

    public void test_write() throws IOException {
        writer.write("data".getBytes());

        assertTrue(Arrays.equals("data".getBytes(), output.getBytes()));
        assertEquals(1, output.getFlushCount());
    }


}
