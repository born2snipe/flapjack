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
import java.util.Iterator;
import java.util.List;


public class RecordBuilder {
    private RecordLayoutResolver recordLayoutResolver;
    private ObjectMappingStore objectMappingStore;


    public void build(List domainObject, OutputStream output) {
        Object domain = domainObject.get(0);
        RecordLayout recordLayout = recordLayoutResolver.resolve(domain);
        ObjectMapping objectMapping = objectMappingStore.find(domain.getClass());
        Iterator it = recordLayout.getFieldDefinitions().iterator();
        try {
            while (it.hasNext()) {
                FieldDefinition fieldDefinition = (FieldDefinition) it.next();
                FieldMapping fieldMapping = objectMapping.findRecordField(fieldDefinition.getName());
                output.write(getField(fieldMapping.getDomainFieldName(), domain).getBytes());
                output.flush();
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
