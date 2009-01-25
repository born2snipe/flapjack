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

import flapjack.layout.RecordLayout;
import flapjack.util.ClassUtil;
import flapjack.util.TypeConverter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class BeanMappedRecordFactory implements RecordFactory {
    private Class clazz;
    private TypeConverter typeConverter;
    private Map fieldMappings = new HashMap();

    public BeanMappedRecordFactory(Class clazz, TypeConverter typeConverter) {
        this.clazz = clazz;
        this.typeConverter = typeConverter;
    }

    public Object build(Object obj, RecordLayout recordLayout) {
        Map fields = (Map) obj;
        Object domain = createInstance();
        String beanPath = (String) fieldMappings.get("field1");
        Field field = ClassUtil.findField(domain, beanPath);
        ClassUtil.setBean(domain, beanPath, typeConverter.convert(field.getType(), (byte[]) fields.get("field1")));
        return domain;
    }

    private Object createInstance() {
        // TODO - pull out to a separate class
        return ClassUtil.newInstance(clazz);
    }

    public void setFieldMappings(Map fieldMappings) {
        this.fieldMappings.putAll(fieldMappings);
    }
}
