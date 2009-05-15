/**
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License. 
 */
package flapjack.model;

import java.util.*;

/**
 * This class contains all the FieldMappings for a given domain class
 */
public class ObjectMapping {
    private Map recordFieldToDomainFieldMappings = new HashMap();
    private Map domainFieldToRecordFieldMappings = new HashMap();
    private Class clazz;

    public ObjectMapping(Class domainClass) {
        this.clazz = domainClass;
    }

    public void field(String[] recordFields, String domainFieldName, DomainFieldFactory domainFieldFactory) {
        register(new CompoundFieldMapping(Arrays.asList(recordFields), domainFieldName, domainFieldFactory));
    }

    public void field(List recordFields, String domainFieldName, DomainFieldFactory domainFieldFactory) {
        register(new CompoundFieldMapping(recordFields, domainFieldName, domainFieldFactory));
    }

    /**
     * Add a field mapping
     *
     * @param recordFieldName - the name/id that will be given to a record field
     * @param domainFieldName - the field name on the domain object
     */
    public void field(String recordFieldName, String domainFieldName) {
        register(new SingleFieldMapping(recordFieldName, domainFieldName));
    }

    /**
     * Add a field mapping
     *
     * @param recordFieldName     - the name/id that will be given to a record field
     * @param domainFieldName     - the field name on the domain object
     * @param valueConverterClass - the ValueConverter class to be used converting the field data
     */
    public void field(String recordFieldName, String domainFieldName, Class valueConverterClass) {
        register(new SingleFieldMapping(recordFieldName, domainFieldName, valueConverterClass));
    }

    /**
     * Find a field mapping by the record field name
     *
     * @param recordFieldName - the name/id that will be given to a record field
     * @return the FieldMapping for the given field
     */
    public FieldMapping findRecordField(String recordFieldName) {
        if (recordFieldName == null) {
            return null;
        }
        return (FieldMapping) recordFieldToDomainFieldMappings.get(recordFieldName.toLowerCase());
    }

    /**
     * The class that is being mapped
     *
     * @return the class being mapped
     */
    public Class getMappedClass() {
        return clazz;
    }

    /**
     * The number of fields for the given mapping
     *
     * @return the field count
     */
    public int getFieldCount() {
        return recordFieldToDomainFieldMappings.size();
    }

    /**
     * Find a field mapping by the domain field name
     *
     * @param domainFieldName - the field name on the domain object
     * @return the FieldMapping for the given field
     */
    public FieldMapping findDomainField(String domainFieldName) {
        return (FieldMapping) domainFieldToRecordFieldMappings.get(domainFieldName);
    }

    public boolean hasFieldMappingFor(String recordFieldName) {
        return findRecordField(recordFieldName) != null;
    }

    private void register(FieldMapping fieldMapping) {
        Iterator it = fieldMapping.getRecordFields().iterator();
        while (it.hasNext()) {
            String recordFieldName = (String) it.next();
            recordFieldToDomainFieldMappings.put(recordFieldName.toLowerCase(), fieldMapping);
        }
        domainFieldToRecordFieldMappings.put(fieldMapping.getDomainFieldName(), fieldMapping);
    }

}
