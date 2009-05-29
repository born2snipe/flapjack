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
package flapjack.cobol.layout;

import flapjack.layout.Padding;
import junit.framework.TestCase;


public class AlphaNumericFieldDefinitionTest extends TestCase {
    public void test_constructor_SinglePattern() {
        AlphaNumericFieldDefinition fieldDef = new AlphaNumericFieldDefinition("name", 1, "X");

        assertEquals(1, fieldDef.getPosition());
        assertEquals("name", fieldDef.getName());
        assertEquals(1, fieldDef.getLength());
        assertEquals("X", fieldDef.getPattern());
        assertEquals(Padding.RIGHT, fieldDef.getPadding());
    }

    public void test_constructor_SinglePattern_WithSpaces() {
        AlphaNumericFieldDefinition fieldDef = new AlphaNumericFieldDefinition("name", 1, " X ");

        assertEquals(1, fieldDef.getPosition());
        assertEquals("name", fieldDef.getName());
        assertEquals(1, fieldDef.getLength());
        assertEquals("X", fieldDef.getPattern());
    }

    public void test_constructor_MultiPattern() {
        AlphaNumericFieldDefinition fieldDef = new AlphaNumericFieldDefinition("two", 2, "XX");

        assertEquals(2, fieldDef.getPosition());
        assertEquals("two", fieldDef.getName());
        assertEquals(2, fieldDef.getLength());
        assertEquals("XX", fieldDef.getPattern());
    }

    public void test_constructor_MultiplierPattern_SingleDigit() {
        AlphaNumericFieldDefinition fieldDef = new AlphaNumericFieldDefinition("two", 2, "X(2)");

        assertEquals(2, fieldDef.getPosition());
        assertEquals("two", fieldDef.getName());
        assertEquals(2, fieldDef.getLength());
        assertEquals("X(2)", fieldDef.getPattern());
    }

    public void test_constructor_MultiplierPattern_MultipleDigits() {
        AlphaNumericFieldDefinition fieldDef = new AlphaNumericFieldDefinition("two", 2, "X(21)");

        assertEquals(2, fieldDef.getPosition());
        assertEquals("two", fieldDef.getName());
        assertEquals(21, fieldDef.getLength());
        assertEquals("X(21)", fieldDef.getPattern());
    }

    public void test_constructor_MultiplierPattern_Lowercase() {
        AlphaNumericFieldDefinition fieldDef = new AlphaNumericFieldDefinition("two", 2, "x(21)");

        assertEquals(2, fieldDef.getPosition());
        assertEquals("two", fieldDef.getName());
        assertEquals(21, fieldDef.getLength());
        assertEquals("X(21)", fieldDef.getPattern());
    }
}
