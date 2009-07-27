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


public class ListMap {
    private Map map = new LinkedHashMap();
    private List values = new ArrayList();
    private List keys = new ArrayList();

    public void put(String key, Object value) {
        if (map.containsKey(key)) {
            int index = keys.indexOf(key);
            values.remove(index);
            values.add(index, value);
        } else {
            keys.add(key);
            values.add(value);
        }
        map.put(key, value);
    }

    public int size() {
        return map.size();
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Object get(int index) {
        return values.get(index);
    }

    public Iterator iterator() {
        return values.iterator();
    }

    public Set keys() {
        return map.keySet();
    }

    public String toString() {
        return map.toString();
    }
}
