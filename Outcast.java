import stdlib.StdIn;
import stdlib.StdOut;

public class Outcast {
    WordNet wordnet; // make WordNet object

    // Constructs an Outcast object given the WordNet semantic lexicon.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // Returns the outcast noun from nouns.
    public String outcast(String[] nouns) {
        int distanceFullLoop = 0;
        int max = Integer.MIN_VALUE;
        String outty = null;
        for (String noun1: nouns) {
            int distance = 0;
            for (String noun2: nouns) {
                distance += wordnet.distance(noun1, noun2);
            }
            distanceFullLoop = distance;
            if (distanceFullLoop > max) {
                max = distanceFullLoop;
                outty = noun1;
            }
        }
        return outty;
        // javac -d out src/Outcast.java
        // java Outcast data/synsets.txt data/hypernyms.txt < data/outcast11.txt
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        String[] nouns = StdIn.readAllStrings();
        String outcastNoun = outcast.outcast(nouns);
        for (String noun : nouns) {
            StdOut.print(noun.equals(outcastNoun) ? "*" + noun + "* " : noun + " ");
        }
        StdOut.println();
    }
}
