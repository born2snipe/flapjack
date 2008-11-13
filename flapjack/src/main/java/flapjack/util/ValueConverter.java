package flapjack.util;

/**
 * Used to covert text values into some other value
 */
public interface ValueConverter {

    /**
     * Attempts to convert the given text into the appropriate type
     *
     * @param text - the text to be converted
     * @return the result of the conversion attempt
     */
    Object convert(String text);

    /**
     * This is used to determine what class type should the text be converted to
     *
     * @return the resulting coversion class types
     */
    Class[] types();
}