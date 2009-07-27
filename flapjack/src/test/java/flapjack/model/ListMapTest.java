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

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;


public class ListMapTest extends TestCase {
    private ListMap listMap;

    protected void setUp() throws Exception {
        super.setUp();
        listMap = new ListMap();
    }

    public void test_keys_MultipleItems() {
        listMap.put("key", "value");
        listMap.put("key2", "value");

        assertEquals(new LinkedHashSet(Arrays.asList(new String[]{"key", "key2"})), listMap.keys());
    }

    public void test_keys_SingleItem() {
        listMap.put("key", "value");

        assertEquals(new LinkedHashSet(Arrays.asList(new String[]{"key"})), listMap.keys());
    }

    public void test_iterator_MultipleItems() {
        listMap.put("key", "value");
        listMap.put("key2", "value2");

        Iterator it = listMap.iterator();
        assertTrue(it.hasNext());
        assertEquals("value", it.next());
        assertTrue(it.hasNext());
        assertEquals("value2", it.next());
        assertFalse(it.hasNext());
    }

    public void test_iterator_SingleItem() {
        listMap.put("key", "value");

        Iterator it = listMap.iterator();
        assertTrue(it.hasNext());
        assertEquals("value", it.next());
    }

    public void test_iterator_SingleItem_AddMultipleTimes() {
        listMap.put("key", "value");
        listMap.put("key", "value2");

        Iterator it = listMap.iterator();
        assertTrue(it.hasNext());
        assertEquals("value2", it.next());
    }

    public void test_getByIndex_SingleItem() {
        listMap.put("key", "value");

        assertEquals("value", listMap.get(0));
    }

    public void test_getByIndex_MultipleItems() {
        listMap.put("key", "value");
        listMap.put("key2", "value2");

        assertEquals("value2", listMap.get(1));
    }

    public void test_getByKey_MultipleItems() {
        listMap.put("key", "value");
        listMap.put("key2", "value2");

        assertEquals("value2", listMap.get("key2"));
    }

    public void test_getByKey_OneItem() {
        listMap.put("key", "value");

        assertEquals("value", listMap.get("key"));
    }

    public void test_size_OneItem() {
        listMap.put("key", "value");

        assertEquals(1, listMap.size());
    }

    public void test_size_MultipleItems() {
        listMap.put("key", "value");
        listMap.put("key2", "value2");

        assertEquals(2, listMap.size());
    }


}
