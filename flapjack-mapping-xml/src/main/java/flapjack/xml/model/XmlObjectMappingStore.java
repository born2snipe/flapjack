package flapjack.xml.model;

import flapjack.model.ObjectMapping;
import flapjack.model.ObjectMappingStore;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;


public class XmlObjectMappingStore extends ObjectMappingStore {

    public XmlObjectMappingStore(URL xmlFile) {
        Digester digester = new Digester();

        digester.addObjectCreate("mappings", "java.util.ArrayList");

        digester.addObjectCreate("mappings/object-mapping", "flapjack.xml.model.ObjectMappingBean");
        digester.addSetProperties("mappings/object-mapping");

        digester.addObjectCreate("mappings/object-mapping/field", "flapjack.xml.model.FieldMappingBean");
        digester.addSetProperties("mappings/object-mapping/field");
        digester.addSetNext("mappings/object-mapping/field", "add", "flapjack.xml.model.FieldMappingBean");

        digester.addSetNext("mappings/object-mapping", "add", "flapjack.xml.model.ObjectMappingBean");

        try {
            List mappings = (List) digester.parse(xmlFile);
            Iterator it = mappings.iterator();
            while (it.hasNext()) {
                add(convert((ObjectMappingBean) it.next()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

    }

    private ObjectMapping convert(ObjectMappingBean bean) {
        ObjectMapping mapping = new ObjectMapping(bean.getMappedClass());
        Iterator it = bean.getFieldMappings().iterator();
        while (it.hasNext()) {
            FieldMappingBean fieldBean = (FieldMappingBean) it.next();
            mapping.add(fieldBean.getId(), fieldBean.getBean());
        }
        return mapping;
    }
}
