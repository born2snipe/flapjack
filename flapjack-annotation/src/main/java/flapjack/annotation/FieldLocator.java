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
import java.util.ArrayList;
import java.util.List;


public class FieldLocator {

    private List<java.lang.reflect.Field> locateAnnotated(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<java.lang.reflect.Field> fields = new ArrayList<java.lang.reflect.Field>();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(annotationClass) != null)
                fields.add(field);
        }
        return fields;
    }

    public List<String> gatherFieldIds(Class<?> clazz) {
        List<String> fieldNames = new ArrayList<String>();
        for (java.lang.reflect.Field field : locateAnnotated(clazz, Field.class)) {
            String id = field.getAnnotation(Field.class).value();
            if (id == null || id.trim().length() == 0) {
                id = field.getName();
            }
            fieldNames.add(id);
        }
        return fieldNames;
    }

    /**
     * Attempts to locate a Field on the given class with the matching id.
     * <p/>
     * The 'id' will attempt to match on value passed into the @Field annotation. If this value is not provided
     * it will attempt to find a field defined in the class with the same name.
     *
     * @param clazz - the class to look for the field with the given id
     * @param id    - the id to match the Field on
     * @return the Field found on the class or will return  null if no matching field was found
     */
    public java.lang.reflect.Field locateById(Class<?> clazz, String id) {
        for (java.lang.reflect.Field field : locateAnnotated(clazz, Field.class)) {
            String annotationValue = field.getAnnotation(Field.class).value();
            if (!isEmpty(annotationValue) && id.equals(annotationValue)) {
                return field;
            } else if (isEmpty(annotationValue) && (field.getName().equals(id) || field.getName().equals(convertIdIntoJavaFieldName(id)))) {
                return field;
            }
        }
        return null;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    private String convertIdIntoJavaFieldName(String id) {
        String[] sections = id.split("\\s");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sections.length; i++) {
            if (i == 0) {
                builder.append(sections[i].toLowerCase());
            } else {
                builder.append(Character.toUpperCase(sections[i].charAt(0)));
                builder.append(sections[i].substring(1));
            }
        }
        return builder.toString();
    }
}
