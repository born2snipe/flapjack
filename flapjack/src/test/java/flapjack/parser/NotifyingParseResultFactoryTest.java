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

import junit.framework.TestCase;


public class NotifyingParseResultFactoryTest extends TestCase {
    private static final BadRecord BAD_RECORD = new BadRecord(new byte[0]);
    private NotifyingParseResultFactory factory;
    private CollectingNotifier firstNotifier;
    private CollectingNotifier secondNotifier;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new NotifyingParseResultFactory();
        firstNotifier = new CollectingNotifier();
        secondNotifier = new CollectingNotifier();
    }

    public void test_build_SingleNotifier_UnresolvedRecord() {
        factory.addNotifier(firstNotifier);

        factory.build().addUnresolvedRecord(BAD_RECORD);

        assertEquals(1, firstNotifier.results.getUnresolvedRecords().size());
        assertSame(BAD_RECORD, firstNotifier.results.getUnresolvedRecords().get(0));
        assertEquals(0, firstNotifier.results.getPartialRecords().size());
        assertEquals(0, firstNotifier.results.getUnparseableRecords().size());
        assertEquals(0, firstNotifier.results.getRecords().size());
    }

    public void test_build_MultipleNotifiers_UnresolvedRecord() {
        factory.addNotifier(firstNotifier);
        factory.addNotifier(secondNotifier);

        factory.build().addUnresolvedRecord(BAD_RECORD);

        assertEquals(1, firstNotifier.results.getUnresolvedRecords().size());
        assertSame(BAD_RECORD, firstNotifier.results.getUnresolvedRecords().get(0));
        assertEquals(0, firstNotifier.results.getPartialRecords().size());
        assertEquals(0, firstNotifier.results.getUnparseableRecords().size());
        assertEquals(0, firstNotifier.results.getRecords().size());

        assertEquals(1, secondNotifier.results.getUnresolvedRecords().size());
        assertSame(BAD_RECORD, secondNotifier.results.getUnresolvedRecords().get(0));
        assertEquals(0, secondNotifier.results.getPartialRecords().size());
        assertEquals(0, secondNotifier.results.getUnparseableRecords().size());
        assertEquals(0, secondNotifier.results.getRecords().size());
    }

    public void test_build_SingleNotifier_Record() {
        factory.addNotifier(firstNotifier);

        factory.build().addRecord(BAD_RECORD);

        assertEquals(1, firstNotifier.results.getRecords().size());
        assertSame(BAD_RECORD, firstNotifier.results.getRecords().get(0));
        assertEquals(0, firstNotifier.results.getPartialRecords().size());
        assertEquals(0, firstNotifier.results.getUnparseableRecords().size());
        assertEquals(0, firstNotifier.results.getUnresolvedRecords().size());
    }

    public void test_build_MultipleNotifiers_Record() {
        factory.addNotifier(firstNotifier);
        factory.addNotifier(secondNotifier);

        factory.build().addRecord(BAD_RECORD);

        assertEquals(1, firstNotifier.results.getRecords().size());
        assertSame(BAD_RECORD, firstNotifier.results.getRecords().get(0));
        assertEquals(0, firstNotifier.results.getPartialRecords().size());
        assertEquals(0, firstNotifier.results.getUnparseableRecords().size());
        assertEquals(0, firstNotifier.results.getUnresolvedRecords().size());

        assertEquals(1, secondNotifier.results.getRecords().size());
        assertSame(BAD_RECORD, secondNotifier.results.getRecords().get(0));
        assertEquals(0, secondNotifier.results.getPartialRecords().size());
        assertEquals(0, secondNotifier.results.getUnparseableRecords().size());
        assertEquals(0, secondNotifier.results.getUnresolvedRecords().size());
    }

    public void test_build_SingleNotifier_UnparseableRecord() {
        factory.addNotifier(firstNotifier);

        factory.build().addUnparseableRecord(BAD_RECORD);

        assertEquals(1, firstNotifier.results.getUnparseableRecords().size());
        assertSame(BAD_RECORD, firstNotifier.results.getUnparseableRecords().get(0));
        assertEquals(0, firstNotifier.results.getPartialRecords().size());
        assertEquals(0, firstNotifier.results.getRecords().size());
        assertEquals(0, firstNotifier.results.getUnresolvedRecords().size());
    }

    public void test_build_MultipleNotifier_UnparseableRecord() {
        factory.addNotifier(firstNotifier);
        factory.addNotifier(secondNotifier);

        factory.build().addUnparseableRecord(BAD_RECORD);

        assertEquals(1, firstNotifier.results.getUnparseableRecords().size());
        assertSame(BAD_RECORD, firstNotifier.results.getUnparseableRecords().get(0));
        assertEquals(0, firstNotifier.results.getPartialRecords().size());
        assertEquals(0, firstNotifier.results.getRecords().size());
        assertEquals(0, firstNotifier.results.getUnresolvedRecords().size());

        assertEquals(1, secondNotifier.results.getUnparseableRecords().size());
        assertSame(BAD_RECORD, secondNotifier.results.getUnparseableRecords().get(0));
        assertEquals(0, secondNotifier.results.getPartialRecords().size());
        assertEquals(0, secondNotifier.results.getRecords().size());
        assertEquals(0, secondNotifier.results.getUnresolvedRecords().size());
    }

    public void test_build_SingleNotifier_PartialRecord() {
        factory.addNotifier(firstNotifier);

        factory.build().addPartialRecord(BAD_RECORD);

        assertEquals(1, firstNotifier.results.getPartialRecords().size());
        assertSame(BAD_RECORD, firstNotifier.results.getPartialRecords().get(0));
        assertEquals(0, firstNotifier.results.getUnparseableRecords().size());
        assertEquals(0, firstNotifier.results.getRecords().size());
        assertEquals(0, firstNotifier.results.getUnresolvedRecords().size());
    }

    public void test_build_MultipleNotifiers_PartialRecord() {
        factory.addNotifier(firstNotifier);
        factory.addNotifier(secondNotifier);

        factory.build().addPartialRecord(BAD_RECORD);

        assertEquals(1, firstNotifier.results.getPartialRecords().size());
        assertSame(BAD_RECORD, firstNotifier.results.getPartialRecords().get(0));
        assertEquals(0, firstNotifier.results.getUnparseableRecords().size());
        assertEquals(0, firstNotifier.results.getRecords().size());
        assertEquals(0, firstNotifier.results.getUnresolvedRecords().size());

        assertEquals(1, secondNotifier.results.getPartialRecords().size());
        assertSame(BAD_RECORD, secondNotifier.results.getPartialRecords().get(0));
        assertEquals(0, secondNotifier.results.getUnparseableRecords().size());
        assertEquals(0, secondNotifier.results.getRecords().size());
        assertEquals(0, secondNotifier.results.getUnresolvedRecords().size());
    }

    private static class CollectingNotifier implements NotifyingParseResultFactory.Notifier {
        private ParseResult results = new ParseResult();

        public void addRecord(Object obj) {
            results.addRecord(obj);
        }

        public void addPartialRecord(BadRecord badRecord) {
            results.addPartialRecord(badRecord);
        }

        public void addUnparseableRecord(BadRecord record) {
            results.addUnparseableRecord(record);
        }

        public void addUnresolvedRecord(BadRecord record) {
            results.addUnresolvedRecord(record);
        }
    }
}
