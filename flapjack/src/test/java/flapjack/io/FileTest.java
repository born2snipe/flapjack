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

import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.SimpleRecord;
import flapjack.parser.*;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;


public class FileTest extends TestCase {
    private RecordParserImpl recordParser;
    private FlatFileParser fileParser;
    private File file;

    protected void setUp() throws Exception {
        super.setUp();
        recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(TestRecordLayout.class));
        recordParser.setRecordFieldParser(new ByteRecordFieldParser());
        fileParser = new FlatFileParser();
        fileParser.setRecordParser(recordParser);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        file = new File(new URI(loader.getResource("flapjack/io/two_megs.txt").toString()));
    }

    public void test_MappedFile() throws IOException, URISyntaxException {
        verifyRecordReader(new MappedRecordReaderFactory(10));
    }


    public void test_NIOFile() throws IOException, URISyntaxException {
        verifyRecordReader(new NioRecordReaderFactory(10));
    }

    public void test_NIOFile_UseDirect() throws IOException, URISyntaxException {
        verifyRecordReader(new NioRecordReaderFactory(10, true));
    }

    public void test_StreamFile() throws IOException, URISyntaxException {
        verifyRecordReader(new FileStreamRecordReaderFactory(10));
    }

    private void verifyRecordReader(RecordReaderFactory recordReaderFactory) throws IOException {
        fileParser.setRecordReaderFactory(recordReaderFactory);

        ParseResult result = fileParser.parse(file);

        Iterator it = result.getRecords().iterator();
        while (it.hasNext()) {
            SimpleRecord record = (SimpleRecord) it.next();
            assertEquals("1234567890", new String((byte[]) record.getField(0)));
        }
        assertEquals(231000, result.getRecords().size());
    }


    private static class TestRecordLayout extends SimpleRecordLayout {
        private TestRecordLayout() {
            addFieldDefinition(new SimpleFieldDefinition("data", 0, 10));
        }
    }
}
