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
package flapjack.cobol.layout;

import java.util.regex.Pattern;


public class CobolFieldTypeResolver {
    public CobolFieldType resolve(String pattern) {
        if (Pattern.matches(CobolFieldPatternValidator.ALPHANUMERIC_PATTERN, pattern)) {
            return CobolFieldType.ALPHA_NUMERIC;
        } else if (Pattern.matches(CobolFieldPatternValidator.INTEGER_PATTERN, pattern)) {
            return CobolFieldType.INTEGER;
        } else if (Pattern.matches(CobolFieldPatternValidator.DECIMAL_PATTERN, pattern)) {
            return CobolFieldType.DECIMAL;
        }
        return null;
    }
}
