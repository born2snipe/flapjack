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

public class LongTextValueConverterTest extends ValueConverterTestCase {
    private LongTextValueConverter converter;

    public void setUp() {
        converter = new LongTextValueConverter();
    }

    public void test_toBytes() {
        assertEquals(binary("1"), converter.toBytes(new Long(1L)));
        assertNull(converter.toBytes(null));
    }

    public void test_toDomain() {
        assertEquals(new Long(1), converter.toDomain(binary("1")));
        assertEquals(new Long(1), converter.toDomain(binary("01")));
    }

    public void test_types() {
        Class[] classes = converter.types();

        assertNotNull(classes);
        assertEquals(2, classes.length);
        assertEquals(new Class[]{long.class, Long.class}, classes);
    }
}
