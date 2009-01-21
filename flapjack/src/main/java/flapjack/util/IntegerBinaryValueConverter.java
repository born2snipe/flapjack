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

import java.math.BigInteger;


public class IntegerBinaryValueConverter implements ValueConverter {
    public Object convert(byte[] bytes) {
        if (bytes.length < 4) {
            throw new IllegalArgumentException("There are not enough bytes expected 4 got "+bytes.length+" bytes");
        }
        return new Integer(new BigInteger(bytes).intValue());
    }

    public Class[] types() {
        return new Class[]{int.class, Integer.class};
    }
}
