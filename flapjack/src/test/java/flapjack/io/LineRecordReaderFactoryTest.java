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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class LineRecordReaderFactoryTest extends TestCase {

    public void test() throws URISyntaxException {
        LineRecordReaderFactory factory = new LineRecordReaderFactory();

        URL url = getClass().getClassLoader().getResource("flapjack/io/test.txt");
        RecordReader reader = factory.build(new File(new URI(url.toString())));

        assertNotNull(reader);
        assertTrue(reader instanceof LineRecordReader);
    }
}
