package flapjack.util;

import org.jmock.MockObjectTestCase;


public class TypeConverterTest extends MockObjectTestCase {
    private TypeConverter converter;

    public void test_typeRegistered_SingleParam() {
        assertEquals(new Integer(1), converter.convert(Integer.class, "text"));
    }

    public void test_typeRegistered_TwoParam() {
        assertEquals(new Integer(1), converter.convert(Integer.class, "text", "text2"));
    }

    public void test_typeNotRegistered() {
        try {
            converter.convert(Double.class, "text");
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("No flapjack.util.ValueConverter registered for type java.lang.Double", err.getMessage());
        }
    }
    
    public void test_ValueConverterThrowsException() {
        try {
            converter.convert(Long.class, "text");
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Problem converting \"text\" to java.lang.Long", err.getMessage());
            assertTrue(err.getCause() instanceof NumberFormatException);
        }
    }

    protected void setUp() throws Exception {
        converter = new TypeConverter();
        converter.registerConverter(new ShuntValueConverter(new Integer(1)));
        converter.registerConverter(new ExplodingValueConverter(new NumberFormatException()));
    }

    private static class ExplodingValueConverter implements ValueConverter {
        private RuntimeException exception;

        private ExplodingValueConverter(RuntimeException exception) {
            this.exception = exception;
        }

        public Object convert(String text) {
            throw exception;
        }

        public Class type() {
            return Long.class;
        }
    }

    private static class ShuntValueConverter implements ValueConverter {
        private Object value;

        public ShuntValueConverter(Object value) {
            this.value = value;
        }

        public Object convert(String text) {
            return value;
        }

        public Class type() {
            return Integer.class;
        }
    }
}
