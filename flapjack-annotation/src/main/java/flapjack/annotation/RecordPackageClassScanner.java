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
package flapjack.annotation;

import flapjack.annotation.util.ClassLocator;
import flapjack.annotation.util.JarFileClassLocator;
import flapjack.annotation.util.LocalFileSystemClassLocator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * This class scans the given packages for classes that contain the flapjack.annotation.Record annotation.
 */
public class RecordPackageClassScanner {

    /**
     * Scans for classes that have the @Record annotation defined on them.
     *
     * @param packages - the packages to scan
     * @return a list of classes marked as records
     */
    public List<Class> scan(List<String> packages) {
        List<Class> classes = new ArrayList<Class>();
        for (String packageName : packages) {
            for (Class clazz : findClassesInPackage(packageName)) {
                if (hasRecordAnnoation(clazz))
                    classes.add(clazz);
            }
        }
        return classes;
    }

    private boolean hasRecordAnnoation(Class clazz) {
        return clazz.getAnnotation(Record.class) != null;
    }

    private List<Class> findClassesInPackage(String packageName) {
        List<Class> classes = new ArrayList<Class>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = loader.getResources(convertPackageToPath(packageName));
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                classes.addAll(createClassLocator(url).locate(url, packageName));
            }
            if (classes.size() == 0) {
                if (loader.getResource(convertPackageToPath(packageName)) == null) {
                    throw new IllegalArgumentException("Could not find package \"" + packageName + "\"");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }

    private ClassLocator createClassLocator(URL url) {
        if (isJarUrl(url)) {
            return new JarFileClassLocator();
        }
        return new LocalFileSystemClassLocator();
    }

    private boolean isJarUrl(URL url) {
        return "jar".equals(url.getProtocol());
    }

    private String convertPackageToPath(String packageName) {
        return packageName.replace('.', '/');
    }

}
