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


public class StringValueConverterTest extends TestCase {
    private StringValueConverter converter;

    public void setUp() {
        converter = new StringValueConverter();
    }

    public void test_types() {
        Class[] classes = converter.types();

        assertNotNull(classes);
        assertEquals(1, classes.length);
        assertTrue(Arrays.equals(new Class[]{String.class}, classes));
    }
    
    public void test_convert() {
        String value = "value";
        assertEquals(value, converter.convert(value.getBytes()));
    }
}
