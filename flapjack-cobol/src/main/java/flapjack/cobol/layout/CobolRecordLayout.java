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


public class CobolRecordLayout extends AbstractRecordLayout {
    private int offset = 0;
    private CobolFieldPatternValidator patternValidator = new CobolFieldPatternValidator();
    private CobolFieldTypeResolver fieldTypeResolver = new CobolFieldTypeResolver();
    private FieldDefinitionFactory fieldDefFactory;

    public CobolRecordLayout(String id) {
        super(id);
        setFieldDefFactory(new CobolFieldDefinitionFactory());
    }

    public void field(String name, String pattern) {
        field(new CobolFieldInfo(name, pattern));
    }

    public void field(CobolFieldInfo fieldInfo) {
        if (!patternValidator.validate(fieldInfo.getPattern())) {
            throw new IllegalArgumentException("'" + fieldInfo.getPattern() + "' is not a valid COBOL field definition pattern");
        }

        fieldInfo.setType(fieldTypeResolver.resolve(fieldInfo.getPattern()));
        fieldInfo.setPosition(offset);

        CobolFieldDefinition fieldDef = fieldDefFactory.build(fieldInfo);
        addFieldDefinition(fieldDef);
        offset += fieldDef.getLength();
    }

    public void setFieldDefFactory(FieldDefinitionFactory fieldDefFactory) {
        this.fieldDefFactory = fieldDefFactory;
    }
}
