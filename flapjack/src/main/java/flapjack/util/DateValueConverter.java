package flapjack.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converts a text value to a java.util.Date
 * <p/>
 * It takes in multiple date patterns and will go until if finds a match otherwise it will return null.
 */
public class DateValueConverter implements ValueConverter {
    private String[] patterns;

    public DateValueConverter(String[] patterns) {
        this.patterns = patterns;
    }

    public Object convert(String text) {
        for (int i = 0; i < patterns.length; i++) {
            SimpleDateFormat formatter = new SimpleDateFormat(patterns[i]);
            try {
                return formatter.parse(text);
            } catch (ParseException e) {

            }
        }
        return null;
    }

    public Class[] types() {
        return new Class[]{Date.class};
    }
}
