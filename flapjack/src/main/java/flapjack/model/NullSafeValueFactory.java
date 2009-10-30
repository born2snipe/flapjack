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
package flapjack.model;


public class NullSafeValueFactory {
    public Object build(Class typeToBuild) {
        if (int.class.isAssignableFrom(typeToBuild)){
            return new Integer(0);
        } else if (long.class.isAssignableFrom(typeToBuild)) {
            return new Long(0);
        } else if (short.class.isAssignableFrom(typeToBuild)) {
            return new Short((short)0);
        } else if (byte.class.isAssignableFrom(typeToBuild)) {
            return new Byte((byte) 0);
        } else if (char.class.isAssignableFrom(typeToBuild)) {
            return new Character((char) 0);
        } else if (double.class.isAssignableFrom(typeToBuild)) {
            return new Double(0.0d);
        } else if (float.class.isAssignableFrom(typeToBuild)) {
            return new Float(0.0f);
        } else if (boolean.class.isAssignableFrom(typeToBuild)) {
            return Boolean.FALSE;
        }
        return null;
    }
}
