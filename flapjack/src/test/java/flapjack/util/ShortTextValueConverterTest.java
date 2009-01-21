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

import junit.framework.TestCase;

import java.util.Arrays;


public class ShortTextValueConverterTest extends TestCase {
    private ShortTextValueConverter converter;

    protected void setUp() throws Exception {
        super.setUp();
        converter = new ShortTextValueConverter();
    }

    public void test_convert() {
        assertEquals(new Short((short) 2), converter.convert(ValueConverterTestUtil.text(2)));
        assertEquals(new Short((short) 0), converter.convert("".getBytes()));
        assertEquals(new Short((short) 0), converter.convert(null));
    }

    public void test_types() {
        Class[] classes = converter.types();

        assertNotNull(classes);
        assertEquals(2, classes.length);
        assertTrue(Arrays.equals(new Class[]{short.class, Short.class}, classes));
    }

}
