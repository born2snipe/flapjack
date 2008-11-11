package flapjack.model;

import java.util.List;
import java.util.ArrayList;

public class SimpleRecord {
    private List fields = new ArrayList();

    public SimpleRecord(List fields) {
        this.fields = fields;
    }

    public SimpleRecord() {
    }

    public Object getField(int index) {
        return fields.get(index);
    }

    public void setFields(List fields) {
        this.fields = fields;
    }
}
