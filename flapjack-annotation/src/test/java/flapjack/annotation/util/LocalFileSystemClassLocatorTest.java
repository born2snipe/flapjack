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
package flapjack.annotation.util;

import flapjack.test.Address;
import flapjack.test.User;
import flapjack.test.UserRecordLayout;
import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class LocalFileSystemClassLocatorTest extends TestCase {
    private LocalFileSystemClassLocator locator;

    public void setUp() {
        locator = new LocalFileSystemClassLocator();
    }

    public void test_locate_GoodPath() throws MalformedURLException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("flapjack/test");

        List<Class> classes = locator.locate(url, "flapjack.test");

        assertEquals(3, classes.size());
        assertEquals(Arrays.asList(Address.class, User.class, UserRecordLayout.class), classes);
    }

}

