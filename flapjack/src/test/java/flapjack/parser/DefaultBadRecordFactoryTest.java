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
package flapjack.parser;

import junit.framework.TestCase;


public class DefaultBadRecordFactoryTest extends TestCase {
    private DefaultBadRecordFactory factory;

    public void setUp() {
        factory = new DefaultBadRecordFactory();
    }

    public void test_ByteArray() {
        byte[] data = new byte[0];

        BadRecord badRecord = factory.build(data);

        assertSame(data, badRecord.getContents());
        assertNull(badRecord.getException());
    }
    
    public void test_ByteArrayWithException() {
        byte[] data = new byte[0];
        ParseException err = new ParseException(null, null, null);

        BadRecord badRecord = factory.build(data, err);

        assertSame(data, badRecord.getContents());
        assertSame(err, badRecord.getException());
    }
}
