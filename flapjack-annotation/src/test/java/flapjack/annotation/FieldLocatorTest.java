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

import flapjack.layout.SimpleRecordLayout;
import junit.framework.TestCase;

import java.util.List;


public class FieldLocatorTest extends TestCase {
    private FieldLocator locator;

    public void setUp() {
        locator = new FieldLocator();
    }

    public void test_locateById_NotFoundField() {
        assertNull(locator.locateById(Dummy.class, "doesNotExist"));
    }

    public void test_locateById_FoundFieldByIdProvided() {
        java.lang.reflect.Field field = locator.locateById(Dummy.class, "field1");

        assertNotNull(field);
        assertEquals("fieldOne", field.getName());
    }

    public void test_locateById_FoundFieldByFieldNameMatching() {
        java.lang.reflect.Field field = locator.locateById(Dummy.class, "field2");

        assertNotNull(field);
        assertEquals("field2", field.getName());
    }
    
    public void test_locateById_FoundFieldByFieldNameMatching_FieldIdDoesNotMeetJavaSpec_WithNumber() {
        java.lang.reflect.Field field = locator.locateById(Dummy.class, "Field 2");

        assertNotNull(field);
        assertEquals("field2", field.getName());
    }

    public void test_locateById_FoundFieldByFieldNameMatching_FieldIdDoesNotMeetJavaSpec() {
        java.lang.reflect.Field field = locator.locateById(Dummy.class, "Field three");

        assertNotNull(field);
        assertEquals("fieldThree", field.getName());
    }
    
    public void test_locateById_FoundFieldByFieldNameMatching_FieldIdDoesNotMeetJavaSpec_SingleLetter() {
        java.lang.reflect.Field field = locator.locateById(Dummy.class, "Field a");

        assertNotNull(field);
        assertEquals("fieldA", field.getName());
    }

    public void test_locateById_FoundFieldByFieldNameMatching_FieldIdExactMatch() {
        java.lang.reflect.Field field = locator.locateById(Dummy.class, "FieldB");

        assertNotNull(field);
        assertEquals("FieldB", field.getName());
    }

    @Record(SimpleRecordLayout.class)
    private static class Dummy {
        @Field("field1")
        private String fieldOne;
        @Field
        private String field2;
        @Field
        private String fieldThree;
        @Field
        private String fieldA;
        @Field
        private String FieldB;
        private String fieldNotInLayout;
    }
}
