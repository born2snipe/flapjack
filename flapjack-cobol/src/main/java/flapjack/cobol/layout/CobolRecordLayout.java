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

import flapjack.layout.AbstractRecordLayout;
import flapjack.layout.PaddingDescriptor;
import flapjack.layout.TextPaddingDescriptor;


public class CobolRecordLayout extends AbstractRecordLayout {
    protected static final TextPaddingDescriptor ZERO_PADDER = new TextPaddingDescriptor(TextPaddingDescriptor.Padding.LEFT, '0');
    protected static final TextPaddingDescriptor SPACE_PADDER = new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' ');

    private CobolFieldPatternValidator patternValidator;
    private CobolFieldTypeResolver fieldTypeResolver;
    private FieldDefinitionFactory fieldDefFactory;

    public CobolRecordLayout(String id) {
        this(id, new CobolFieldPatternValidator(), new CobolFieldTypeResolver(), new CobolFieldDefinitionFactory());
    }

    protected CobolRecordLayout(String id, CobolFieldPatternValidator patternValidator, CobolFieldTypeResolver fieldTypeResolver, FieldDefinitionFactory fieldDefFactory) {
        super(id);
        this.patternValidator = patternValidator;
        this.fieldTypeResolver = fieldTypeResolver;
        this.fieldDefFactory = fieldDefFactory;
    }

    public void field(String name, String pattern) {
        field(new CobolFieldInfo(name, pattern), null);
    }

    public void field(String name, String pattern, PaddingDescriptor paddingDescriptor) {
        field(new CobolFieldInfo(name, pattern), paddingDescriptor);
    }

    private void field(CobolFieldInfo fieldInfo, PaddingDescriptor paddingDescriptor) {
        if (!patternValidator.validate(fieldInfo.getPattern())) {
            throw new IllegalArgumentException("'" + fieldInfo.getPattern() + "' is not a valid COBOL field definition pattern");
        }

        fieldInfo.setType(fieldTypeResolver.resolve(fieldInfo.getPattern()));
        fieldInfo.setPosition(offset);

        PaddingDescriptor tmpPaddingDescriptor = paddingDescriptor;
        if (paddingDescriptor == null) {
            if (fieldInfo.getType() == CobolFieldType.ALPHA_NUMERIC) {
                tmpPaddingDescriptor = SPACE_PADDER;
            } else if (fieldInfo.getType() == CobolFieldType.DECIMAL || fieldInfo.getType() == CobolFieldType.INTEGER) {
                tmpPaddingDescriptor = ZERO_PADDER;
            }
        }

        addFieldDefinition(fieldDefFactory.build(fieldInfo, tmpPaddingDescriptor));
    }


}
