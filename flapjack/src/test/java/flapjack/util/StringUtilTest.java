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


public class StringUtilTest extends TestCase {

    public void test_rightPad() {
        assertEquals("O", StringUtil.rightPad("O", 1, ' '));
        assertEquals("O ", StringUtil.rightPad("O", 2, ' '));
    }

    public void test_rightPad_NegativeLength() {
        try {
            StringUtil.rightPad("", -1, ' ');
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Pad length MUST be over zero", err.getMessage());
        }
    }

    public void test_rightPad_ZeroLength() {
        try {
            StringUtil.rightPad("", 0, ' ');
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Pad length MUST be over zero", err.getMessage());
        }
    }

    public void test_leftPad_NegativeLength() {
        try {
            StringUtil.leftPad("", -1, ' ');
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Pad length MUST be over zero", err.getMessage());
        }
    }

    public void test_leftPad_ZeroLength() {
        try {
            StringUtil.leftPad("", 0, ' ');
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Pad length MUST be over zero", err.getMessage());
        }
    }

    public void test_leftPad() {
        assertEquals("O", StringUtil.leftPad("O", 1, ' '));
        assertEquals(" O", StringUtil.leftPad("O", 2, ' '));
    }
}
