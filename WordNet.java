//  import dsa.*;
import dsa.SeparateChainingHashST;
import dsa.Set;
import dsa.DiGraph;
import stdlib.In;
// import stdlib.StdIn;
import stdlib.StdOut;
// javac -d out src/WordNet.java
// java WordNet data/synsets.txt data/hypernyms.txt worm bird
public class WordNet {
    private SeparateChainingHashST<String, Set<Integer>> st; // synsets and its IDs
    private SeparateChainingHashST<Integer, String> rst; // IDs and its specific synset
    private ShortestCommonAncestor sca; // SCA object
    // Constructs a WordNet object given the names of the input (synset and hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) {
            throw new NullPointerException("synsets is null");
        }
        if (hypernyms == null) {
            throw new NullPointerException("hypernyms is null");
        }
        st = new SeparateChainingHashST<>(); // (synset, IDs of hypernyms)
        rst = new SeparateChainingHashST<>(); // (ID, synset)
        In synset = new In(synsets);
        In hyper = new In(hypernyms);
        int count = 0; // keep track of number of synsets;
        while (synset.hasNextLine()) { // synsets.txt
            String current = synset.readLine();
            count++;
            String[] line = current.split(",");
            int ID = Integer.parseInt(line[0]);
            rst.put(ID, line[1]);
            String[] nouns = line[1].split(" ");
            for (String noun : nouns) {
                if (st.get(noun) == null) {
                    Set<Integer> ids = new Set<>();
                    ids.add(ID);
                    st.put(noun, ids);
                } else {
                    Set<Integer> newIDs = st.get(noun);
                    newIDs.add(ID);
                    st.put(noun, newIDs);
                }
            }

        }
        DiGraph G = new DiGraph(count);
        while (hyper.hasNextLine()) {
            String current = hyper.readLine();
            String[] hypes = current.split(",");
            int synsetsID = Integer.parseInt(hypes[0]); // starting edge
            for (int hype = 0; hype < hypes.length; hype++) {
                int hypernymID = Integer.parseInt(hypes[hype]);
                G.addEdge(synsetsID, hypernymID); // connect itself and the hypernyms
            }
        }
        sca = new ShortestCommonAncestor(G);

    }

    // Returns all WordNet nouns.
    public Iterable<String> nouns() {
        return st.keys();
    }

    // Returns true if the given word is a WordNet noun, and false otherwise.
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException("word is null");
        }
        return st.contains(word);
    }

    // Returns a synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!st.contains(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!st.contains(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }


        int ancestor = sca.ancestor(st.get(noun1), st.get(noun2));
        return rst.get(ancestor);
    }

    // Returns the length of the shortest ancestral path between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!st.contains(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!st.contains(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }
        return sca.length(st.get(noun1), st.get(noun2));
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }

        StdOut.printf("# of nouns = %d\n", nouns);
        StdOut.printf("isNoun(%s)? %s\n", word1, wordnet.isNoun(word1));
        StdOut.printf("isNoun(%s)? %s\n", word2, wordnet.isNoun(word2));
        StdOut.printf("isNoun(%s %s)? %s\n", word1, word2, wordnet.isNoun(word1 + " " + word2));
        StdOut.printf("sca(%s, %s) = %s\n", word1, word2, wordnet.sca(word1, word2));
        StdOut.printf("distance(%s, %s) = %s\n", word1, word2, wordnet.distance(word1, word2));
    }
}