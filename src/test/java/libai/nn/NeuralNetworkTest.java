/*
 * MIT License
 *
 * Copyright (c) 2016 Federico Vera <https://github.com/dktcoding>
 *
 * Permission is hereby granted, free of charge, to any person obtaining ada copy
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
package libai.nn;

import libai.common.matrix.Column;
import libai.common.matrix.Matrix;
import libai.io.MatrixIO;
import org.junit.Test;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static libai.common.MatrixIOTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 * @author Federico Vera {@literal <dktcoding [at] gmail>}
 */
public class NeuralNetworkTest {
    @Test
    public void testErrorRegression() {
        //This test is to check that the code an be replaced with
        //euclideandistance2
        NN nn = new NN();
        Column[] patterns = new Column[10];
        Column[] answers = new Column[10];
        Random r = new Random(0);
        for (int i = 0; i < 10; i++) {
            patterns[i] = Column.random(20, true, r);
            answers[i] = Column.random(10, true, r);
        }
        //old error -> 7.498344587525985
        assertEquals(7.498344587525985, nn.error(patterns, answers), 1e-12);
    }

    @Test
    public void testErrorRegression2() {
        //This test is to check that the code an be replaced with
        //euclideandistance2
        NN nn = new NN();
        Column[] patterns = new Column[10];
        Column[] answers = new Column[10];
        Random r = new Random(0);
        for (int i = 0; i < 10; i++) {
            patterns[i] = Column.random(35, true, r);
            answers[i] = Column.random(25, true, r);
        }
        //old error -> 17.015311661715778
        assertEquals(17.015311661715778, nn.error(patterns, answers), 1e-12);
    }

    @Test
    public void testErrorZero() {
        NN nn = new NN();
        Column[] patterns = new Column[10];
        Column[] answers = new Column[10];
        for (int i = 0; i < 10; i++) {
            patterns[i] = Column.random(20);
            answers[i] = new Column(20);
            patterns[i].copy(answers[i]);
        }
        assertEquals(0, nn.error(patterns, answers), 1e-12);
    }

    @Test
    public void testErrorZeroOffset() {
        NN nn = new NN();
        Column[] patterns = new Column[10];
        Column[] answers = new Column[10];
        for (int i = 0; i < 10; i++) {
            patterns[i] = Column.random(20);
            answers[i] = new Column(20);
            patterns[i].copy(answers[i]);
        }
        assertEquals(0, nn.error(patterns, answers, 2, 7), 1e-12);
    }

    @Test
    public void testEuclideanDistanceMatrix() {
        assumeTrue("Can't use temp dir...", checkTemp());
        assumeTrue("Can't find Octave...", checkOctaveInstall());
        Column a = Column.random(10);
        Column b = Column.random(10);

        String tmp = System.getProperty("java.io.tmpdir") + File.separator;
        File matFile = new File(tmp + "euclidean.mat");
        try (OutputStream os = Files.newOutputStream(matFile.toPath())) {
            Map<String, Matrix> mat = new HashMap<>(2);
            mat.put("a", a);
            mat.put("b", b);
            MatrixIO.write(os, mat, MatrixIO.Target.OCTAVE);
        } catch (Exception ignored) {
        }

        String command = "load " + tmp + "euclidean.mat; sum((a-b).^2)";
        double distLai = NeuralNetwork.euclideanDistance2(a, b);
        double distOct = Double.parseDouble(eval(command));
        assertEquals(distOct, distLai, 1e-3);
    }

    @Test
    public void testEuclideanDistanceDouble() {
        assumeTrue("Can't use temp dir...", checkTemp());
        assumeTrue("Can't find Octave...", checkOctaveInstall());
        Matrix a = Matrix.random(10, 1);
        Matrix b = Matrix.random(10, 1);

        String tmp = System.getProperty("java.io.tmpdir") + File.separator;
        File matFile = new File(tmp + "euclidean.mat");
        try (OutputStream os = Files.newOutputStream(matFile.toPath())) {
            Map<String, Matrix> mat = new HashMap<>(2);
            mat.put("a", a);
            mat.put("b", b);
            MatrixIO.write(os, mat, MatrixIO.Target.OCTAVE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String command = "load " + tmp + "euclidean.mat; sum((a-b).^2)";
        double distLai = NeuralNetwork.euclideanDistance2(a.getCol(0), b.getCol(0));
        double distOct = Double.parseDouble(eval(command));
        assertEquals(distOct, distLai, 1e-3);
    }

//    @Test
//    public void testShuffle() {
//        int[] arr1 = new int[10];
//        int[] arr2 = new int[10];
//        for (int i = 0; i < 10; i++) arr1[i] = arr2[i] = i;
//        assertArrayEquals(arr1, arr2);
//        new NN().shuffle(arr1);
//        assertFalse(Arrays.equals(arr1, arr2));
//        //No value should be ommited
//        int foo = 0;
//        for (int i = 0; i < arr2.length; i++) {
//            foo ^= arr1[i];
//            foo ^= arr2[i];
//        }
//        assertEquals(0, foo);
//    }

    private static class NN extends NeuralNetwork {
        @Override
        public void train(Column[] patterns, Column[] answers, double alpha, int epochs, int offset, int length, double minerror) {
            // not needed
        }

        @Override
        public Column simulate(Column pattern) {
            return null;
        }

        @Override
        public void simulate(Column pattern, Column result) {
            //Dummy simulation
            for (int i = 0; i < result.getRows(); i++) {
                result.position(i, 0, pattern.position(i, 0));
            }
        }
    }

}
