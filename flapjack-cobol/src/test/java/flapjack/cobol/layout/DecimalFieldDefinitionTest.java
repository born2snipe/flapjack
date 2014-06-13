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
import static org.junit.Assert.assertNotNull;


public class DecimalFieldDefinitionTest {
    @Test
    public void test_constructor() {
        assertEquals(2, new DecimalFieldDefinition("name", 0, "9v9").getLength());
        assertEquals(2, new DecimalFieldDefinition("name", 0, "9V9").getLength());
        assertEquals(2, new DecimalFieldDefinition("name", 0, " 9v9 ").getLength());
        assertEquals(3, new DecimalFieldDefinition("name", 0, "9(2)v9").getLength());
        assertEquals(11, new DecimalFieldDefinition("name", 0, "9(10)v9").getLength());
        assertEquals(3, new DecimalFieldDefinition("name", 0, "9v9(2)").getLength());
        assertEquals(11, new DecimalFieldDefinition("name", 0, "9v9(10)").getLength());
        assertEquals(20, new DecimalFieldDefinition("name", 0, "9(10)v9(10)").getLength());
        assertNotNull(new DecimalFieldDefinition("name", 0, "9(10)v9(10)").getPaddingDescriptor());
    }
}
