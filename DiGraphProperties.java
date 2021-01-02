import dsa.DiCycle;
import dsa.DiGraph;
import dsa.LinkedBag;
import stdlib.In;
import stdlib.StdOut;
// javac -d out src/DiGraphProperties.java
// java DiGraphProperties data/tinyDG.txt
public class DiGraphProperties {
    private boolean isDAG;              // is the digraph a DAG?
    private boolean isMap;              // is the digraph a map?
    private LinkedBag<Integer> sources; // the sources in the digraph
    private LinkedBag<Integer> sinks;   // the sinks in the digraph
    private DiCycle dag; // Dicyle isDag or not
    // Computes graph properties for the digraph G.
    public DiGraphProperties(DiGraph G) {
        // isDAG = false;
        if (G.equals(null)) {
            isDAG = false;
            isMap = false;
        }
        dag = new DiCycle(G);
        isDAG = !dag.hasCycle();
        // isMap = false;
        sources = new LinkedBag<>();
        sinks = new LinkedBag<>();
        for (int u = 0; u < G.V(); u++) {
            if (G.outDegree(u) != 1) {
                isMap = false;
                break;
            }
        }
        for (int u = 0; u < G.V(); u++) {
            if (G.outDegree(u) == 0) {
                sinks.add(u);
            }
        }
        for (int u = 0; u < G.V(); u++) {
            if (G.inDegree(u) == 0) {
                sources.add(u);
            }
        }

    }

    // Returns true if the digraph is a directed acyclic graph (DAG), and false otherwise.
    public boolean isDAG() {
        return isDAG;
    }

    // Returns true if the digraph is a map, and false otherwise.
    public boolean isMap() {
        return isMap;
    }

    // Returns all the sources (ie, vertices without any incoming edges) in the digraph.
    public Iterable<Integer> sources() {
        return sources;
    }

    // Returns all the sinks (ie, vertices without any outgoing edges) in the digraph.
    public Iterable<Integer> sinks() {
        return sinks;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        DiGraphProperties gp = new DiGraphProperties(G);
        StdOut.print("Sources: ");
        for (int v : gp.sources()) {
            StdOut.print(v + " ");
        }
        StdOut.println();
        StdOut.print("Sinks: ");
        for (int v : gp.sinks()) {
            StdOut.print(v + " ");
        }
        StdOut.println();
        StdOut.println("Is DAG? " + gp.isDAG());
        StdOut.println("Is Map? " + gp.isMap());
    }
}
