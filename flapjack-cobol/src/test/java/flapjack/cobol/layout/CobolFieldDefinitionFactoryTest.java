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

import flapjack.layout.PaddingDescriptor;
import junit.framework.TestCase;


public class CobolFieldDefinitionFactoryTest extends TestCase {
    private CobolFieldDefinitionFactory factory;
    private StubPaddingDescriptor paddingDescriptor;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new CobolFieldDefinitionFactory();
        paddingDescriptor = new StubPaddingDescriptor();
    }

    public void test_integer() {
        CobolFieldDefinition fieldDefinition = factory.build(createInfo(CobolFieldType.INTEGER, "name", "9", 0), paddingDescriptor);
        assertTrue(fieldDefinition instanceof IntegerFieldDefinition);
        assertEquals("name", fieldDefinition.getName());
        assertEquals("9", fieldDefinition.getPattern());
        assertEquals(0, fieldDefinition.getPosition());
        assertSame(paddingDescriptor, fieldDefinition.getPaddingDescriptor());
    }
    
    public void test_decimal() {
        CobolFieldDefinition fieldDefinition = factory.build(createInfo(CobolFieldType.DECIMAL, "name", "9v9", 0), paddingDescriptor);
        assertTrue(fieldDefinition instanceof DecimalFieldDefinition);
        assertEquals("name", fieldDefinition.getName());
        assertEquals("9V9", fieldDefinition.getPattern());
        assertEquals(0, fieldDefinition.getPosition());
        assertSame(paddingDescriptor, fieldDefinition.getPaddingDescriptor());
    }

    
    public void test_alphaNumeric() {
        CobolFieldDefinition fieldDefinition = factory.build(createInfo(CobolFieldType.ALPHA_NUMERIC, "name", "pattern", 0), paddingDescriptor);

        assertNotNull(fieldDefinition);
        assertTrue(fieldDefinition instanceof AlphaNumericFieldDefinition);
        assertEquals("name", fieldDefinition.getName());
        assertEquals("PATTERN", fieldDefinition.getPattern());
        assertEquals(0, fieldDefinition.getPosition());
        assertSame(paddingDescriptor, fieldDefinition.getPaddingDescriptor());
    }

    private CobolFieldInfo createInfo(CobolFieldType type, String name, String pattern, int position) {
        CobolFieldInfo info = new CobolFieldInfo();
        info.setType(type);
        info.setName(name);
        info.setPattern(pattern);
        info.setPosition(position);
        return info;
    }

    private static final class StubPaddingDescriptor implements PaddingDescriptor {
        public byte[] applyPadding(byte[] data, int length) {
            return data;
        }
    }
}
