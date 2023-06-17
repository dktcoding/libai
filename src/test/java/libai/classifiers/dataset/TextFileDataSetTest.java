/*
 * MIT License
 *
 * Copyright (c) 2017 Federico Vera <https://github.com/dktcoding>
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
package libai.classifiers.dataset;

import libai.classifiers.Attribute;
import libai.classifiers.ContinuousAttribute;
import libai.classifiers.DiscreteAttribute;
import libai.common.MatrixIOTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Federico Vera {@literal <dktcoding [at] gmail>}
 */
public class TextFileDataSetTest {
    private static final String toString =
            """
                    [[[0]]=4.0, [[1]]=b, [[2]]=false, [[3]]=1.5, [[4]]=same]
                    [[[0]]=2.0, [[1]]=b, [[2]]=false, [[3]]=12.0, [[4]]=same]
                    [[[0]]=0.0, [[1]]=a, [[2]]=true, [[3]]=4.4, [[4]]=same]
                    [[[0]]=3.0, [[1]]=a, [[2]]=true, [[3]]=-12.0, [[4]]=same]
                    [[[0]]=1.0, [[1]]=f, [[2]]=true, [[3]]=4.4, [[4]]=same]
                    [[[0]]=5.0, [[1]]=d, [[2]]=true, [[3]]=8.0, [[4]]=same]
                    """;

