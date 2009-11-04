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

import junit.framework.TestCase;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class AbstractClassLocatorTest extends TestCase {

    public void test_ensureClassesAreSorted() {
        StubClassLocator classLocator = new StubClassLocator();
        classLocator.classesFound.add(String.class);
        classLocator.classesFound.add(JarFileClassLocator.class);
        classLocator.classesFound.add(LocalFileSystemClassLocator.class);

        List<Class> classes = classLocator.locate(null, "doesNotMatter");

        assertEquals(Arrays.asList(JarFileClassLocator.class, LocalFileSystemClassLocator.class, String.class), classes);
    }
    
    private static class StubClassLocator extends AbstractClassLocator {
        private List<Class> classesFound = new ArrayList<Class>();

        @Override
        protected void findClasses(List<Class> classes, URL url, Pattern packageNamePattern) {
            classes.addAll(classesFound);
        }
    }
}
