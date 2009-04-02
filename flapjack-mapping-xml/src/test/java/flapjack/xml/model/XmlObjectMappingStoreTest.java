package flapjack.xml.model;

import flapjack.model.FieldMapping;
import flapjack.model.ObjectMapping;
import junit.framework.TestCase;


public class XmlObjectMappingStoreTest extends TestCase {
    public void test_singleMapping() {
        XmlObjectMappingStore store = new XmlObjectMappingStore(Thread.currentThread().getContextClassLoader().getResource("single-mapping.xml"));

        ObjectMapping objMapping = store.find(User.class);
        assertNotNull(objMapping);

        FieldMapping fieldMapping = objMapping.findRecordField("field1");
        assertNotNull(fieldMapping);
        assertEquals("field1", fieldMapping.getRecordFieldName());
        assertEquals("bean1", fieldMapping.getDomainFieldName());

        fieldMapping = objMapping.findRecordField("field2");
        assertNotNull(fieldMapping);
        assertEquals("field2", fieldMapping.getRecordFieldName());
        assertEquals("bean2", fieldMapping.getDomainFieldName());

        assertEquals(2, objMapping.getFieldCount());
    }

    public void test_multipleMappings() {
        XmlObjectMappingStore store = new XmlObjectMappingStore(Thread.currentThread().getContextClassLoader().getResource("multiple-mappings.xml"));

        ObjectMapping objMapping = store.find(User.class);
        assertNotNull(objMapping);

        FieldMapping fieldMapping = objMapping.findRecordField("field1");
        assertNotNull(fieldMapping);
        assertEquals("field1", fieldMapping.getRecordFieldName());
        assertEquals("bean1", fieldMapping.getDomainFieldName());

        fieldMapping = objMapping.findRecordField("field2");
        assertNotNull(fieldMapping);
        assertEquals("field2", fieldMapping.getRecordFieldName());
        assertEquals("bean2", fieldMapping.getDomainFieldName());

        assertEquals(2, objMapping.getFieldCount());

        objMapping = store.find(Address.class);
        assertNotNull(objMapping);

        fieldMapping = objMapping.findRecordField("field1");
        assertNotNull(fieldMapping);
        assertEquals("field1", fieldMapping.getRecordFieldName());
        assertEquals("line", fieldMapping.getDomainFieldName());

        fieldMapping = objMapping.findRecordField("field2");
        assertNotNull(fieldMapping);
        assertEquals("field2", fieldMapping.getRecordFieldName());
        assertEquals("city", fieldMapping.getDomainFieldName());

        fieldMapping = objMapping.findRecordField("field3");
        assertNotNull(fieldMapping);
        assertEquals("field3", fieldMapping.getRecordFieldName());
        assertEquals("state", fieldMapping.getDomainFieldName());

        assertEquals(3, objMapping.getFieldCount());
    }

    public void test_duplicateMappings() {
        try {
            new XmlObjectMappingStore(Thread.currentThread().getContextClassLoader().getResource("duplicate-mappings.xml"));
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Duplicate ObjectMapping for class flapjack.xml.model.Address", err.getMessage());
        }
    }
    
    public void test_duplicateFieldMappings() {
        try {
            new XmlObjectMappingStore(Thread.currentThread().getContextClassLoader().getResource("duplicate-field-mappings.xml"));
            fail();
        } catch (IllegalArgumentException err) {
            assertEquals("Duplicate mapping of field='field1' for class=flapjack.xml.model.User", err.getMessage());
        }
    }
}
