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

import flapjack.layout.FieldDefinition;
import flapjack.layout.RecordLayout;
import flapjack.model.FieldMapping;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;


public class RecordBuilder {
    private static final String NO_RECORD_LAYOUT = "Could not resolve RecordLayout(s) for {0}";
    private static final String NO_FIELD_MAPPING = "Could not find a FieldMapping for field=\"{0}\" on class {1}";
    private static final String NO_OBJECT_MAPPING = "Could not find an ObjectMapping for {0}";
    private RecordLayoutResolver recordLayoutResolver;
    private ObjectMappingStore objectMappingStore;


    public void build(List domainObjects, OutputStream output) {
        try {
            Iterator domainObjectIterator = domainObjects.iterator();
            while (domainObjectIterator.hasNext()) {
                Object domain = domainObjectIterator.next();
                List recordLayouts = locateRecordLayouts(domain);
                ObjectMapping objectMapping = locateObjectMapping(domain);
                Iterator it = ((RecordLayout) recordLayouts.get(0)).getFieldDefinitions().iterator();
                while (it.hasNext()) {
                    FieldDefinition fieldDefinition = (FieldDefinition) it.next();
                    FieldMapping fieldMapping = locateFieldMapping(domain, objectMapping, fieldDefinition);
                    output.write(getField(fieldMapping.getDomainFieldName(), domain).getBytes());
                    output.flush();
                }
            }
        } catch (IOException err) {
            throw new BuilderException("A problem occured while building file", err);
        } finally {
            try {
                output.close();
            } catch (IOException e) {

            }
        }

    }

    private FieldMapping locateFieldMapping(Object domain, ObjectMapping objectMapping, FieldDefinition fieldDefinition) {
        FieldMapping fieldMapping = objectMapping.findRecordField(fieldDefinition.getName());
        if (fieldMapping == null) {
            throw new BuilderException(MessageFormat.format(NO_FIELD_MAPPING, new Object[]{fieldDefinition.getName(), domain.getClass().getName()}));
        }
        return fieldMapping;
    }

    private List locateRecordLayouts(Object domain) {
        List recordLayouts = recordLayoutResolver.resolve(domain);
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

    private String getField(String fieldName, Object domain) {
        // TODO - needs to be pulled out to another class!!
        try {
            Field field = domain.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (String) field.get(domain);
        } catch (NoSuchFieldException e) {
            // TODO - needs coverage
        } catch (IllegalAccessException e) {
            // TODO = needs coverage
        }
        return null;
    }

    public void setRecordLayoutResolver(RecordLayoutResolver recordLayoutResolver) {
        this.recordLayoutResolver = recordLayoutResolver;
    }

    public void setObjectMappingStore(ObjectMappingStore objectMappingStore) {
        this.objectMappingStore = objectMappingStore;
    }
}