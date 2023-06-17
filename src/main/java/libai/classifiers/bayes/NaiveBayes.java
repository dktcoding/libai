/*
 * MIT License
 *
 * Copyright (c) 2009-2016 Ignacio Calderon <https://github.com/kronenthaler>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package libai.classifiers.bayes;

import libai.classifiers.Attribute;
import libai.classifiers.ContinuousAttribute;
import libai.classifiers.DiscreteAttribute;
import libai.classifiers.dataset.DataSet;
import libai.classifiers.dataset.MetaData;
import libai.common.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

/**
 * @author kronenthaler
 */
public class NaiveBayes {
    protected int outputIndex;
    protected int totalCount;
    protected MetaData metadata;
    protected HashMap<Attribute, Object[]> params;

    //Factories
    public static NaiveBayes getInstance(File path) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(Files.newInputStream(path.toPath()));
            Node root = doc.getElementsByTagName("NaiveBayes").item(0);

            return new NaiveBayes().load(root);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NaiveBayes getInstance(DataSet ds) {
        return new NaiveBayes().train(ds);
    }

    public NaiveBayes train(DataSet ds) {
        outputIndex = ds.getOutputIndex();
        totalCount = ds.getItemsCount();
        metadata = ds.getMetaData();

        params = new HashMap<>();
        initialize(ds);
        precalculate(ds);

        return this;
    }

    private void initialize(DataSet ds) {
        int attributeCount = metadata.getAttributeCount();
        for (Attribute c : metadata.getClasses()) {
            params.put(c, new Object[attributeCount]);
            for (int j = 0; j < attributeCount; j++) {
                if (j == outputIndex) {
                    params.get(c)[j] = 0;
                } else if (metadata.isCategorical(j)) {
                    params.get(c)[j] = new HashMap<>(); //value, count
                } else {
                    params.get(c)[j] = new Pair<>(0.0, 0.0);//mean, sd
                }
            }
        }
    }

    private void precalculate(DataSet ds) {
        for (List<Attribute> record : ds) {
            Attribute outputAttr = record.get(outputIndex);
            int j = 0;
            for (Attribute attr : record) {
                Object value = attr.getValue();
                if (j == outputIndex) {
                    //count simple frequencies of the output class
                    int current = (Integer) params.get(outputAttr)[j];
                    params.get(outputAttr)[j] = current + 1;
                } else if (metadata.isCategorical(j)) {
                    // count frequencies of each different values in this attribute
                    HashMap<String, Integer> freq = (HashMap<String, Integer>) params.get(outputAttr)[j];
                    freq.putIfAbsent((String) value, 0);
                    freq.put((String) value, freq.get((String) value) + 1);
                } else {
                    // precalculate the mean and standard deviation, acumulate part.
                    Pair<Double, Double> acum = (Pair<Double, Double>) params.get(outputAttr)[j];
                    //acum for mean and SD
                    acum.first = acum.first + (Double) value;
                    acum.second = acum.second + Math.pow((Double) value, 2);
                }
                j++;
            }
        }

        // just for the continuous attributes, finish the calculation of the
        // gausian parameters.
        // for each class values
        for (Object[] data : params.values()) {
            // for each look up table
            for (Object o : data) {
                // look for the continuos attributes, that are not the output
                if (o instanceof Pair) {
                    Pair<Double, Double> acum = (Pair<Double, Double>) o;
                    double sd = acum.second;
                    double mean = acum.first;
                    double length = (double) ((Integer) data[outputIndex]);

                    sd = (sd - ((mean * mean) / length));
                    acum.second = sd / (length - 1);
                    acum.first = mean / length;
                }
            }
        }
    }

    //calculate the maximum posterior probability this data record (x) in the data set
    //P(Ci|x) > P(Cj|x) 1 <= j < m, i!=j
    public Attribute eval(List<Attribute> x) {
        Attribute winner = null;
        double max = -Double.MAX_VALUE;
        for (Attribute c : params.keySet()) {
            double tmp = probability(c, x);
            if (tmp > max) {
                max = tmp;
                winner = c;
            }
        }
        return winner;
    }

    //P(H|x) = P(x|H)P(H) / P(x)
    //relaxed calculation of P(H|x). the exact value is not necessary, just to know which class
    //has the highest value.
    private double probability(Attribute h, List<Attribute> x) {
        return probability(x, h) * probability(h);
    }

    private double probability(List<Attribute> x, Attribute h) {
        double p = 1;
        //look for all records in ds with class h.
        for (int k = 0, n = x.size(); k < n; k++) {
            Attribute attr = x.get(k);
            if (metadata.isCategorical(k)) {
                p *= (count((DiscreteAttribute) attr, k, h) + 1) / (double) (((Integer) params.get(h)[outputIndex]) + 1);
            } else {
                p *= gaussian((ContinuousAttribute) attr, k, h);
            }
        }
        return p;
    }

