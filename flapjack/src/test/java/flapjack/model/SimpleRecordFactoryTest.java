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

import java.util.List;
import java.util.Arrays;

import flapjack.layout.SimpleRecordLayout;


public class SimpleRecordFactoryTest extends TestCase {

    public void test_build_WithFields() {
        SimpleRecordFactory factory = new SimpleRecordFactory();

        List fields = Arrays.asList(new String[]{"1", "2"});
        Object obj = factory.build(fields, new SimpleRecordLayout());

        assertNotNull(obj);
        assertTrue(obj instanceof SimpleRecord);

        SimpleRecord record = (SimpleRecord) obj;
        assertEquals("1", record.getField(0));
        assertEquals("2", record.getField(1));
    }

    public void test_build_WithEmptyFields() {
        SimpleRecordFactory factory = new SimpleRecordFactory();

        List fields = Arrays.asList(new String[0]);
        Object obj = factory.build(fields, new SimpleRecordLayout());

        assertNull(obj);
    }
    
    public void test_build_WithNullFields() {
        SimpleRecordFactory factory = new SimpleRecordFactory();

        Object obj = factory.build(null, new SimpleRecordLayout());

        assertNull(obj);
    }
    
}
