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


public final class Padding {
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
