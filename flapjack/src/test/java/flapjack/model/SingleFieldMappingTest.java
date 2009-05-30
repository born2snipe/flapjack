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
import junit.framework.TestCase;


public class SingleFieldMappingTest extends TestCase {
    private TypeConverter typeConverter;
    private SingleFieldMapping mapping;
    private BinaryFieldFactory binaryFactory;
    private DomainFieldFactory domainFactory;

    protected void setUp() throws Exception {
        super.setUp();
        typeConverter = new TypeConverter();
        typeConverter.registerConverter(new ReverseValueConverter());
        mapping = new SingleFieldMapping("1", "2");
        binaryFactory = mapping.getBinaryFieldFactory();
        domainFactory = mapping.getDomainFieldFactory();
    }

    public void test_domainFieldFactory_CustomValueConverter() {
        mapping = new SingleFieldMapping("1", "2", ReverseValueConverter.class);
        domainFactory = mapping.getDomainFieldFactory();

        ListMap listMap = new ListMap();
        listMap.put("1", "value".getBytes());

        assertEquals("eulav", domainFactory.build(listMap, String.class, typeConverter));
    }

    public void test_domainFieldFactory() {
        ListMap listMap = new ListMap();
        listMap.put("1", "value".getBytes());

        assertEquals("value", domainFactory.build(listMap, String.class, typeConverter));
    }

    public void test_binaryFieldFactory_NoPadding() {
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 0);

        assertEquals("value", new String(binaryFactory.build("value", typeConverter, fieldDefinition)));
    }

    public void test_binaryFieldFactory_NoPadding_CustomValueConverter() {
        mapping = new SingleFieldMapping("1", "2", ReverseValueConverter.class);
        binaryFactory = mapping.getBinaryFieldFactory();
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 0);

        assertEquals("eulav", new String(binaryFactory.build("value", typeConverter, fieldDefinition)));
    }

    public void test_binaryFieldFactory_LeftPadding_CustomValueConverter() {
        mapping = new SingleFieldMapping("1", "2", ReverseValueConverter.class);
        binaryFactory = mapping.getBinaryFieldFactory();
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 10, new PaddingDescriptor(PaddingDescriptor.Padding.LEFT, ' '));

        assertEquals("     eulav", new String(binaryFactory.build("value", typeConverter, fieldDefinition)));
    }

    public void test_binaryFieldFactory_RightPadding_CustomValueConverter() {
        mapping = new SingleFieldMapping("1", "2", ReverseValueConverter.class);
        binaryFactory = mapping.getBinaryFieldFactory();
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 10, new PaddingDescriptor(PaddingDescriptor.Padding.RIGHT, ' '));

        assertEquals("eulav     ", new String(binaryFactory.build("value", typeConverter, fieldDefinition)));
    }

    public void test_binaryFieldFactory_NonePadding() {
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 10, new PaddingDescriptor(PaddingDescriptor.Padding.NONE, 'A'));

        assertEquals("value", new String(binaryFactory.build("value", typeConverter, fieldDefinition)));
    }

    public void test_binaryFieldFactory_LeftPadding() {
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 10, new PaddingDescriptor(PaddingDescriptor.Padding.LEFT, ' '));

        assertEquals("     value", new String(binaryFactory.build("value", typeConverter, fieldDefinition)));
    }

    public void test_binaryFieldFactory_RightPadding() {
        SimpleFieldDefinition fieldDefinition = new SimpleFieldDefinition("id", 0, 10, new PaddingDescriptor(PaddingDescriptor.Padding.RIGHT, ' '));

        assertEquals("value     ", new String(binaryFactory.build("value", typeConverter, fieldDefinition)));
    }


}
