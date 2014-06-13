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

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class FieldLocatorTest {
    private FieldLocator locator;

    @Before
    public void setUp() {
        locator = new FieldLocator();
    }

    @Test
    public void test_locateById_NotFoundField() {
        assertNull(locator.locateById(Dummy.class, "doesNotExist"));
    }

    @Test
    public void test_locateById_FoundFieldByIdProvided() {
        ReflectionField field = locator.locateById(Dummy.class, "field1");

        assertNotNull(field);
        assertEquals("fieldOne", field.getName());
    }

    @Test
    public void test_locateById_FoundFieldByFieldNameMatching() {
        ReflectionField field = locator.locateById(Dummy.class, "field2");

        assertNotNull(field);
        assertEquals("field2", field.getName());
    }

    @Test
    public void test_locateById_FoundFieldByFieldNameMatching_FieldIdDoesNotMeetJavaSpec_WithNumber() {
        ReflectionField field = locator.locateById(Dummy.class, "Field 2");

        assertNotNull(field);
        assertEquals("field2", field.getName());
    }

    @Test
    public void test_locateById_FoundFieldByFieldNameMatching_FieldIdDoesNotMeetJavaSpec() {
        ReflectionField field = locator.locateById(Dummy.class, "Field three");

        assertNotNull(field);
        assertEquals("fieldThree", field.getName());
    }

    @Test
    public void test_locateById_FoundFieldByFieldNameMatching_FieldIdDoesNotMeetJavaSpec_SingleLetter() {
        ReflectionField field = locator.locateById(Dummy.class, "Field a");

        assertNotNull(field);
        assertEquals("fieldA", field.getName());
    }

    @Test
    public void test_locateById_FoundFieldByFieldNameMatching_FieldIdExactMatch() {
        ReflectionField field = locator.locateById(Dummy.class, "FieldB");

        assertNotNull(field);
        assertEquals("FieldB", field.getName());
    }

    @Test
    public void test_locateAnnotated() {
        List<String> fields = locator.gatherFieldIds(Dummy.class);
        assertNotNull(fields);
        assertEquals(5, fields.size());
        assertEquals(Arrays.asList("field1", "field2", "fieldThree", "fieldA", "FieldB"), fields);
    }

    @Record("dummy")
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
