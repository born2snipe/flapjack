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

import flapjack.test.Address;
import flapjack.test.User;
import flapjack.test2.Phone;
import junit.framework.TestCase;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;


public class RecordPackageClassScannerTest extends TestCase {

    public void test_scan_ScanJar() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("flapjack-annotation-test-helper.jar");

        Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[]{url}, loader));

        RecordPackageClassScanner scanner = new RecordPackageClassScanner();

        List<Class> classes = scanner.scan(Arrays.<String>asList("flapjack.annotation.test"));

        assertNotNull(classes);
        assertEquals(1, classes.size());
        assertEquals("flapjack.annotation.test.Dog", classes.get(0).getName());
    }

    public void test_scan_SinglePackage() {
        RecordPackageClassScanner scanner = new RecordPackageClassScanner();

        List<Class> classes = scanner.scan(Arrays.<String>asList("flapjack.test"));

        assertNotNull(classes);
        assertTrue(classes.contains(User.class));
        assertFalse(classes.contains(Address.class));
        assertEquals(1, classes.size());
    }

    public void test_scan_MultiplePackages() {
        RecordPackageClassScanner scanner = new RecordPackageClassScanner();

        List<Class> classes = scanner.scan(Arrays.<String>asList("flapjack.test", "flapjack.test2"));

        assertNotNull(classes);
        assertTrue(classes.contains(User.class));
        assertTrue(classes.contains(Phone.class));
        assertFalse(classes.contains(Address.class));
        assertEquals(2, classes.size());
    }

    public void test_scan_PackageGivenDoesNotExist() {
        RecordPackageClassScanner scanner = new RecordPackageClassScanner();

        try {
            scanner.scan(Arrays.<String>asList("does.not.exist"));
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Could not findRecordField package \"does.not.exist\"", err.getMessage());
        }
    }
}
