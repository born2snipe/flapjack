package flapjack.parser;

import flapjack.layout.FieldDefinition;
import flapjack.layout.RecordLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Breaks up the array of bytes into fields and converts them to Strings
 */
public class StringRecordFieldParser extends ByteRecordFieldParser {
    public Object parse(byte[] bytes, RecordLayout recordLayout) throws ParseException {
        List byteArrays = (List) super.parse(bytes, recordLayout);
        if (byteArrays.size() == 0) {
            return byteArrays;
        }

        List fields = new ArrayList();
        Iterator it = byteArrays.iterator();
        while (it.hasNext()) {
            fields.add(new String((byte[]) it.next()));
        }

        return fields;
    }


}
