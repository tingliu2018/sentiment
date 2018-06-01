/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package depdendcy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class stores information about each word in a sentence.
 * @author ting
 */
public class Node implements Comparable<Node>, Serializable {

    private int id;
    private String name;
    private String pos;
    private String stemmedWord;
    private String lemmaWord;
    private String wholePhrase;
    private boolean bad_node; //for m4
    private static final long serialVersionUID = 30135220683341766L;

    public String getLemmaWord() {
        if (lemmaWord == null) {
            System.out.println("lemmaword is null, return original word: " + name);
            return name;
        }
        return lemmaWord;
    }

    public void setLemmaWord(String lemmaWord) {
        this.lemmaWord = lemmaWord;
    }
    public double minDist = Double.POSITIVE_INFINITY;
    public ArrayList<Edge> adjacencies = new ArrayList<Edge>();

    public ArrayList<Edge> getAdjacencies() {
        return adjacencies;
    }
    public Node previous;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPos() {
        return pos;
    }

    public boolean isBadNode() {
        return bad_node;
    }

    public String getWholePhrase() {
        return wholePhrase;
    }

    public void setWholePhrase(String wP) {
        wholePhrase = wP;
    }

    public boolean equal(String name, String pos) {
        if (this.name.equalsIgnoreCase(name) && this.pos.equalsIgnoreCase(pos)) {
            return true;
        }
        return false;
    }

    public boolean equal(String name) {
        if (this.name.equalsIgnoreCase(name)) {
            return true;
        }
        return false;
    }

    /**
     * This constructor creates a node that contains a word in the sentence, the
     * position of the word in the sentence, and the POS of the word.
     * @param id
     * @param nm
     * @param pos
     */
    public Node(int id, String nm, String pos) {
        this.id = id;
        this.name = nm;
        this.pos = pos;
//        this.stemmedWord=Util.stem(nm);

    }

    public int compareTo(Node o) {
        return Double.compare(minDist, o.minDist);
    }

    public void setBadNode(boolean bad_node) {
        this.bad_node = bad_node;
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("name: " + name + " , POS: " + pos);
        return out.toString();
    }
}
