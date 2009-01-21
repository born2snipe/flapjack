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


public class DoubleBinaryValueConverterTest extends TestCase {
    private DoubleBinaryValueConverter converter;

    public void setUp() {
        converter = new DoubleBinaryValueConverter();
    }
    
    public void test_convert() {
        assertEquals(new Double(1.0d), converter.convert(ValueConverterTestUtil.binary(1.0d)));
    }

    public void test_convert_NotEnoughData() {
        try {
            converter.convert(new byte[7]);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("There are not enough bytes expected 8 got 7 bytes", err.getMessage());
        }
    }
    
    public void test_convert_NullByteArray() {
        try {
            converter.convert(null);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Byte array given was null", err.getMessage());
        }
    }

    public void test_types() {
        assertTrue(Arrays.equals(new Class[]{double.class, Double.class}, converter.types()));
    }
}
