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

import flapjack.layout.SimpleFieldDefinition;

import java.util.regex.Matcher;


public class DecimalFieldDefinition extends SimpleFieldDefinition implements CobolFieldDefinition {
    private String pattern;

    public DecimalFieldDefinition(String name, int position, String pattern) {
        super(name, position, -1);
        this.pattern = pattern.trim();
        String[] parts = this.pattern.split("v|V");
        setLength(parseLength(parts[0]) + parseLength(parts[1]));
    }

    private int parseLength(String part) {
        Matcher matcher = MULTIPLIER_PATTERN.matcher(part);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return part.length();
    }

    public String getPattern() {
        return pattern;
    }
}
