package libai.fuzzy;

import libai.fuzzy.operators.AndMethod;
import libai.fuzzy.operators.OrMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * Created by kronenthaler on 30/04/2017.
 */
public class RuleTest {
    @Test
    public void testXMLGeneration() {
        Clause a = new Clause("variable1", "good");
        Clause b = new Clause("variable2", "big");
        Antecedent antecedent = new Antecedent(a, b);

        Clause c = new Clause("variable3", "bad");
        Clause d = new Clause("variable4", "small");
        Consequent consequent = new Consequent(c, d);

        Rule rule = new Rule("tipper", 1, OrMethod.PROBOR, Rule.Connector.OR, antecedent, consequent);

        Assertions.assertEquals("""
                <Rule name="tipper" weight="1.000000" operator="PROBOR" connector="OR">
                \t<Antecedent>
                \t\t<Clause>
                \t\t\t<Variable>variable1</Variable>
                \t\t\t<Term>good</Term>
                \t\t</Clause>
                \t\t<Clause>
                \t\t\t<Variable>variable2</Variable>
                \t\t\t<Term>big</Term>
                \t\t</Clause>
                \t</Antecedent>
                \t<Consequent>
                \t\t<Clause>
                \t\t\t<Variable>variable3</Variable>
                \t\t\t<Term>bad</Term>
                \t\t</Clause>
                \t\t<Clause>
                \t\t\t<Variable>variable4</Variable>
                \t\t\t<Term>small</Term>
                \t\t</Clause>
                \t</Consequent>
                </Rule>""", rule.toXMLString(""));
    }

    @Test
    public void testXMLConstructor() throws Exception {
        Clause a = new Clause("variable1", "good");
        Clause b = new Clause("variable2", "big");
        Antecedent antecedent = new Antecedent(a, b);

        Clause c = new Clause("variable3", "bad");
        Clause d = new Clause("variable4", "small");
        Consequent consequent = new Consequent(c, d);

        Rule rule = new Rule("tipper", 1, OrMethod.PROBOR, Rule.Connector.OR, antecedent, consequent);
        String xml = rule.toXMLString("");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        Element root = doc.getDocumentElement();

        Rule newRule = new Rule(root);
        Assertions.assertEquals(rule.toXMLString(""), newRule.toXMLString(""));
    }

    @Test
    public void testInvalidOperatorToOrConnector() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            new Rule("tipper", 1, AndMethod.MIN, Rule.Connector.OR, null, null);
        });
    }

    @Test
    public void testInvalidOperatorToAndConnector() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            new Rule("tipper", 1, OrMethod.MAX, Rule.Connector.AND, null, null);
        });
    }
}
