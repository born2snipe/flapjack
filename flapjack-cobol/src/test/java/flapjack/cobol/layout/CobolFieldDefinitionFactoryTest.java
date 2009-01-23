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
package flapjack.cobol.layout;

import flapjack.layout.FieldDefinition;
import junit.framework.TestCase;


public class CobolFieldDefinitionFactoryTest extends TestCase {
    private CobolFieldDefinitionFactory factory;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new CobolFieldDefinitionFactory();
    }

    public void test_build_TextField() {
        FieldDefinition fieldDef = factory.build("X");

        assertNotNull(fieldDef);
        assertTrue(fieldDef instanceof AlphaNumericFieldDefinition);
    }

    public void test_build_InvalidPattern() {
        try {
            factory.build("WTF");
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("'WTF' is not a valid COBOL field definition pattern", err.getMessage());
        }
    }

    
}
