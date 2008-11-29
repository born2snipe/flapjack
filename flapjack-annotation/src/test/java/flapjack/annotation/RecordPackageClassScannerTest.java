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
package flapjack.annotation;

import flapjack.test.User;
import flapjack.test.Address;
import flapjack.test2.Phone;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;


public class RecordPackageClassScannerTest extends TestCase {

    public void test_scan_SinglePackage() {
        RecordPackageClassScanner scanner = new RecordPackageClassScanner();
        scanner.setPackages(Arrays.<String>asList("flapjack.test"));

        List<Class> classes = scanner.scan();

        assertNotNull(classes);
        assertTrue(classes.contains(User.class));
        assertFalse(classes.contains(Address.class));
        assertEquals(1, classes.size());
    }
    
    public void test_scan_MultiplePackages() {
        RecordPackageClassScanner scanner = new RecordPackageClassScanner();
        scanner.setPackages(Arrays.<String>asList("flapjack.test", "flapjack.test2"));

        List<Class> classes = scanner.scan();

        assertNotNull(classes);
        assertTrue(classes.contains(User.class));
        assertTrue(classes.contains(Phone.class));
        assertFalse(classes.contains(Address.class));
        assertEquals(2, classes.size());
    }
    
    public void test_scan_PackageGivenDoesNotExist() {
        RecordPackageClassScanner scanner = new RecordPackageClassScanner();
        scanner.setPackages(Arrays.<String>asList("does.not.exist"));

        try {
            scanner.scan();
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Could not find package \"does.not.exist\"", err.getMessage());
        }
    }
}
