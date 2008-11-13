package flapjack.util;


public interface ValueConverter {
    Object convert(String text);
    Class type();
}