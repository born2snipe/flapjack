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

import flapjack.layout.FieldDefinition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class FieldByteMap {
    private Map fieldBytes = new HashMap();

    public void clear() {
        fieldBytes.clear();
    }

    public void putAll(FieldByteMap fieldByteMap) {
        fieldBytes.putAll(fieldByteMap.fieldBytes);
    }

    public boolean contains(FieldDefinition fieldDefinition) {
        return fieldBytes.containsKey(fieldDefinition);
    }

    public void put(FieldDefinition fieldDefinition, byte[] data) {
        fieldBytes.put(fieldDefinition, data);
    }

    public byte[] get(FieldDefinition fieldDefinition) {
        return (byte[]) fieldBytes.get(fieldDefinition);
    }

    public int size() {
        return fieldBytes.size();
    }

    public Map toMap() {
        return Collections.unmodifiableMap(fieldBytes);
    }
}
