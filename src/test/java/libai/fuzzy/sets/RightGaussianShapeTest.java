package libai.fuzzy.sets;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by kronenthaler on 28/05/2017.
 */
public class RightGaussianShapeTest {
    @Test
    public void testXMLGeneration() {
        FuzzySet set = new RightGaussianShape(5, 1);

        assertEquals("<RightGaussianShape Param1=\"5.000000\" Param2=\"1.000000\"/>", set.toXMLString(""));
    }

    @Test
    public void testXMLConstructor() throws Exception {
        FuzzySet set = new RightGaussianShape(5, 1);
        String xml = set.toXMLString("");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        Element root = doc.getDocumentElement();

        FuzzySet newSet = new RightGaussianShape(root);
        assertEquals(set.toXMLString(""), newSet.toXMLString(""));
    }

    @Test
    public void testAfterOmega(){
        FuzzySet set = new RightGaussianShape(4, 0.5);

        assertEquals(0, set.eval(6), 1.e-3);
    }

    @Test
    public void testBeforeCenter(){
        FuzzySet set = new RightGaussianShape(4, 0.5);

        assertEquals(1, set.eval(3.75), 1.e-3);
    }

    @Test
    public void testAfterCenter(){
        FuzzySet set = new RightGaussianShape(4, 0.5);

        assertEquals(0.882, set.eval(4.25), 1.e-3);
    }
}