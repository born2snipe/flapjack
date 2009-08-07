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

import flapjack.io.RecordWriter;
import flapjack.layout.*;
import flapjack.model.*;
import flapjack.util.ReverseValueConverter;
import flapjack.util.TypeConverter;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class RecordBuilderTest extends MockObjectTestCase {
    private Mock recordLayoutResolver;
    private ObjectMappingStore objectMappingStore;
    private RecordBuilder builder;
    private TypeConverter typeConverter;
    private Mock writer;
    private Mock mockObjectMappingStore;
    private Mock mockRecordLayout;
    private Mock mockFieldDefinition;
    private Mock mockObjectMapping;
    private Mock mockFieldMapping;
    private Mock mockBinaryFieldFactory;
    private Mock mockTypeConverter;
    private Mock mockPaddingDescriptor;
    private FieldByteMap byteMap;

    protected void setUp() throws Exception {
        super.setUp();
        recordLayoutResolver = mock(BuilderRecordLayoutResolver.class);
        mockObjectMappingStore = mock(ObjectMappingStore.class);
        mockRecordLayout = mock(RecordLayout.class);
        mockFieldDefinition = mock(FieldDefinition.class);
        mockObjectMapping = mock(MockableObjectMapping.class);
        mockFieldMapping = mock(FieldMapping.class);
        mockBinaryFieldFactory = mock(BinaryFieldFactory.class);
        mockTypeConverter = mock(TypeConverter.class);
        mockPaddingDescriptor = mock(PaddingDescriptor.class);

        objectMappingStore = new ObjectMappingStore();
        typeConverter = new TypeConverter();
        writer = mock(RecordWriter.class);

        builder = new RecordBuilder();
        builder.setBuilderRecordLayoutResolver((BuilderRecordLayoutResolver) recordLayoutResolver.proxy());
        builder.setObjectMappingStore(objectMappingStore);
        builder.setTypeConverter(typeConverter);
        byteMap = new FieldByteMap();
    }

    public void test_build_OnlyApplyPaddingWhenNeeded_ByteLengthMatchesFieldLength() {
        Person person = new Person("Joe ", "Smith");
        byte[] data = {1, 2, 3, 4};
        byteMap.put((FieldDefinition) mockFieldDefinition.proxy(), data);

        builder.setObjectMappingStore((ObjectMappingStore) mockObjectMappingStore.proxy());
        builder.setTypeConverter((TypeConverter) mockTypeConverter.proxy());

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{mockRecordLayout.proxy()})));
        mockObjectMappingStore.expects(once()).method("find").with(eq(Person.class)).will(returnValue(mockObjectMapping.proxy()));
        mockRecordLayout.expects(once()).method("getFieldDefinitions").will(returnValue(Arrays.asList(new Object[]{mockFieldDefinition.proxy()})));
        mockFieldDefinition.expects(exactly(2)).method("getName").will(returnValue("field-1"));
        mockFieldDefinition.expects(once()).method("getLength").will(returnValue(4));
        mockFieldDefinition.expects(once()).method("getPaddingDescriptor").will(returnValue(mockPaddingDescriptor.proxy()));
        mockObjectMapping.expects(once()).method("findRecordField").with(eq("field-1")).will(returnValue(mockFieldMapping.proxy()));
        mockFieldMapping.expects(once()).method("getRecordFields").will(returnValue(Arrays.asList(new String[]{"field-1"})));
        mockFieldMapping.expects(once()).method("getDomainFieldName").will(returnValue("firstName"));
        mockFieldMapping.expects(once()).method("getBinaryFieldFactory").will(returnValue(mockBinaryFieldFactory.proxy()));
        mockBinaryFieldFactory.expects(once()).method("build").with(eq(person.firstName), eq(mockTypeConverter.proxy()), eq(Arrays.asList(new Object[]{mockFieldDefinition.proxy()}))).will(returnValue(byteMap));
        writer.expects(once()).method("close");
        writer.expects(once()).method("write").with(eq(data));

        builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
    }

    public void test_build_TooMuchData_PaddedField_AfterPadding() {
        Person person = new Person("Joe", "Smith");
        byte[] data = {1, 2, 3, 4};
        byteMap.put((FieldDefinition) mockFieldDefinition.proxy(), data);

        builder.setObjectMappingStore((ObjectMappingStore) mockObjectMappingStore.proxy());
        builder.setTypeConverter((TypeConverter) mockTypeConverter.proxy());

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{mockRecordLayout.proxy()})));
        mockObjectMappingStore.expects(once()).method("find").with(eq(Person.class)).will(returnValue(mockObjectMapping.proxy()));
        mockRecordLayout.expects(once()).method("getFieldDefinitions").will(returnValue(Arrays.asList(new Object[]{mockFieldDefinition.proxy()})));
        mockRecordLayout.expects(once()).method("getId").will(returnValue("record-layout-id"));
        mockFieldDefinition.expects(exactly(2)).method("getName").will(returnValue("field-1"));
        mockFieldDefinition.expects(once()).method("getLength").will(returnValue(5));
        mockFieldDefinition.expects(once()).method("getPaddingDescriptor").will(returnValue(mockPaddingDescriptor.proxy()));
        mockObjectMapping.expects(once()).method("findRecordField").with(eq("field-1")).will(returnValue(mockFieldMapping.proxy()));
        mockFieldMapping.expects(once()).method("getRecordFields").will(returnValue(Arrays.asList(new String[]{"field-1"})));
        mockFieldMapping.expects(once()).method("getDomainFieldName").will(returnValue("firstName"));
        mockFieldMapping.expects(once()).method("getBinaryFieldFactory").will(returnValue(mockBinaryFieldFactory.proxy()));
        mockBinaryFieldFactory.expects(once()).method("build").with(eq(person.firstName), eq(mockTypeConverter.proxy()), eq(Arrays.asList(new Object[]{mockFieldDefinition.proxy()}))).will(returnValue(byteMap));
        mockPaddingDescriptor.expects(once()).method("applyPadding").with(eq(data), eq(5)).will(returnValue("padded".getBytes()));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Too much data given! Expected 5, but was 6, for field=\"field-1\" on layout=\"record-layout-id\"", err.getMessage());
        }
    }

    public void test_build_TooMuchData_PaddedField_BeforePadding() {
        Person person = new Person("Joe", "Smith");
        byte[] data = {1, 2, 3, 4};
        byteMap.put((FieldDefinition) mockFieldDefinition.proxy(), data);

        builder.setObjectMappingStore((ObjectMappingStore) mockObjectMappingStore.proxy());
        builder.setTypeConverter((TypeConverter) mockTypeConverter.proxy());

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{mockRecordLayout.proxy()})));
        mockObjectMappingStore.expects(once()).method("find").with(eq(Person.class)).will(returnValue(mockObjectMapping.proxy()));
        mockRecordLayout.expects(once()).method("getFieldDefinitions").will(returnValue(Arrays.asList(new Object[]{mockFieldDefinition.proxy()})));
        mockRecordLayout.expects(once()).method("getId").will(returnValue("record-layout-id"));
        mockFieldDefinition.expects(exactly(2)).method("getName").will(returnValue("field-1"));
        mockFieldDefinition.expects(once()).method("getLength").will(returnValue(1));
        mockFieldDefinition.expects(once()).method("getPaddingDescriptor").will(returnValue(mockPaddingDescriptor.proxy()));
        mockObjectMapping.expects(once()).method("findRecordField").with(eq("field-1")).will(returnValue(mockFieldMapping.proxy()));
        mockFieldMapping.expects(once()).method("getRecordFields").will(returnValue(Arrays.asList(new String[]{"field-1"})));
        mockFieldMapping.expects(once()).method("getDomainFieldName").will(returnValue("firstName"));
        mockFieldMapping.expects(once()).method("getBinaryFieldFactory").will(returnValue(mockBinaryFieldFactory.proxy()));
        mockBinaryFieldFactory.expects(once()).method("build").with(eq(person.firstName), eq(mockTypeConverter.proxy()), eq(Arrays.asList(new Object[]{mockFieldDefinition.proxy()}))).will(returnValue(byteMap));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Too much data given! Expected 1, but was 4, for field=\"field-1\" on layout=\"record-layout-id\"", err.getMessage());
        }
    }

    public void test_build_TooMuchData_UnpaddedField() {
        Person person = new Person("Joe", "Smith");
        byte[] data = {1, 2, 3, 4};
        byteMap.put((FieldDefinition) mockFieldDefinition.proxy(), data);

        builder.setObjectMappingStore((ObjectMappingStore) mockObjectMappingStore.proxy());
        builder.setTypeConverter((TypeConverter) mockTypeConverter.proxy());

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{mockRecordLayout.proxy()})));
        mockObjectMappingStore.expects(once()).method("find").with(eq(Person.class)).will(returnValue(mockObjectMapping.proxy()));
        mockRecordLayout.expects(once()).method("getFieldDefinitions").will(returnValue(Arrays.asList(new Object[]{mockFieldDefinition.proxy()})));
        mockRecordLayout.expects(once()).method("getId").will(returnValue("record-layout-id"));
        mockFieldDefinition.expects(exactly(2)).method("getName").will(returnValue("field-1"));
        mockFieldDefinition.expects(once()).method("getLength").will(returnValue(1));
        mockFieldDefinition.expects(once()).method("getPaddingDescriptor").will(returnValue(null));
        mockObjectMapping.expects(once()).method("findRecordField").with(eq("field-1")).will(returnValue(mockFieldMapping.proxy()));
        mockFieldMapping.expects(once()).method("getRecordFields").will(returnValue(Arrays.asList(new String[]{"field-1"})));
        mockFieldMapping.expects(once()).method("getDomainFieldName").will(returnValue("firstName"));
        mockFieldMapping.expects(once()).method("getBinaryFieldFactory").will(returnValue(mockBinaryFieldFactory.proxy()));
        mockBinaryFieldFactory.expects(once()).method("build").with(eq(person.firstName), eq(mockTypeConverter.proxy()), eq(Arrays.asList(new Object[]{mockFieldDefinition.proxy()}))).will(returnValue(byteMap));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Too much data given! Expected 1, but was 4, for field=\"field-1\" on layout=\"record-layout-id\"", err.getMessage());
        }
    }

    public void test_build_BinaryFieldFactoryGivesANullByteArray() {
        Person person = new Person("Joe", "Smith");
        byteMap.put((FieldDefinition) mockFieldDefinition.proxy(), null);

        builder.setObjectMappingStore((ObjectMappingStore) mockObjectMappingStore.proxy());
        builder.setTypeConverter((TypeConverter) mockTypeConverter.proxy());

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{mockRecordLayout.proxy()})));
        mockObjectMappingStore.expects(once()).method("find").with(eq(Person.class)).will(returnValue(mockObjectMapping.proxy()));
        mockRecordLayout.expects(once()).method("getFieldDefinitions").will(returnValue(Arrays.asList(new Object[]{mockFieldDefinition.proxy()})));
        mockRecordLayout.expects(once()).method("getId").will(returnValue("record-layout-id"));
        mockFieldDefinition.expects(exactly(2)).method("getName").will(returnValue("field-1"));
        mockFieldDefinition.expects(once()).method("getLength").will(returnValue(1));
        mockFieldDefinition.expects(once()).method("getPaddingDescriptor").will(returnValue(null));
        mockObjectMapping.expects(once()).method("findRecordField").with(eq("field-1")).will(returnValue(mockFieldMapping.proxy()));
        mockFieldMapping.expects(once()).method("getRecordFields").will(returnValue(Arrays.asList(new String[]{"field-1"})));
        mockFieldMapping.expects(once()).method("getDomainFieldName").will(returnValue("firstName"));
        mockFieldMapping.expects(once()).method("getBinaryFieldFactory").will(returnValue(mockBinaryFieldFactory.proxy()));
        mockBinaryFieldFactory.expects(once()).method("build").with(eq(person.firstName), eq(mockTypeConverter.proxy()), eq(Arrays.asList(new Object[]{mockFieldDefinition.proxy()}))).will(returnValue(byteMap));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Not enough data given! Did you forget the padding? Expected 1, but was 0, for field=\"field-1\" on layout=\"record-layout-id\"", err.getMessage());
        }
    }

    public void test_build_NotEnoughDataForPaddedField() {
        Person person = new Person("Joe", "Smith");
        ObjectMapping objectMapping = new ObjectMapping(Person.class);
        objectMapping.field("First Name", "firstName");
        objectMappingStore.add(objectMapping);

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 4, new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' '));

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));
        writer.expects(once()).method("write").with(eq("Joe ".getBytes()));
        writer.expects(once()).method("close");

        builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
    }

    public void test_build_NotEnoughDataForUnpaddedField() {
        Person person = new Person("Joe", "Smith");
        ObjectMapping objectMapping = new ObjectMapping(Person.class);
        objectMapping.field("First Name", "firstName");
        objectMappingStore.add(objectMapping);

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 4);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Not enough data given! Did you forget the padding? Expected 4, but was 3, for field=\"First Name\" on layout=\"" + recordLayout.getId() + "\"", err.getMessage());
        }
    }

    public void test_build_CouldNotFindFieldMapping_NoPadding() {
        Person person = new Person("Joe", "Smith");
        objectMappingStore.add(new ObjectMapping(Person.class));

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Not enough data given! Did you forget the padding? Expected 3, but was 0, for field=\"First Name\" on layout=\"" + recordLayout.getId() + "\"", err.getMessage());
        }
    }

    public void test_build_CouldNotFindObjectMapping() {
        Person person = new Person("Joe", "Smith");

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{new SimpleRecordLayout("person")})));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Could not find an ObjectMapping for " + Person.class.getName(), err.getMessage());
        }
    }

    public void test_build_CouldNotResolveRecordLayout() {
        Person person = new Person("Joe", "Smith");

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(new ArrayList()));
        writer.expects(once()).method("close");

        try {
            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("Could not resolve RecordLayout(s) for " + Person.class.getName(), err.getMessage());
        }
    }

    public void test_build_ThrowExceptionWriting() {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName");

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        IOException error = new IOException();

        try {
            writer.expects(once()).method("write").with(eq("Joe".getBytes())).will(throwException(error));
            writer.expects(once()).method("close");

            builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
            fail();
        } catch (BuilderException err) {
            assertEquals("A problem occured while building file", err.getMessage());
            assertSame(error, err.getCause());
        }
    }

    public void test_build_MultipleDomainObjectsOfDifferentType() throws IOException {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName");
        personMapping.field("Last Name", "lastName");

        ObjectMapping addressMapping = new ObjectMapping(Address.class);
        addressMapping.field("Line 1", "line1");

        objectMappingStore.add(personMapping);
        objectMappingStore.add(addressMapping);

        Person person = new Person("Joe", "Smith");
        Address address = new Address("12 Easy St");

        SimpleRecordLayout personRecordLayout = new SimpleRecordLayout("person");
        personRecordLayout.field("First Name", 3);
        personRecordLayout.field("Last Name", 5);

        SimpleRecordLayout addressRecordLayout = new SimpleRecordLayout("address");
        addressRecordLayout.field("Line 1", 10);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{personRecordLayout})));
        recordLayoutResolver.expects(once()).method("resolve").with(eq(address)).will(returnValue(Arrays.asList(new Object[]{addressRecordLayout})));

        writer.expects(once()).method("write").with(eq("JoeSmith".getBytes()));
        writer.expects(once()).method("write").with(eq(address.line1.getBytes()));
        writer.expects(once()).method("close");

        builder.build(Arrays.asList(new Object[]{person, address}), (RecordWriter) writer.proxy());
    }

    public void test_build_MultipleDomainObjectsOfTheSameType() throws IOException {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName");
        personMapping.field("Last Name", "lastName");

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");
        Person person2 = new Person("Tim", "Roger");

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);
        recordLayout.field("Last Name", 5);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));
        recordLayoutResolver.expects(once()).method("resolve").with(eq(person2)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        writer.expects(once()).method("write").with(eq("JoeSmith".getBytes()));
        writer.expects(once()).method("write").with(eq("TimRoger".getBytes()));
        writer.expects(once()).method("close");

        builder.build(Arrays.asList(new Object[]{person, person2}), (RecordWriter) writer.proxy());
    }

    public void test_build_SingleDomainObject_WithCompoundField() throws IOException {
        Mock binaryFieldFactory = mock(BinaryFieldFactory.class);

        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field(new String[]{"dob month", "dob day", "dob year"}, "dob", null, (BinaryFieldFactory) binaryFieldFactory.proxy());

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");
        person.dob = new Date();

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("dob month", 2);
        recordLayout.field("dob day", 2);
        recordLayout.field("dob year", 4);

        byteMap.put((FieldDefinition) recordLayout.getFieldDefinitions().get(0), new byte[]{1, 2});
        byteMap.put((FieldDefinition) recordLayout.getFieldDefinitions().get(1), new byte[]{3, 4});
        byteMap.put((FieldDefinition) recordLayout.getFieldDefinitions().get(2), new byte[]{5, 6, 7, 8});

        binaryFieldFactory.expects(once()).method("build").with(eq(person.dob), eq(typeConverter), eq(recordLayout.getFieldDefinitions())).will(returnValue(byteMap));
        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        writer.expects(once()).method("write").with(eq(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}));
        writer.expects(once()).method("close");

        builder.build(person, (RecordWriter) writer.proxy());
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

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        writer.expects(once()).method("write").with(eq("JoeSmith".getBytes()));
        writer.expects(once()).method("close");

        builder.build(person, (RecordWriter) writer.proxy());
    }

    public void test_build_CustomValueConverter() throws IOException {
        ObjectMapping personMapping = new ObjectMapping(Person.class);
        personMapping.field("First Name", "firstName", ReverseValueConverter.class);

        objectMappingStore.add(personMapping);

        Person person = new Person("Joe", "Smith");

        SimpleRecordLayout recordLayout = new SimpleRecordLayout("person");
        recordLayout.field("First Name", 3);

        recordLayoutResolver.expects(once()).method("resolve").with(eq(person)).will(returnValue(Arrays.asList(new Object[]{recordLayout})));

        typeConverter.registerConverter(new ReverseValueConverter());

        writer.expects(once()).method("write").with(eq("eoJ".getBytes()));
        writer.expects(once()).method("close");

        builder.build(Arrays.asList(new Object[]{person}), (RecordWriter) writer.proxy());
    }

    private static class Person {
        private String firstName;
        private String lastName;
        private Date dob;

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    private static class Address {
        private String line1;

        private Address(String line1) {
            this.line1 = line1;
        }
    }
}
