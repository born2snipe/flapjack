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
package flapjack.model;

import flapjack.layout.PaddingDescriptor;
import flapjack.layout.SimpleFieldDefinition;
import flapjack.util.ReverseValueConverter;
import flapjack.util.TypeConverter;
import flapjack.util.ValueConverter;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;


public class SingleFieldMappingTest extends MockObjectTestCase {
    private SingleFieldMapping mapping;
    private BinaryFieldFactory binaryFactory;
    private DomainFieldFactory domainFactory;
    private Mock mockTypeConverter;
    private Mock valueConverter;
    private Mock paddingDescriptor;

    protected void setUp() throws Exception {
        super.setUp();
        mockTypeConverter = mock(TypeConverter.class);
        mapping = new SingleFieldMapping("1", "2", null);
        domainFactory = mapping.getDomainFieldFactory();
        binaryFactory = mapping.getBinaryFieldFactory();
        valueConverter = mock(ValueConverter.class);
        paddingDescriptor = mock(PaddingDescriptor.class);
    }

    public void test_domainFieldFactory_CustomValueConverter() {
        SingleFieldMapping mapping = new SingleFieldMapping("1", "2", ReverseValueConverter.class);
        DomainFieldFactory domainFactory = mapping.getDomainFieldFactory();

        ListMap listMap = new ListMap();
        listMap.put("1", "value".getBytes());

        mockTypeConverter.expects(once()).method("isRegistered").with(eq(ReverseValueConverter.class)).will(returnValue(true));
        mockTypeConverter.expects(once()).method("find").with(eq(ReverseValueConverter.class)).will(returnValue(valueConverter.proxy()));
        valueConverter.expects(once()).method("toDomain").with(eq("value".getBytes())).will(returnValue("domain"));

        assertEquals("domain", domainFactory.build(listMap, String.class, (TypeConverter) mockTypeConverter.proxy()));
    }

    public void test_domainFieldFactory() {
        ListMap listMap = new ListMap();
        listMap.put("1", "value".getBytes());

        mockTypeConverter.expects(once()).method("type").with(eq(String.class)).will(returnValue(valueConverter.proxy()));
        valueConverter.expects(once()).method("toDomain").with(eq("value".getBytes())).will(returnValue("domain"));

        assertEquals("domain", domainFactory.build(listMap, String.class, (TypeConverter) mockTypeConverter.proxy()));
    }

    public void test_binaryFieldFactory_NoPadding_Null() {
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 0);

        assertEquals("", new String(binaryFactory.build(null, (TypeConverter) mockTypeConverter.proxy(), fieldDefinition)));
    }

    public void test_binaryFieldFactory_WithPadding_Null() {
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 0, (PaddingDescriptor) paddingDescriptor.proxy());

        paddingDescriptor.expects(once()).method("applyPadding").with(isA(byte[].class), eq(fieldDefinition.getLength())).will(returnValue("padded".getBytes()));

        assertEquals("padded", new String(binaryFactory.build(null, (TypeConverter) mockTypeConverter.proxy(), fieldDefinition)));
    }

    public void test_binaryFieldFactory_NoPadding() {
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 0);

        mockTypeConverter.expects(once()).method("type").with(eq(String.class)).will(returnValue(valueConverter.proxy()));
        valueConverter.expects(once()).method("toBytes").with(eq("value")).will(returnValue("binary".getBytes()));

        assertEquals("binary", new String(binaryFactory.build("value", (TypeConverter) mockTypeConverter.proxy(), fieldDefinition)));
    }

    public void test_binaryFieldFactory_WithPadding() {
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 0, (PaddingDescriptor) paddingDescriptor.proxy());

        mockTypeConverter.expects(once()).method("type").with(eq(String.class)).will(returnValue(valueConverter.proxy()));
        byte[] data = "binary".getBytes();
        valueConverter.expects(once()).method("toBytes").with(eq("value")).will(returnValue(data));
        paddingDescriptor.expects(once()).method("applyPadding").with(eq(data), eq(fieldDefinition.getLength())).will(returnValue("padded".getBytes()));

        assertEquals("padded", new String(binaryFactory.build("value", (TypeConverter) mockTypeConverter.proxy(), fieldDefinition)));
    }

    public void test_binaryFieldFactory_NoPadding_CustomValueConverter() {
        mapping = new SingleFieldMapping("1", "2", ReverseValueConverter.class);
        domainFactory = mapping.getDomainFieldFactory();
        binaryFactory = mapping.getBinaryFieldFactory();
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 0);

        mockTypeConverter.expects(once()).method("isRegistered").with(eq(ReverseValueConverter.class)).will(returnValue(true));
        mockTypeConverter.expects(once()).method("find").with(eq(ReverseValueConverter.class)).will(returnValue(valueConverter.proxy()));
        valueConverter.expects(once()).method("toBytes").with(eq("value")).will(returnValue("binary".getBytes()));

        assertEquals("binary", new String(binaryFactory.build("value", (TypeConverter) mockTypeConverter.proxy(), fieldDefinition)));
    }

    public void test_binaryFieldFactory_NoPadding_CustomValueConverterNotRegistered() {
        mapping = new SingleFieldMapping("1", "2", ReverseValueConverter.class);
        domainFactory = mapping.getDomainFieldFactory();
        binaryFactory = mapping.getBinaryFieldFactory();
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 0);

        mockTypeConverter.expects(once()).method("isRegistered").with(eq(ReverseValueConverter.class)).will(returnValue(false));

        try {
            binaryFactory.build("value", (TypeConverter) mockTypeConverter.proxy(), fieldDefinition);
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Could not find a " + ReverseValueConverter.class.getName() + " registered! Are you sure you registered " + ReverseValueConverter.class.getName() + " in the TypeConverter?", err.getMessage());
        }
    }

}
