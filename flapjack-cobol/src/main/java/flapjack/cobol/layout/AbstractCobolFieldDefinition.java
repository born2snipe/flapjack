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

import flapjack.layout.PaddingDescriptor;
import flapjack.layout.SimpleFieldDefinition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AbstractCobolFieldDefinition extends SimpleFieldDefinition implements CobolFieldDefinition {
    private static final Pattern MULTIPLIER_PATTERN = Pattern.compile(".\\(([0-9]+)\\)", Pattern.CASE_INSENSITIVE);
    private String pattern;

    protected AbstractCobolFieldDefinition(String name, int position, int length, String pattern, PaddingDescriptor paddingDescriptor) {
        super(name, position, length, paddingDescriptor);
        this.pattern = pattern.trim().toUpperCase();
    }

    protected int parseLength(String pattern) {
        Matcher matcher = MULTIPLIER_PATTERN.matcher(pattern.trim());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return pattern.trim().length();
    }

    public String getPattern() {
        return pattern;
    }
}
