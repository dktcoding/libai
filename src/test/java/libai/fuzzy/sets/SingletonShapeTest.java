package libai.fuzzy.sets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * Created by kronenthaler on 20/05/2017.
 */
public class SingletonShapeTest {
    @Test
    public void testXMLGeneration() {
        SingletonShape set = new SingletonShape(3);

        Assertions.assertEquals("<SingletonShape Param1=\"3.000000\"/>", set.toXMLString(""));
    }

    @Test
    public void testXMLConstructor() throws Exception {
        SingletonShape set = new SingletonShape(3);
        String xml = set.toXMLString("");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        Element root = doc.getDocumentElement();

        SingletonShape newSet = new SingletonShape(root);
        Assertions.assertEquals(set.toXMLString(""), newSet.toXMLString(""));
    }

    @Test
    public void testA() {
        SingletonShape set = new SingletonShape(3);
        Assertions.assertEquals(1, set.eval(3), 1.e-3);
    }

    @Test
    public void testNotA() {
        SingletonShape set = new SingletonShape(3);
        Assertions.assertEquals(0, set.eval(3.000001), 1.e-3);
        Assertions.assertEquals(0, set.eval(2.999999), 1.e-3);
    }
}
