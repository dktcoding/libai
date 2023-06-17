/*
 * MIT License
 *
 * Copyright (c) 2016 Federico Vera <https://github.com/dktcoding>
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
package libai.nn.supervised;

import libai.common.MatrixIOTest;
import libai.common.matrix.Column;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author Federico Vera {@literal <dktcoding [at] gmail>}
 */
public class PerceptronTest {

    @Test
    public void testTrainOr() {
        Perceptron p = new Perceptron(2, 1, new Random(0));
        Column[] ins = new Column[4];
        ins[0] = new Column(2, new double[]{0, 0});
        ins[1] = new Column(2, new double[]{0, 1});
        ins[2] = new Column(2, new double[]{1, 0});
        ins[3] = new Column(2, new double[]{1, 1});
        Column[] out = new Column[4];
        out[0] = new Column(1, new double[]{0});
        out[1] = new Column(1, new double[]{1});
        out[2] = new Column(1, new double[]{1});
        out[3] = new Column(1, new double[]{1});
        p.train(ins, out, 0.1, 1000);
        Assertions.assertEquals(0, p.error(ins, out), 0);
        Column res = new Column(1);
        p.simulate(ins[0], res);
        Assertions.assertEquals(out[0], res);
        p.simulate(ins[1], res);
        Assertions.assertEquals(out[1], res);
        p.simulate(ins[2], res);
        Assertions.assertEquals(out[2], res);
        p.simulate(ins[3], res);
        Assertions.assertEquals(out[3], res);
    }

    @Test
    public void testTrainAnd() {
        Perceptron p = new Perceptron(2, 1, new Random(0));
        Column[] ins = new Column[4];
        ins[0] = new Column(2, new double[]{0, 0});
        ins[1] = new Column(2, new double[]{0, 1});
        ins[2] = new Column(2, new double[]{1, 0});
        ins[3] = new Column(2, new double[]{1, 1});
        Column[] out = new Column[4];
        out[0] = new Column(1, new double[]{0});
        out[1] = new Column(1, new double[]{0});
        out[2] = new Column(1, new double[]{0});
        out[3] = new Column(1, new double[]{1});
        p.train(ins, out, 0.1, 1000);
        Assertions.assertEquals(0, p.error(ins, out), 0);
        Assertions.assertEquals(0, p.simulate(ins[0]).position(0, 0), 0);
        Assertions.assertEquals(0, p.simulate(ins[1]).position(0, 0), 0);
        Assertions.assertEquals(0, p.simulate(ins[2]).position(0, 0), 0);
        Assertions.assertEquals(1, p.simulate(ins[3]).position(0, 0), 0);
        Assertions.assertEquals(new Column(1, new double[]{0}), p.simulate(ins[0]));
        Assertions.assertEquals(new Column(1, new double[]{0}), p.simulate(ins[1]));
        Assertions.assertEquals(new Column(1, new double[]{0}), p.simulate(ins[2]));
        Assertions.assertEquals(new Column(1, new double[]{1}), p.simulate(ins[3]));
    }

    @Test
    public void testIO() {
        Assumptions.assumeTrue(MatrixIOTest.checkTemp(), "Can't use temp dir...");
        Perceptron p = new Perceptron(2, 2, new Random(0));
        Column[] ins = new Column[4];
        ins[0] = new Column(2, new double[]{0, 0});
        ins[1] = new Column(2, new double[]{0, 1});
        ins[2] = new Column(2, new double[]{1, 0});
        ins[3] = new Column(2, new double[]{1, 1});
        Column[] out = new Column[4];
        out[0] = new Column(2, new double[]{1, 0});
        out[1] = new Column(2, new double[]{1, 0});
        out[2] = new Column(2, new double[]{1, 0});
        out[3] = new Column(2, new double[]{0, 1});
        p.train(ins, out, 0.1, 1000);
        Assertions.assertEquals(1, p.simulate(ins[0]).position(0, 0), 0);
        Assertions.assertEquals(1, p.simulate(ins[1]).position(0, 0), 0);
        Assertions.assertEquals(1, p.simulate(ins[2]).position(0, 0), 0);
        Assertions.assertEquals(0, p.simulate(ins[3]).position(0, 0), 0);
        Assertions.assertEquals(0, p.simulate(ins[0]).position(1, 0), 0);
        Assertions.assertEquals(0, p.simulate(ins[1]).position(1, 0), 0);
        Assertions.assertEquals(0, p.simulate(ins[2]).position(1, 0), 0);
        Assertions.assertEquals(1, p.simulate(ins[3]).position(1, 0), 0);

        String foo = System.getProperty("java.io.tmpdir")
                + File.separator + "perceptron.tmp";
        new File(foo).deleteOnExit();

        Assertions.assertTrue(p.save(foo));
        try {
            Perceptron p2 = Perceptron.open(foo);
            Assertions.assertNotNull(p2);
            Assertions.assertNotEquals(p, p2);

            Assertions.assertEquals(p.simulate(ins[0]), p2.simulate(ins[0]));
            Assertions.assertEquals(p.simulate(ins[1]), p2.simulate(ins[1]));
            Assertions.assertEquals(p.simulate(ins[2]), p2.simulate(ins[2]));
            Assertions.assertEquals(p.simulate(ins[3]), p2.simulate(ins[3]));
        } catch (IOException | ClassNotFoundException e) {
            Assertions.fail();
        }
    }

}
