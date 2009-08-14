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

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Locates classes on the local file system
 */
public class LocalFileSystemClassLocator extends AbstractClassLocator {

    protected void findClasses(List<Class> classes, URL url, Pattern packageNamePattern) {
        try {
            File dir = new File(url.toURI());
            File files[] = dir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.getName().endsWith(".class");
                }
            });
            for (File file : files) {
                String className = convertFilePathToPackage(file);
                if (packageNamePattern.matcher(className).find()) {
                    classes.add(goUntilItLoads(trimOfClassExtension(className)));
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String trimOfClassExtension(String className) {
        return className.substring(0, className.lastIndexOf('.'));
    }

    private String convertFilePathToPackage(File file) {
        return file.getAbsolutePath().replace("/", ".");
    }

    private Class goUntilItLoads(String path) {
        try {
            return loadClass(path);
        } catch (ClassNotFoundException e) {
            return goUntilItLoads(path.substring(path.indexOf('.') + 1));
        }
    }
}
