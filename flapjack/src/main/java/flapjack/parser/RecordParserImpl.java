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
import flapjack.layout.RecordLayout;
import flapjack.model.RecordFactory;
import flapjack.model.RecordFactoryResolver;
import flapjack.model.SimpleRecordFactoryResolver;

import java.io.IOException;

public class RecordParserImpl implements RecordParser {
    private ParseResultFactory parseResultFactory;
    private RecordLayoutResolver recordLayoutResolver;
    private RecordFieldParser recordFieldParser;
    private BadRecordFactory badRecordFactory;
    private RecordFactoryResolver recordFactoryResolver;

    public RecordParserImpl() {
        setBadRecordFactory(new DefaultBadRecordFactory());
        setRecordFieldParser(new ByteRecordFieldParser());
        setParseResultFactory(new DefaultParseResultFactory());
        setRecordFactoryResolver(new SimpleRecordFactoryResolver());
    }

    public ParseResult parse(RecordReader recordReader) throws IOException {
        ParseResult result = parseResultFactory.build();

        byte[] record = null;
        try {
            while ((record = recordReader.readRecord()) != null) {
                RecordLayout recordLayout = recordLayoutResolver.resolve(record);
                RecordFactory recordFactory = recordFactoryResolver.resolve(recordLayout);
                if (recordLayout == null || recordFactory == null) {
                    result.addUnresolvedRecord(badRecordFactory.build(record));
                } else {
                    if (recordLayout.getLength() != record.length) {
                        result.addPartialRecord(badRecordFactory.build(record));
                    } else {
                        try {
                            result.addRecord(recordFactory.build(recordFieldParser.parse(record, recordLayout), recordLayout));
                        } catch (ParseException e) {
                            result.addUnparseableRecord(badRecordFactory.build(record, e));
                        }
                    }
                }
            }
        } finally {
            recordReader.close();
        }
        return result;
    }

    public RecordLayoutResolver getRecordLayoutResolver() {
        return recordLayoutResolver;
    }

    public void setParseResultFactory(ParseResultFactory parseResultFactory) {
        this.parseResultFactory = parseResultFactory;
    }

    public void setRecordLayoutResolver(RecordLayoutResolver recordLayoutResolver) {
        this.recordLayoutResolver = recordLayoutResolver;
    }

    public void setRecordFieldParser(RecordFieldParser recordFieldParser) {
        this.recordFieldParser = recordFieldParser;
    }

    public void setBadRecordFactory(BadRecordFactory badRecordFactory) {
        this.badRecordFactory = badRecordFactory;
    }

    public void setRecordFactoryResolver(RecordFactoryResolver recordFactoryResolver) {
        this.recordFactoryResolver = recordFactoryResolver;
    }
}