    private static boolean writeDummyDataSet(String fname) {
        Assumptions.assumeTrue(MatrixIOTest.checkTemp(), "Can't use temp dir...");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + fname;
        new File(tmp).deleteOnExit();
        try (PrintStream ps = new PrintStream(tmp)) {
            Random r = ThreadLocalRandom.current();
            for (int i = 0; i < 100; i++) {
                ps.append("").append(String.valueOf(i)).append(',');
                ps.append("").append(String.valueOf(r.nextDouble() * 20 - 10)).append(',');
                ps.append("").append(String.valueOf(r.nextGaussian())).append(',');
                ps.append("").append(String.valueOf(r.nextBoolean()));
                if (i != 99) ps.append('\n');
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean writeDummyDataSetKnown(String fname) {
        Assumptions.assumeTrue(MatrixIOTest.checkTemp(), "Can't use temp dir...");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + fname;
        new File(tmp).deleteOnExit();
        try (PrintStream ps = new PrintStream(tmp)) {
            ps.append("0,a,true,4.4,same\n");
            ps.append("4,b,false,1.5,same\n");
            ps.append("3,a,true,-12.0,same\n");
            ps.append("1,f,true,4.4,same\n");
            ps.append("5,d,true,8,same\n");
            ps.append("2,b,false,12.0,same\n");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean writeDummyDataSetKnown2(String fname) {
        Assumptions.assumeTrue(MatrixIOTest.checkTemp(), "Can't use temp dir...");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + fname;
        new File(tmp).deleteOnExit();
        try (PrintStream ps = new PrintStream(tmp)) {
            ps.append("1,2,3,same\n");
            ps.append("1,2,3,same\n");
            ps.append("1,2,3,same\n");
            ps.append("1,2,3,same\n");
            ps.append("1,2,3,same\n");
            ps.append("1,2,3,same\n");
            ps.append("1,2,3,same\n");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testGetSubset() {
        Assumptions.assumeTrue(writeDummyDataSet("dummy.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 0);
        Assertions.assertNotEquals(0, ds.getItemsCount());
        DataSet ds2 = ds.getSubset(10, 20);
        Assertions.assertEquals(10, ds2.getItemsCount());

        double i = 10;
        for (List<Attribute> attrib : ds2) {
            Assertions.assertEquals(i++, attrib.get(ds.getOutputIndex()).getValue());
        }
        ds.close();
    }

    @Test
    public void testGetOutputIndex() {
        TextFileDataSet ds = new TextFileDataSet(2);
        Assertions.assertEquals(2, ds.getOutputIndex());
        DataSet ds2 = ds.getSubset(0, 0);
        Assertions.assertEquals(2, ds2.getOutputIndex());
        ds.close();
    }

    @Test
    public void testGetMetaData() {
        Assumptions.assumeTrue(writeDummyDataSet("dummy.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 0);
        MetaData md = ds.getMetaData();
        Assertions.assertEquals(4, md.getAttributeCount());
        Assertions.assertFalse(md.isCategorical(0));
        Assertions.assertFalse(md.isCategorical(1));
        Assertions.assertFalse(md.isCategorical(2));
        Assertions.assertTrue(md.isCategorical(3));
        Assertions.assertEquals("[0]", md.getAttributeName(0));
        Assertions.assertEquals("[1]", md.getAttributeName(1));
        Assertions.assertEquals("[2]", md.getAttributeName(2));
        Assertions.assertEquals("[3]", md.getAttributeName(3));
        ds.close();
    }

    @Test
    public void testSortOverInt() {
        Assumptions.assumeTrue(writeDummyDataSetKnown("dummy1.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy1.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 0);
        Iterator<List<Attribute>> attribs = ds.sortOver(0).iterator();
        Assertions.assertEquals(0.0, attribs.next().get(0).getValue());
        Assertions.assertEquals(1.0, attribs.next().get(0).getValue());
        Assertions.assertEquals(2.0, attribs.next().get(0).getValue());
        Assertions.assertEquals(3.0, attribs.next().get(0).getValue());
        Assertions.assertEquals(4.0, attribs.next().get(0).getValue());
        Assertions.assertEquals(5.0, attribs.next().get(0).getValue());
        attribs = ds.sortOver(1).iterator();
        Assertions.assertEquals("a", attribs.next().get(1).getValue());
        Assertions.assertEquals("a", attribs.next().get(1).getValue());
        Assertions.assertEquals("b", attribs.next().get(1).getValue());
        Assertions.assertEquals("b", attribs.next().get(1).getValue());
        Assertions.assertEquals("d", attribs.next().get(1).getValue());
        Assertions.assertEquals("f", attribs.next().get(1).getValue());
        attribs = ds.sortOver(2).iterator();
        Assertions.assertEquals("false", attribs.next().get(2).getValue());
        Assertions.assertEquals("false", attribs.next().get(2).getValue());
        Assertions.assertEquals("true", attribs.next().get(2).getValue());
        Assertions.assertEquals("true", attribs.next().get(2).getValue());
        Assertions.assertEquals("true", attribs.next().get(2).getValue());
        Assertions.assertEquals("true", attribs.next().get(2).getValue());
        attribs = ds.sortOver(3).iterator();
        Assertions.assertEquals(-12.0, attribs.next().get(3).getValue());
        Assertions.assertEquals(1.5, attribs.next().get(3).getValue());
        Assertions.assertEquals(4.4, attribs.next().get(3).getValue());
        Assertions.assertEquals(4.4, attribs.next().get(3).getValue());
        Assertions.assertEquals(8.0, attribs.next().get(3).getValue());
        Assertions.assertEquals(12.0, attribs.next().get(3).getValue());
        ds.close();
    }

    @Test
    public void testSortOver2() {
        Assumptions.assumeTrue(writeDummyDataSetKnown("dummy2.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy2.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 0);
        Iterator<List<Attribute>> attribs = ds.sortOver(2, 5, 0).iterator();
        Assertions.assertEquals(2.0, attribs.next().get(0).getValue());
        Assertions.assertEquals(3.0, attribs.next().get(0).getValue());
        attribs = ds.sortOver(2, 5, 2).iterator();
        Assertions.assertEquals("true", attribs.next().get(2).getValue());
        Assertions.assertEquals("true", attribs.next().get(2).getValue());
        ds.close();
    }

    @Test
    public void testSplitKeepingRelation() {
        Assumptions.assumeTrue(writeDummyDataSetKnown("dummy3.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy3.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 2);
        DataSet[] dss = ds.splitKeepingRelation(0.5);
        Assertions.assertEquals(3, dss[0].getItemsCount());
        Assertions.assertEquals(3, dss[1].getItemsCount());
        dss = ds.splitKeepingRelation(0.3);
        Assertions.assertEquals(1, dss[0].getItemsCount());
        Assertions.assertEquals(5, dss[1].getItemsCount());
        ds.close();
    }

    @Test
    public void testToString() {
        Assumptions.assumeTrue(writeDummyDataSetKnown("dummy4.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy4.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 2);
        Assertions.assertEquals(toString, ds.toString());
    }

    @Test
    public void testGetFrequencies() {
        Assumptions.assumeTrue(writeDummyDataSetKnown("dummy5.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy5.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 2);
        HashMap<Attribute, Integer> map = ds.getFrequencies(0, ds.getItemsCount(), 2);
        Assertions.assertEquals(Integer.valueOf(2), map.get(new DiscreteAttribute("false")));
        Assertions.assertEquals(Integer.valueOf(4), map.get(new DiscreteAttribute("true")));
        map = ds.getFrequencies(0, ds.getItemsCount(), 2); //Test cache
        Assertions.assertEquals(Integer.valueOf(2), map.get(new DiscreteAttribute("false")));
        Assertions.assertEquals(Integer.valueOf(4), map.get(new DiscreteAttribute("true")));
    }

    @Test
    public void testGetFrequencies2() {
        Assumptions.assumeTrue(writeDummyDataSetKnown("dummy6.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy6.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 2);
        ds.sortOver(2);
        HashMap<Attribute, Integer> map = ds.getFrequencies(0, 2, 2);
        Assertions.assertEquals(Integer.valueOf(2), map.get(new DiscreteAttribute("false")));
        Assertions.assertNull(map.get(new DiscreteAttribute("true")));
    }

    @Test
    public void testGetFrequencies3() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            Assumptions.assumeTrue(writeDummyDataSetKnown("dummy7.csv"), "Couldn't create dummy dataset");
            String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy7.csv";
            TextFileDataSet ds = new TextFileDataSet(new File(tmp), 1);
            ds.getFrequencies(0, 0, 0); //Non categorical
        });
    }

    @Test
    public void testAllTheSameOutput() {
        Assumptions.assumeTrue(writeDummyDataSetKnown("dummy8.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy8.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 2);
        Assertions.assertFalse(ds.allTheSameOutput());
    }

    @Test
    public void testAllTheSameOutput2() {
        Assumptions.assumeTrue(writeDummyDataSetKnown("dummy9.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy9.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 4);
        Assertions.assertTrue(ds.allTheSameOutput());
    }

    @Test
    public void testAllTheSame() {
        Assumptions.assumeTrue(writeDummyDataSetKnown("dummy10.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy10.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 4);
        Assertions.assertNull(ds.allTheSame());
    }

    @Test
    public void testAllTheSame2() {
        Assumptions.assumeTrue(writeDummyDataSetKnown2("dummy11.csv"), "Couldn't create dummy dataset");
        String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy11.csv";
        TextFileDataSet ds = new TextFileDataSet(new File(tmp), 3);
        Assertions.assertEquals(new DiscreteAttribute("3", "same"), ds.allTheSame());
    }

    @Test
    public void testAllTheSame3() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            Assumptions.assumeTrue(writeDummyDataSetKnown("dummy12.csv"), "Couldn't create dummy dataset");
            String tmp = System.getProperty("java.io.tmpdir") + File.separator + "dummy12.csv";
            TextFileDataSet ds = new TextFileDataSet(new File(tmp), 0);
            ds.allTheSame();
        });
    }

    @Test
    public void testAttributtesWithNames() {
        ContinuousAttribute ca = new ContinuousAttribute("name", 3.4);
        Assertions.assertEquals("[name]=3.4", ca.toString());
    }

}
