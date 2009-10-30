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

import junit.framework.TestCase;


public class NullSafeValueFactoryTest extends TestCase {

    public void test() {
        NullSafeValueFactory factory = new NullSafeValueFactory();

        assertEquals(new Integer(0), factory.build(int.class));
        assertEquals(new Long(0), factory.build(long.class));
        assertEquals(new Short((short) 0), factory.build(short.class));
        assertEquals(new Byte((byte) 0), factory.build(byte.class));
        assertEquals(new Character((char) 0), factory.build(char.class));
        assertEquals(new Double(0.0d), factory.build(double.class));
        assertEquals(new Float(0.0f), factory.build(float.class));
        assertEquals(new Boolean(false), factory.build(boolean.class));
        assertNull(factory.build(String.class));
    }


}
