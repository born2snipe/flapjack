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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;


public abstract class AbstractClassLocator implements ClassLocator {
    public List<Class> locate(URL url, String packageName) {
        return locate(url, Pattern.compile(packageName));
    }

    public List<Class> locate(URL url, Pattern packageNamePattern) {
        List<Class> classes = new ArrayList<Class>();
        findClasses(classes, url, packageNamePattern);
        Collections.sort(classes, new Comparator<Class>(){
            public int compare(Class o1, Class o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return classes;
    }

    protected abstract void findClasses(List<Class> classes, URL url, Pattern packageNamePattern);

    protected Class loadClass(String className) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(className);
    }

}
