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
package flapjack.util;

import junit.framework.TestCase;


public class IntegerBinaryValueConverterTest extends TestCase {
    private IntegerBinaryValueConverter converter;

    public void setUp() {
        converter = new IntegerBinaryValueConverter();
    }

    public void test_convert() {
        assertEquals(new Integer(1), converter.convert(ValueConverterTestUtil.binary(1)));
    }

    public void test_convert_NotEnoughBytes() {
        try {
            converter.convert(new byte[3]);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("There are not enough bytes expected 4 got 3 bytes", err.getMessage());
        }
    }

    public void test_convertFrom() {
        DataType[] types = converter.convertFrom();

        assertNotNull(types);
        assertEquals(1, types.length);
        assertEquals(DataType.BINARY, types[0]);
    }

    public void test_types() {
        Class[] classes = converter.types();

        assertNotNull(classes);
        assertEquals(2, classes.length);
        assertEquals(int.class, classes[0]);
        assertEquals(Integer.class, classes[1]);
    }


}
