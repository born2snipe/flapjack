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


public class ParseResultTest extends TestCase {
    private ParseResult result;

    public void setUp() {
        result = new ParseResult();
    }

    public void test_hadErrors_NoBadRecords() {
        assertFalse(result.hadErrors());
    }

    public void test_hadErrors_PartialRecord() {
        result.addPartialRecord(new BadRecord(null));

        assertTrue(result.hadErrors());
    }

    public void test_hadErrors_UnresolvedRecord() {
        result.addUnresolvedRecord(new BadRecord(null));

        assertTrue(result.hadErrors());
    }
    
    public void test_hadErrors_UnparseableRecord() {
        result.addUnparseableRecord(new BadRecord(null));

        assertTrue(result.hadErrors());
    }
}
