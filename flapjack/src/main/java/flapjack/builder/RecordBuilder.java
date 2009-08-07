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
package flapjack.builder;

import flapjack.io.RecordWriter;
import flapjack.layout.FieldDefinition;
import flapjack.layout.NoOpPaddingDescriptor;
import flapjack.layout.PaddingDescriptor;
import flapjack.layout.RecordLayout;
import flapjack.model.FieldByteMap;
import flapjack.model.FieldMapping;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.util.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RecordBuilder {
    private static final NoOpPaddingDescriptor NO_OP_PADDING_DESCRIPTOR = new NoOpPaddingDescriptor();
    private static final String NO_RECORD_LAYOUT = "Could not resolve RecordLayout(s) for {0}";
    private static final String NO_OBJECT_MAPPING = "Could not find an ObjectMapping for {0}";
    private static final String NOT_ENOUGH_DATA = "Not enough data given! Did you forget the padding? Expected {0}, but was {1}, for field=\"{2}\" on layout=\"{3}\"";
    private static final String TOO_MUCH_DATA = "Too much data given! Expected {0}, but was {1}, for field=\"{2}\" on layout=\"{3}\"";
    private BuilderRecordLayoutResolver builderRecordLayoutResolver;
    private ObjectMappingStore objectMappingStore;
    private TypeConverter typeConverter = new TypeConverter();
    private static final byte[] ZERO_BYTES = new byte[0];

    public void build(Object domainObject, RecordWriter writer) {
        build(Arrays.asList(new Object[]{domainObject}), writer);
    }

    public void build(List domainObjects, RecordWriter writer) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Iterator domainObjectIterator = domainObjects.iterator();
            FieldByteMap alreadyBuiltFields = new FieldByteMap();
            while (domainObjectIterator.hasNext()) {
                Object domain = domainObjectIterator.next();
                List recordLayouts = locateRecordLayouts(domain);
                ObjectMapping objectMapping = locateObjectMapping(domain);
                RecordLayout recordLayout = (RecordLayout) recordLayouts.get(0);
                List fieldDefinitions = recordLayout.getFieldDefinitions();
                Iterator it = fieldDefinitions.iterator();
                while (it.hasNext()) {
                    FieldDefinition fieldDefinition = (FieldDefinition) it.next();
                    String fieldName = fieldDefinition.getName();
                    int fieldLength = fieldDefinition.getLength();
                    PaddingDescriptor paddingDescriptor = getPaddingDescriptor(fieldDefinition);
                    FieldMapping fieldMapping = objectMapping.findRecordField(fieldName);

                    byte bytes[] = ZERO_BYTES;
                    if (alreadyBuiltFields.contains(fieldDefinition)) {
                        bytes = alreadyBuiltFields.get(fieldDefinition);
                    } else if (fieldMapping != null) {
                        List mappedFieldDefinitions = findFieldDefinitionsforMapping(fieldMapping, fieldDefinitions);
                        Object fieldValue = getField(fieldMapping.getDomainFieldName(), domain);
                        FieldByteMap byteMap = fieldMapping.getBinaryFieldFactory().build(fieldValue, typeConverter, mappedFieldDefinitions);
                        bytes = byteMap.get(fieldDefinition);
                        alreadyBuiltFields.putAll(byteMap);
                    }

                    if (shouldPaddingBeApplied(bytes, fieldLength)) {
                        bytes = paddingDescriptor.applyPadding(bytes, fieldLength);
                    }

                    if (notEnoughDataForField(bytes, fieldLength)) {
                        Integer expected = new Integer(fieldLength);
                        Integer actual = new Integer(bytes.length);
                        String layoutId = recordLayout.getId();
                        throw new BuilderException(MessageFormat.format(NOT_ENOUGH_DATA, new Object[]{expected, actual, fieldName, layoutId}));
                    } else if (tooMuchDataForField(bytes, fieldLength)) {
                        Integer expected = new Integer(fieldLength);
                        Integer actual = new Integer(bytes.length);
                        String layoutId = recordLayout.getId();
                        throw new BuilderException(MessageFormat.format(TOO_MUCH_DATA, new Object[]{expected, actual, fieldName, layoutId}));
                    } else {
                        output.write(bytes);
                    }
                }
                writer.write(output.toByteArray());
                output.reset();
                alreadyBuiltFields.clear();
            }
        } catch (IOException err) {
            throw new BuilderException("A problem occured while building file", err);
        } finally {
            writer.close();
        }

    }

    private PaddingDescriptor getPaddingDescriptor(FieldDefinition fieldDefinition) {
        PaddingDescriptor descriptor = fieldDefinition.getPaddingDescriptor();
        if (descriptor == null) {
            return NO_OP_PADDING_DESCRIPTOR;
        }
        return descriptor;
    }

    private boolean shouldPaddingBeApplied(byte[] bytes, int fieldLength) {
        return fieldLength > bytes.length;
    }

    private boolean tooMuchDataForField(byte[] bytes, int fieldLength) {
        return fieldLength < bytes.length;
    }

    private List findFieldDefinitionsforMapping(FieldMapping fieldMapping, List fieldDefinitions) {
        List mappedFieldDefinitions = new ArrayList();
        Iterator it = fieldMapping.getRecordFields().iterator();
        while (it.hasNext()) {
            mappedFieldDefinitions.add(locateFieldDefinitionFor((String) it.next(), fieldDefinitions));
        }
        return mappedFieldDefinitions;
    }

    private FieldDefinition locateFieldDefinitionFor(String fieldName, List fieldDefinitions) {
        Iterator it = fieldDefinitions.iterator();
        while (it.hasNext()) {
            FieldDefinition fieldDef = (FieldDefinition) it.next();
            if (fieldDef.getName().equalsIgnoreCase(fieldName)) {
                return fieldDef;
            }
        }
        return null;
    }

    private boolean notEnoughDataForField(byte[] bytes, int fieldLength) {
        return bytes.length < fieldLength;
    }

    private List locateRecordLayouts(Object domain) {
        List recordLayouts = builderRecordLayoutResolver.resolve(domain);
        if (recordLayouts.size() == 0) {
            throw new BuilderException(MessageFormat.format(NO_RECORD_LAYOUT, new Object[]{domain.getClass().getName()}));
        }
        return recordLayouts;
    }

    private ObjectMapping locateObjectMapping(Object domain) {
        ObjectMapping objectMapping = objectMappingStore.find(domain.getClass());
        if (objectMapping == null) {
            throw new BuilderException(MessageFormat.format(NO_OBJECT_MAPPING, new Object[]{domain.getClass().getName()}));
        }
        return objectMapping;
    }

    private Object getField(String fieldName, Object domain) {
        // TODO - needs to be pulled out to another class!!
        try {
            Field field = domain.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(domain);
        } catch (NoSuchFieldException e) {
            // TODO - needs coverage
        } catch (IllegalAccessException e) {
            // TODO = needs coverage
        }
        return null;
    }

    public void setBuilderRecordLayoutResolver(BuilderRecordLayoutResolver recordLayoutResolver) {
        this.builderRecordLayoutResolver = recordLayoutResolver;
    }

    public void setObjectMappingStore(ObjectMappingStore objectMappingStore) {
        this.objectMappingStore = objectMappingStore;
    }

    public void setTypeConverter(TypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }
}
