/**
 * Copyright 2008 Dan Dudley
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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


public class MappedRecordFactory implements RecordFactory {
    private static final FieldLocator FIELD_LOCATOR = new FieldLocator();
    private Class clazz;
    private TypeConverter typeConverter;

    public MappedRecordFactory(Class clazz, TypeConverter typeConverter) {
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
            try {
                PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
                Method method = descriptor.getWriteMethod();
                method.invoke(domain, typeConverter.convert(descriptor.getPropertyType(), value));
            } catch (IntrospectionException e) {
                throw new RuntimeException("Problem occured trying to find setter for field [name=" + field.getName() + "]", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object createInstance() {
        return ClassUtil.newInstance(clazz);
    }
}
