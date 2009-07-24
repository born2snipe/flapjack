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

import java.util.Arrays;


public class FloatTextValueConverterTest extends ValueConverterTestCase {
    private FloatTextValueConverter converter;

    public void setUp() {
        converter = new FloatTextValueConverter();
    }

    public void test_toBytes() {
        assertEquals(binary("1.0"), converter.toBytes(new Float(1)));
        assertEquals(new byte[0], converter.toBytes(null));
    }

    public void test_toDomain() {
        assertEquals(new Float(1), converter.toDomain("1".getBytes()));
        assertEquals(new Float(1), converter.toDomain("1.0".getBytes()));
        assertEquals(new Float(1), converter.toDomain("01".getBytes()));
    }

    public void test_types() {
        Class[] classes = converter.types();

        assertNotNull(classes);
        assertEquals(2, classes.length);
        assertTrue(Arrays.equals(new Class[]{float.class, Float.class}, classes));
    }
}
