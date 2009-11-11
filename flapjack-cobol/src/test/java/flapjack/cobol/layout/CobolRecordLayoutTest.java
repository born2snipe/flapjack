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

import flapjack.layout.NoOpPaddingDescriptor;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;


public class CobolRecordLayoutTest extends MockObjectTestCase {
    private Mock fieldDefFactory;
    private Mock fieldTypeResolver;
    private Mock fieldPatternValidator;
    private CobolRecordLayout layout;
    private Mock fieldDefinition;

    protected void setUp() throws Exception {
        super.setUp();
        fieldDefFactory = mock(CobolFieldDefinitionFactory.class);
        fieldTypeResolver = mock(CobolFieldTypeResolver.class);
        fieldPatternValidator = mock(CobolFieldPatternValidator.class);
        fieldDefinition = mock(CobolFieldDefinition.class);
        layout = new CobolRecordLayout("id", (CobolFieldPatternValidator) fieldPatternValidator.proxy(), (CobolFieldTypeResolver) fieldTypeResolver.proxy(), (FieldDefinitionFactory) fieldDefFactory.proxy());
    }


    public void test_paddingDescriptorGiven_Decimal() {
        NoOpPaddingDescriptor paddingDescriptor = new NoOpPaddingDescriptor();

        fieldPatternValidator.expects(once()).method("validate").with(eq("pattern")).will(returnValue(true));
        fieldTypeResolver.expects(once()).method("resolve").with(eq("pattern")).will(returnValue(CobolFieldType.DECIMAL));
        fieldDefFactory.expects(once()).method("build").with(isA(CobolFieldInfo.class), eq(paddingDescriptor)).will(returnValue(fieldDefinition.proxy()));
        fieldDefinition.expects(once()).method("getPosition").will(returnValue(0));
        fieldDefinition.expects(once()).method("getLength").will(returnValue(1));

        layout.field("name", "pattern", paddingDescriptor);
    }
    
    public void test_noPaddingDescriptorGiven_Decimal() {
        fieldPatternValidator.expects(once()).method("validate").with(eq("pattern")).will(returnValue(true));
        fieldTypeResolver.expects(once()).method("resolve").with(eq("pattern")).will(returnValue(CobolFieldType.DECIMAL));
        fieldDefFactory.expects(once()).method("build").with(isA(CobolFieldInfo.class), eq(CobolRecordLayout.ZERO_PADDER)).will(returnValue(fieldDefinition.proxy()));
        fieldDefinition.expects(once()).method("getPosition").will(returnValue(0));
        fieldDefinition.expects(once()).method("getLength").will(returnValue(1));

        layout.field("name", "pattern");
    }

    public void test_noPaddingDescriptorGiven_Integer() {
        fieldPatternValidator.expects(once()).method("validate").with(eq("pattern")).will(returnValue(true));
        fieldTypeResolver.expects(once()).method("resolve").with(eq("pattern")).will(returnValue(CobolFieldType.INTEGER));
        fieldDefFactory.expects(once()).method("build").with(isA(CobolFieldInfo.class), eq(CobolRecordLayout.ZERO_PADDER)).will(returnValue(fieldDefinition.proxy()));
        fieldDefinition.expects(once()).method("getPosition").will(returnValue(0));
        fieldDefinition.expects(once()).method("getLength").will(returnValue(1));

        layout.field("name", "pattern");
    }

    public void test_noPaddingDescriptorGiven_Alpha() {
        fieldPatternValidator.expects(once()).method("validate").with(eq("pattern")).will(returnValue(true));
        fieldTypeResolver.expects(once()).method("resolve").with(eq("pattern")).will(returnValue(CobolFieldType.ALPHA_NUMERIC));
        fieldDefFactory.expects(once()).method("build").with(isA(CobolFieldInfo.class), eq(CobolRecordLayout.SPACE_PADDER)).will(returnValue(fieldDefinition.proxy()));
        fieldDefinition.expects(once()).method("getPosition").will(returnValue(0));
        fieldDefinition.expects(once()).method("getLength").will(returnValue(1));

        layout.field("name", "pattern");

        assertEquals(1, layout.getFieldDefinitions().size());
        assertEquals(fieldDefinition.proxy(), layout.getFieldDefinitions().get(0));
    }

    public void test_badPattern() {
        fieldPatternValidator.expects(once()).method("validate").with(eq("pattern")).will(returnValue(false));

        try {
            layout.field("name", "pattern");
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("'pattern' is not a valid COBOL field definition pattern", err.getMessage());
        }

    }


}
