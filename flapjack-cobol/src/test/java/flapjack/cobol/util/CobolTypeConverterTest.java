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
package flapjack.cobol.util;

import flapjack.cobol.layout.AlphaNumericFieldDefinition;
import flapjack.cobol.layout.DecimalFieldDefinition;
import flapjack.cobol.layout.IntegerFieldDefinition;
import flapjack.layout.FieldDefinition;
import flapjack.util.IntegerTextValueConverter;
import flapjack.util.ValueConverter;
import junit.framework.TestCase;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.math.BigDecimal;


public class CobolTypeConverterTest extends MockObjectTestCase {
    private ShuntCobolTypeConverter converter;


    protected void setUp() throws Exception {
        super.setUp();
        converter = new ShuntCobolTypeConverter();
    }

    public void test_typeIsIntegerAndFieldIsDecimal() {
        assertSame(ShuntCobolTypeConverter.superConverter, converter.type(Integer.class, new DecimalFieldDefinition("decimal", 0, "9v9")));
    }

    public void test_typeIsDoubleAndFieldIsDecimal() {
        assertTrue(converter.type(Double.class, new DecimalFieldDefinition("decimal", 0, "9v9")) instanceof CobolDecimalTextValueConverter);
    }
    
    public void test_typeIsPrimitiveDoubleAndFieldIsDecimal() {
        assertTrue(converter.type(double.class, new DecimalFieldDefinition("decimal", 0, "9v9")) instanceof CobolDecimalTextValueConverter);
    }
    
    public void test_typeIsPrimitiveFloatAndFieldIsDecimal() {
        assertTrue(converter.type(float.class, new DecimalFieldDefinition("decimal", 0, "9v9")) instanceof CobolDecimalTextValueConverter);
    }
    
    public void test_typeIsFloatAndFieldIsDecimal() {
        assertTrue(converter.type(Float.class, new DecimalFieldDefinition("decimal", 0, "9v9")) instanceof CobolDecimalTextValueConverter);
    }
    
    public void test_typeIsBigDecimalAndFieldIsDecimal() {
        assertTrue(converter.type(BigDecimal.class, new DecimalFieldDefinition("decimal", 0, "9v9")) instanceof CobolDecimalTextValueConverter);
    }

    public void test_AlphaNumericField() {
        assertSame(ShuntCobolTypeConverter.superConverter, converter.type(Double.class, new AlphaNumericFieldDefinition("alpha",0,"X")));
    }

    public void test_IntegerField() {
        assertSame(ShuntCobolTypeConverter.superConverter, converter.type(Double.class, new IntegerFieldDefinition("int", 0, "999")));
    }



    private static class ShuntCobolTypeConverter extends CobolTypeConverter {
        private static final ValueConverter superConverter = new IntegerTextValueConverter();

        protected ValueConverter superType(Class domainType, FieldDefinition fieldDef) {
            return superConverter;
        }
    }
}
