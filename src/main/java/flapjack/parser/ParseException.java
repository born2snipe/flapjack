package flapjack.parser;

import flapjack.layout.RecordLayout;
import flapjack.layout.FieldDefinition;


public class ParseException extends Exception {
    private FieldDefinition fieldDefinition;
    private RecordLayout recordLayout;

    public ParseException(Throwable cause, RecordLayout recordLayout, FieldDefinition fieldDefinition) {
        super(cause);
        this.recordLayout = recordLayout;
        this.fieldDefinition = fieldDefinition;
    }

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public RecordLayout getRecordLayout() {
        return recordLayout;
    }
}
