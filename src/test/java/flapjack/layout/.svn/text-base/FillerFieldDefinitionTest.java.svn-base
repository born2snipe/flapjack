package commonline.core.layout;

import junit.framework.TestCase;


public class FillerFieldDefinitionTest extends TestCase {

    public void test() {
        FillerFieldDefinition definition = new FillerFieldDefinition(20, 300);

        assertNull(definition.getJustified());
        assertEquals(300, definition.getLength());
        assertEquals("Filler", definition.getName());
        assertNull(definition.getPadding());
        assertEquals(20, definition.getPosition());
        assertEquals(Type.ALPHA_NUMERIC, definition.getType());
    }
}
