package libai.fuzzy;

import libai.fuzzy.sets.FuzzySet;
import libai.fuzzy.sets.TriangularShape;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * Created by kronenthaler on 23/04/2017.
 */
public class FuzzyTermTest {
    @Test
    public void testEvalWithComplement() {
        FuzzyTerm term = new FuzzyTerm(new FuzzySet() {
            @Override
            public double eval(double s) {
                return 0;
            }

            @Override
            public String toXMLString(String indent) {
                return null;
            }

            @Override
            public void load(Node xml) {
                throw new UnsupportedOperationException("");
            }
        }, "term", true);

        Assertions.assertEquals(1, term.eval(1), 0.0);
    }

    @Test
    public void testEvalWithoutComplement() {
        FuzzyTerm term = new FuzzyTerm(new FuzzySet() {
            @Override
            public double eval(double s) {
                return 0;
            }

            @Override
            public String toXMLString(String indent) {
                return null;
            }

            @Override
            public void load(Node xml) {
                throw new UnsupportedOperationException("");
            }
        }, "term");

        Assertions.assertEquals(0, term.eval(1), 0.0);
    }

    @Test
    public void testXMLGeneration() {
        FuzzyTerm term = new FuzzyTerm(new FuzzySet() {
            @Override
            public double eval(double s) {
                return 0;
            }

            @Override
            public String toXMLString(String indent) {
                return String.format("%s<SingletonShape Param1=\"%d\"/>", indent, 10);
            }

            @Override
            public void load(Node xml) {
                throw new UnsupportedOperationException("");
            }
        }, "term");

        Assertions.assertEquals("""
                <FuzzyTerm name="term" complement="false">
                \t<SingletonShape Param1="10"/>
                </FuzzyTerm>""", term.toXMLString(""));
    }

    @Test
    public void testXMLConstructor() throws Exception {
        FuzzyTerm term = new FuzzyTerm(new TriangularShape(0, 5, 10), "term", true);
        String xml = term.toXMLString("");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        Element root = doc.getDocumentElement();

        FuzzyTerm newTerm = new FuzzyTerm(root);
        Assertions.assertEquals(term.toXMLString(""), newTerm.toXMLString(""));
    }

    @Test
    public void testUnknownSetClass() throws Exception {
        String xml = "<FuzzyTerm name=\"term\"><UnknownSet /></FuzzyTerm>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        Element root = doc.getDocumentElement();

        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            new FuzzyTerm(root);
        });
    }

}
