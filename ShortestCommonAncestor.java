// import dsa.*;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;
import dsa.DiGraph;
import dsa.BFSDiPaths;
import dsa.SeparateChainingHashST;

import java.util.Iterator;

public class ShortestCommonAncestor {
    private DiGraph G; // digraph
    private int length; // shortest distance to ancestral path of two vertices
    // Constructs a ShortestCommonAncestor object given a rooted DAG
    public ShortestCommonAncestor(DiGraph G) {
        if (G == null) {
            throw new NullPointerException("G is null");
        }
        this.G = G;
        length = 0;
    }

    // Returns length of the shortest ancestral path between vertices v and w.
    public int length(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }
        int ancestor = ancestor(v, w);
        length = distFrom(v).get(ancestor) + distFrom(w).get(ancestor);
        return length;
    }

    // Returns a shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }
        SeparateChainingHashST<Integer, Integer> firstPath = distFrom(v);
        SeparateChainingHashST<Integer, Integer> secondPath = distFrom(w);
        int ancestor = 0;
        int minDistance = Integer.MAX_VALUE;
        LinkedQueue<Integer> ancestors = new LinkedQueue<>();
        for (int i: firstPath.keys()) {
            for (int j : secondPath.keys()) {
                if (i == j) {
                    ancestors.enqueue(i);

                }
            }
        }
        while (!ancestors.isEmpty()) {
            int i = ancestors.dequeue();
            if (firstPath.get(i) + secondPath.get(i) < minDistance) {
                minDistance = firstPath.get(i) + secondPath.get(i);
                ancestor = i;
            }
        }
        return ancestor;
    }

    // Returns length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null) {
            throw new NullPointerException("A is null"); // right
        }
        if (B == null) {
            throw new NullPointerException("B is null"); // right
        }
        LinkedQueue<Integer> aA = new LinkedQueue<>();
        for (int i: A) {
            aA.enqueue(i);
        }
        LinkedQueue<Integer> bB = new LinkedQueue<>();
        for (int i: B) {
            bB.enqueue(i);
        }
        if (aA.isEmpty()) {
            throw new IllegalArgumentException("A is empty");
        }
        if (bB.isEmpty()) {
            throw new IllegalArgumentException("B is empty");
        }
        int minDistance = Integer.MAX_VALUE;
        int v = 0;
        int a = 0;
        int w = 0;
        for (int subset: A) {
            for (int subset2: B) {
                if (length(subset, subset2) < minDistance) {
                    minDistance = length(subset, subset2);
                    v = subset;
                    a = ancestor(subset, subset2);
                    w = subset2;
                }
            }
        }
        int[] tri =  new int[3];
        tri[0] = a;
        tri[1] = v;
        tri[2] = w;
        return minDistance;

    }

    // Returns a shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        if (B == null) {
            throw new NullPointerException("B is null");
        }
        LinkedQueue<Integer> aA = new LinkedQueue<>();
        for (int i: A) {
            aA.enqueue(i);
        }
        LinkedQueue<Integer> bB = new LinkedQueue<>();
        for (int i: B) {
            bB.enqueue(i);
        }
        if (aA.isEmpty()) {
            throw new IllegalArgumentException("A is empty");
        }
        if (bB.isEmpty()) {
            throw new IllegalArgumentException("B is empty");
        }
        int minDistance = Integer.MAX_VALUE;
        int v = 0;
        int a = 0;
        int w = 0;
        for (int subset: A) {
            for (int subset2: B) {
                if (length(subset, subset2) < minDistance) {
                    minDistance = length(subset, subset2);
                    v = subset;
                    a = ancestor(subset, subset2);
                    w = subset2;
                }
            }
        }
        int[] tri =  new int[3];
        tri[0] = a;
        tri[1] = v;
        tri[2] = w;
        return a;

    }

    // Returns a map of vertices reachable from v and their respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> Disty = new SeparateChainingHashST<>();
        LinkedQueue<Integer> q = new LinkedQueue();
        boolean[] marked = new boolean[G.V()];
        int[] distanceTo = new int[G.V()];
        int[] vertices = new int[G.V()];
        q.enqueue(v);
        marked[v] = true;
        distanceTo[v] = 0;

        while(!q.isEmpty()) {
            int current = (Integer)q.dequeue();
            Iterator neighbors = G.adj(v).iterator();
            while(neighbors.hasNext()) {
                int w = (Integer)neighbors.next();
                if (!marked[w]) {
                    marked[w] = true;
                    vertices[w] = current;
                    distanceTo[w] = distanceTo[current] + 1;
                    q.enqueue(w);
                    Disty.put(w, distanceTo[w]);
                }
            }
        }
        return Disty;
    }

    // Returns an array consisting of a shortest common ancestor a of vertex subsets A and B,
    // vertex v from A, and vertex w from B such that the path v-a-w is the shortest ancestral
    // path of A and B.
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        int minDistance = Integer.MAX_VALUE;
        int v = 0;
        int a = 0;
        int w = 0;
        for (int subset: A) {
            for (int subset2: B) {
                if (length(subset, subset2) < minDistance) {

                    minDistance = length(subset, subset2);
                    v = subset;
                    a = ancestor(subset, subset2);
                    w = subset2;
                }
            }
        }
        int[] tri =  new int[3];
        tri[0] = a;
        tri[1] = v;
        tri[2] = w;
        return tri;

    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        in.close();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            // StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            for (int i: sca.distFrom(v).keys()) {
                StdOut.print("vertex " + i + "distance " + sca.distFrom(v).get(i));
            }
        }


    }
}
// java ShortestCommonAncestor data/digraph1.txt
// javac -d out src/ShortestCommonAncestor.java
