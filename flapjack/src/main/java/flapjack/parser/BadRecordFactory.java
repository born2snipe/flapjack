package flapjack.parser;


public interface BadRecordFactory {
    BadRecord build(byte[] record);
    BadRecord build(byte[] record, ParseException err);
}
