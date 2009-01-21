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
package flapjack.io;

import junit.framework.TestCase;

import java.util.Arrays;


public class ByteArrayBuilderTest extends TestCase {

    public void test_append_SingleArray_WithOffsetAndLength() {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append("1234".getBytes(), 1, 2);

        byte[] bytes = builder.toByteArray();

        assertNotNull(bytes);
        assertTrue(Arrays.equals("23".getBytes(), bytes));
    }
    
    public void test_append_SingleArray() {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        byte[] expected = "1234".getBytes();
        builder.append(expected);

        byte[] bytes = builder.toByteArray();

        assertNotNull(bytes);
        assertTrue(Arrays.equals(expected, bytes));
    }

    public void test_append_MultipleArrays() {
        ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append("1234".getBytes()).append("5678".getBytes());

        byte[] bytes = builder.toByteArray();

        assertNotNull(bytes);
        assertTrue(Arrays.equals("12345678".getBytes(), bytes));
    }
}
