package libai.fuzzy;

import org.w3c.dom.Node;

/**
 * Created by kronenthaler on 23/04/2017.
 */
public interface XMLSerializer {
    /**
     * Returns the XML representation of this Fuzzy set according with the FML schema definition.
     *
     * @return XML representation of this Fuzzy Set.
     **/
    String toXMLString(String indent);

    /**
     * Initializes the instance with the xml node information.
     *
     * @param xmlNode XML node with the information to load the current object with.
     **/
    void load(Node xmlNode);
}
