package flapjack.layout;

import junit.framework.TestCase;


public class SimpleRecordLayoutTest extends TestCase {
    SimpleRecordLayout layout;

    public void setUp() {
        layout = new SimpleRecordLayout();
    }

    public void test_OneFieldDefinition() {
        layout.addFieldDefinition(new SimpleFieldDefinition("", 0, 2));

        assertEquals(2, layout.getLength());
        assertEquals(1, layout.getFieldDefinitions().size());
    }
    
    public void test_TwoFieldDefinition() {
        layout.addFieldDefinition(new SimpleFieldDefinition("", 0, 2));
        layout.addFieldDefinition(new SimpleFieldDefinition("", 2, 2));

        assertEquals(4, layout.getLength());
        assertEquals(2, layout.getFieldDefinitions().size());
    }
}
