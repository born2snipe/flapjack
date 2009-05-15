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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class BeanPathObjectMapper implements ObjectMapper {
    private static final String NO_FIELD_MAPPING = "Could not locate field mapping for field=\"{0}\"";
    private static final String NO_FIELD_ON_OBJECT = "Could not map {0} to {1} on {2}, \"{1}\" could NOT be found";
    private static final String NO_OBJECT_MAPPING = "Could not locate object mapping for class={0}";
    private static final String X = "'{0}' was not found in record, field mapping for '{1}' on {2}";
    private static final FieldNameMassager MASSAGER = new FieldNameMassager();

    private TypeConverter typeConverter = new TypeConverter();
    private boolean ignoreUnmappedFields;
    private ObjectMappingStore objectMappingStore;


    public void mapOnTo(Object parsedFields, Object domain) throws IllegalArgumentException {
        Map fields = (Map) parsedFields;
        Class domainClass = domain.getClass();
        verifyClassIsMapped(domainClass);
        ObjectMapping objectMapping = objectMappingStore.find(domainClass);
        List alreadyMappedFields = new ArrayList();
        Iterator it = fields.keySet().iterator();
        while (it.hasNext()) {
            String recordFieldId = (String) it.next();
            if (!objectMapping.hasFieldMappingFor(recordFieldId)) {
                if (!ignoreUnmappedFields) {
                    throw new IllegalArgumentException(MessageFormat.format(NO_FIELD_MAPPING, new String[]{recordFieldId}));
                }
            } else {
                if (!alreadyMappedFields.contains(recordFieldId)) {
                    FieldMapping fieldMapping = objectMapping.findRecordField(recordFieldId);
                    String beanPath = fieldMapping.getDomainFieldName();
                    Field field = locateDomainField(domain, recordFieldId, beanPath);
                    DomainFieldFactory domainFieldFactory = fieldMapping.getFactory();
                    ListMap recordData = grabRecordDataForField(fields, recordFieldId, fieldMapping.getRecordFields());
                    ClassUtil.setBean(domain, beanPath, domainFieldFactory.build(recordData, field.getType(), typeConverter));
                    alreadyMappedFields.addAll(fieldMapping.getRecordFields());
                }
            }
        }
    }

    private ListMap grabRecordDataForField(Map fields, String recordFieldId, List recordFields) {
        ListMap listMap = new ListMap();
        Iterator fieldIterator = recordFields.iterator();
        while (fieldIterator.hasNext()) {
            listMap.put(recordFieldId, MASSAGER.get(fields, (String) fieldIterator.next()));
        }
        return listMap;
    }

    private void verifyClassIsMapped(Class domainClass) {
        if (!objectMappingStore.isMapped(domainClass)) {
            throw new IllegalArgumentException(MessageFormat.format(NO_OBJECT_MAPPING, new String[]{domainClass.getName()}));
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
