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

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CobolDecimalPatternParser {
    private static final String NOT_A_DECIMAL_PATTERN = "Unable to parse out the decimal points from cobol pattern ({0})";
    private static final Pattern NO_MULTIPLIER = Pattern.compile("(?i)9+$");
    private static final Pattern MULTIPLIER = Pattern.compile("9\\(([0-9]+)\\)$", Pattern.CASE_INSENSITIVE);

    public int parseDecimalPlaces(String pattern) {
        if (pattern.toLowerCase().indexOf("v") == -1) {
            throw new IllegalArgumentException(MessageFormat.format(NOT_A_DECIMAL_PATTERN, new Object[]{pattern}));
        }

        String decimal = pattern.split("v|V")[1];
        if (Pattern.matches(NO_MULTIPLIER.pattern(), decimal))  {
            return decimal.length();
        }
        
        Matcher matcher = MULTIPLIER.matcher(decimal);
        matcher.find();
        return Integer.parseInt(matcher.group(1));
    }
}
