/**
 * Copyright 2008 Dan Dudley
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
package flapjack.util;

import junit.framework.TestCase;
import flapjack.layout.SimpleRecordLayout;


public class ClassUtilTest extends TestCase {

    public void test_findDefaultContructor() {
        assertNotNull(ClassUtil.findDefaultConstructor(SimpleRecordLayout.class));
        assertNull(ClassUtil.findDefaultConstructor(NoDefaultConstructor.class));
    }

    public void test_newInstance() {
        Object obj = ClassUtil.newInstance(SimpleRecordLayout.class);

        assertNotNull(obj);
        assertTrue(obj instanceof SimpleRecordLayout);
    }
    
    public void test_newInstance_NoDefaultConstructor() {
        try {
            ClassUtil.newInstance(NoDefaultConstructor.class);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Cannot construct without a default constructor", err.getMessage());
        }
    }
    
    private static class NoDefaultConstructor {
        private NoDefaultConstructor(String value){}
    }
}
