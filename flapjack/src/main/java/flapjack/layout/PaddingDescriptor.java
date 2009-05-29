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


public class PaddingDescriptor {
    public final Padding padding;
    public final char paddingCharacter;

    public PaddingDescriptor(Padding padding, char paddingCharacter) {
        this.padding = padding;
        this.paddingCharacter = paddingCharacter;
    }

    public String toString() {
        return "padding=" + padding + ", character=" + paddingCharacter;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaddingDescriptor that = (PaddingDescriptor) o;

        if (paddingCharacter != that.paddingCharacter) return false;
        if (padding != null ? !padding.equals(that.padding) : that.padding != null) return false;

        return true;
    }

    public int hashCode() {
        int result = padding != null ? padding.hashCode() : 0;
        result = 31 * result + (int) paddingCharacter;
        return result;
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
