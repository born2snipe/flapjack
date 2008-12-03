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

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;


public class LocalFileSystemClassLocator extends AbstractClassLocator {
    protected void findClasses(List<Class> classes, URL url, String packageName) {
        try {
            File dir = new File(url.toURI());
            File files[] = dir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.getName().endsWith(".class");
                }
            });
            for (File file : files) {
                String className = file.getName().replace("/", ".");
                classes.add(loadClass(packageName + "." + className.substring(0, className.lastIndexOf('.'))));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
