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

import flapjack.layout.SimpleFieldDefinition;
import junit.framework.TestCase;


public class StringFieldParserTest extends TestCase {

    public void test_parse() {
        StringFieldParser parser = new StringFieldParser();
        assertEquals("@1", parser.parse("@132", new SimpleFieldDefinition("", 0, 2)));
    }

    public void test_parse_DoNotTrim() {
        StringFieldParser parser = new StringFieldParser();
        assertEquals("32 ", parser.parse("@132  ", new SimpleFieldDefinition("", 2, 3), false));
    }

    public void test_parse_Trim() {
        StringFieldParser parser = new StringFieldParser();
        assertEquals("32", parser.parse("@132  ", new SimpleFieldDefinition("", 2, 3), true));
    }
}
