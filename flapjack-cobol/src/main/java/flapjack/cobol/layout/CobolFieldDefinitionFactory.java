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

import flapjack.layout.TextPaddingDescriptor;


public class CobolFieldDefinitionFactory implements FieldDefinitionFactory {
    private static final TextPaddingDescriptor ZERO_PADDER = new TextPaddingDescriptor(TextPaddingDescriptor.Padding.LEFT, '0');
    private static final TextPaddingDescriptor SPACE_PADDER = new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' ');

    public CobolFieldDefinition build(CobolFieldInfo fieldInfo) {
        if (fieldInfo.getType() == CobolFieldType.DECIMAL)
            return new DecimalFieldDefinition(fieldInfo.getName(), fieldInfo.getPosition(), fieldInfo.getPattern(), ZERO_PADDER);
        else if (fieldInfo.getType() == CobolFieldType.INTEGER)
            return new IntegerFieldDefinition(fieldInfo.getName(), fieldInfo.getPosition(), fieldInfo.getPattern(), ZERO_PADDER);
        else
            return new AlphaNumericFieldDefinition(fieldInfo.getName(), fieldInfo.getPosition(), fieldInfo.getPattern(), SPACE_PADDER);
    }
}
