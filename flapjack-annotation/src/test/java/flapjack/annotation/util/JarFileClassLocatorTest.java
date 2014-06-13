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

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class JarFileClassLocatorTest {
    private JarFileClassLocator locator;

    @Before
    public void setUp() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        URL url = loader.getResource("flapjack-annotation-test-helper.jar");

        Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[]{url}, loader));

        locator = new JarFileClassLocator();
    }

    @Test
    public void test_exactPackagename() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("flapjack/annotation/test");

        List<Class> classes = locator.locate(url, "flapjack.annotation.test");
        
        assertEquals(2, classes.size());
        assertEquals("flapjack.annotation.test.Dog", classes.get(0).getName());
        assertEquals("flapjack.annotation.test.DogRecordLayout", classes.get(1).getName());
    }

    @Test
    public void test_matchingPattern() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("flapjack/annotation/test");

        List<Class> classes = locator.locate(url, "flapjack.+");

        assertEquals(2, classes.size());
        assertEquals("flapjack.annotation.test.Dog", classes.get(0).getName());
        assertEquals("flapjack.annotation.test.DogRecordLayout", classes.get(1).getName());
    }

    @Test
    public void test_noMatches() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("flapjack/annotation/test");

        List<Class> classes = locator.locate(url, "nothingFound");

        assertEquals(0, classes.size());
    }
}
