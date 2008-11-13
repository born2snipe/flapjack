package flapjack.util;


public class IntegerValueConverter implements ValueConverter {
    public Object convert(String text) {
        if (text == null || text.trim().length() == 0) {
            return new Integer(0);
        }
        return new Integer(Integer.parseInt(text));
    }

    public Class[] types() {
        return new Class[]{Integer.class, int.class};
    }
}
