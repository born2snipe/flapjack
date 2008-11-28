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
package flapjack.annotation.parser;

import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import flapjack.parser.ParseException;
import junit.framework.TestCase;

import java.util.Map;


public class StringMapRecordFieldParserTest extends TestCase {
    private StringMapRecordFieldParser fieldParser;

    public void test_MultipleFields() throws ParseException {
        SimpleRecordLayout layout = new SimpleRecordLayout();
        layout.addFieldDefinition(new SimpleFieldDefinition("last name", 0, 10));
        layout.addFieldDefinition(new SimpleFieldDefinition("first name", 11, 10));

        Object obj = fieldParser.parse("Smith     Joe       ".getBytes(), layout);

        assertNotNull(obj);
        assertTrue(obj instanceof Map);

        Map<String, String> record = (Map<String, String>) obj;
        assertEquals("Smith     ", record.get("last name"));
        assertEquals("Joe       ", record.get("first name"));
    }

    protected void setUp() throws Exception {
        super.setUp();
        fieldParser = new StringMapRecordFieldParser();
    }
}
