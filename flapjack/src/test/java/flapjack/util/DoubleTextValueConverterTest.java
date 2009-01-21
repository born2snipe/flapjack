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


public class DoubleTextValueConverterTest extends TestCase {
    private DoubleTextValueConverter converter;
    
    public void test_convert() {
        assertEquals(new Double(2), converter.convert("2".getBytes()));
        assertEquals(new Double(2), converter.convert("2.0".getBytes()));
        assertEquals(new Double(2), converter.convert("02.0".getBytes()));
    }

    public void test_types() {

        Class[] classes = converter.types();

        assertNotNull(classes);
        assertEquals(2, classes.length);
        assertTrue(Arrays.equals(new Class[]{double.class, Double.class}, classes));
    }

    protected void setUp() throws Exception {
        super.setUp();
        converter = new DoubleTextValueConverter();
    }
}
