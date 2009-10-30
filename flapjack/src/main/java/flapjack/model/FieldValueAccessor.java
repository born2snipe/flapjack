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

import java.lang.reflect.Field;
import java.text.MessageFormat;


public class FieldValueAccessor {
    private static final String NULL_ELEMENT = "Encountered a NULL element on {0} field \"{1}\"";
    private static final String NO_FIELD_FOUND = "Could NOT find field \"{0}\" on {1}";

    private NullSafeValueFactory nullSafeValueFactory;

    public FieldValueAccessor() {
        this(new NullSafeValueFactory());
    }

    protected FieldValueAccessor(NullSafeValueFactory nullSafeValueFactory) {
        this.nullSafeValueFactory = nullSafeValueFactory;
    }

    public Object get(Object obj, String path) {
        String fieldName = path.split("\\.")[0];
        Field field = findField(obj.getClass(), fieldName);
        field.setAccessible(true);
        try {
            Object child = field.get(obj);
            if (path.indexOf('.') == -1) {
                return child;
            }
            if (child == null) {
                throw new ObjectMappingException(MessageFormat.format(NULL_ELEMENT, new Object[]{obj.getClass().getName(), field.getName()}));
            }
            return get(child, path.substring(path.indexOf('.') + 1));
        } catch (IllegalAccessException e) {

        }
        return null;
    }

    public void set(Object value, Object rootObject, String path) {
        try {
            String fieldName = path.split("\\.")[0];
            Field field = findField(rootObject.getClass(), fieldName);
            if (field == null) {
                throw new ObjectMappingException(MessageFormat.format(NO_FIELD_FOUND, new Object[]{fieldName, rootObject.getClass().getName()}));
            }

            field.setAccessible(true);

            if (path.indexOf('.') == -1) {
                Object tmpValue = value;
                if (tmpValue == null) {
                    tmpValue = nullSafeValueFactory.build(field.getType());
                }
                field.set(rootObject, tmpValue);
            } else {
                Object child = field.get(rootObject);
                if (child == null) {
                    throw new ObjectMappingException(MessageFormat.format(NULL_ELEMENT, new Object[]{rootObject.getClass().getName(), field.getName()}));
                }
                set(value, child, path.substring(path.indexOf('.') + 1));
            }

        } catch (IllegalAccessException e) {

        }
    }

    private Field findField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}
