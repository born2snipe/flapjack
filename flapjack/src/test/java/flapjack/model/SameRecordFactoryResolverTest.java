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
package flapjack.model;

import junit.framework.TestCase;
import flapjack.layout.SimpleRecordLayout;


public class SameRecordFactoryResolverTest extends TestCase {

    public void test_contructor_NoDefaultConstructor() {
         try {
            new SameRecordFactoryResolver(NoDefaultConstructor.class);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Cannot construct without a default constructor", err.getMessage());
        }
    }

    public void test_constructor_NullClass() {
        try {
            new SameRecordFactoryResolver(null);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Cannot construct with a 'null' RecordFactory", err.getMessage());
        }
    }

    public void test_resolve() {
        SameRecordFactoryResolver resolver = new SameRecordFactoryResolver(SimpleRecordFactory.class);

        RecordFactory factory = resolver.resolve(null);

        assertNotNull(factory);
        assertTrue(factory instanceof SimpleRecordFactory);
    }

    private static class NoDefaultConstructor extends SimpleRecordLayout {
        private NoDefaultConstructor(String blah) {
        }
    }
}
