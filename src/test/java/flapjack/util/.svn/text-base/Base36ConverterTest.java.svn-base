package commonline.core.util;

import junit.framework.TestCase;


public class Base36ConverterTest extends TestCase {
    private Base36Converter converter;

    public void test_convertTo() {
        // March 15, 1998
        assertEquals("SBF", converter.convertTo(36699));
    }

    public void test_convertFrom_Uppercase() {
        // March 15, 1998
        assertEquals(36699, converter.convertFrom("SBF"));
    }

    public void test_convertFrom_Lowercase() {
        // March 15, 1998
        assertEquals(36699, converter.convertFrom("sbf"));
    }

    public void test_convertFrom_Mixcase() {
        // March 15, 1998
        assertEquals(36699, converter.convertFrom("sBf"));
    }

    protected void setUp() throws Exception {
        super.setUp();
        converter = new Base36Converter();
    }

}
