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

import flapjack.util.ClassUtil;
import flapjack.util.TypeConverter;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;


public class BeanPathObjectMapper implements ObjectMapper {
    private static final String NO_FIELD_MAPPING = "Could not locate field mapping for field=\"{0}\"";
    private static final String NO_FIELD_ON_OBJECT = "Could not map {0} to {1} on {2}, \"{1}\" could NOT be found";
    private static final String NO_OBJECT_MAPPING = "Could not locate object mapping for class={0}";
    private static final String NO_VALUE_CONVERTER = "Could not find a {0} in the TypeConverter";

    private TypeConverter typeConverter = new TypeConverter();
    private boolean ignoreUnmappedFields;
    private ObjectMappingStore objectMappingStore;


    public void mapOnTo(Object parsedFields, Object domain) throws IllegalArgumentException {
        Map fields = (Map) parsedFields;
        Class domainClass = domain.getClass();
        if (!objectMappingStore.isMapped(domainClass)) {
            throw new IllegalArgumentException(MessageFormat.format(NO_OBJECT_MAPPING, new String[]{domainClass.getName()}));
        }
        ObjectMapping objectMapping = objectMappingStore.find(domainClass);
        Iterator it = fields.keySet().iterator();
        while (it.hasNext()) {
            String recordFieldId = (String) it.next();
            if (!objectMapping.hasFieldMappingFor(recordFieldId)) {
                if (!ignoreUnmappedFields) {
                    throw new IllegalArgumentException(MessageFormat.format(NO_FIELD_MAPPING, new String[]{recordFieldId}));
                }
            } else {
                FieldMapping fieldMapping = objectMapping.findRecordField(recordFieldId);
                String beanPath = fieldMapping.getDomainFieldName();
                Field field = locateDomainField(domain, recordFieldId, beanPath);
                byte[] data = (byte[]) fields.get(recordFieldId);
                Object value = convertRecordFieldToDomainType(fieldMapping, field, data);
                ClassUtil.setBean(domain, beanPath, value);
            }
        }
    }


    private Object convertRecordFieldToDomainType(FieldMapping fieldMapping, Field field, byte[] data) {
        Class converterClass = fieldMapping.getValueConverterClass();
        if (converterClass != null) {
            if (!typeConverter.isRegistered(converterClass)) {
                throw new IllegalArgumentException(MessageFormat.format(NO_VALUE_CONVERTER, new String[]{converterClass.getName()}));
            }
            return typeConverter.find(converterClass).convert(data);
        } else {
            return typeConverter.convert(field.getType(), data);
        }
    }

    private Field locateDomainField(Object domain, String key, String beanPath) {
        Field field = ClassUtil.findField(domain, beanPath);
        if (field == null) {
            throw new IllegalArgumentException(MessageFormat.format(NO_FIELD_ON_OBJECT, new String[]{key, beanPath, domain.getClass().getName()}));
        }
        return field;
    }

    public void setIgnoreUnmappedFields(boolean ignoreUnmappedFields) {
        this.ignoreUnmappedFields = ignoreUnmappedFields;
    }

    public void setObjectMappingStore(ObjectMappingStore objectMappingStore) {
        this.objectMappingStore = objectMappingStore;
    }

    public void setTypeConverter(TypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }
}
