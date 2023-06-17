package libai.fuzzy;

import libai.fuzzy.sets.FuzzySet;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;

/**
 * Created by kronenthaler on 23/04/2017.
 */
public class FuzzyTerm implements FuzzySet, XMLSerializer {
    private FuzzySet set;
    private String name;
    private boolean complement;

    public FuzzyTerm(Node xmlNode) {
        load(xmlNode);
    }

    public FuzzyTerm(FuzzySet set, String name) {
        this(set, name, false);
    }

    public FuzzyTerm(FuzzySet set, String name, boolean complement) {
        this.set = set;
        this.name = name;
        this.complement = complement;
    }

    @Override
    public double eval(double x) {
        if (complement)
            return 1 - set.eval(x);
        return set.eval(x);
    }

    @Override
    public String toXMLString(String indent) {
        return String.format("%s<FuzzyTerm name=\"%s\" complement=\"%s\">\n%s\n%s</FuzzyTerm>", indent, name, complement, set.toXMLString(indent + "\t"), indent);
    }

    @Override
    public void load(Node xmlNode) {
        NamedNodeMap attributes = xmlNode.getAttributes();
        name = attributes.getNamedItem("name").getTextContent();

        if (attributes.getNamedItem("complement") != null)
            complement = Boolean.parseBoolean(attributes.getNamedItem("complement").getTextContent());

        NodeList children = xmlNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node current = children.item(i);
            if (current.getNodeType() != Node.ELEMENT_NODE)
                continue;

            String setClass = current.getNodeName();
            try {
                Class<?> clazz = Class.forName("libai.fuzzy.sets." + setClass);
                Constructor<?> ctor = clazz.getConstructor(Node.class);
                set = (FuzzySet) ctor.newInstance(new Object[]{current});
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    public String getName() {
        return name;
    }
}
