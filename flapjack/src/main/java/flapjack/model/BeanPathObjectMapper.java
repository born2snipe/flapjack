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

// TODO - should processing stop if a mapping problem occurs?!
public class BeanPathObjectMapper implements ObjectMapper {
    private static final String NO_FIELD_MAPPING = "Could not locate field mapping for field=\"{0}\"";
    private static final String NO_FIELD_ON_OBJECT = "Could not map {0} -> {1} on {2}, \"{1}\" could NOT be found";
    private static final String NO_OBJECT_MAPPING = "Could not locate object mapping for class={0}";

    private TypeConverter typeConverter = new TypeConverter();
    private boolean ignoreUnmappedFields;
    private ObjectMappingStore objectMappingStore;

    public void mapOnTo(Object parsedFields, Object domain) throws IllegalArgumentException {
        Map fields = (Map) parsedFields;
        Class domainClass = domain.getClass();
        ObjectMapping objectMapping = objectMappingStore.find(domainClass);
        if (objectMapping == null) {
            throw new IllegalArgumentException(MessageFormat.format(NO_OBJECT_MAPPING, new String[]{domainClass.getName()}));
        }
        Iterator it = fields.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            FieldMapping fieldMapping = objectMapping.findRecordField(key);
            if (fieldMapping == null) {
                if (!ignoreUnmappedFields) {
                    throw new IllegalArgumentException(MessageFormat.format(NO_FIELD_MAPPING, new String[]{key}));
                }
            } else {
                String beanPath = fieldMapping.getDomainFieldName();
                Field field = ClassUtil.findField(domain, beanPath);
                if (field == null) {
                    throw new IllegalArgumentException(MessageFormat.format(NO_FIELD_ON_OBJECT, new String[]{key, beanPath, domainClass.getName()}));
                }
                Object value = null;
                byte[] data = (byte[]) fields.get(key);
                if (fieldMapping.getValueConverter() != null) {
                    value = fieldMapping.getValueConverter().convert(data);
                } else {
                    value = typeConverter.convert(field.getType(), data);
                }
                ClassUtil.setBean(domain, beanPath, value);
            }
        }
    }

    public void setIgnoreUnmappedFields(boolean ignoreUnmappedFields) {
        this.ignoreUnmappedFields = ignoreUnmappedFields;
    }

    public void setObjectMappingStore(ObjectMappingStore objectMappingStore) {
        this.objectMappingStore = objectMappingStore;
    }
}
