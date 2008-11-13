package flapjack.parser;

/**
 * This represents a record that caused some sort of problem
 */
public class BadRecord {
    private byte[] contents;
    private ParseException exception;

    public BadRecord(byte[] contents) {
        this.contents = contents;
    }

    public BadRecord(byte[] contents, ParseException exception) {
        this(contents);
        this.exception = exception;
    }

    public byte[] getContents() {
        return contents;
    }

    public ParseException getException() {
        return exception;
    }
    
}
