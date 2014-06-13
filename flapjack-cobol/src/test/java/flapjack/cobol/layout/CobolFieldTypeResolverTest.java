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
package flapjack.cobol.layout;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CobolFieldTypeResolverTest {
    @Test
    public void test_resolve() {
        CobolFieldTypeResolver resolver = new CobolFieldTypeResolver();

        assertEquals(CobolFieldType.ALPHA_NUMERIC, resolver.resolve("X"));
        assertEquals(CobolFieldType.ALPHA_NUMERIC, resolver.resolve("XX"));
        assertEquals(CobolFieldType.ALPHA_NUMERIC, resolver.resolve("X(2)"));
        assertEquals(CobolFieldType.ALPHA_NUMERIC, resolver.resolve("X(22)"));
        assertEquals(CobolFieldType.ALPHA_NUMERIC, resolver.resolve("x"));
        assertEquals(CobolFieldType.ALPHA_NUMERIC, resolver.resolve("xx"));
        assertEquals(CobolFieldType.ALPHA_NUMERIC, resolver.resolve("x(2)"));
        assertEquals(CobolFieldType.ALPHA_NUMERIC, resolver.resolve("x(22)"));

        assertEquals(CobolFieldType.INTEGER, resolver.resolve("9"));
        assertEquals(CobolFieldType.INTEGER, resolver.resolve("99"));
        assertEquals(CobolFieldType.INTEGER, resolver.resolve("9(2)"));
        assertEquals(CobolFieldType.INTEGER, resolver.resolve("9(22)"));
        assertEquals(CobolFieldType.DECIMAL, resolver.resolve("9v9"));
        assertEquals(CobolFieldType.DECIMAL, resolver.resolve("9v99"));
        assertEquals(CobolFieldType.DECIMAL, resolver.resolve("99v9"));
        assertEquals(CobolFieldType.DECIMAL, resolver.resolve("9(2)v9"));
        assertEquals(CobolFieldType.DECIMAL, resolver.resolve("9(22)v9"));
        assertEquals(CobolFieldType.DECIMAL, resolver.resolve("9(2)v99"));
        assertEquals(CobolFieldType.DECIMAL, resolver.resolve("9(2)v9(2)"));
        assertEquals(CobolFieldType.DECIMAL, resolver.resolve("9(22)v9(22)"));
        assertEquals(CobolFieldType.DECIMAL, resolver.resolve("9(2)V9"));
    }
}