    //laplace's correction. x+1 / |d|+|c|
    private double probability(Attribute h) {
        return (((Integer) params.get(h)[outputIndex]) + 1) / (double) (totalCount + params.size());
    }

    private int count(DiscreteAttribute xk, int k, Attribute h) {
        @SuppressWarnings("unchecked")
        HashMap<String, Integer> freq = (HashMap<String, Integer>) params.get(h)[k];
        return freq.get(xk.getValue());
    }

    private double gaussian(ContinuousAttribute xk, int k, Attribute h) {
        @SuppressWarnings("unchecked")
        Pair<Double, Double> ps = (Pair<Double, Double>) params.get(h)[k];
        double mean = ps.first;
        double sd = ps.second;
        double x = xk.getValue();
        return Math.exp(-(Math.pow(x - mean, 2) / (2 * sd))) * (1 / (Math.sqrt(2 * Math.PI * sd)));
    }

    //IO functions
    public boolean save(File path) {
        try (FileOutputStream fos = new FileOutputStream(path);
             PrintStream out = new PrintStream(fos)) {
            out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            out.println("<" + getClass().getSimpleName() + " "
                    + "outputIndex=\"" + outputIndex + "\" "
                    + "totalCount=\"" + totalCount + "\" "
                    + "attributes=\"" + metadata.getAttributeCount() + "\">");
            save(out, "\t");
            out.println("</" + getClass().getSimpleName() + ">");
            //safe format into a XML file.
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void save(PrintStream out, String indent) {
        for (Attribute c : params.keySet()) {
            out.println(indent + "<class>");
            out.println(indent + "\t<params type=\"" + c.getClass().getName() + "\" name=\"" + c.getName() + "\" ><![CDATA[" + c.getValue() + "]]></params>");
            int i = 0;
            for (Object o : params.get(c)) {
                out.println(indent + "\t<attribute index=\"" + i + "\">");
                if (o instanceof Integer) { //class count
                    out.println(indent + "\t\t<count>" + o + "</count>");
                } else if (o instanceof Pair) { //continuous parameter
                    Pair<Double, Double> p = (Pair<Double, Double>) o;
                    out.println(indent + "\t\t<stats mean=\"" + p.first + "\" sd=\"" + p.second + "\"/>");
                } else { //discrete parameter
                    HashMap<String, Integer> freq = (HashMap<String, Integer>) o;
                    for (String key : freq.keySet()) {
                        out.println(indent + "\t\t<item count=\"" + freq.get(key) + "\"><![CDATA[" + key + "]]></item>");
                    }
                }
                out.println(indent + "\t</attribute>");
                i++;
            }
            out.println(indent + "</class>");
        }
    }

    private NaiveBayes load(Node root) {
        outputIndex = Integer.parseInt(root.getAttributes().getNamedItem("outputIndex").getTextContent());
        totalCount = Integer.parseInt(root.getAttributes().getNamedItem("totalCount").getTextContent());
        params = new HashMap<>();
        int attributeCount = Integer.parseInt(root.getAttributes().getNamedItem("attributes").getTextContent());

        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node clazz = children.item(i);
            if (clazz.getNodeName().equals("class")) {
                NodeList p = clazz.getChildNodes();
                Attribute key = null;
                int index = -1;
                for (int j = 0; j < p.getLength(); j++) {
                    Node current = p.item(j);
                    if (current.getNodeName().equals("params")) {
                        key = Attribute.load(current);
                    } else if (current.getNodeName().equals("attribute")) {
                        index = Integer.parseInt(current.getAttributes().getNamedItem("index").getTextContent());
                        if (params.get(key) == null)
                            params.put(key, new Object[attributeCount]);
                        params.get(key)[index] = getParams(current);
                    }
                }
            }
        }

        System.err.println(this);

        return this;
    }

    private Object getParams(Node root) {
        NodeList children = root.getChildNodes();
        HashMap<String, Integer> freq = new HashMap<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node current = children.item(i);
            if (current.getNodeName().equals("count"))
                return Integer.parseInt(current.getTextContent());
            else if (current.getNodeName().equals("stats")) {
                double mean = Double.parseDouble(current.getAttributes().getNamedItem("mean").getTextContent());
                double sd = Double.parseDouble(current.getAttributes().getNamedItem("sd").getTextContent());
                return new Pair<>(mean, sd);
            } else if (current.getNodeName().equals("item")) {
                int count = Integer.parseInt(current.getAttributes().getNamedItem("count").getTextContent());
                String key = current.getTextContent();
                freq.put(key, count);
            }
        }
        return freq;
    }
}