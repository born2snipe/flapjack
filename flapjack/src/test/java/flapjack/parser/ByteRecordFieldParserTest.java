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

import flapjack.layout.FieldDefinition;
import flapjack.layout.SimpleFieldDefinition;
import flapjack.layout.SimpleRecordLayout;
import junit.framework.TestCase;

import java.util.List;


public class ByteRecordFieldParserTest extends TestCase {
    private ByteRecordFieldParser parser;
    private SimpleRecordLayout recordLayout;

    protected void setUp() throws Exception {
        super.setUp();
        parser = new ByteRecordFieldParser();
        recordLayout = new SimpleRecordLayout();
    }

    public void test_parse_ExceptionHandling() {
        FieldDefinition fieldDefinition = createFieldDefinition(0, 4);
        recordLayout.addFieldDefinition(fieldDefinition);

        try {
            parser.parse(new byte[1], recordLayout);
            fail("Exception should have been thrown");
        } catch (ParseException err) {
            assertTrue(err.getCause() instanceof ArrayIndexOutOfBoundsException);
            assertEquals(recordLayout, err.getRecordLayout());
            assertEquals(fieldDefinition, err.getFieldDefinition());
        }
    }

    public void test_parse_SingleField_EmptyBytes() throws ParseException {
        recordLayout.addFieldDefinition(createFieldDefinition(0, 4));

        List fields = (List) parser.parse("".getBytes(), recordLayout);

        assertNotNull(fields);
        assertEquals(0, fields.size());
    }


    public void test_parse_SingleField_EntireContents() throws ParseException {
        recordLayout.addFieldDefinition(createFieldDefinition(0, 4));

        List fields = (List) parser.parse("1234".getBytes(), recordLayout);

        assertNotNull(fields);
        assertEquals(1, fields.size());
        assertEquals("1234", new String((byte[]) fields.get(0)));
    }

    public void test_parse_SingleField_PartOfContents() throws ParseException {
        recordLayout.addFieldDefinition(createFieldDefinition(0, 2));

        List fields = (List) parser.parse("1234".getBytes(), recordLayout);

        assertNotNull(fields);
        assertEquals(1, fields.size());
        assertEquals("12", new String((byte[]) fields.get(0)));
    }

    public void test_parse_MultipleField_WholeContents() throws ParseException {
        recordLayout.addFieldDefinition(createFieldDefinition(0, 2));
        recordLayout.addFieldDefinition(createFieldDefinition(2, 2));

        List fields = (List) parser.parse("1234".getBytes(), recordLayout);

        assertNotNull(fields);
        assertEquals(2, fields.size());
        assertEquals("12", new String((byte[]) fields.get(0)));
        assertEquals("34", new String((byte[]) fields.get(1)));
    }

    public void test_parse_MultipleField_PartialContents() throws ParseException {
        recordLayout.addFieldDefinition(createFieldDefinition(0, 2));
        recordLayout.addFieldDefinition(createFieldDefinition(2, 1));

        List fields = (List) parser.parse("1234".getBytes(), recordLayout);

        assertNotNull(fields);
        assertEquals(2, fields.size());
        assertEquals("12", new String((byte[]) fields.get(0)));
        assertEquals("3", new String((byte[]) fields.get(1)));
    }

    private FieldDefinition createFieldDefinition(int position, int length) {
        return new SimpleFieldDefinition("Doesn't matter", position, length);
    }

}
