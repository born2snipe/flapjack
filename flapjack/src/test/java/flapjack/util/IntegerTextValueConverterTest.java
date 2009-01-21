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


public class IntegerTextValueConverterTest extends TestCase {
    private IntegerTextValueConverter converter;

    public void setUp() {
        converter = new IntegerTextValueConverter();
    }
    
    public void test_type() {
        assertTrue(Arrays.equals(new Class[]{Integer.class, int.class}, converter.types()));
    }

    public void test_convert() {
        assertEquals(new Integer(1), converter.convert("1".getBytes()));
    }
    
    public void test_convert_LeadingZeros() {
        assertEquals(new Integer(999), converter.convert("0000999".getBytes()));
    }

    
    public void test_convert_EmptyString() {
        assertEquals(new Integer(0), converter.convert("".getBytes()));
    }
    
    public void test_convert_NullString() {
        assertEquals(new Integer(0), converter.convert((byte[])null));
    }
}
