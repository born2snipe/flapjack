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

import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import junit.framework.TestCase;

import java.util.List;


public class StringRecordFieldParserTest extends TestCase {
    private StringRecordFieldParser parser;
    private SimpleRecordLayout recordLayout;

    protected void setUp() throws Exception {
        super.setUp();
        parser = new StringRecordFieldParser();
        recordLayout = new SimpleRecordLayout();
        recordLayout.addFieldDefinition(new SimpleFieldDefinition("", 0, 1));
        recordLayout.addFieldDefinition(new SimpleFieldDefinition("", 1, 4));
    }

    public void test_parse_ThrowsException() {
        try {
            parser.parse("1".getBytes(), recordLayout);
            fail();
        } catch (ParseException err) {
            assertNotNull(err.getCause());
        }
    }

    public void test_parse() throws ParseException {
        List fields = (List) parser.parse("@1234".getBytes(), recordLayout);

        assertEquals(2, fields.size());
        assertEquals("@", fields.get(0));
        assertEquals("1234", fields.get(1));
    }

    public void test_parse_EmptyLine() throws ParseException {
        List fields = (List) parser.parse("".getBytes(), recordLayout);

        assertNotNull(fields);
        assertEquals(0, fields.size());
    }

}
