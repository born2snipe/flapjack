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

import flapjack.cobol.layout.CobolFieldDefinition;
import flapjack.cobol.layout.DecimalFieldDefinition;
import flapjack.layout.FieldDefinition;
import flapjack.util.TypeConverter;
import flapjack.util.ValueConverter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public class CobolTypeConverter extends TypeConverter {
    private static final List decimalClasses = Arrays.asList(new Class[]{Double.class, double.class, float.class, Float.class, BigDecimal.class});

    public ValueConverter type(Class domainType, FieldDefinition fieldDef) {
        if (fieldDef instanceof DecimalFieldDefinition && isDomainTypeADecimal(domainType)) {
            CobolFieldDefinition cobolFieldDef = (CobolFieldDefinition) fieldDef;
            return new CobolDecimalTextValueConverter(domainType, cobolFieldDef.getPattern());
        }
        return superType(domainType, fieldDef);
    }

    private boolean isDomainTypeADecimal(Class domainType) {
        return decimalClasses.contains(domainType);
    }

    protected ValueConverter superType(Class domainType, FieldDefinition fieldDef) {
        return super.type(domainType, fieldDef);
    }

}
