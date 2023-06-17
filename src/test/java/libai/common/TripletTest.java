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

import java.util.HashMap;

/**
 * @author Federico Vera {@literal <dktcoding [at] gmail>}
 */
public class TripletTest {

    @Test
    public void testHashCode() {
        Triplet<Integer, Integer, Integer>[] trips = new Triplet[]{
                new Triplet<>(-10, 4, -1),
                new Triplet<>(10, 6, -1),
                new Triplet<>(3, 2, -1),
                new Triplet<>(2, 4, -1),
        };
        HashMap<Triplet<?, ?, ?>, Triplet<?, ?, ?>> map = new HashMap<>(4);
        for (Triplet<?, ?, ?> t : trips) {
            map.put(t, t);
        }
        for (Triplet<?, ?, ?> t : trips) {
            Assertions.assertEquals(t, map.get(t));
            Assertions.assertSame(t, map.get(t));
        }
    }

    @Test
    public void testHashCode2() {
        Triplet<Integer, Integer, Integer> t1 = new Triplet<>(-10, 4, -1);
        Triplet<Integer, Integer, Integer> t2 = new Triplet<>(-10, 4, -1);
        Assertions.assertEquals(t1.hashCode(), t1.hashCode());
        Assertions.assertEquals(t1.hashCode(), t2.hashCode());
        t2.first = -9;
        Assertions.assertNotEquals(t1.hashCode(), t2.hashCode());
        t2.first = -10;
        Assertions.assertEquals(t1.hashCode(), t2.hashCode());
        t2.second = 4;
        Assertions.assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    public void testEquals() {
        Triplet<Integer, Integer, Integer> triplet = new Triplet<>(2, 4, -1);
        Assertions.assertEquals(triplet, triplet);
        Assertions.assertNotEquals(triplet, null);
        Assertions.assertNotEquals(triplet, new Object());
        Assertions.assertEquals(triplet, new Triplet<>(2, 4, -1));
        Assertions.assertNotEquals(triplet, new Triplet<>(2, 4, 1));
        Assertions.assertNotEquals(triplet, new Triplet<>(2, 2, -1));
        Assertions.assertNotEquals(triplet, new Triplet<>(1, 4, -1));
        Assertions.assertNotEquals(triplet, new Triplet<>("1", 4, -1));
    }

    @Test
    public void testToString() {
        Triplet<Integer, Integer, Integer> triplet = new Triplet<>(2, 4, -1);
        Assertions.assertEquals("(2,4,-1)", triplet.toString());
        triplet = new Triplet<>(2, 4, null);
        Assertions.assertEquals("(2,4,null)", triplet.toString());
    }
}
