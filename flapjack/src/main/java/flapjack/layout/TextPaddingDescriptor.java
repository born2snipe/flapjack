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
package flapjack.layout;

import flapjack.util.StringUtil;


public class TextPaddingDescriptor implements PaddingDescriptor {
    private Padding padding;
    private char paddingCharacter;

    public TextPaddingDescriptor(Padding padding, char paddingCharacter) {
        this.padding = padding;
        this.paddingCharacter = paddingCharacter;
    }

    public String toString() {
        return "padding=" + padding + ", character=" + paddingCharacter;
    }

    public byte[] applyPadding(byte[] data, int length) {
        if (padding == Padding.LEFT) {
            return StringUtil.leftPad(new String(data), length, paddingCharacter).getBytes();
        } else if (padding == Padding.RIGHT) {
            return StringUtil.rightPad(new String(data), length, paddingCharacter).getBytes();
        } else
            return data;
    }

    public static final class Padding {
        public static final Padding LEFT = new Padding();
        public static final Padding RIGHT = new Padding();
        public static final Padding NONE = new Padding();

        private Padding() {
        }

        public String toString() {
            if (this == LEFT) {
                return "LEFT";
            } else if (this == RIGHT) {
                return "RIGHT";
            } else {
                return "NONE";
            }
        }
    }
}
