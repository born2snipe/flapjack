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
    private Map fieldMappings = new HashMap();
    private Class clazz;

    public ObjectMapping(Class clazz) {
        this.clazz = clazz;
    }

    public void add(String recordFieldName, String domainFieldName) {
        fieldMappings.put(recordFieldName, new FieldMapping(recordFieldName, domainFieldName));

    }

    public void add(String recordFieldName, String domainFieldName, ValueConverter valueConverter) {
        fieldMappings.put(recordFieldName, new FieldMapping(recordFieldName, domainFieldName, valueConverter));
    }

    public FieldMapping find(String recordFieldName) {
        return (FieldMapping) fieldMappings.get(recordFieldName);
    }

    public Class getMappedClass() {
        return clazz;
    }

}
