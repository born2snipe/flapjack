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


public class ListMap implements Iterable {
    private Map map = new LinkedHashMap();
    private List list = new ArrayList();

    public void put(String key, Object value) {
        map.put(key, value);
        list.add(value);
    }

    public int size() {
        return map.size();
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public Iterator iterator() {
        return list.iterator();
    }

    public Set keys() {
        return map.keySet();
    }
}
