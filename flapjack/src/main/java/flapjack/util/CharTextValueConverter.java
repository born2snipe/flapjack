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

import java.text.MessageFormat;


public class CharTextValueConverter extends AbstractTextValueConverter implements ValueConverter {
    private static final String TOO_MANY_CHARACTERS = "To many bytes was given length={0}, was expecting a single byte";

    protected Object fromText(String text) {
        if (text.length() > 1) {
            throw new IllegalArgumentException(MessageFormat.format(TOO_MANY_CHARACTERS, new Object[]{new Integer(text.length())}));
        }
        return new Character(text.charAt(0));
    }

    public Class[] types() {
        return new Class[]{char.class, Character.class};
    }


}
