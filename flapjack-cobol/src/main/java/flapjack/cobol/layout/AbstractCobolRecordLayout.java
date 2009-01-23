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

import flapjack.layout.SimpleRecordLayout;


public abstract class AbstractCobolRecordLayout extends SimpleRecordLayout {
    private int offset = 0;
    private CobolFieldPatternValidator patternValidator = new CobolFieldPatternValidator();
    private CobolFieldTypeResolver fieldTypeResolver = new CobolFieldTypeResolver();

    protected AbstractCobolRecordLayout() {
        defineFields();
    }

    protected void cobolField(String name, String pattern) {
        if (!patternValidator.validate(pattern)) {
            throw new IllegalArgumentException("'" + pattern + "' is not a valid COBOL field definition pattern");
        }

        CobolFieldType fieldType = fieldTypeResolver.resolve(pattern);
        CobolFieldDefinition fieldDef = null;
        if (fieldType == CobolFieldType.DECIMAL) {
            fieldDef = new DecimalFieldDefinition(name, offset, pattern);
        } else if (fieldType == CobolFieldType.INTEGER) {
            fieldDef = new IntegerFieldDefinition(name, offset, pattern);
        } else if (fieldType == CobolFieldType.ALPHA_NUMERIC) {
            fieldDef = new AlphaNumericFieldDefinition(name, offset, pattern);
        }
        addFieldDefinition(fieldDef);
        offset += fieldDef.getLength();
    }

    protected abstract void defineFields();

}
