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
package flapjack.util;

import junit.framework.TestCase;

import java.util.Arrays;


public class CharBinaryValueConverterTest extends TestCase {
    private CharBinaryValueConverter converter;

    public void setUp() {
        converter = new CharBinaryValueConverter();
    }

    public void test_convert() {
        assertEquals(new Character('1'), converter.toDomain(ValueConverterTestUtil.binary('1')));
    }

    public void test_convert_NotEnoughData() {
        try {
            converter.toDomain(new byte[1]);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("There are not enough bytes expected 2 got 1 bytes", err.getMessage());
        }
    }

    public void test_convert_NullByteArray() {
        try {
            converter.toDomain(null);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Byte array given was null", err.getMessage());
        }
    }

    public void test_types() {
        Class[] classes = converter.types();

        assertNotNull(classes);
        assertTrue(Arrays.equals(new Class[]{char.class, Character.class}, classes));
    }
}
