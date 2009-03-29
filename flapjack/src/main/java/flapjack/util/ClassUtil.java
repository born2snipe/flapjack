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
package flapjack.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;


public class ClassUtil {
    private static final String NO_DEFALUT_CONSTRUCTOR = "Cannot construct an instance of {0} without a default constructor";

    /**
     * Attempts to locate the default constructor of the given class
     *
     * @param clazz - the class to search
     * @return the default constructor or null if not found
     */
    public static Constructor findDefaultConstructor(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (int i = 0; i < constructors.length; i++) {
            Constructor constructor = constructors[i];
            if (constructor.getParameterTypes().length == 0) {
                return constructor;
            }
        }
        return null;
    }

    /**
     * Attempts to create a new instance of the given class using the default constructor
     *
     * @param clazz - the class to create a new instance of
     * @return a new instance of the given class
     */
    public static Object newInstance(Class clazz) {
        try {
            Constructor constructor = findDefaultConstructor(clazz);
            if (constructor == null) {
                throw new IllegalArgumentException(MessageFormat.format(NO_DEFALUT_CONSTRUCTOR, new Object[]{clazz.getName()}));
            }
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[0]);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field findField(Object obj, String beanPath) {
        try {
            if (beanPath.indexOf('.') > -1) {
                String name = beanPath.substring(0, beanPath.indexOf('.'));
                String path = beanPath.substring(beanPath.indexOf('.') + 1);
                Field field = obj.getClass().getDeclaredField(name);
                field.setAccessible(true);
                return findField(field.get(obj), path);
            } else if (obj == null) {
                return null;
            } else {
                return obj.getClass().getDeclaredField(beanPath);
            }
        } catch (NoSuchFieldException e) {
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setField(Object parent, String fieldName, Object value) {
        try {
            Method method = findSetter(parent, fieldName);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(parent, new Object[]{value});
            } else {
                Field field = parent.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(parent, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field '" + fieldName + "' was not found on " + parent.getClass().getName());
        }
    }

    private static Method findSetter(Object parent, String fieldName) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(fieldName, parent.getClass());
            return descriptor.getWriteMethod();
        } catch (IntrospectionException e) {
            return null;
        }
    }

    public static void setBean(Object obj, String beanPath, Object value) throws IllegalStateException {
        try {
            if (beanPath.indexOf('.') > -1) {
                String name = beanPath.substring(0, beanPath.indexOf('.'));
                String path = beanPath.substring(beanPath.indexOf('.') + 1);
                Field field = findField(obj, name);
                field.setAccessible(true);
                Object child = field.get(obj);
                if (child == null) {
                    throw new IllegalStateException("Choked on a null field '" + name + "' on " + obj.getClass().getName());
                }
                setBean(child, path, value);
            } else {
                setField(obj, beanPath, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
