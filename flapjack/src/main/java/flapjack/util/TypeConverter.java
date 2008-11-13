package flapjack.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds on to a collection of ValueConverters.
 */
public class TypeConverter {
    private Map stringConverters = new HashMap();

    public TypeConverter() {
        registerConverter(new IntegerValueConverter());
    }

    /**
     * Call to register additional ValueConverters
     *
     * @param valueConverter - the ValueConverter to be registered
     */
    public void registerConverter(ValueConverter valueConverter) {
        stringConverters.put(valueConverter.type(), valueConverter);
    }

    /**
     * Attempts to convert the given text to the class type given
     *
     * @param clazz - the class to convert the text to
     * @param text - the text to be converted
     * @return the result of the coversion attempt
     * @throws IllegalArgumentException thrown when an error is encoutered during convesion
     */
    public Object convert(Class clazz, String text) throws IllegalArgumentException {
        ValueConverter converter = (ValueConverter) stringConverters.get(clazz);
        if (converter == null) {
            throw new IllegalArgumentException("No " + ValueConverter.class.getName() + " registered for type " + clazz.getName());
        }
        try {
            return converter.convert(text);
        } catch (RuntimeException err) {
            throw new IllegalArgumentException("Problem converting \""+text+"\" to "+clazz.getName(), err);
        }
    }

    /**
     * Attempts to covert the two given text values appended together into the class type given
     *
     * @param clazz - the class to covert the text to
     * @param text - the text to be converted
     * @param text2 - the text to be converted
     * @return the result of the conversion attempt
     * @throws IllegalArgumentException thrown when an error is encoutered during convesion
     */
    public Object convert(Class clazz, String text, String text2) throws IllegalArgumentException {
        return convert(clazz, new String[]{text, text2});
    }

    /**
     * Attempts to covert the text values appended together into the class type given
     *
     * @param clazz - the class to covert the text to
     * @param texts - the text values to be converted
     * @return the result of the conversion attempt
     * @throws IllegalArgumentException thrown when an error is encoutered during convesion
     */

    public Object convert(Class clazz, String[] texts) {
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<texts.length;i++)
            builder.append(texts[i]);
        return convert(clazz, builder.toString());
    }
}
