/**
 * Copyright 2008 Dan Dudley
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;


public class DateValueConverterTest extends TestCase {
    private DateValueConverter converter;

    public void setUp() {
        converter = new DateValueConverter(new String[]{"MM/dd/yyyy", "MM/yyyy"});
    }
    
    public void test_type() {
        assertTrue(Arrays.equals(new Class[]{Date.class}, converter.types()));
    }

    public void test_convert() {
        assertEquals(createDate("01/20/1908 00:00"), converter.convert("01/20/1908".getBytes()));
        assertEquals(createDate("01/01/1908 00:00"), converter.convert("01/1908".getBytes()));
    }

    public void test_convert_NoPatternFound() {
        assertNull(converter.convert("01-20-1908".getBytes()));
    }

    private Date createDate(String value) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }
}
