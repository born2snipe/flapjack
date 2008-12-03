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
package flapjack.annotation.util;

import junit.framework.TestCase;

import java.net.URLClassLoader;
import java.net.URL;
import java.util.List;


public class JarFileClassLocatorTest extends TestCase {
    private JarFileClassLocator locator;

    public void setUp() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        URL url = loader.getResource("flapjack-annotation-test-helper.jar");

        Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[]{url}, loader));

        locator = new JarFileClassLocator();
    }

    public void test_locate() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("flapjack/annotation/test");

        List<Class> classes = locator.locate(url, "flapjack.annotation.test");
        
        assertEquals(2, classes.size());
        assertEquals("flapjack.annotation.test.Dog", classes.get(0).getName());
        assertEquals("flapjack.annotation.test.DogRecordLayout", classes.get(1).getName());
    }
}
