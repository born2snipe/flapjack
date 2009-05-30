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

import flapjack.builder.RecordBuilder;
import flapjack.builder.SameBuilderRecordLayoutResolver;
import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.model.RecordFactory;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.FlatFileParser;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import flapjack.util.TypeConverter;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FileTest extends TestCase {
    private RecordParserImpl recordParser;
    private FlatFileParser fileParser;
    private File file;
    private ObjectMappingStore store;
    private static final int NUMBER_OF_RECORDS = 231000;
    private static final String RECORD_DATA = "1234567890";

    protected void setUp() throws Exception {
        super.setUp();
        recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(new TestRecordLayout()));
        fileParser = new FlatFileParser();
        fileParser.setRecordParser(recordParser);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        file = new File(new URI(loader.getResource("flapjack/io/two_megs.txt").toString()));

        store = new ObjectMappingStore();
        ObjectMapping objMapping = new ObjectMapping(Record.class);
        objMapping.field("data", "data");
        store.add(objMapping);
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
        verifyRecordBuilder(new StreamRecordWriter(new FileOutputStream(file)));
        verifyRecordReader(new FileStreamRecordReaderFactory(10));
    }

    private void verifyRecordBuilder(StreamRecordWriter writer) {
        RecordBuilder builder = new RecordBuilder();
        builder.setObjectMappingStore(store);
        builder.setTypeConverter(new TypeConverter());
        builder.setBuilderRecordLayoutResolver(new SameBuilderRecordLayoutResolver(new TestRecordLayout()));

        List records = new ArrayList();
        for (int i = 0; i < NUMBER_OF_RECORDS; i++) {
            records.add(new Record(RECORD_DATA));
        }

        builder.build(records, writer);
        assertEquals(2310000L, file.length());
    }

    private void verifyRecordReader(RecordReaderFactory recordReaderFactory) throws IOException {
        recordParser.setObjectMappingStore(store);
        recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(DummyFactory.class));

        fileParser.setRecordReaderFactory(recordReaderFactory);

        ParseResult result = fileParser.parse(file);

        Iterator it = result.getRecords().iterator();
        while (it.hasNext()) {
            Record record = (Record) it.next();
            assertEquals(RECORD_DATA, record.data);
        }
        assertEquals(NUMBER_OF_RECORDS, result.getRecords().size());
    }


    private static class TestRecordLayout extends SimpleRecordLayout {
        private TestRecordLayout() {
            super("id");
            addFieldDefinition(new SimpleFieldDefinition("data", 0, 10));
        }
    }

    private static class DummyFactory implements RecordFactory {
        public Object build() {
            return new Record("");
        }
    }

    private static class Record {
        private String data;

        private Record(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
