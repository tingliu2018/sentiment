package depdendcy;

import edu.stanford.nlp.io.EncodingPrintWriter;
import edu.stanford.nlp.ling.HasWord;
import java.io.*;
import java.util.*;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.*;
import edu.stanford.nlp.parser.lexparser.*;
import utils.Out;

/**
 * This class parses sentences for useful data, then prints out onto the screen
 * what data it has collected from the sentences it parsed.
 *
 * @author Ting Liu, Logan Brandt
 */
public class StanfordParser {

    Tree t;
    LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
    //LexicalizedParser lp = new LexicalizedParser("lib/stanford-parser-2012-02-03/grammar/englishFactored.ser.gz");
    TokenizerFactory tf = PTBTokenizer.factory(new WordTokenFactory(), "");
    static TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
    int count = 0;
    int subj_count = 0;
    int ad_subj_count = 0;
    int ad_count = 0;
    Collection tdl;
    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    static ArrayList<Node> roots = new ArrayList<Node>();
    static ArrayList<String> nouns = new ArrayList<String>();
    static int timesRun = 0;

    /**
     * This method examines a sentence, finding the Part Of Speech of each word
     * in a sentence, then finds the relationship between each of these words,
     * as well as what the root of a sentence is.
     *
     * @param sentence - the string to be examined
     */
    public void parse(String sentence) {
        try {
            //System.out.println("--------- Start Parsing ---------");
            //			FileInputStream fis = new FileInputStream(filepath);
            //			DataInputStream dis = new DataInputStream(fis);
            //			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            // prepare parser, tokenizer and tree printer
            //System.out.println("Parse sentence: " + sentence);
            if (sentence.trim().length() == 0) {
                return;
            }
            List tokens = tf.getTokenizer(new StringReader(sentence)).tokenize();
            //lp.parse(tokens);
            //t = lp.getBestParse();
            t = (Tree) lp.apply(tokens);
            //System.out.println("penn Structure: ");
            //t.pennPrint();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(t);
            //tdl = gs.typedDependenciesCollapsed();
            tdl = gs.typedDependencies();
            //System.out.println("Relationship: " + tdl);
            //System.out.println("--------- End Parsing --------- " + count);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("count is while exception: " + sentence);
            e.printStackTrace();
        }

    }

    public void showAllDependencies(Collection tdl,
            String tag) {
        boolean added = false;
        boolean ad_added = false;
        boolean has_subj = false;
        count++;
        for (Iterator it = tdl.iterator(); it.hasNext();) {
            TypedDependency td = (TypedDependency) it.next();
            if (td.reln().getLongName().indexOf("subject") != -1) {
                has_subj = true;
                if (!added) {
                    subj_count++;
                    if (tag.equalsIgnoreCase("Action-Directive")) {
                        ad_subj_count++;
                    }
                    added = true;
                }
            }

            if (tag.equalsIgnoreCase("Action-Directive")
                    && !ad_added) {
                ad_count++;
                ad_added = true;
                System.out.println("This utterance is " + tag);
            }
            /*
             * System.out.println("Dependency " + (++count));
             * System.out.println("Dependent: " + td.dep().value());
             * System.out.println("Governor: " + td.gov().value());
             * System.out.println("Relation: " + td.reln().getLongName());
             System.out.println("---------------");
             */
        }
        if (!has_subj) {
            printTree(getTree());
        }
        System.out.println("Total action-directive: " + ad_count);
        System.out.println("has subj: " + ad_subj_count);
        System.out.println("Total utterances: " + count);
        System.out.println("Total subjects of the utterances: " + subj_count);
    }

    public Collection getDependencies() {
        return tdl;
    }

    public Tree getTree() {
        return t;
    }

    public static void printTree(Tree t) {
        tp.printTree(t);
        tp.printTree(t.headTerminal(new CollinsHeadFinder()));//SemanticHeadFinder()));
        //System.out.println("tree label: " + t.label());
        List trees = t.subTreeList();

        for (int i = 0; i < trees.size(); i++) {
            Tree sbt = (Tree) trees.get(i);
            /*
             * if (!sbt.isLeaf()) { trees.addAll(sbt.subTreeList()); }
             */
            //System.out.println("sbt lable: " + sbt.label());
        }
        //System.out.println("done");
        List<Tree> leaves = t.getLeaves();
        for (int i = 0; i < leaves.size(); i++) {
            Tree leaf = leaves.get(i);
            //if (leaf.parent() != null) {
            System.out.println(leaf.pennString() + " " + leaf.value());
            //}
        }
        /*
         * Set dependencies = t.dependencies(); Iterator it =
         * dependencies.iterator(); while (it.hasNext()) { Dependency dependency
         * = (Dependency)it.next(); System.out.println(dependency.toString());
         * System.out.println(dependency.name()); }
         */
    }

