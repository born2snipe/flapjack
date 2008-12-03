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
package flapjack.annotation;

import java.util.ArrayList;
import java.util.List;


public class FieldLocator {
    /**
     * Locate all Fields on the given class
     *
     * @param clazz - the class to look for Fields on
     * @return a list of Fields found on the given class
     */
    public List<java.lang.reflect.Field> locate(Class<?> clazz) {
        List<java.lang.reflect.Field> fields = new ArrayList();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Field.class) != null)
                fields.add(field);
        }
        return fields;
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
        for (java.lang.reflect.Field field : locate(clazz)) {
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
