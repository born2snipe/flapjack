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
package flapjack.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


public class ClassUtil {

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
                throw new IllegalArgumentException("Cannot construct without a default constructor");
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

    public static Object findField(Object obj, String beanPath) {
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
                Field field = obj.getClass().getDeclaredField(beanPath);
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (NoSuchFieldException e) {
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