    /**
     * This method builds a graph based on the sentence it is given, then sends
     * it to another method to get the noun phrase for it
     *
     * @param sentence - the string being parsed.
     * @param g - an empty graph that will store information from the sentence.
     */
    public void buildDependcyGraph(PrintWriter pw, String sentence, Graph g) {
        parse(sentence);
        if (getTree() == null) {
            g = null;
            return;
        }
        ArrayList<TaggedWord> alWords = getTree().taggedYield();

        //Get relationship
        //String peen=sp.getTree().pennString();
        //fill in the required information(td index is from 1)
//        Graph g = new Graph();
        //test for undirected graph
//        Graph bidirectg = new Graph();
        Collection<TypedDependency> colTD = getDependencies();
        Iterator it = colTD.iterator();
        //String nounPhrase = "";
        while (it.hasNext()) {
            TypedDependency td = (TypedDependency) it.next();

            String govWord = (td.gov()).value();
            //System.out.println("govWord: " + govWord);
            int govId = td.gov().index();
            String govTag = "";
            if (govId == 0) {
                govTag = "";
            } else {
                govTag = alWords.get(govId - 1).tag();
            }

            String depWord = td.dep().value();
            int depId = td.dep().index();
            String depTag = "";

            if (depId == 0) {
                depTag = "";
            } else {
                depTag = alWords.get(depId - 1).tag();
                if (govWord.equals("ROOT")) {
                    roots.add(new Node(depId, depWord, depTag));
                }
            }

            String rel = td.reln().getShortName();

            Node sNode = new Node(govId, govWord, govTag);
            Node dNode = new Node(depId, depWord, depTag);
            //Edge edge = new Edge(sNode, dNode, rel);
            //g.addEdge(edge);
            //updated by CSLin
            g.checkNewEdge(sNode, dNode, rel);
//            bidirectg.checkNewEdge(sNode, dNode, rel);
//            bidirectg.checkNewEdge(dNode, sNode, "reversedRelation");
        }
        //nouns.add(nounPhrase);
        getNounPhrases(pw, g);
        colTD.clear();

//        Node head=bidirectg.getNode("government");
//        Node tail=bidirectg.getNode("veins");
//        List<Node> nodList=bidirectg.shortestPath(head, tail);
//        for(int i=0; i<nodList.size(); i++)
//            System.out.println(nodList.get(i).getName());
//        System.out.print("");
//        return g;
    }

    /**
     * @param targetConcept: e.g "federal bureaucracy"
     * @param highICWord: e.g "home" true: targetConcept is in the
     * sub-sentenceWords outside the highICWord (get rid of this highICWord)
     */
    public boolean checkSubSentence(Graph g, String targetConcept, String highICWord) {
        ArrayList<String> relSubSentence = new ArrayList<String>();
        relSubSentence.add("rcmod");
        //root
        Node nRoot = g.getNode("ROOT");

        //root verb
        ArrayList<Edge> rootedge = (ArrayList<Edge>) (g.outFlow(nRoot));
        Node rootVerb = rootedge.get(0).getChild();

        //node of target concept and high IC word
        Node nTarget = g.getHead(targetConcept);
        Node nHighICWord = g.getNode(highICWord);

        //check if nHighICWord directly links to nTarget
        while (true) {
            ArrayList<Edge> edges = (ArrayList<Edge>) (g.inFlow(nHighICWord));
            Edge edge = edges.get(0);

            Node node = edge.getParent();

            if (node == nTarget) {
                return false;
            } else if (node == rootVerb) {
                break;
            } else {
                nHighICWord = node;
            }
        }

        //check if the nTarget is in the sub-sentenceWords
        nHighICWord = g.getNode(highICWord);

        while (true) {
            ArrayList<Edge> edges = (ArrayList<Edge>) (g.inFlow(nTarget));
            Edge edge = edges.get(0);

            if (relSubSentence.contains(edge.getRelation())) {
                return true;
            }

            Node node = edge.getParent();

            if (node == nHighICWord) {
                return false;
            } else if (node == rootVerb) {
                break;
            } else {
                nTarget = node;
            }
        }

        return false;
    }

