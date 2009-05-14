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

public class IntegerTextValueConverterTest extends ValueConverterTestCase {
    private IntegerTextValueConverter converter;

    public void setUp() {
        converter = new IntegerTextValueConverter();
    }

    public void test_type() {
        assertEquals(new Class[]{Integer.class, int.class}, converter.types());
    }

    public void test_toDomain() {
        assertEquals(new Integer(1), converter.toDomain("1".getBytes()));
    }

    public void test_toDomain_LeadingZeros() {
        assertEquals(new Integer(999), converter.toDomain("0000999".getBytes()));
    }


    public void test_toDomain_EmptyString() {
        assertNull(converter.toDomain("".getBytes()));
    }

    public void test_toDomain_NullString() {
        assertNull(converter.toDomain(null));
    }
}
