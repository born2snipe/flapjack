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
package flapjack.annotation.model;

import flapjack.annotation.Converter;
import flapjack.annotation.FieldLocator;
import flapjack.annotation.RecordPackageClassScanner;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import flapjack.util.ValueConverter;

import java.util.ArrayList;
import java.util.List;


public class AnnotatedObjectMappingStore extends ObjectMappingStore {
    private static final RecordPackageClassScanner CLASS_SCANNER = new RecordPackageClassScanner();
    private static final FieldLocator FIELD_LOCATOR = new FieldLocator();
    private List<String> packages = new ArrayList<String>();

    @Override
    public ObjectMapping find(Class domainClass) {
        if (getObjectMappingCount() == 0) {
            initializeObjectMappings();
        }
        return super.find(domainClass);
    }

    private void initializeObjectMappings() {
        for (Class clazz : CLASS_SCANNER.scan(packages)) {
            ObjectMapping objMapping = new ObjectMapping(clazz);
            List<String> fieldIds = FIELD_LOCATOR.gatherFieldIds(clazz);
            for (String id : fieldIds) {
                java.lang.reflect.Field domainField = FIELD_LOCATOR.locateById(clazz, id);
                Converter converter = domainField.getAnnotation(Converter.class);
                if (converter != null && !converter.value().equals(ValueConverter.class)) {
                    objMapping.field(id, domainField.getName(), converter.value());
                } else {
                    objMapping.field(id, domainField.getName());
                }
            }
            add(objMapping);
        }
    }

    public void setPackages(List<String> packages) {
        this.packages.clear();
        this.packages.addAll(packages);
    }
}
