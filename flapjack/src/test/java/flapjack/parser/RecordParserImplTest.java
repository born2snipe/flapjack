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
package flapjack.parser;

import flapjack.io.StubRecordReader;
import flapjack.layout.FieldDefinition;
import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.RecordFactory;
import flapjack.model.RecordFactoryResolver;
import flapjack.util.DataType;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RecordParserImplTest extends MockObjectTestCase {
    private RecordParserImpl parser;
    private Mock parseResultFactory;
    private ParseResult result;
    private StubRecordReader recordReader;
    private Mock recordLayoutResolver;
    private SimpleRecordLayout recordLayout;
    private Mock recordFieldParser;
    private static final byte[] RECORD = "record #1".getBytes();
    private static final byte[] RECORD2 = "record #2".getBytes();
    private List fields;
    private Mock recordFactory;
    private Mock badRecordFactory;
    private Mock recordFactoryResolver;

    public void setUp() {
        parseResultFactory = mock(ParseResultFactory.class);
        recordLayoutResolver = mock(RecordLayoutResolver.class);
        recordFieldParser = mock(RecordFieldParser.class);
        recordFactory = mock(RecordFactory.class);
        badRecordFactory = mock(BadRecordFactory.class);
        recordFactoryResolver = mock(RecordFactoryResolver.class);

        recordReader = new StubRecordReader();

        result = new ParseResult();
        recordReader.addRecord(RECORD);

        recordLayout = new SimpleRecordLayout();
        recordLayout.addFieldDefinition(new SimpleFieldDefinition("label", 0, 9, DataType.TEXT));

        fields = new ArrayList();
        fields.add(RECORD);

        parser = new RecordParserImpl();
        parser.setParseResultFactory((ParseResultFactory) parseResultFactory.proxy());
        parser.setRecordLayoutResolver((RecordLayoutResolver) recordLayoutResolver.proxy());
        parser.setRecordFieldParser((RecordFieldParser) recordFieldParser.proxy());
        parser.setBadRecordFactory((BadRecordFactory) badRecordFactory.proxy());
        parser.setRecordFactoryResolver((RecordFactoryResolver) recordFactoryResolver.proxy());
    }

    public void test_parse_ParseExceptionThrown() throws IOException {
        parseResultFactory.expects(once()).method("build").will(returnValue(this.result));

        recordLayoutResolver.expects(once()).method("resolve").with(eq(RECORD)).will(returnValue(recordLayout));

        recordFactoryResolver.expects(once()).method("resolve").with(eq(recordLayout)).will(returnValue(recordFactory.proxy()));

        ParseException parseException = new ParseException(null, recordLayout, (FieldDefinition) recordLayout.getFieldDefinitions().get(0));
        BadRecord badRecord = new BadRecord(RECORD);
        badRecordFactory.expects(once()).method("build").with(eq(RECORD), eq(parseException)).will(returnValue(badRecord));

        recordFieldParser.expects(once()).method("parse").with(eq(RECORD), eq(recordLayout)).will(throwException(parseException));

        ParseResult result = parser.parse(recordReader);

        assertNotNull(result);
        assertEquals(this.result, result);
        assertEquals(0, this.result.getPartialRecords().size());
        assertEquals(0, this.result.getUnresolvedRecords().size());
        assertEquals(0, this.result.getRecords().size());
        assertEquals(1, this.result.getUnparseableRecords().size());
        assertEquals(badRecord, this.result.getUnparseableRecords().get(0));
        assertTrue(recordReader.isClosed());
    }


    public void test_parse_RecordDoesNotMatchTheRecordLayoutLength() throws IOException {
        SimpleRecordLayout recordLayout = new SimpleRecordLayout();
        recordLayout.addFieldDefinition(new SimpleFieldDefinition("label", 0, 12, DataType.TEXT));

        parseResultFactory.expects(once()).method("build").will(returnValue(this.result));

        recordLayoutResolver.expects(once()).method("resolve").with(eq(RECORD)).will(returnValue(recordLayout));

        recordFactoryResolver.expects(once()).method("resolve").with(eq(recordLayout)).will(returnValue(recordFactory.proxy()));

        BadRecord badRecord = new BadRecord(RECORD);
        badRecordFactory.expects(once()).method("build").with(eq(RECORD)).will(returnValue(badRecord));

        ParseResult result = parser.parse(recordReader);

        assertNotNull(result);
        assertEquals(this.result, result);
        assertEquals(1, this.result.getPartialRecords().size());
        assertEquals(0, this.result.getUnresolvedRecords().size());
        assertEquals(0, this.result.getRecords().size());
        assertEquals(0, this.result.getUnparseableRecords().size());
        assertEquals(badRecord, this.result.getPartialRecords().get(0));
        assertTrue(recordReader.isClosed());
    }


    public void test_parse_UnableToResolveRecordLayout() throws IOException {
        parseResultFactory.expects(once()).method("build").will(returnValue(this.result));

        recordLayoutResolver.expects(once()).method("resolve").with(eq(RECORD)).will(returnValue(null));

        recordFactoryResolver.expects(once()).method("resolve").with(eq(null)).will(returnValue(recordFactory.proxy()));

        BadRecord badRecord = new BadRecord(RECORD);
        badRecordFactory.expects(once()).method("build").with(eq(RECORD)).will(returnValue(badRecord));

        ParseResult result = parser.parse(recordReader);

        assertNotNull(result);
        assertEquals(this.result, result);
        assertEquals(0, this.result.getPartialRecords().size());
        assertEquals(1, this.result.getUnresolvedRecords().size());
        assertEquals(0, this.result.getRecords().size());
        assertEquals(0, this.result.getUnparseableRecords().size());
        assertEquals(badRecord, this.result.getUnresolvedRecords().get(0));
        assertTrue(recordReader.isClosed());
    }
    
    public void test_parse_UnableToResolveRecordFactory() throws IOException {
        parseResultFactory.expects(once()).method("build").will(returnValue(this.result));

        recordLayoutResolver.expects(once()).method("resolve").with(eq(RECORD)).will(returnValue(recordLayout));

        recordFactoryResolver.expects(once()).method("resolve").with(eq(recordLayout)).will(returnValue(null));

        BadRecord badRecord = new BadRecord(RECORD);
        badRecordFactory.expects(once()).method("build").with(eq(RECORD)).will(returnValue(badRecord));

        ParseResult result = parser.parse(recordReader);

        assertNotNull(result);
        assertEquals(this.result, result);
        assertEquals(0, this.result.getPartialRecords().size());
        assertEquals(1, this.result.getUnresolvedRecords().size());
        assertEquals(0, this.result.getRecords().size());
        assertEquals(0, this.result.getUnparseableRecords().size());
        assertEquals(badRecord, this.result.getUnresolvedRecords().get(0));
        assertTrue(recordReader.isClosed());
    }

    public void test_parse_SingleRecord() throws IOException {
        parseResultFactory.expects(once()).method("build").will(returnValue(this.result));

        recordLayoutResolver.expects(once()).method("resolve").with(eq(RECORD)).will(returnValue(recordLayout));

        recordFieldParser.expects(once()).method("parse").with(eq(RECORD), eq(recordLayout)).will(returnValue(fields));

        recordFactoryResolver.expects(once()).method("resolve").with(eq(recordLayout)).will(returnValue(recordFactory.proxy()));

        recordFactory.expects(once()).method("build").with(eq(fields), eq(recordLayout)).will(returnValue("record"));

        ParseResult result = parser.parse(recordReader);

        assertNotNull(result);
        assertEquals(this.result, result);
        assertEquals(0, this.result.getPartialRecords().size());
        assertEquals(0, this.result.getUnresolvedRecords().size());
        assertEquals(1, this.result.getRecords().size());
        assertEquals(0, this.result.getUnparseableRecords().size());
        assertEquals("record", this.result.getRecords().get(0));
        assertTrue(recordReader.isClosed());
    }

    public void test_parse_MultipleRecord() throws IOException {
        parseResultFactory.expects(once()).method("build").will(returnValue(this.result));

        recordLayoutResolver.expects(once()).method("resolve").with(eq(RECORD)).will(returnValue(recordLayout));
        recordLayoutResolver.expects(once()).method("resolve").with(eq(RECORD2)).will(returnValue(recordLayout));

        recordFieldParser.expects(once()).method("parse").with(eq(RECORD), eq(recordLayout)).will(returnValue(fields));
        recordFieldParser.expects(once()).method("parse").with(eq(RECORD2), eq(recordLayout)).will(returnValue(fields));

        recordFactoryResolver.expects(once()).method("resolve").with(eq(recordLayout)).will(returnValue(recordFactory.proxy()));
        recordFactoryResolver.expects(once()).method("resolve").with(eq(recordLayout)).will(returnValue(recordFactory.proxy()));

        recordFactory.expects(once()).method("build").with(eq(fields), eq(recordLayout)).will(returnValue("record-2"));
        recordFactory.expects(once()).method("build").with(eq(fields), eq(recordLayout)).will(returnValue("record-1"));

        recordReader.addRecord(RECORD2);

        ParseResult result = parser.parse(recordReader);

        assertNotNull(result);
        assertEquals(this.result, result);
        assertEquals(0, this.result.getPartialRecords().size());
        assertEquals(0, this.result.getUnresolvedRecords().size());
        assertEquals(2, this.result.getRecords().size());
        assertEquals(0, this.result.getUnparseableRecords().size());
        assertEquals("record-1", this.result.getRecords().get(0));
        assertEquals("record-2", this.result.getRecords().get(1));
        assertTrue(recordReader.isClosed());
    }
}
