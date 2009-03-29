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


public class IntegerTextValueConverter implements TypedValueConverter {
    public Class[] types() {
        return new Class[]{Integer.class, int.class};
    }

    public Object convert(byte[] bytes) {
        if (bytes == null)
            return new Integer(0);
        String text = new String(bytes);
        if (text.trim().length() == 0) {
            return new Integer(0);
        }
        return new Integer(Integer.parseInt(text));
    }
}
