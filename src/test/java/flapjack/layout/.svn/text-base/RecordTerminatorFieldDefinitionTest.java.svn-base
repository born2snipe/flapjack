package commonline.core.layout;

import junit.framework.TestCase;


public class RecordTerminatorFieldDefinitionTest extends TestCase {

    public void test() {
        RecordTerminatorFieldDefinition definition = new RecordTerminatorFieldDefinition(300);

        assertNull(definition.getJustified());
        assertEquals(1, definition.getLength());
        assertEquals("Record Terminator", definition.getName());
        assertNull(definition.getPadding());
        assertEquals(300, definition.getPosition());
        assertEquals(Type.ALPHA_NUMERIC, definition.getType());
    }
}
