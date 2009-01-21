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

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;


public class TypeConverterTest extends MockObjectTestCase {
    private TypeConverter converter;
    private Mock mockConverter;

    protected void setUp() throws Exception {
        mockConverter = mock(ValueConverter.class);

        converter = new TypeConverter();
        converter.registerConverter(new MockWrappingValueConverter(Integer.class, (ValueConverter) mockConverter.proxy()));
        converter.registerConverter(new MockWrappingValueConverter(Long.class, (ValueConverter) mockConverter.proxy()));
    }

    public void test_registerConverter_NullTypesOnValueConverter() {
        try {
            converter.registerConverter(new MockWrappingValueConverter((Class[]) null, null));
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("flapjack.util.TypeConverterTest$MockWrappingValueConverter does NOT have the class types it supports", err.getMessage());
        }
    }

    public void test_registerConverter_EmptyArrayOfTypesOnValueConverter() {
        try {
            converter.registerConverter(new MockWrappingValueConverter(new Class[0], null));
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("flapjack.util.TypeConverterTest$MockWrappingValueConverter does NOT have the class types it supports", err.getMessage());
        }
    }

    public void test_typeRegistered_TEXT() {
        byte[] bytes = "text".getBytes();
        mockConverter.expects(once()).method("convert").with(eq(bytes)).will(returnValue(new Long(1)));
        assertEquals(new Long(1), converter.convert(Long.class, bytes));
    }

    public void test_typeRegistered_BINARY() {
        byte[] bytes = "text".getBytes();
        mockConverter.expects(once()).method("convert").with(eq(bytes)).will(returnValue(new Integer(1)));
        assertEquals(new Integer(1), converter.convert(Integer.class, bytes));
    }

    public void test_NoConverterRegisterdForClass() {
        try {
            converter.convert(MockWrappingValueConverter.class, "text".getBytes());
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("No flapjack.util.ValueConverter registered for types flapjack.util.TypeConverterTest$MockWrappingValueConverter", err.getMessage());
        }
    }

    public void test_ValueConverterThrowsException() {
        byte[] bytes = "text".getBytes();
        NullPointerException original = new NullPointerException();
        mockConverter.expects(once()).method("convert").with(eq(bytes)).will(throwException(original));
        try {
            converter.convert(Long.class, bytes);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Problem converting \"text\" to java.lang.Long", err.getMessage());
            assertSame(original, err.getCause());
        }
    }

    private static class MockWrappingValueConverter implements ValueConverter {
        private ValueConverter mock;
        private Class[] types;

        private MockWrappingValueConverter(Class type, ValueConverter mock) {
            this(new Class[]{type}, mock);
        }

        private MockWrappingValueConverter(Class types[], ValueConverter mock) {
            this.mock = mock;
            this.types = types;
        }

        public Object convert(byte[] bytes) {
            return mock.convert(bytes);
        }

        public Class[] types() {
            return types;
        }
    }
}
