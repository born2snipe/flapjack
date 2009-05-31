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

import junit.framework.TestCase;


public class TextPaddingDescriptorTest extends TestCase {
    public void test_applyPadding_LEFT_NeedsToPad() {
        assertEquals("     value", new String(new TextPaddingDescriptor(TextPaddingDescriptor.Padding.LEFT, ' ').applyPadding("value".getBytes(), 10)));
    }

    public void test_applyPadding_RIGHT_NeedsToPad() {
        assertEquals("value     ", new String(new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' ').applyPadding("value".getBytes(), 10)));
    }

    public void test_applyPadding_RIGHT_DoesNotNeedToPad() {
        assertEquals("valueXXXXX", new String(new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' ').applyPadding("valueXXXXX".getBytes(), 10)));
    }

    public void test_applyPadding_LEFT_DoesNotNeedToPad() {
        assertEquals("XXXXXvalue", new String(new TextPaddingDescriptor(TextPaddingDescriptor.Padding.RIGHT, ' ').applyPadding("XXXXXvalue".getBytes(), 10)));
    }

    public void test_applyPadding_NONE() {
        assertEquals("value", new String(new TextPaddingDescriptor(TextPaddingDescriptor.Padding.NONE, ' ').applyPadding("value".getBytes(), 10)));
    }
}
