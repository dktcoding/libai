package libai.fuzzy.sets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * Created by kronenthaler on 23/04/2017.
 */
public class TriangularShapeTest {
    @Test
    public void testXMLGeneration() {
        TriangularShape set = new TriangularShape(0, 5, 10);

        Assertions.assertEquals("<TriangularShape Param1=\"0.000000\" Param2=\"5.000000\" Param3=\"10.000000\"/>", set.toXMLString(""));
    }

    @Test
    public void testEvalBeforeA() {
        TriangularShape set = new TriangularShape(0, 5, 10);

        Assertions.assertTrue(set.eval(-1) == 0);
    }

    @Test
    public void testEvalAfterC() {
        TriangularShape set = new TriangularShape(0, 5, 10);

        Assertions.assertTrue(set.eval(11) == 0);
    }

    @Test
    public void testEvalBetweenAB() {
        TriangularShape set = new TriangularShape(0, 5, 10);

        Assertions.assertTrue(set.eval(2.5) == 0.5);
    }

    @Test
    public void testEvalBetweenBC() {
        TriangularShape set = new TriangularShape(0, 5, 10);

        Assertions.assertTrue(set.eval(7.5) == 0.5);
    }

    @Test
    public void testEvalAtC() {
        TriangularShape set = new TriangularShape(0, 5, 10);

        Assertions.assertTrue(set.eval(5) == 1);
    }

    @Test
    public void testRightLeftTriangle() {
        TriangularShape set = new TriangularShape(0, 0, 10);

        Assertions.assertTrue(set.eval(0) == 1);
    }

    @Test
    public void testRightRightTriangle() {
        TriangularShape set = new TriangularShape(0, 10, 10);

        Assertions.assertTrue(set.eval(10) == 1);
    }

    @Test
    public void testXMLConstructor() throws Exception {
        TriangularShape set = new TriangularShape(0, 5, 10);
        String xml = set.toXMLString("");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        Element root = doc.getDocumentElement();

        TriangularShape newSet = new TriangularShape(root);
        Assertions.assertEquals(set.toXMLString(""), newSet.toXMLString(""));
    }
}
