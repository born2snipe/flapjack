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
package flapjack.parser;

import flapjack.layout.RecordLayout;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class constructs the same type RecordLayout regardless of what data is given in the resolve method.
 */
public class SameRecordLayoutResolver implements RecordLayoutResolver {
    private Constructor constructor;

    public SameRecordLayoutResolver(Class recordLayoutClass) {
        if (recordLayoutClass == null) {
            throw new IllegalArgumentException("Cannot construct with a 'null' RecordLayout");
        }
        constructor = findDefaultConstructor(recordLayoutClass);
        if (constructor == null) {
            throw new IllegalArgumentException("Cannot construct without a default constructor");
        }
    }

    public RecordLayout resolve(byte[] bytes) {
        try {
            constructor.setAccessible(true);
            return (RecordLayout) constructor.newInstance(new Object[0]);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor findDefaultConstructor(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (int i = 0; i < constructors.length; i++) {
            Constructor constructor = constructors[i];
            if (constructor.getParameterTypes().length == 0) {
                return constructor;
            }
        }
        return null;
    }
}
