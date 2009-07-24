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


public class CharTextValueConverterTest extends ValueConverterTestCase {
    private CharTextValueConverter converter;

    protected void setUp() throws Exception {
        super.setUp();
        converter = new CharTextValueConverter();
    }

    public void test_toDomain() {
        assertEquals(new Character('1'), converter.toDomain(text(1)));
        assertNull(converter.toDomain("".getBytes()));
        assertNull(converter.toDomain(null));
    }

    public void test_toBytes() {
        assertEquals(text(1), converter.toBytes(new Character('1')));
        assertEquals(new byte[0], converter.toBytes(null));
    }

    public void test_toDomain_MoreThanOneByteGiven() {
        try {
            converter.toDomain(binary("12"));
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("To many bytes was given length=2, was expecting a single byte", err.getMessage());
        }
    }

    public void test_types() {
        Class[] classes = converter.types();

        assertNotNull(classes);
        assertEquals(2, classes.length);
        assertTrue(Arrays.equals(new Class[]{char.class, Character.class}, classes));
    }


}
