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

import java.util.HashMap;
import java.util.Map;


public class ObjectMapStore {
    private Map mappings = new HashMap();

    public ObjectMapping find(Class clazz) {
        return (ObjectMapping) mappings.get(clazz);
    }

    public void add(ObjectMapping objectMapping) throws IllegalArgumentException {
        Class mappedClass = objectMapping.getMappedClass();
        if (mappings.containsKey(mappedClass)) {
            throw new IllegalArgumentException("Duplicate ObjectMapping for class "+mappedClass.getName());
        }
        mappings.put(mappedClass, objectMapping);
    }
}
