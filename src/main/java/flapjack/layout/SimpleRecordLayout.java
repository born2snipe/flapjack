package flapjack.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Basic implementation of the RecordLayout
 */
public class SimpleRecordLayout implements RecordLayout {
    private List fieldDefinitions = new ArrayList();

    public void addFieldDefinition(FieldDefinition fieldDef) {
        fieldDefinitions.add(fieldDef);
    }

    public List getFieldDefinitions() {
        return Collections.unmodifiableList(fieldDefinitions);
    }

    public int getLength() {
        int len = 0;
        Iterator it = fieldDefinitions.iterator();
        while (it.hasNext()) {
            FieldDefinition fieldDef = (FieldDefinition) it.next();
            len += fieldDef.getLength();
        }
        return len;
    }
}
