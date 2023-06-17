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
package libai.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Federico Vera {@literal <dktcoding [at] gmail>}
 */
public class PairTest {
    @Test
    public void testSort() {
        Pair<Integer, Integer>[] pairs = new Pair[]{
                new Pair<>(-10, 4),
                new Pair<>(10, 6),
                new Pair<>(3, 2),
                new Pair<>(2, 4),
        };
        Pair<Integer, Integer>[] pairsSorted = new Pair[]{
                new Pair<>(-10, 4),
                new Pair<>(2, 4),
                new Pair<>(3, 2),
                new Pair<>(10, 6),
        };
        Arrays.sort(pairs);
        Assertions.assertArrayEquals(pairs, pairsSorted);
    }

    @Test
    public void testHashCodeEquals() {
        HashMap<Pair<Integer, Integer>, Pair<Integer, Integer>> map = new HashMap<>();

        Pair<Integer, Integer>[] pairs = new Pair[]{
                new Pair<>(-10, 4),
                new Pair<>(10, 6),
                new Pair<>(3, 2),
                new Pair<>(2, 4),
        };

        for (Pair<Integer, Integer> p : pairs) {
            map.put(p, p);
        }

        for (Pair<Integer, Integer> p : pairs) {
            Assertions.assertEquals(p, map.get(p));
            Assertions.assertSame(p, map.get(p));
        }
    }

    @Test
    public void testHashCode() {
        Pair<Integer, Integer> p1 = new Pair<>(-10, 4);
        Pair<Integer, Integer> p2 = new Pair<>(-10, 4);

        Assertions.assertEquals(p1.hashCode(), p1.hashCode());
        Assertions.assertEquals(p1.hashCode(), p2.hashCode());
        p2.first = Integer.valueOf("-10");
        Assertions.assertEquals(p1.hashCode(), p2.hashCode());
        p2.first = -10;
        Assertions.assertEquals(p1.hashCode(), p2.hashCode());
        p2.second = (int) 5.0;
        Assertions.assertNotEquals(p1.hashCode(), p2.hashCode());
        p2.second = (int) 4.0;
        Assertions.assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testToString() {
        Pair<String, String> pair = new Pair<>("abc", "def");
        Assertions.assertEquals("(abc,def)", pair.toString());
    }

    @Test
    public void testEquals() {
        Pair<String, String> pair = new Pair<>("abc", "def");
        Assertions.assertNotEquals(pair, null);
        Assertions.assertNotEquals(new Object(), pair);
        Assertions.assertNotEquals(4, pair);
    }
}
