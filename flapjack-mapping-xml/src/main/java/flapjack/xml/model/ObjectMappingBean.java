package flapjack.xml.model;

import java.util.ArrayList;
import java.util.List;


public class ObjectMappingBean {
    private Class mappedClass;
    private List fieldMappings = new ArrayList();

    public Class getMappedClass() {
        return mappedClass;
    }

    public void setMappedClass(Class mappedClass) {
        this.mappedClass = mappedClass;
    }

    public void add(FieldMappingBean fieldMapping) {
        fieldMappings.add(fieldMapping);
    }

    public List getFieldMappings() {
        return fieldMappings;
    }
}
