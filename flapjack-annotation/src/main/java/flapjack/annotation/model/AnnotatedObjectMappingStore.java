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

import flapjack.annotation.FieldLocator;
import flapjack.annotation.RecordPackageClassScanner;
import flapjack.annotation.ReflectionField;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;

import java.util.ArrayList;
import java.util.List;


public class AnnotatedObjectMappingStore extends ObjectMappingStore {
    private RecordPackageClassScanner classScanner;
    private FieldLocator fieldLocator;
    private List<String> packages = new ArrayList<String>();

    public AnnotatedObjectMappingStore() {
        this(new RecordPackageClassScanner(), new FieldLocator());
    }

    protected AnnotatedObjectMappingStore(RecordPackageClassScanner classScanner, FieldLocator fieldLocator) {
        this.classScanner = classScanner;
        this.fieldLocator = fieldLocator;
    }

    @Override
    public ObjectMapping find(Class domainClass) {
        if (getObjectMappingCount() == 0) {
            initializeObjectMappings();
        }
        return super.find(domainClass);
    }

    private void initializeObjectMappings() {
        for (Class clazz : classScanner.scan(packages)) {
            ObjectMapping objMapping = new ObjectMapping(clazz);
            List<String> fieldIds = fieldLocator.gatherFieldIds(clazz);
            for (String id : fieldIds) {
                ReflectionField domainField = fieldLocator.locateById(clazz, id);
                if (domainField.hasConverter()) {
                    objMapping.field(id, domainField.getName(), domainField.getConverterClass());
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
