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

import java.util.Arrays;


public class FloatValueConverterTest extends TestCase {
    private FloatValueConverter converter;

    public void setUp() {
        converter = new FloatValueConverter();
    }

    public void test_convertFrom() {
        DataType[] types = converter.convertFrom();

        assertNotNull(types);
        assertEquals(1, types.length);
        assertEquals(DataType.TEXT, types[0]);
    }

    public void test_convert() {
        assertEquals(new Float(1), converter.convert("1".getBytes()));
        assertEquals(new Float(1), converter.convert("1.0".getBytes()));
        assertEquals(new Float(1), converter.convert("01".getBytes()));
    }

    public void test_types() {
        Class[] classes = converter.types();

        assertNotNull(classes);
        assertEquals(2, classes.length);
        assertTrue(Arrays.equals(new Class[]{float.class, Float.class}, classes));
    }
}
