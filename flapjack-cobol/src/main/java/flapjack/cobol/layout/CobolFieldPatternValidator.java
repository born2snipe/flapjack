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
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;


public class CobolFieldPatternValidator {
    private static final String ALPHANUMERIC_PATTERN = "(?i)[X]+|[X]\\([1-9]+\\)";
    private static final String NUMERIC_PATTERN = "(?i)9+|9\\([1-9]+\\)|9+v9+|9\\([1-9]+\\)v9+|9\\([1-9]+\\)v9\\([1-9]+\\)";

    public boolean validate(String pattern) {
        List patterns = Arrays.asList(new String[]{ALPHANUMERIC_PATTERN, NUMERIC_PATTERN});
        Iterator it = patterns.iterator();
        while (it.hasNext()) {
            if (Pattern.matches((String) it.next(), pattern)) {
                return true;
            }
        }
        return false;
    }
}
