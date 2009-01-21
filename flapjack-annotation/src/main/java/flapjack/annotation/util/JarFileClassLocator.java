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

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Locates classes found in a Jar file
 */
public class JarFileClassLocator extends AbstractClassLocator {

    private boolean isInPackage(JarEntry entry, String packageName) {
        return entry.getName().replace("/", ".").startsWith(packageName);
    }

    private boolean isClass(JarEntry entry) {
        return entry.getName().endsWith(".class");
    }

    protected void findClasses(List<Class> classes, URL url, String packageName) {
        JarFile jarFile = null;
        try {
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            connection.setUseCaches(false);
            jarFile = connection.getJarFile();

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (isClass(entry) && isInPackage(entry, packageName)) {
                    String fixedPath = entry.getName().replace("/", ".");
                    classes.add(loadClass(fixedPath.substring(0, fixedPath.lastIndexOf('.'))));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
