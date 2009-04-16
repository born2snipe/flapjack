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


public class ObjectMappingStoreTest extends TestCase {
    private ObjectMappingStore store;

    protected void setUp() throws Exception {
        super.setUp();
        store = new ObjectMappingStore();
    }

    public void test_add_MappingAlreadyExists() {
        ObjectMapping mapping = new ObjectMapping(String.class);
        store.add(mapping);

        try {
            store.add(mapping);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Duplicate ObjectMapping for class java.lang.String", err.getMessage());
        }
        assertEquals(1, store.getObjectMappingCount());
    }

    public void test_find_NoMappings() {
        assertNull(store.find(String.class));
    }

    public void test_find() {
        store.add(new ObjectMapping(String.class));

        ObjectMapping objMap = store.find(String.class);

        assertNotNull(objMap);
        assertEquals(String.class, objMap.getMappedClass());
        assertEquals(1, store.getObjectMappingCount());
    }

    public void test_isMapped_NotMapped() {
        assertFalse(store.isMapped(String.class));
    }

    public void test_isMapped_Mapped() {
        store.add(new ObjectMapping(String.class));

        assertTrue(store.isMapped(String.class));
    }
}
