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

import flapjack.layout.FieldDefinition;
import flapjack.layout.PaddingDescriptor;
import flapjack.util.StringUtil;
import flapjack.util.TypeConverter;
import flapjack.util.ValueConverter;

import java.text.MessageFormat;
import java.util.Arrays;


public class SingleFieldMapping extends AbstractFieldMapping {
    private static final String NO_VALUE_CONVERTER = "Could not find a {0} registered! Are you sure you registered {0} in the TypeConverter?";
    private Class valueConverter;

    public SingleFieldMapping(String recordFieldName, String domainFieldName) {
        super(Arrays.asList(new String[]{recordFieldName}), domainFieldName);
    }

    public SingleFieldMapping(String recordFieldName, String domainFieldName, Class valuevalueConverter) {
        super(Arrays.asList(new String[]{recordFieldName}), domainFieldName);
        this.valueConverter = valuevalueConverter;
    }


    public DomainFieldFactory getDomainFieldFactory() {
        return new DomainFieldFactory() {
            public Object build(ListMap fields, Class domainFieldType, TypeConverter typeConverter) {
                return findValueConverter(domainFieldType, typeConverter).toDomain((byte[]) fields.get(0));
            }
        };
    }

    public BinaryFieldFactory getBinaryFieldFactory() {
        return new BinaryFieldFactory() {
            public byte[] build(Object domain, TypeConverter typeConverter, FieldDefinition fieldDefinition) {
                byte[] bytes = findValueConverter(domain.getClass(), typeConverter).toBytes(domain);
                PaddingDescriptor paddingDescriptor = fieldDefinition.getPaddingDescriptor();
                if (paddingDescriptor != null) {
                    if (paddingDescriptor.padding == PaddingDescriptor.Padding.RIGHT) {
                        bytes = StringUtil.rightPad(new String(bytes), fieldDefinition.getLength(), paddingDescriptor.paddingCharacter).getBytes();
                    } else if (paddingDescriptor.padding == PaddingDescriptor.Padding.LEFT) {
                        bytes = StringUtil.leftPad(new String(bytes), fieldDefinition.getLength(), paddingDescriptor.paddingCharacter).getBytes();
                    }
                }
                return bytes;
            }
        };
    }

    private ValueConverter findValueConverter(Class domainType, TypeConverter typeConverter) {
        if (valueConverter != null) {
            if (!typeConverter.isRegistered(valueConverter)) {
                throw new IllegalArgumentException(MessageFormat.format(NO_VALUE_CONVERTER, new String[]{valueConverter.getName()}));
            }
            return typeConverter.find(valueConverter);
        } else {
            return typeConverter.type(domainType);
        }
    }
}
