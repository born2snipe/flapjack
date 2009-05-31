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
package flapjack.layout;

import junit.framework.TestCase;


public class SimpleRecordLayoutTest extends TestCase {
    SimpleRecordLayout layout;

    public void setUp() {
        layout = new SimpleRecordLayout("123");
    }

    public void test_FieldWithPadding_NoOffset() {
        layout.field("", 2, TextPaddingDescriptor.Padding.LEFT, ' ');

        FieldDefinition fieldDef = (FieldDefinition) layout.getFieldDefinitions().get(0);
        assertNotNull(fieldDef.getPaddingDescriptor());
    }

    public void test_FieldWithPadding_WithOffset() {
        layout.field("", 0, 2, TextPaddingDescriptor.Padding.LEFT, ' ');

        FieldDefinition fieldDef = (FieldDefinition) layout.getFieldDefinitions().get(0);
        assertNotNull(fieldDef.getPaddingDescriptor());
    }

    public void test_OneFieldDefinition() {
        layout.addFieldDefinition(new SimpleFieldDefinition("", 0, 2));

        assertEquals(2, layout.getLength());
        assertEquals(1, layout.getFieldDefinitions().size());
    }

    public void test_TwoFieldDefinition() {
        layout.addFieldDefinition(new SimpleFieldDefinition("", 0, 2));
        layout.addFieldDefinition(new SimpleFieldDefinition("", 2, 2));

        assertEquals(4, layout.getLength());
        assertEquals(2, layout.getFieldDefinitions().size());
    }

    public void test_OneFieldDefinition_DoesNotStartFromBeginning() {
        layout.addFieldDefinition(new SimpleFieldDefinition("", 2, 2));

        assertEquals(4, layout.getLength());
        assertEquals(1, layout.getFieldDefinitions().size());
    }

    public void test_field_OneFieldDefinition_DoesNotStartFromBeginning() {
        layout.field("", 2, 2);

        assertEquals(4, layout.getLength());
        assertEquals(1, layout.getFieldDefinitions().size());
        FieldDefinition fieldDefinition = (FieldDefinition) layout.getFieldDefinitions().get(0);
        assertNull(fieldDefinition.getPaddingDescriptor());
    }

    public void test_field_TwoFieldDefinition_DoesNotStartFromBeginning() {
        layout.field("", 2, 2);
        layout.field("", 2);

        assertEquals(6, layout.getLength());
        assertEquals(2, layout.getFieldDefinitions().size());
    }

    public void test_TwoFieldDefinition_OutOfOrder() {
        layout.addFieldDefinition(new SimpleFieldDefinition("", 2, 2));
        layout.addFieldDefinition(new SimpleFieldDefinition("", 0, 2));

        assertEquals(4, layout.getLength());
        assertEquals(2, layout.getFieldDefinitions().size());
        FieldDefinition fieldDef = (FieldDefinition) layout.getFieldDefinitions().get(0);
        assertEquals(0, fieldDef.getPosition());
    }
}
