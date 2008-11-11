package flapjack.util;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateValueConverterTest extends TestCase {
    private DateValueConverter converter;

    public void setUp() {
        converter = new DateValueConverter(new String[]{"MM/dd/yyyy", "MM/yyyy"});
    }
    
    public void test_type() {
        assertEquals(Date.class, converter.type());
    }

    public void test_convert() {
        assertEquals(createDate("01/20/1908 00:00"), converter.convert("01/20/1908"));
        assertEquals(createDate("01/01/1908 00:00"), converter.convert("01/1908"));
    }

    public void test_convert_NoPatternFound() {
        assertNull(converter.convert("01-20-1908"));
    }

    private Date createDate(String value) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }
}
