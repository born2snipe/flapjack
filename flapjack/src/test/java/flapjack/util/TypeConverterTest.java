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

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;


public class TypeConverterTest extends MockObjectTestCase {
    private TypeConverter converter;
    private Mock mockConverter;

    public void test_typeRegistered_SingleParam() {
        mockConverter.expects(once()).method("convert").with(eq("text")).will(returnValue(new Integer(1)));
        assertEquals(new Integer(1), converter.convert(Integer.class, "text"));
    }

    public void test_typeRegistered_TwoParam() {
        mockConverter.expects(once()).method("convert").with(eq("texttext2")).will(returnValue(new Integer(1)));
        assertEquals(new Integer(1), converter.convert(Integer.class, "text", "text2"));
    }

    public void test_typeRegistered_MoreThanTwoParam() {
        mockConverter.expects(once()).method("convert").with(eq("texttext2text3")).will(returnValue(new Integer(1)));
        assertEquals(new Integer(1), converter.convert(Integer.class, new String[]{"text", "text2", "text3"}));
    }

    public void test_typeNotRegistered() {
        try {
            converter.convert(Double.class, "text");
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("No flapjack.util.ValueConverter registered for types java.lang.Double", err.getMessage());
        }
    }

    public void test_ValueConverterThrowsException() {
        NullPointerException original = new NullPointerException();
        mockConverter.expects(once()).method("convert").with(eq("text")).will(throwException(original));
        try {
            converter.convert(Long.class, "text");
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Problem converting \"text\" to java.lang.Long", err.getMessage());
            assertSame(original, err.getCause());
        }
    }

    protected void setUp() throws Exception {
        mockConverter = mock(ValueConverter.class);

        converter = new TypeConverter();
        converter.registerConverter(new MockWrappingValueConverter(Integer.class, (ValueConverter) mockConverter.proxy()));
        converter.registerConverter(new MockWrappingValueConverter(Long.class, (ValueConverter) mockConverter.proxy()));
    }

    private static class MockWrappingValueConverter implements ValueConverter {
        private ValueConverter mock;
        private Class type;

        private MockWrappingValueConverter(Class type, ValueConverter mock) {
            this.mock = mock;
            this.type = type;
        }

        public Object convert(String text) {
            return mock.convert(text);
        }

        public Class[] types() {
            return new Class[]{type};
        }
    }
}
