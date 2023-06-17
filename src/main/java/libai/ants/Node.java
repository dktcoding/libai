/*
 * MIT License
 *
 * Copyright (c) 2009-2016 Enrique Areyan <enrique3 at gmail.com>
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
package libai.ants;

/**
 * This class belong to the core classes of the Ant Framework.
 * <p>
 * Implements a single node of a problem Graph. It stores the graph index,
 * distance relative to other Node and heuristic information It is mainly used
 * as a handy tool to compute candidate list nodes.
 *
 * @param index         Index of the node in the graph
 * @param distance      Distance according to other node
 * @param heuristicInfo Heuristic information
 * @author Enrique Areyan, enrique3 at gmail.com
 * @version 1
 */
public record Node(int index, double distance, double heuristicInfo) implements Comparable<Node> {
    /**
     * Constructor. Allocates the basic information of a node.
     *
     * @param index         index of the node
     * @param distance      distance according to another node
     * @param heuristicInfo heuristic information
     */
    public Node {
    }

    /**
     * Implements Comparable interface. Compares two nodes.
     *
     * @param n another node object
     */
    @Override
    public int compareTo(Node n) {
        return Double.compare(this.distance, n.distance);
    }

    /**
     * Return the string representation of this node. Useful for debugging.
     *
     * @return A string with the index, distance and heuristic information of
     * the node.
     */
    @Override
    public String toString() {
        return "{" + this.index + " , " + this.distance + " , " + this.heuristicInfo + "}";
    }

    /**
     * @return the index
     */
    @Override
    public int index() {
        return index;
    }

    /**
     * @return the heuristicInfo
     */
    @Override
    public double heuristicInfo() {
        return heuristicInfo;
    }

    /**
     * @return the distance
     */
    @Override
    public double distance() {
        return distance;
    }
}
