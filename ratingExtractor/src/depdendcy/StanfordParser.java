package depdendcy;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.*;
import edu.stanford.nlp.parser.lexparser.*;

public class StanfordParser {

    Tree t;
    LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
    //LexicalizedParser lp = new LexicalizedParser("lib/stanford-parser-2012-02-03/grammar/englishFactored.ser.gz");
    TokenizerFactory tf = PTBTokenizer.factory(new WordTokenFactory(), "");
    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
    int count = 0;
    int subj_count = 0;
    int ad_subj_count = 0;
    int ad_count = 0;
    Collection tdl;
    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

    public void parse(String sentence) {
        try {
            System.out.println("--------- Start Parsing ---------");
            //			FileInputStream fis = new FileInputStream(filepath);
            //			DataInputStream dis = new DataInputStream(fis);
            //			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            // prepare parser, tokenizer and tree printer
            System.out.println("Parse sentence: " + sentence);
            if (sentence.trim().length() == 0) {
                return;
            }
            List tokens = tf.getTokenizer(new StringReader(sentence)).tokenize();
            //lp.parse(tokens);
            //t = lp.getBestParse();
            t = (Tree) lp.apply(tokens);
            System.out.println("penn Structure: ");
            t.pennPrint();
            GrammaticalStructure gs = gsf.newGrammaticalStructure(t);
            //tdl = gs.typedDependenciesCollapsed();
            tdl = gs.typedDependencies();
            System.out.println("Relationship: " + tdl);
            System.out.println("--------- End Parsing --------- " + count);
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

    public void printTree(Tree t) {
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

    public void buildDependcyGraph(String sentence, Graph g) {
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
        while (it.hasNext()) {
            TypedDependency td = (TypedDependency) it.next();

            String govWord = (td.gov()).value();
            System.out.println("govWord: " + govWord);
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
     * @param highICWord: e.g "home" true: targetConcept is in the sub-sentence
     * outside the highICWord (get rid of this highICWord)
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

        //check if the nTarget is in the sub-sentence
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

    public static void main(String[] args) {
        StanfordParser sp_ = new StanfordParser();
        Graph g = new Graph();
        sp_.buildDependcyGraph("Love her flipped style teaching ", g);
//        boolean bSubSen = sp_.checkSubSentence(g, "affirmative action", "sore");
        System.out.println();
    }
}
