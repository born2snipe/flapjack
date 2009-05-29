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
package flapjack.util;


public class StringUtil {

    public static String leftPad(String value, int makeLength, char paddingCharacter) {
        if (makeLength <= 0) {
            throw new IllegalArgumentException("Pad length MUST be over zero");
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(buildString(paddingCharacter, makeLength - value.length()));
        buffer.append(value);
        return buffer.toString();
    }

    public static String rightPad(String value, int makeLength, char paddingCharacter) {
        if (makeLength <= 0) {
            throw new IllegalArgumentException("Pad length MUST be over zero");
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(value);
        buffer.append(buildString(paddingCharacter, makeLength - value.length()));
        return buffer.toString();
    }

    private static String buildString(char character, int count) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < count; i++) {
            buffer.append(character);
        }
        return buffer.toString();
    }
}
