package flapjack.util;

import junit.framework.TestCase;

import java.util.Arrays;


public class IntegerValueConverterTest extends TestCase {
    private IntegerValueConverter converter;

    public void setUp() {
        converter = new IntegerValueConverter();
    }
    
    public void test_type() {
        assertTrue(Arrays.equals(new Class[]{Integer.class, int.class}, converter.types()));
    }

    public void test_convert() {
        assertEquals(new Integer(1), converter.convert("1"));
    }
    
    public void test_convert_LeadingZeros() {
        assertEquals(new Integer(999), converter.convert("0000999"));
    }

    
    public void test_convert_EmptyString() {
        assertEquals(new Integer(0), converter.convert(""));
    }
    
    public void test_convert_NullString() {
        assertEquals(new Integer(0), converter.convert(null));
    }
}
