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
package flapjack.builder;

import flapjack.io.MockOutputStream;
import flapjack.layout.SimpleRecordLayout;
import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.io.IOException;
import java.util.Arrays;


public class RecordBuilderTest extends MockObjectTestCase {
    private Mock recordLayoutResolver;
    private ObjectMappingStore objectMappingStore;
    private RecordBuilder builder;
    private MockOutputStream output;

    protected void setUp() throws Exception {
        super.setUp();
        recordLayoutResolver = mock(RecordLayoutResolver.class);
        objectMappingStore = new ObjectMappingStore();

        builder = new RecordBuilder();
        builder.setRecordLayoutResolver((RecordLayoutResolver) recordLayoutResolver.proxy());
        builder.setObjectMappingStore(objectMappingStore);
        output = new MockOutputStream();
    }

    public void test_build_CouldNotResolveRecordLayout() {
        Person person = new Person("Joe", "Smith");

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(null));

        try {
            builder.build(Arrays.asList(new Object[]{person}), output);
            fail();
        } catch (BuilderException err) {
            assertEquals("Could not resolve RecordLayout for " + Person.class.getName(), err.getMessage());
        }
    }

    public void test_build_ThrowExceptionWriting() {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName");

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(recordLayout));

        IOException error = new IOException();

        try {
            output.willThrowException(error);

            builder.build(Arrays.asList(new Object[]{person}), output);
            fail();
        } catch (BuilderException err) {
            assertEquals("A problem occured while building file", err.getMessage());
            assertSame(error, err.getCause());
            assertTrue(output.isClosed());
        }
    }

    public void test_build_SingleDomainObject() throws IOException {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName");
        personMapping.field("Last Name", "lastName");

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);
        recordLayout.field("Last Name", 5);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(recordLayout));

        builder.build(Arrays.asList(new Object[]{person}), output);

        assertOutput("JoeSmith".getBytes(), 2);
    }

    private void assertOutput(byte[] expectedOutput, int expectedFlushCount) {
        assertTrue(Arrays.equals(expectedOutput, output.getBytes()));
        assertTrue(output.isClosed());
        assertEquals(expectedFlushCount, output.getFlushCount());
    }


    private static class Person {
        private String firstName;
        private String lastName;

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }
}