    /**
     * This method takes a string input for a file with sentences, then obtains
     * all the sentences from that file.
     *
     * @param doc string that may contain a list of sentences
     * @return ArryList<String> a list of sentences
     */
    public ArrayList<String> sentenceSplitter(String doc) {
        DocumentPreprocessor dp = new DocumentPreprocessor(doc);
        ArrayList<String> sentences = new ArrayList();
        for (List<HasWord> sentenceWords : dp) {
            String sentence = SentenceUtils.listToString(sentenceWords);
            sentences.add(sentence);
            //Out.print(sentence);
        }
        return sentences;
    }

    public static void main(String[] args) throws FileNotFoundException {
        StanfordParser sp_ = new StanfordParser();
        Graph g = new Graph();
        String path = "C:\\research\\rateMyProfessors\\genderReviews\\";
        File dir = new File(path);
        File[] fileList = dir.listFiles();
        for (File file : fileList) {
            File out = new File("C:\\research\\rateMyProfessors\\genderReviewsOutput\\" + file.getName() + ".txt");
            PrintWriter pw = new PrintWriter(out);
            ArrayList<String> sentenceList = sp_.sentenceSplitter(file.getAbsolutePath());
        //sp_.buildDependcyGraph("Joe is a person", g);
            //sp_.sentenceSplitter("C:\\Users\\Logan Brandt\\workstudy\\Project\\sentiment\\ratingExtractor\\data\\test.txt");
            for (int i = 0; i < sentenceList.size(); i++) {
                sp_.buildDependcyGraph(pw, sentenceList.get(i), g);
                g = new Graph();
            }

//        boolean bSubSen = sp_.checkSubSentence(g, "affirmative action", "sore");
            System.out.println();
            pw.close();
        }
    }

