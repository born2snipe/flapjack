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
package flapjack.cobol.util;

import junit.framework.TestCase;


public class CobolDecimalPatternParserTest extends TestCase {
    private CobolDecimalPatternParser parser;

    protected void setUp() throws Exception {
        super.setUp();
        parser = new CobolDecimalPatternParser();
    }

    public void test_problemWithCobolPattern() {
        try {
            parser.parseDecimalPlaces("999");
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Unable to parse out the decimal points from cobol pattern (999)", err.getMessage());
        }
    }

    public void test() {
        assertEquals(1, parser.parseDecimalPlaces("9v9"));
        assertEquals(2, parser.parseDecimalPlaces("9v99"));
        assertEquals(2, parser.parseDecimalPlaces("9V99"));
        assertEquals(2, parser.parseDecimalPlaces("9V9(2)"));
        assertEquals(12, parser.parseDecimalPlaces("9V9(12)"));
        assertEquals(3, parser.parseDecimalPlaces("999V999"));
        assertEquals(3, parser.parseDecimalPlaces("9(2)v999"));
    }

}
