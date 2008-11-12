package flapjack.layout;

import java.util.List;


/**
 * Represents the layout of a record
 */
public interface RecordLayout {

    /**
     * The field definitions to be used for this specific record type
     *
     * @return a list of FieldDefinitions
     */
    List getFieldDefinitions();

    /**
     * The length of this type of record
     *
     * @return the number bytes
     */
    int getLength();

}
