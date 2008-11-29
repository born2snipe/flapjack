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
}
