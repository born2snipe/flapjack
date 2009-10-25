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

import flapjack.util.AbstractTextValueConverter;
import flapjack.util.ConversionException;

import java.math.BigDecimal;
import java.text.MessageFormat;


public class CobolDecimalTextValueConverter extends AbstractTextValueConverter {
    private static final String NOT_ENOUGH_DATA = "There are not enough bytes expected at least {0} byte(s) got {1} byte(s)";
    private static final CobolDecimalPatternParser parser = new CobolDecimalPatternParser();
    private Class domainType;
    private String cobolPattern;

    public CobolDecimalTextValueConverter(Class domainType, String cobolPattern) {
        this.domainType = domainType;
        this.cobolPattern = cobolPattern;
    }

    protected Object fromTextToDomain(String text) {
        int decimalPlaceCount = parser.parseDecimalPlaces(cobolPattern);
        if (text.length() < decimalPlaceCount) {
            throw new ConversionException(MessageFormat.format(NOT_ENOUGH_DATA, new Object[]{new Integer(decimalPlaceCount), new Integer(text.length())}));
        }
        String inFrontOfDecimal = text.substring(0, text.length() - decimalPlaceCount);
        return build(inFrontOfDecimal+"."+text.substring(text.length() - decimalPlaceCount));
    }

    protected String fromDomainToText(Object domain) {
        return super.fromDomainToText(domain).replaceAll("\\.", "");
    }

    private Object build(String value) {
        if (Double.class.isAssignableFrom(domainType) || double.class.isAssignableFrom(domainType)) {
            return new Double(value);
        } else if (Float.class.isAssignableFrom(domainType) || float.class.isAssignableFrom(domainType)) {
            return new Float(value);
        } else if (BigDecimal.class.isAssignableFrom(domainType)) {
            return new BigDecimal(value);
        }
        return null;
    }
}
