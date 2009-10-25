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
package flapjack.cobol.util;

import flapjack.util.ConversionException;
import junit.framework.TestCase;

import java.math.BigDecimal;


public class CobolDecimalTextValueConverterTest extends TestCase {

    public void test_notEnoughData() {
        try {
            converter(Double.class, "9v99").toDomain("1".getBytes());
            fail();
        } catch (ConversionException err) {
            assertEquals("There are not enough bytes expected at least 2 byte(s) got 1 byte(s)", err.getMessage());
        }
    }

    public void test_BigDecimal() {
        assertEquals(new BigDecimal("1.2"), converter(BigDecimal.class, "9v9").toDomain("12".getBytes()));
    }

    public void test_float() {
        assertEquals(new Float(1.2), converter(float.class, "9v9").toDomain("12".getBytes()));
    }

    public void test_Float() {
        assertEquals(new Float(1.2), converter(Float.class, "9v9").toDomain("12".getBytes()));
    }

    public void test_double() {
        assertEquals(new Double(1.2), converter(double.class, "9v9").toDomain("12".getBytes()));
    }

    public void test_Double() {
        assertEquals(new Double(1.2), converter(Double.class, "9v9").toDomain("12".getBytes()));
    }

    public void test_NoBytes() {
        assertNull(converter(Double.class, "9v9").toDomain(new byte[0]));
    }

    public void test_NullBytes() {
        assertNull(converter(Double.class, "9v9").toDomain(null));
    }

    private CobolDecimalTextValueConverter converter(Class type, String cobolPattern) {
        return new CobolDecimalTextValueConverter(type, cobolPattern);
    }
}
