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
import flapjack.parser.ParseException;
import flapjack.parser.ByteMapRecordFieldParser;
import flapjack.layout.SimpleRecordLayout;
import flapjack.layout.SimpleFieldDefinition;

import java.util.Map;
import java.util.Arrays;


public class ByteMapRecordFieldParserTest extends TestCase {
    private ByteMapRecordFieldParser fieldParser;

    public void test_MultipleFields() throws ParseException {
        SimpleRecordLayout layout = new SimpleRecordLayout();
        layout.addFieldDefinition(new SimpleFieldDefinition("last name", 0, 10));
        layout.addFieldDefinition(new SimpleFieldDefinition("first name", 11, 10));

        Object obj = fieldParser.parse("Smith     Joe       ".getBytes(), layout);

        assertNotNull(obj);
        assertTrue(obj instanceof Map);

        Map record = (Map) obj;
        assertTrue(Arrays.equals("Smith     ".getBytes(), (byte[]) record.get("last name")));
        assertTrue(Arrays.equals("Joe       ".getBytes(), (byte[]) record.get("first name")));
    }

    protected void setUp() throws Exception {
        super.setUp();
        fieldParser = new ByteMapRecordFieldParser();
    }
}
