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

import flapjack.util.ValueConverter;

import java.util.HashMap;
import java.util.Map;


public class ObjectMapping {
    private Map recordFieldToDomainFieldMappings = new HashMap();
    private Map domainFieldToRecordFieldMappings = new HashMap();
    private Class clazz;

    public ObjectMapping(Class clazz) {
        this.clazz = clazz;
    }

    public void add(String recordFieldName, String domainFieldName) {
        add(recordFieldName, domainFieldName, null);
    }

    public void add(String recordFieldName, String domainFieldName, ValueConverter valueConverter) {
        FieldMapping fieldMapping = new FieldMapping(recordFieldName, domainFieldName, valueConverter);
        recordFieldToDomainFieldMappings.put(recordFieldName, fieldMapping);
        domainFieldToRecordFieldMappings.put(domainFieldName, fieldMapping);
    }

    public FieldMapping find(String recordFieldName) {
        return (FieldMapping) recordFieldToDomainFieldMappings.get(recordFieldName);
    }

    public Class getMappedClass() {
        return clazz;
    }

    public int getFieldCount() {
        return recordFieldToDomainFieldMappings.size();
    }

    public FieldMapping findDomainField(String domainFieldName) {
        return (FieldMapping) domainFieldToRecordFieldMappings.get(domainFieldName);
    }
}
