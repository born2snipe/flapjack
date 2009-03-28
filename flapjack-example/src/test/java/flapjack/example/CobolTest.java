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
package flapjack.example;

import flapjack.annotation.Field;
import flapjack.annotation.Record;
import flapjack.annotation.model.AnnotatedMappedRecordFactoryResolver;
import flapjack.parser.ByteMapRecordFieldParser;
import flapjack.cobol.layout.AbstractCobolRecordLayout;
import flapjack.io.LineRecordReader;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;


public class CobolTest extends TestCase {
    public void test() throws IOException {
        String record = "123456789JOE A SCHMOE                  01500";

        /**
         * Initialize the MappedRecordFactoryResolver with what packages need to be scanned for the domain classes
         * that contain the annotations.
         */
        AnnotatedMappedRecordFactoryResolver recordFactoryResolver = new AnnotatedMappedRecordFactoryResolver();
        recordFactoryResolver.setPackages(Arrays.asList("flapjack.example"));

        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(LoanRecordLayout.class));
        recordParser.setRecordFactoryResolver(recordFactoryResolver);
        recordParser.setRecordFieldParser(new ByteMapRecordFieldParser());

        /**
         * Actually call the parser with our RecordReader
         */
        LineRecordReader recordReader = new LineRecordReader(new ByteArrayInputStream(record.getBytes()));
        ParseResult result = recordParser.parse(recordReader);

        assertEquals(0, result.getUnparseableRecords().size());
        assertEquals(0, result.getUnresolvedRecords().size());
        assertEquals(0, result.getPartialRecords().size());
        assertEquals(1, result.getRecords().size());

        Loan loan = (Loan) result.getRecords().get(0);
        assertEquals("123456789", loan.getSsn());
        assertEquals("JOE A SCHMOE                  ", loan.getName());
        assertEquals(1500, loan.getAmount());
    }

    /**
     * A COBOL style record layout
     */
    private static class LoanRecordLayout extends AbstractCobolRecordLayout {
        protected void defineFields() {
            cobolField("SSN", "9(9)");
            cobolField("NAME", "X(30)");
            cobolField("AMOUNT", "9(5)");
        }
    }

    @Record(LoanRecordLayout.class)
    private static class Loan {
        @Field
        private String ssn;
        @Field
        private String name;
        @Field
        private int amount;

        public String getSsn() {
            return ssn;
        }

        public String getName() {
            return name;
        }

        public int getAmount() {
            return amount;
        }
    }
}
