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
import flapjack.annotation.model.AnnotatedObjectMappingStore;
import flapjack.cobol.layout.CobolRecordLayout;
import flapjack.cobol.util.CobolTypeConverter;
import flapjack.io.LineRecordReader;
import flapjack.layout.RecordLayout;
import flapjack.model.RecordFactory;
import flapjack.model.SameRecordFactoryResolver;
import flapjack.parser.ParseResult;
import flapjack.parser.RecordParserImpl;
import flapjack.parser.SameRecordLayoutResolver;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;


public class CobolTest extends TestCase {
    private AnnotatedObjectMappingStore objectMappingStore;

    public void setUp() {
        /**
         * Initialize the AnnotatedObjctMappingStore with what packages need to be scanned for the domain classes
         * that contain the annotations.
         */
        objectMappingStore = new AnnotatedObjectMappingStore();
        objectMappingStore.setPackages(Arrays.asList("flapjack.example"));
    }

    public void test_parser() throws IOException {
        String record = "123456789JOE A SCHMOE                  01500012";

        /**
         * Initialize the RecordParser with our RecordLayoutResolver and RecordFactoryResolver
         */
        RecordParserImpl recordParser = new RecordParserImpl();
        recordParser.setRecordLayoutResolver(new SameRecordLayoutResolver(new LoanRecordLayout()));
        recordParser.setRecordFactoryResolver(new SameRecordFactoryResolver(LoanRecordFactory.class));
        recordParser.setObjectMappingStore(objectMappingStore);
        recordParser.setTypeConverter(new CobolTypeConverter());

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
        assertEquals(0.12d, loan.rate);
    }

    /**
     * A COBOL style record layout
     */
    private static class LoanRecordLayout extends CobolRecordLayout {
        private LoanRecordLayout() {
            super("loan");

            field("SSN", "9(9)");
            field("NAME", "X(30)");
            field("AMOUNT", "9(5)");
            field("RATE", "9v99");
        }
    }

    @Record("loan")
    private static class Loan {
        @Field
        private String ssn;
        @Field
        private String name;
        @Field
        private int amount;
        @Field
        private double rate;

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

    public static class LoanRecordFactory implements RecordFactory {
        public Object build(RecordLayout recordLayout) {
            return new Loan();
        }
    }
}
