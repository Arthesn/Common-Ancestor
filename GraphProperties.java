import dsa.BFSPaths;
import dsa.Graph;
import dsa.RedBlackBinarySearchTreeST;
import stdlib.In;
import stdlib.StdOut;
// javac -d out src/GraphProperties.java
// java GraphProperties data/tinyG.txt
public class GraphProperties {
    private RedBlackBinarySearchTreeST<Integer, Integer> st; // degree -> frequency
    private double avgDegree;                                // average degree of the graph
    private double avgPathLength;                            // average path length of the graph
    private double clusteringCoefficient;                    // clustering coefficient of the graph
    // Computes graph properties for the undirected graph G.
    public GraphProperties(Graph G) {
        // DD
        // loop over vertices in G
        //      get the degrees of vertex u
        //      add degree/freq key/value to st or increment value already existed
        st = new RedBlackBinarySearchTreeST<>();
        int[] count = new int[G.V()];
        for (int u = 0; u < G.V(); u++) {
            count[G.degree(u)]++;
        }
        for (int u = 0; u < G.V(); u++) {
            if (count[u] > 0) {
                st.put(u, count[u]);
            }
        }
        double length  = 0.0;
        avgDegree = 2.0 * G.E() / G.V();
        for (int u = 0; u < G.V(); u++) {
            BFSPaths path = new BFSPaths(G, u);
            for (int v = 0; v < G.V(); v++) {
                if (path.hasPathTo(v)) {
                    length += path.distTo(v);
                }
            }
        }
        avgPathLength = length / (G.V() *(G.V() - 1));
        // average path length
        // loop over vertices in G (u)
        //      create BSPaths object for G, u
        //      loop over vertices again (v)
        //          if there path u to v, add that path length to a running sum
        //  divide running sum by V(V -1)

        //  clusterin coeff
        // loop over vertices G (u)
        //  int actualCount = 0;
        double total = 0.0;
        for (int u = 0; u < G.V(); u++) {
            int edges = G.degree(u);
            int actualCount = 0;
            int possibleCount = (edges * (edges - 1)) / 2;
            //  BFSPaths path = new BFSPaths(G, v);
            for (int v: G.adj(u)) {
                for (int w : G.adj(u)) {
                    //  if (path.distTo(w) == 1) {
                    if (hasEdge(G, v, w)) {
                        actualCount++;
                    }
                }
            }
            actualCount = actualCount / 2;
            if (possibleCount > 0) {
                total += 1.0 * actualCount / possibleCount;
            }
        }
        clusteringCoefficient = total / G.V();

    }



    // Returns the degree distribution of the graph (a symbol table mapping each degree value to
    // the number of vertices with that value).
    public RedBlackBinarySearchTreeST<Integer, Integer> degreeDistribution() {
        return st;
    }

    // Returns the average degree of the graph.
    public double averageDegree() {
        return avgDegree;
    }

    // Returns the average path length of the graph.
    public double averagePathLength() {
        return avgPathLength;
    }

    // Returns the global clustering coefficient of the graph.
    public double clusteringCoefficient() {
        return clusteringCoefficient;
    }

    // Returns true if G has an edge between vertices v and w, and false otherwise.
    private static boolean hasEdge(Graph G, int v, int w) {
        for (int u : G.adj(v)) {
            if (u == w) {
                return true;
            }
        }
        return false;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        GraphProperties gp = new GraphProperties(G);
        RedBlackBinarySearchTreeST<Integer, Integer> st = gp.degreeDistribution();
        StdOut.println("Degree distribution:");
        for (int degree : st.keys()) {
            StdOut.println("  " + degree + ": " + st.get(degree));
        }
        StdOut.printf("Average degree         = %7.3f\n", gp.averageDegree());
        StdOut.printf("Average path length    = %7.3f\n", gp.averagePathLength());
        StdOut.printf("Clustering coefficient = %7.3f\n", gp.clusteringCoefficient());
    }
}
