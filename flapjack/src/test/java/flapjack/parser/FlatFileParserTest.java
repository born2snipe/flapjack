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
package flapjack.parser;

import flapjack.io.RecordReader;
import flapjack.io.RecordReaderFactory;
import flapjack.io.StubRecordReader;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.io.File;
import java.io.IOException;


public class FlatFileParserTest extends MockObjectTestCase {
    private FlatFileParser parser;
    private Mock recordReaderFactory;
    private RecordReader recordReader;
    private Mock recordParser;

    protected void setUp() throws Exception {
        recordReaderFactory = mock(RecordReaderFactory.class);
        recordParser = mock(RecordParser.class);

        recordReader = new StubRecordReader();

        parser = new FlatFileParser();
        parser.setRecordReaderFactory((RecordReaderFactory) recordReaderFactory.proxy());
        parser.setRecordParser((RecordParser) recordParser.proxy());
    }

    public void test_parse_() throws IOException {
        File file = new File("test.txt");
        ParseResult expectedResult = new ParseResult();

        recordReaderFactory.expects(once()).method("build").with(eq(file)).will(returnValue(recordReader));

        recordParser.expects(once()).method("parse").with(eq(recordReader)).will(returnValue(expectedResult));


        ParseResult result = parser.parse(file);

        assertNotNull(result);
        assertSame(expectedResult, result);
    }
}
