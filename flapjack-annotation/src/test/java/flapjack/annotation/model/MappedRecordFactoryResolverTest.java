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
package flapjack.annotation.model;

import flapjack.model.RecordFactory;
import flapjack.test.User;
import flapjack.test.UserRecordLayout;
import flapjack.test2.Phone;
import flapjack.test2.PhoneRecordLayout;
import flapjack.util.ValueConverter;
import flapjack.annotation.Record;
import flapjack.annotation.Field;
import flapjack.layout.SimpleRecordLayout;
import flapjack.layout.SimpleFieldDefinition;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MappedRecordFactoryResolverTest extends TestCase {
    private MappedRecordFactoryResolver resolver;

    protected void setUp() throws Exception {
        super.setUp();
        resolver = new MappedRecordFactoryResolver();
    }

    public void test_resolve_AddAdditionalValueConverters() {
        resolver.setPackages(Arrays.asList("flapjack.annotation.model"));
        resolver.setValueConverters(Arrays.asList(new BarValueConverter()));

        FooRecordLayout layout = new FooRecordLayout();
        layout.addFieldDefinition(new SimpleFieldDefinition("bar", 0, 0));

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("bar", "foo bar");

        Object obj = resolver.resolve(layout).build(fields, layout);

        assertNotNull(obj);
        System.out.println("obj = " + obj);
        assertTrue(obj instanceof Foo);

        Foo foo = (Foo) obj;
        
    }

    public void test_resolve_NoPackagesRegistered() {
        try {
            resolver.resolve(new UserRecordLayout());
            fail();
        } catch (IllegalStateException err) {
            assertEquals("There are no packages configured for scanning! Was this intended?", err.getMessage());
        }
    }

    public void test_resolve_NoPackagesRegistered_EmptyList() {
        try {
            resolver.setPackages(new ArrayList());

            resolver.resolve(new UserRecordLayout());
            fail();
        } catch (IllegalStateException err) {
            assertEquals("There are no packages configured for scanning! Was this intended?", err.getMessage());
        }
    }

    public void test_resolve() {
        resolver.setPackages(Arrays.asList("flapjack.test"));

        UserRecordLayout layout = new UserRecordLayout();

        RecordFactory recordFactory = resolver.resolve(layout);

        assertNotNull(recordFactory);
        assertTrue(recordFactory instanceof MappedRecordFactory);

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("First Name", "foo");
        fields.put("Last Name", "bar");

        Object obj = recordFactory.build(fields, layout);
        assertNotNull(obj);
        assertTrue(obj instanceof User);

        User user = (User) obj;
        assertEquals("foo", user.getFirstName());
        assertEquals("bar", user.getLastName());
    }

    public void test_resolve_MultipleClasses() {
        resolver.setPackages(Arrays.asList("flapjack.test", "flapjack.test2"));

        PhoneRecordLayout layout = new PhoneRecordLayout();

        RecordFactory recordFactory = resolver.resolve(layout);

        assertNotNull(recordFactory);
        assertTrue(recordFactory instanceof MappedRecordFactory);

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("Area", "123");
        fields.put("Number", "456-7890");

        Object obj = recordFactory.build(fields, layout);
        assertNotNull(obj);
        assertTrue(obj instanceof Phone);

        Phone phone = (Phone) obj;
        assertEquals("123", phone.getAreaCode());
        assertEquals("456-7890", phone.getNumber());
    }

    @Record(FooRecordLayout.class)
    public static class Foo {
        @Field("bar")
        private Bar bar;

        public Bar getBar() {
            return bar;
        }

        public void setBar(Bar bar) {
            this.bar = bar;
        }
    }

    private static class Bar {
        public final String value;

        private Bar(String value) {
            this.value = value;
        }
    }

    private static class BarValueConverter implements ValueConverter {
        public Object convert(String text) {
            return new Bar(text);
        }

        public Class[] types() {
            return new Class[]{Bar.class};
        }
    }

    private static class FooRecordLayout extends SimpleRecordLayout {
    }

}
