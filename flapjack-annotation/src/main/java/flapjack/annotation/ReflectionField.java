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
package flapjack.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


public class ReflectionField {
    private java.lang.reflect.Field field;

    public ReflectionField(Field field) {
        this.field = field;
    }

    public String getName() {
        return field.getName();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return (T) field.getAnnotation(annotationClass);
    }

    public boolean isAnnotatedWith(Class annotationClass) {
        return getAnnotation(annotationClass) != null;
    }

    public boolean hasConverter() {
        return isAnnotatedWith(Converter.class);
    }

    public Class getConverterClass() {
        return getAnnotation(Converter.class).value();
    }
}
