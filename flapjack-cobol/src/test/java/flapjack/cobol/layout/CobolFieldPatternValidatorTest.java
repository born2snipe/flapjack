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

import junit.framework.TestCase;


public class CobolFieldPatternValidatorTest extends TestCase {
    public void test_validate() {
        CobolFieldPatternValidator validator = new CobolFieldPatternValidator();
        assertTrue(validator.validate("X"));
        assertTrue(validator.validate("XX"));
        assertTrue(validator.validate("X(2)"));
        assertTrue(validator.validate("X(22)"));
        assertTrue(validator.validate("x"));
        assertTrue(validator.validate("xx"));
        assertTrue(validator.validate("x(2)"));
        assertTrue(validator.validate("x(22)"));

        assertTrue(validator.validate("9"));
        assertTrue(validator.validate("99"));
        assertTrue(validator.validate("9(2)"));
        assertTrue(validator.validate("9(22)"));
        assertTrue(validator.validate("9v9"));
        assertTrue(validator.validate("9v99"));
        assertTrue(validator.validate("99v9"));
        assertTrue(validator.validate("9(2)v9"));
        assertTrue(validator.validate("9(22)v9"));
        assertTrue(validator.validate("9(2)v99"));
        assertTrue(validator.validate("9(2)v9(2)"));
        assertTrue(validator.validate("9(22)v9(22)"));
        assertTrue(validator.validate("9(2)V9"));

        assertFalse(validator.validate("X(0)"));
        assertFalse(validator.validate("x(0)"));
        assertFalse(validator.validate("9(0)"));
        assertFalse(validator.validate("9(0)v9"));
        assertFalse(validator.validate("9(0)v9(0)"));
    }

}