    /**
     * This method creates a noun phrase based on the graph of a sentence.
     *
     * @param pw - The printwriter for the file that the data is sent to
     * @param g - the graph of the sentence being examined.
     */
    public static void getNounPhrases(PrintWriter pw, Graph g) {
        System.out.println(timesRun);
        timesRun++;
        ArrayList<Node> temp;
        List<List<Node>> stringArray = new ArrayList<>();
        List<Node> toAdd = new ArrayList<>();
        toAdd.add(roots.get(0));
        stringArray.add(toAdd);

        // This loop prints out what each word of the sentence means.
        for (int i = 0; i < stringArray.size(); i++) {
            for (int j = 0; j < stringArray.get(i).size(); j++) {
                temp = g.getChildren(stringArray.get(i).get(j));
                if (!temp.isEmpty()) {
                    if (stringArray.size() > i + 1) {
                        for (int d = 0; d < temp.size(); d++) {
                            Node comeOn = temp.get(d);
                            stringArray.get(i + 1).add(comeOn);
                        }
                    } else {
                        stringArray.add(temp);
                    }
                }
            }
        }
        Node n;
        ArrayList<Node> children;
        //ArrayList<Node> innerChildren = new ArrayList<Node>();
        ArrayList<String> usedNouns = new ArrayList<>();
        ArrayList<Node> usedWords = new ArrayList<>();
        ArrayList<Node> phrase = new ArrayList<>();
        //This loop goes through and gets a noun phrase, while also making sure it doesn't repeat nouns
        /*for (int k = 0; k < stringArray.size(); k++) {
            toAdd = stringArray.get(k);
            for (int j = 0; j < toAdd.size(); j++) {
                n = toAdd.get(j);
                if (!n.getPos().equals("")) {
                    if (n.getPos().substring(0, 1).equals("N")) {
                        if (!usedNouns.contains(n.getName())) {
                            phrase.add(n);
                            Node parent = g.getParent(n);
                            children = g.getChildren(n);
                            usedNouns.add(n.getName());
                            int r = 0;
                            while (r < children.size()) {
                                Node child = children.get(r);
                                if (child.getPos().substring(0, 1).equals("N")) {
                                    if (usedNouns.contains(child.getName())) {
                                        r++;
                                        continue;
                                    } else {
                                        /*innerChildren = g.getChildren(child);
                                         for(int c = 0; c < innerChildren.size(); c++)
                                         {
                                         Node addTo = innerChildren.get(c);
                                         children.add(addTo);
                                        }
                                        usedNouns.add(child.getName());
                                    }
                                }
                                phrase.add(child);
                                usedWords.add(child);
                                r++;
                            }
                            if (parent != null) {
                                if (!usedWords.contains(parent) && !parent.getName().equals("ROOT")) {
                                    phrase.add(parent);
                                    usedWords.add(parent);
                                }
                            }
                            if (!(phrase.size() < 2 || phrase.size() > 8)) {
                                //sort(pw, phrase);
                            }
                            phrase.clear();
                            children.clear();
                        }
                    }
                }
            }
        }*/
         System.out.println("");
        Edge child;
        Node child2;
        usedNouns.clear();
        ArrayList<Node> nounPhrase = new ArrayList<Node>();
        ArrayList<Edge> children2 = new ArrayList<Edge>();
        //This method prints out the parent nodes and their children, or leaf if they have no children
        for (int k = 0; k < stringArray.size(); k++) {
            toAdd = stringArray.get(k);
            for (int j = 0; j < toAdd.size(); j++) {
                n = toAdd.get(j);
                children2 = g.outFlow(n);
                if (!children2.isEmpty() && n.getPos().substring(0, 1).equals("N")) {
                        if (!usedNouns.contains(n.getName())) {
                        nounPhrase.add(n);
                            usedNouns.add(n.getName());
                                    }
                    for (int y = 0; y < children2.size(); y++) {
                        child = children2.get(y);
                        child2 = child.getChild();
                        if ((child2.getPos().substring(0, 1).equals("N")&&!child.getRelation().contains("obj"))) {
                            nounPhrase.add(child2);
                            usedNouns.add(child2.getName());
                        } else if (!(child2.getPos().substring(0, 1).equals("V")&&!child.getRelation().contains("obj"))) {
                            nounPhrase.add(child2);
                            if (y + 1 == children2.size()) {
                                System.out.println("");
                                }
                            }
                                }
                } else {
                    if (n.getPos().substring(0, 1).equals("N") && !usedNouns.contains(n.getName())) {
                        nounPhrase.add(n);
                            }
                            }
                //System.out.println(nounPhrase);
                        }
            System.out.println("");
                    }
        sort(pw, nounPhrase);
        nounPhrase.clear();

        for (int k = 0; k < stringArray.size(); k++) {
            toAdd = stringArray.get(k);
            for (int j = 0; j < toAdd.size(); j++) {
                n = toAdd.get(j);
                            children = g.getChildren(n);
                if (!children.isEmpty()) {
                    System.out.print("parent: " + n.getName() + " ");
                                    } else {
                    System.out.println("Leaf: " + n.getName() + " ");
                                    }
                for (int y = 0; y < children.size(); y++) {
                    n = children.get(y);
                    System.out.print("child: " + n.getName() + " , ");
                    if (y + 1 == children.size()) {
                        System.out.println("");
                                }
                            }
                                }
            System.out.println("");
                            }
        ArrayList<Edge> e = g.outFlow(roots.get(0));
        for (int i = 0; i < e.size(); i++) {
            System.out.println(e.get(i).getChild().getPos());
                            }
        //clears the array lists, since we are done parsing this sentence.
        roots.clear();
        usedNouns.clear();
        usedWords.clear();
                        }

    public static void sort(PrintWriter pw, ArrayList<Node> nodes) {
        int indexMin;
        Node valMin;
        for (int i = 0; i < (nodes.size() - 1); i++) {
            indexMin = i;
            for (int j = i + 1; j < nodes.size(); j++) {
                if (nodes.get(j).getId() < nodes.get(indexMin).getId()) {
                    indexMin = j;
                }
            }
            valMin = nodes.get(indexMin);
            nodes.set(indexMin, nodes.get(i));
            nodes.set(i, valMin);
        }
        for (int k = 0; k < nodes.size(); k++) {
            pw.print(nodes.get(k).getName().toLowerCase() + " ");
            if (k + 1 == nodes.size()) {
                pw.println("");
            }
        }
        pw.flush();
    }
}
