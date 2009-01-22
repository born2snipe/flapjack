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
import flapjack.layout.RecordLayout;
import flapjack.model.RecordFactory;
import flapjack.util.ClassUtil;
import flapjack.util.TypeConverter;

import java.lang.reflect.Field;
import java.util.Map;


public class AnnotatedMappedRecordFactory implements RecordFactory {
    private static final FieldLocator FIELD_LOCATOR = new FieldLocator();
    private Class clazz;
    private TypeConverter typeConverter;

    public AnnotatedMappedRecordFactory(Class clazz, TypeConverter typeConverter) {
        this.clazz = clazz;
        this.typeConverter = typeConverter;
    }

    public Object build(Object obj, RecordLayout recordLayout) {
        Map<String, byte[]> fields = (Map<String, byte[]>) obj;
        Object domain = createInstance();
        for (String id : fields.keySet()) {
            setValue(id, fields.get(id), domain);
        }
        return domain;
    }

    private void setValue(String id, byte[] value, Object domain) {
        Field field = FIELD_LOCATOR.locateById(clazz, id);
        if (field != null) {
            ClassUtil.setField(domain, field.getName(), typeConverter.convert(field.getType(), value));
        }
    }

    private Object createInstance() {
        // TODO - this should be extracted to it's own factory to not rely on default constructors, and to not force someone to extend this class to support thier class that doesn't have a default constuctor
        return ClassUtil.newInstance(clazz);
    }
}
