package parsertest;

//****************************************************************************
//* Program Name: Wordnet.java
//* Programmer: Ting Liu
//* Date: May 12, 2005
//* Purpose: Find the relationship amoung words in a passage.
//* Note:    Uses JWNL, java wordnet library.
//* Updated: Summer 2019, added Antonym getter, -Code Monkey A & Code Monkey B
//****************************************************************************

//import edu.albany.ils.remnd.source.SourceClusterGeneric;
import net.didion.jwnl.*;
import net.didion.jwnl.data.*;
import net.didion.jwnl.data.relationship.*;
import net.didion.jwnl.data.list.*;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.dictionary.*;
import java.util.TreeMap;
import java.io.FileInputStream;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.regex.*;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wordnet implements Serializable {

    private Dictionary dico;
    private MorphologicalProcessor mph_;
    //int num = 0;
    BufferedWriter outputFile;

    public Wordnet() {
        try {
            //System.out.println("flag1");
//            JWNL.initialize(new FileInputStream("conf/file_properties.xml"));
            JWNL.initialize(new FileInputStream("/home/thomas/NetBeansProjects/ParserTest/src/parsertest/file_properties3.0.xml"));

        } catch (JWNLException je) {
            je.printStackTrace();
            System.out.println(je);
        } catch (java.io.FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            System.out.println(fnfe);
        }
        dico = Dictionary.getInstance();
        if (dico == null) {
            System.out.println("dico is null!!!!!!!!!!!!!!!");
        }
        mph_ = dico.getMorphologicalProcessor();
        //initBadSenses();
//        System.out.println(this.pu_);
    }

    public void setDictionary(Dictionary _dico) {
        dico = _dico;
    }

    public void setPU(PointerUtils pu) {
        pu_ = pu;
    }

    public boolean isASynonym(String word1, String word2, String pos) {
        //System.out.println("***************Checking: " + word1 + " " + word2);
        word1 = word1.replaceAll(" ", "_");
        word2 = word2.replaceAll(" ", "_");
        if (isSynonym(word1, word2, pos)) {
            //System.out.println("true");
            return true;
        }
//        if (isSynonym(word2, word1, pos)) {
//            //System.out.println("true");
//            return true;
//        }
        return false;
    }

    /**
     * m2w: this method is not functioning
     *
     * @param phrase
     * @return
     * @deprecated
     */
    public String getLemma(String phrase) {
        IndexWordSet iws = lookupAllIndexWords(phrase);
        if (iws == null) {
            return null;
        }
        if (iws.getIndexWord(NOUN) == null || iws.getIndexWord(VERB) == null) {
            return null;
        }
        if (iws.getIndexWord(NOUN) != null) {
            return iws.getIndexWord(NOUN).getLemma();
        }
        if (iws.getIndexWord(VERB) != null) {
            return iws.getIndexWord(VERB).getLemma();
        }
        return null;
    }

    public String getLemma(String phrase, String pos) {
        ArrayList pair = new ArrayList();
        pair.add(phrase);
        pair.add(pos);
        if (lemma_.keySet().contains(pair)) {
            return (String) lemma_.get(pair);
        }
        IndexWordSet iws = lookupAllIndexWords(phrase);
        if (iws == null) {
            return null;
        }
        if (pos.equals("noun")) {
            if (iws.getIndexWord(NOUN) == null) {
                return null;
            }
            String lemma = iws.getIndexWord(NOUN).getLemma();
            lemma_.put(pair, lemma);
            return lemma;
        }
        if (pos.equals("verb")) {
            if (iws.getIndexWord(POS.VERB) == null) {
                return null;
            }
            String lemma = iws.getIndexWord(POS.VERB).getLemma();
            lemma_.put(pair, lemma);

            return lemma;
        }
        return phrase;
    }

    public boolean isSynonym(Synset sense1, String word2) {
        ArrayList senses = getSenses(word2, sense1.getPOS());
        for (int i = 0; i < senses.size(); i++) {
            Synset sense2 = (Synset) senses.get(i);
            if (isSynonym(sense1, sense2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSynonym(Synset sense1, Synset sense2) {
        if (sense1.equals(sense2)) {
            return true;
        }
        return false;
    }

    public boolean isSynonym(String word1, String word2, String flag) {
        //IF EQUAL RETURN TRUE
        String word1_bf = this.getBaseForm(word1, flag);
        ArrayList<Synset> synonyms = getSynonym(word2, flag);
//        	System.out.println("The synonyms of " + word2 + " are: " + synonyms);
        if (synonyms != null) {
            for (Synset s : synonyms) {
                if (s.containsWord(word1_bf)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSynonym(String word1, String word2) {
        //IF EQUAL RETURN TRUE
        ArrayList synonyms = getSynonymAllSenses(word2);
        ArrayList synonymsOfWord1 = getSynonymAllSenses(word1);
//	System.out.println("The synonyms of " + word2 + " are: " + synonyms);
        if (synonyms != null || synonymsOfWord1 != null) {
            if (synonyms.contains(word1)) {
                return true;
            }
            if (synonymsOfWord1.contains(word2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSynonym(String word1, String word2, String flag, int arrange) {
        //IF EQUAL RETURN TRUE
        ArrayList synonyms = getSynonym(word2, flag, arrange);
        //	System.out.println("The synonyms of " + word2 + " are: " + synonyms);
        if (synonyms != null) {
            if (synonyms.contains(word1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSynonymAllSenses(String word1, String word2, String flag) {
        //IF EQUAL RETURN TRUE
        ArrayList synonyms = getSynonymAllSenses(word2, flag);
        //	System.out.println("The synonyms of " + word2 + " are: " + synonyms);
        if (synonyms != null) {
            if (synonyms.contains(word1)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSenses(Synset[] ss1, String word) throws JWNLException {
        for (int i = 0; i < ss1.length; i++) {
            Synset ss2 = ss1[i];
            //	    System.out.println("The synset is: " + ss2.toString());
            if (ss2.containsWord(word)) {
                return true;
            }
            /*
             * PointerTarget[] pts1 = ss2.getTargets(PointerType.HYPERNYM); //IF
             * ONE OF THE WORDS SYNOYNMS IS THE OTHER WORD RETURN TRUE if
             * (checkPointers(pts1,word)) return true;
             */
        }
        return false;
    }

    private boolean checkPointers(PointerTarget[] pt1, String word)
            throws JWNLException {
        for (int i = 0; i < pt1.length; i++) {
            //System.out.println("target: " + pt1[i].toString());
            PointerTargetNode ptn1 = new PointerTargetNode(pt1[i]);
            Synset ssl = ptn1.getSynset();
            //System.out.println("synset: " + ssl);

            if (ssl.containsWord(word)) {
                //System.out.println("return true");
                return true;
            }
        }
        return false;
    }

    public IndexWord lookupIndexWord(POS pos, String phrase) {
        try {
            //	    String[] words = phrase.split("[-_ ]+");
            //System.out.println("pos: " + pos + " ----- phrase: " + phrase);
            if (dico == null) {
                System.out.println("dico is null!!!!!!!!!!!!!!!!!!!!!!!");
            }
            if (pos == null || phrase == null) {
                return null;
            }
            IndexWord iw = dico.lookupIndexWord(pos, phrase);
//            System.out.println(iw);
            /*
             * if (iw == null) { return iw; } String lem = iw.getLemma();
             * String[] lem_words = lem.split("[-_ ]+"); if (lem_words.length !=
             * words.length) { return null; }
             */
            return iw;
        } catch (net.didion.jwnl.JWNLException jwnlex) {
            jwnlex.printStackTrace();
        }
        return null;
    }

    public List lookupAllNounBaseForms(String phrase) {
        try {
            //	    String[] words = phrase.split("[-_ ]+");
            if (base_forms_.containsKey(phrase)) {
                return base_forms_.get(phrase);
            }
            List bfs = mph_.lookupAllBaseForms(POS.NOUN, phrase);
            base_forms_.put(phrase, bfs);
            //System.out.println("bfs: " + bfs);
	    /*
             * if (iw == null) { return iw; } String lem = iw.getLemma();
             * String[] lem_words = lem.split("[-_ ]+"); if (lem_words.length !=
             * words.length) { return null; }
             */
            return bfs;
        } catch (net.didion.jwnl.JWNLException jwnlex) {
            jwnlex.printStackTrace();
        }
        return null;
    }

    public List lookupAllBaseForms(String phrase, String pos) {
        try {
            //	    String[] words = phrase.split("[-_ ]+");
            POS _pos = POS.getPOSForLabel(pos);
            //System.out.println("POS: " + _pos);
            List bfs = mph_.lookupAllBaseForms(_pos, phrase);
            //System.out.println("baseform of " + phrase + " is " + bfs);
            return bfs;
        } catch (net.didion.jwnl.JWNLException jwnlex) {
            jwnlex.printStackTrace();
        }
        return null;
    }

    public String getBaseForm(String phrase, String pos) {
        String bf = null;
        if (baseforms_.containsKey(phrase+"_"+pos)) {
            return baseforms_.get(phrase+"_"+pos);
        }
        List bfs = lookupAllBaseForms(phrase, pos);
        //System.out.println(phrase + " baseform is: " + bfs.toString());
        if (bfs == null || bfs.size() == 0) {
            baseforms_.put(phrase+"_"+pos, null);
            return null;
        } else {
            baseforms_.put(phrase+"_"+pos, (String)bfs.get(0));
            return (String) bfs.get(0);
        }
    }

    public ArrayList<String> getBaseForms(ArrayList<String> phrases, String pos) {
        ArrayList bfs = new ArrayList();
        for (String phrase : phrases) {
            String bf = getBaseForm(phrase.trim().toLowerCase(), pos);
            if (bf != null && !bfs.contains(bf)) {
                bfs.add(bf);
            } else if (bf == null && !bfs.contains(phrase.trim().toLowerCase())) {
                bfs.add(phrase.trim().toLowerCase());
            }
        }
        return bfs;
    }

    public ArrayList getVerbFrame(String phrase, String pos) {
        ArrayList vfs = new ArrayList();
        POS pos1 = this.getPOS(pos, "1");
        Synset phr_syn1 = this.getFirstSense(phrase, pos1);
        ArrayList<Synset> phr_syns = this.getSenses(phrase, pos1);
        for (Synset phr_syn : phr_syns) {
            System.out.println("phr_syn: " + phr_syn);
            net.didion.jwnl.data.Word[] rel_words = (net.didion.jwnl.data.Word[]) phr_syn.getWords();
            for (net.didion.jwnl.data.Word rel_word : rel_words) {
                if (rel_word.getLemma().equals("graduate")) {
                    vfs.add("Somebody ----s");
                }
//                System.out.println("rel_word's vframe: ");
//                for (String vframe : ((Verb) rel_word).getVerbFrames()) {
//                    System.out.println(vframe);
//                }
            }
            vfs.addAll(Arrays.asList(phr_syn.getVerbFrames()));
            System.out.println("phr_syn's verb frames: " + vfs);
        }
        return vfs;
    }

    public IndexWordSet lookupAllIndexWords(String phrase) {
        try {

            IndexWordSet iws = null;
            //	    System.out.println("The iws of United States are:\n" + iws);
            //	    System.exit(0);
            //	    phrase = phrase.replaceAll("-", "_");
            //	    String[] words = phrase.split("[-_ ]+");
            iws = dico.lookupAllIndexWords(phrase);
            //System.out.println("iws: " + iws.toString());
	    /*
             * if (iws == null) { return null; } IndexWord[] iwa =
             * iws.getIndexWordArray(); for (int i = 0; i < iwa.length; i++) {
             * IndexWord iw = iwa[i]; String lem = iw.getLemma(); String[]
             * lem_words = lem.split("[-_ ]+"); if (lem_words.length !=
             * words.length) { iws.remove(iw.getPOS()); } }
             */
            return iws;
        } catch (net.didion.jwnl.JWNLException jwnlex) {
            jwnlex.printStackTrace();
        }
        return null;
    }

    public Dictionary getDictionary() {
        return dico;
    }

    public void testRelations() {
        try {

            String test_s = "(aksdjfa)aksdfkajsdf";
            //	    System.out.println("@#@#@#@#@@#@#@##@@@@@@@@@@@@@@@@@@@@#######################");
            System.out.println(test_s.replaceAll("\\(.+\\)", ""));
            IndexWord test1 = lookupIndexWord(POS.NOUN, "flower");
            if (test1 != null) {
                Synset[] ss1s = test1.getSenses();
                IndexWord test2 = lookupIndexWord(POS.NOUN, "rose");
                if (test2 != null) {
                    Synset[] ss2s = test2.getSenses();
                    for (int i = 0; i < ss1s.length; i++) {
                        Synset ss1 = ss1s[i];
                        for (int j = 0; j < ss2s.length; j++) {
                            Synset ss2 = ss2s[j];
                            System.out.println("$$$$$$$$$$$$$$$$$the synset of Albany is: " + ss1.toString());
                            System.out.println("$$$$$$$$$$$$$$$$$the synset of Tampa is: " + ss2.toString());
                            getRelationship(ss1, ss2, "noun");
                        }
                    }
                }
                //		System.out.println("^^^^^^^^^^^^^^^^^^^" + test1 + " " + test2 + " have relations? " + relates);
            }
        } catch (JWNLException jwnle) {
            System.out.println("Exception in Wordnet: " + jwnle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //***********************************************GETRELATIONSHIP
    //find relationship among sysnets
    public void getRelationship(Synset ss1, Synset ss2, String flag) throws JWNLException {
        System.out.println("in rel");
        RelationshipList RL1;
        RelationshipFinder rf = RelationshipFinder.getInstance();
        RL1 = rf.findRelationships(ss1, ss2, PointerType.HYPERNYM);
        int test = RL1.size();
        System.out.println(test);
        for (Object r : RL1) {
            System.out.println((Relationship) r);
        }

        if (test > 0) {
            Relationship rshipdeep = RL1.getDeepest();
            Relationship rshipshallow = RL1.getShallowest();

            int i = rshipdeep.getSize();
            int j = ((AsymmetricRelationship) rshipdeep).getCommonParentIndex();

            for (int w = 0; w < test; w++) {
                Relationship rsh = (Relationship) RL1.get(w);
                int comm = ((AsymmetricRelationship) rsh).getCommonParentIndex();
                PointerTargetNodeList ptnl = rsh.getNodeList();
                PointerTargetNode ptn = (PointerTargetNode) ptnl.get(comm);

                //		System.out.println("\nThe node list size for " + w + "th relation is: " + ptnl.size());
                //		System.out.println("The type of this relation is: " + rsh.getType().toString());
                //		System.out.println("Is this relationship symmetric: " + rsh.getType().isSymmetric());
                //		System.out.println("The common parent index is: " + comm +
                //				   "\nThe common parent is: " + ptn.getSynset().getWord(0).getLemma());

                for (int k = 0; k < ptnl.size(); k++) {
                    ptn = (PointerTargetNode) ptnl.get(k);
                    if (ptn != null) {
                        net.didion.jwnl.data.Word[] words = ptn.getSynset().getWords();
                        System.out.print("The offset is: " + ptn.getSynset().getOffset() + " and the words in the synset are: ");
                        for (int kk = 0; kk < words.length; kk++) {
                            System.out.print(words[kk].getLemma() + " " + words[kk].getIndex() + ", ");
                        }
                        //			System.out.println();
                    }
                }
            }

            PointerTargetNodeList ptnl = rshipshallow.getNodeList();
            PointerTargetNode ptn = null;//(PointerTargetNode)ptnl.get(j);

            //	    System.out.println("\n\nThe node list size of shallowest relation is: " + ptnl.size());

            for (int k = 0; k < ptnl.size(); k++) {
                ptn = (PointerTargetNode) ptnl.get(k);
                if (ptn != null) {
                    System.out.println(ptn.getSynset().getWord(0).getLemma());
                }
            }


            ptnl = rshipdeep.getNodeList();

            System.out.println("\n\nThe node list size of deepest relation is: " + ptnl.size());

            for (int k = 0; k < ptnl.size(); k++) {
                ptn = (PointerTargetNode) ptnl.get(k);
                if (ptn != null) {
                    System.out.println(ptn.getSynset().getWord(0).getLemma());
                }
            }


            /*
             * Synset ssc = ptn.getSynset(); int size = ssc.getWordsSize();
             *
             * Word common1 = ssc.getWord(0); String word = new
             * String(common1.getLemma());
             *
             * //remove some concepts to base
             *
             * if (flag == "noun"){ if ((stopConcepts.contains(word)) ||
             * stemmer_.isStopWord(word)){ } else{ //System.out.println("noun
             * relationship: " + word); nounRelates.add(word); } } if (flag ==
             * "verb"){ if (stopConcepts.contains(word)){
             *
             * }
             * else{ //System.out.println("verb relationship: " + word);
             * verbRelates.add(word); } }
             */
        } else {
            System.out.println("empty");
        }
    }

    public LinkedList sortMap(TreeMap nounConcepts) {
        Set keySet = nounConcepts.keySet();
        Iterator it = keySet.iterator();
        String phrase;
        Integer count;
        ArrayList entry = new ArrayList();
        Integer highest = new Integer(0);
        Integer found = new Integer(0);

        LinkedList ll = new LinkedList();

        //System.out.println("*******Tree Map: " + nounConcepts.toString());

        if (!it.hasNext()) {
            return ll;
        }
        phrase = (String) (it.next());
        count = (Integer) (nounConcepts.get(phrase));

        //always start by ading the first phrase
        entry.add(count);
        entry.add(phrase);
        ll.add(entry);

        //then begin from the second
        while (it.hasNext()) {
            phrase = (String) (it.next());
            count = (Integer) (nounConcepts.get(phrase));
            entry = new ArrayList();
            entry.add(count);
            entry.add(phrase);
            boolean inserted = false;

            //loop through the linked list to find where to place this passage
            for (int h = 0; h < ll.size(); h++) {
                //get the element in the list
                ArrayList checkit = (ArrayList) ll.get(h);
                //IF THE SIZE OF THE ONE WE ARE LOOKING TO INSERT IS
                //LESS OR EQUAL TO THE CURRENT ELEMENT,
                //THEN INSERT AND BREAK THE FOR LOOP, ELSE CONTINUE
                //Integer countInt = new Integer(count);
                Integer newInt = (Integer) checkit.get(0);
                //System.out.println("count: " + count);
                //System.out.println("newInt: " + newInt);
                //System.out.println(count.compareTo(newInt));
                if (newInt.compareTo(count) <= 0) {
                    //want to add it behind longer phrases
                    ll.add(h, entry);
                    //System.out.println("LinkedList After: " + ll.toString());
                    inserted = true;
                    break;
                }
            }

            //if we went through whole list and haven't inserted then insert at end
            if (!inserted) {
                ll.add(entry);
            }
        }
        //System.out.println("LinkedList After: " + ll.toString());

        return ll;
    }

    private String stripURLs(String input) {

        //common URL format
        //System.out.println("***INPUT: " + input);
        String output =
                Pattern.compile("http:.*( |\\.)").matcher(input).replaceAll("");

        //System.out.println("OUTPUT: " + output);
        return output;
    }

    //converts an arraylist of strings where strings are
    //phrases separated by commas to one string
    private String concatInput(ArrayList input) {

        String list = new String();

        for (int i = 0; i < input.size(); i++) {
            list = list + " " + (String) input.get(i);
        }

        return list;
    }

    //***********************************************GETRELATIONSHIP
    //find relationship among sysnets
    public boolean getRelationships(Synset ss1, Synset ss2, IndexWord iw1, IndexWord iw2,
            String flag) throws JWNLException, IOException {
        RelationshipList RL1;
        RelationshipFinder rf = RelationshipFinder.getInstance();


        int i = rf.getImmediateRelationship(iw1, iw2);
        //System.out.println("%%%Int: " + i);

        RL1 = rf.findRelationships(ss1, ss2, PointerType.HYPERNYM, 10);
        RelationshipList RL2 = rf.findRelationships(ss1, ss2, PointerType.ENTAILMENT, 10);
        int test = RL1.size();

        if (test > 0) {
            Relationship shallow = RL1.getShallowest();
            Relationship deepest = RL1.getDeepest();
//            System.out.println("shallow: " + shallow.toString());
//            System.out.println("entail: " + deepest.toString());
//            System.out.println("RL: " + RL1.getDeepest());
            System.out.println("RL: " + RL1.getShallowest());
            System.out.println("shallow depth: " + shallow.getDepth());
            System.out.println("deep depth: " + deepest.getDepth());
            System.out.println("relation list size: " + test);
            if (shallow.getDepth() < 4) {
                return true;
            }

        }
        return false;
    }

    //**********************************************getSynsets
    // getSynsets - get the number of synsets this word/phrase has
    //
    public int getSynsets(String text, String pos) throws JWNLException {

        IndexWord base;

        if (pos == "noun") {
            base = lookupIndexWord(POS.NOUN, text);
        } else {
            base = lookupIndexWord(POS.VERB, text);
        }

        //double check sense count

        if (base != null) {
            return base.getSenseCount();
        }
        //needed in case word is not in wordnet
        return 100;

    }

    //************************************************checkabstraction
    //checkAbstraction - return boolean if word is abstract
    public boolean checkAbstraction(String word, String pos) throws JWNLException {

        IndexWord wordIndex, abstractIndex;
        Synset wordSynset, abstractSynset;
        PointerTargetTree ptt, ptta;
        PointerTargetTreeNode found;
        boolean flag = false;
        PointerTargetNode ptn, ptn2;
        PointerUtils pu = PointerUtils.getInstance();

        wordIndex = null;
        if (pos == "noun") {
            wordIndex = lookupIndexWord(POS.NOUN, word);
        } else if (pos == "verb") {
            wordIndex = lookupIndexWord(POS.VERB, word);
        } else if (pos == "adjective") {
            wordIndex = lookupIndexWord(POS.ADJECTIVE, word);
        }
        //word not in wordnet
        if (wordIndex == null) {
            return false;
        }

        int size = wordIndex.getSenseCount();
        //System.out.println("This is size: " + size);

        for (int i = 1; i <= size; i++) {

            wordSynset = wordIndex.getSense(i);
            ptt = pu.getHypernymTree(wordSynset);

            //System.out.println("This is ptt: " + ptt.toString());

            PointerTargetNodeList[] pttnlw = ptt.reverse();
            Iterator it = pttnlw[0].iterator(); //abstraction

            while (it.hasNext()) {
                ptn = (PointerTargetNode) it.next(); //abstraaction
                //System.out.println("*************NEW WORD");
                //System.out.println("This is ptn: " + ptn.toString());

                Synset wss = ptn.getSynset();
                long wsslong = wss.getOffset();
                if (wsslong == 13018 || //abstraction
                        wsslong == 12865) {            //phsycological
                    //System.out.println("TRUE$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                    return true;
                }

            }
        }
        return false;
    }
    String gatepathname = new String("file:/tmp/Seed.html");
    String pathname = new String("/tmp/Seed.html");
    ArrayList stopConcepts;

    //=======================================START OF TINGS WORDPROCESSOR CLASSES
    //FROM HERE TO THE END OF FILE
    public ArrayList getSynonym(String word, String pos) {
        if (pos.equals("noun") && n_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) n_synonym_list_.get(word);
            return (ArrayList) synonyms.clone();
        } else if (pos.equals("adjective") && a_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) a_synonym_list_.get(word);
            return (ArrayList) synonyms.clone();
        } else if (pos.equals("verb") && v_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) v_synonym_list_.get(word);
            return (ArrayList) synonyms.clone();
        }

        ArrayList list = new ArrayList();
        try {
            POS _pos = POS.getPOSForLabel(pos);
            PointerTargetNodeList synonym;

            if (_pos != null) {
                //System.out.println("_pos is not null");
                IndexWord iw = lookupIndexWord(_pos, word);
                if (iw == null) {
                    return list;
                }
                //System.out.println("flag");
                Synset[] synsets = iw.getSenses();
                for (int i = 0; (i < synsets.length) && (i < 2); i++) //use the first sense
                {
                    Synset synset = synsets[i];
                    //if (isBadSense(synset)) {
                    //continue;
                    //}
                    synonym = pu_.getSynonyms(synset);
                    if (synonym.isEmpty()) {
                        continue;
                        //  			    synonym = pu_.getDirectHypernyms(synset);
                    }
                    Set set = getLemmas(synonym);

                    if (set != null) {
                        System.out.println("getLemmas got null");
                        list.addAll(set);
                        break;
                    }
                }
            } else {
                IndexWordSet iws = lookupAllIndexWords(word);
                IndexWord[] iw_array = iws.getIndexWordArray();

                for (int j = 0; j < iw_array.length; j++) {
                    IndexWord iw = iw_array[j];
                    _pos = iw.getPOS();
                    Synset[] synsets = iw.getSenses();
                    for (int i = 0; i < synsets.length; i++) {
                        Synset synset = synsets[i];
                        if (isBadSense(synset)) {
                            continue;
                        }
                        synonym = pu_.getSynonyms(synset);
                        if (synonym.isEmpty()) {
                            synonym = pu_.getDirectHypernyms(synset);
                        }
                        Set set = getLemmas(synonym);
                        if (set != null) {
                            list.addAll(set);
                        }
                        //			    else System.out.println("getLemmas got null");
                    }
                }
            }


        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        //System.out.println("**********");
        //System.out.println("'"+word+"'s"+" Synonym List: "+list);
        if (list != null && !list.isEmpty()) {
            if (pos.equals("noun")) {
                n_synonym_list_.put(word, list);
            } else if (pos.equals("adjective")) {
                a_synonym_list_.put(word, list);
            } else if (pos.equals("verb")) {
                v_synonym_list_.put(word, list);
            }
        }
        return (ArrayList) list.clone();
    }

    public ArrayList getSynonymNoBS(String word, String pos) {
        ArrayList list = new ArrayList();
        try {
            POS _pos = POS.getPOSForLabel(pos);
            PointerTargetNodeList synonym;

            if (_pos != null) {
                //		System.out.println("_pos is not null");
                IndexWord iw = lookupIndexWord(_pos, word);
                if (iw == null) {
                    return list;
                }
                Synset[] synsets = iw.getSenses();
                for (int i = 0; (i < synsets.length) && (i < 1); i++) //use the first sense
                {
                    Synset synset = synsets[i];
                    if (isBadSense(synset)) {
                        continue;
                    }
                    synonym = pu_.getSynonyms(synset);
                    if (synonym.isEmpty()) {
                        continue;
                        //			    synonym = pu_.getDirectHypernyms(synset);
                    }
                    Set set = getLemmas(synonym);

                    if (set != null) {
                        //			    System.out.println("getLemmas got null");
                        list.addAll(set);
                        break;
                    }
                }
            } else {
                IndexWordSet iws = lookupAllIndexWords(word);
                IndexWord[] iw_array = iws.getIndexWordArray();

                for (int j = 0; j < iw_array.length; j++) {
                    IndexWord iw = iw_array[j];
                    _pos = iw.getPOS();
                    Synset[] synsets = iw.getSenses();
                    for (int i = 0; i < synsets.length; i++) {
                        Synset synset = synsets[i];
                        if (isBadSense(synset)) {
                            continue;
                        }
                        synonym = pu_.getSynonyms(synset);
                        if (synonym.isEmpty()) {
                            synonym = pu_.getDirectHypernyms(synset);
                        }
                        Set set = getLemmas(synonym);
                        if (set != null) {
                            list.addAll(set);
                        }
                        //			    else System.out.println("getLemmas got null");
                    }
                }
            }


        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        return (ArrayList) list.clone();
    }

    public ArrayList getSynonym(String word, String pos, int arrange) {
        if (pos.equals("noun") && n_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) n_synonym_list_.get(word);
            return synonyms;
        } else if (pos.equals("adjective") && a_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) a_synonym_list_.get(word);
            return synonyms;
        } else if (pos.equals("verb") && v_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) v_synonym_list_.get(word);
            return synonyms;
        }

        ArrayList list = new ArrayList();
        try {
            POS _pos = POS.getPOSForLabel(pos);
            PointerTargetNodeList synonym;

            if (_pos != null) {
                //		System.out.println("_pos is not null");
                IndexWord iw = lookupIndexWord(_pos, word);
                if (iw == null) {
                    return list;
                }
                Synset[] synsets = iw.getSenses();
                for (int i = 0; (i < synsets.length) && (i < arrange); i++) //use the first sense
                {
                    Synset synset = synsets[i];
                    if (isBadSense(synset)) {
                        continue;
                    }
                    synonym = pu_.getSynonyms(synset);
                    if (synonym.isEmpty()) {
                        continue;
                        //			    synonym = pu_.getDirectHypernyms(synset);
                    }
                    Set set = getLemmas(synonym);

                    if (set != null) {
                        //			    System.out.println("getLemmas got null");
                        list.addAll(set);
                        break;
                    }
                }
            } else {
                IndexWordSet iws = lookupAllIndexWords(word);
                IndexWord[] iw_array = iws.getIndexWordArray();

                for (int j = 0; j < iw_array.length; j++) {
                    IndexWord iw = iw_array[j];
                    _pos = iw.getPOS();
                    Synset[] synsets = iw.getSenses();
                    for (int i = 0; i < synsets.length; i++) {
                        Synset synset = synsets[i];
                        synonym = pu_.getSynonyms(synset);
                        if (synonym.isEmpty()) {
                            synonym = pu_.getDirectHypernyms(synset);
                        }
                        Set set = getLemmas(synonym);
                        if (set != null) {
                            list.addAll(set);
                        }
                        //			    else System.out.println("getLemmas got null");
                    }
                }
            }


        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        //System.out.println("**********");
        //System.out.println("'"+word+"'s"+" Synonym List: "+list);
        if (list != null
                && list.size() != 0) {
            if (pos.equals("noun")) {
                n_synonym_list_.put(word, list);
            } else if (pos.equals("adjective")) {
                a_synonym_list_.put(word, list);
            } else if (pos.equals("verb")) {
                v_synonym_list_.put(word, list);
            }
        }
        return list;
    }

    public ArrayList getHiSynonym(String word, String pos) {
        ArrayList list = new ArrayList();
        try {
            POS _pos = POS.getPOSForLabel(pos);
            PointerTargetNodeList synonym;

            if (_pos != null) {
                //		System.out.println("_pos is not null");
                IndexWord iw = lookupIndexWord(_pos, word);
                if (iw == null) {
                    return list;
                }
                Synset[] synsets = iw.getSenses();
                for (int i = 0; (i < synsets.length) && (i < 1); i++) //use the first sense
                {
                    Synset synset = synsets[i];
                    if (isBadSense(synset)) {
                        continue;
                    }
                    synonym = pu_.getSynonyms(synset);
                    if (synonym.isEmpty()) {
                        continue;
                        //			    synonym = pu_.getDirectHypernyms(synset);
                    }
                    Set set = getLemmas(synonym);

                    if (set != null) {
                        //			    System.out.println("getLemmas got null");
                        list.addAll(set);
                        break;
                    }
                }
            } else {
                IndexWordSet iws = lookupAllIndexWords(word);
                IndexWord[] iw_array = iws.getIndexWordArray();

                for (int j = 0; j < iw_array.length; j++) {
                    IndexWord iw = iw_array[j];
                    _pos = iw.getPOS();
                    Synset[] synsets = iw.getSenses();
                    for (int i = 0; i < synsets.length; i++) {
                        Synset synset = synsets[i];
                        if (isBadSense(synset)) {
                            continue;
                        }
                        synonym = pu_.getSynonyms(synset);
                        if (synonym.isEmpty()) {
                            synonym = pu_.getDirectHypernyms(synset);
                        }
                        Set set = getLemmas(synonym);
                        if (set != null) {
                            list.addAll(set);
                        }
                        //			    else System.out.println("getLemmas got null");
                    }
                }
            }


        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        //System.out.println("**********");
        //System.out.println("'"+word+"'s"+" Synonym List: "+list);
        if (list != null
                && list.size() != 0) {
            if (pos.equals("noun")) {
                n_synonym_list_.put(word, list);
            } else if (pos.equals("adjective")) {
                a_synonym_list_.put(word, list);
            } else if (pos.equals("verb")) {
                v_synonym_list_.put(word, list);
            }
        }
        return list;
    }

    public ArrayList getSynsetAllSenses(String word, String pos) {
        if (pos.equals("noun") && n_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) n_synonym_list_.get(word);
            return synonyms;
        } else if (pos.equals("adjective") && a_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) a_synonym_list_.get(word);
            return synonyms;
        }

        ArrayList list = new ArrayList();
        try {
            POS _pos = POS.getPOSForLabel(pos);
            PointerTargetNodeList synonym;

            if (_pos != null) {
                //		System.out.println("_pos is not null");
                IndexWord iw = lookupIndexWord(_pos, word);
                if (iw == null) {
                    return list;
                }
                Synset[] synsets = iw.getSenses();

                for (int i = 0; i < synsets.length; i++) //use the first sense
                {
                    Synset synset = synsets[i];
                    if (synset != null) {
                        //			    System.out.println("getLemmas got null");
                        list.add(synset);
                    }
                }
            } else {
                IndexWordSet iws = lookupAllIndexWords(word);
                IndexWord[] iw_array = iws.getIndexWordArray();

                for (int j = 0; j < iw_array.length; j++) {
                    IndexWord iw = iw_array[j];
                    _pos = iw.getPOS();
                    Synset[] synsets = iw.getSenses();
                    for (int i = 0; i < synsets.length; i++) {
                        Synset synset = synsets[i];
                        if (synset != null) {
                            list.add(synset);
                        }
                        //			    else System.out.println("getLemmas got null");
                    }
                }
            }


        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        //System.out.println("**********");
        //System.out.println("'"+word+"'s"+" Synonym List: "+list);
        if (list != null
                && list.size() != 0) {
            if (pos.equals("noun")) {
                n_synonym_list_.put(word, list);
            } else if (pos.equals("adjective")) {
                a_synonym_list_.put(word, list);
            }
        }
        return list;
    }

    public ArrayList getSynonymAllSenses(String word, String pos) {
        if (pos.equals("noun") && n_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) n_synonym_list_.get(word);
            return synonyms;
        } else if (pos.equals("adjective") && a_synonym_list_.containsKey(word)) {
            ArrayList synonyms = (ArrayList) a_synonym_list_.get(word);
            return synonyms;
        }

        ArrayList list = new ArrayList();
        try {
            POS _pos = POS.getPOSForLabel(pos);
            PointerTargetNodeList synonym;

            if (_pos != null) {
                //		System.out.println("_pos is not null");
                IndexWord iw = lookupIndexWord(_pos, word);
                if (iw == null) {
                    return list;
                }
                Synset[] synsets = iw.getSenses();

                for (int i = 0; i < synsets.length; i++) //use the first sense
                {
                    Synset synset = synsets[i];
                    if (isBadSense(synset)) {
                        continue;
                    }
                    synonym = pu_.getSynonyms(synset);
                    if (synonym.isEmpty()) {
                        synonym = pu_.getDirectHypernyms(synset);
                    }
                    Set set = getLemmas(synonym);

                    if (set != null) {
                        //			    System.out.println("getLemmas got null");
                        list.addAll(set);
                    }
                }
            } else {
                IndexWordSet iws = lookupAllIndexWords(word);
                IndexWord[] iw_array = iws.getIndexWordArray();

                for (int j = 0; j < iw_array.length; j++) {
                    IndexWord iw = iw_array[j];
                    _pos = iw.getPOS();
                    Synset[] synsets = iw.getSenses();
                    for (int i = 0; i < synsets.length; i++) {
                        Synset synset = synsets[i];
                        if (isBadSense(synset)) {
                            continue;
                        }
                        synonym = pu_.getSynonyms(synset);
                        if (synonym.isEmpty()) {
                            synonym = pu_.getDirectHypernyms(synset);
                        }
                        Set set = getLemmas(synonym);
                        if (set != null) {
                            list.addAll(set);
                        }
                        //			    else System.out.println("getLemmas got null");
                    }
                }
            }


        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        //System.out.println("**********");
        //System.out.println("'"+word+"'s"+" Synonym List: "+list);
        if (list != null
                && list.size() != 0) {
            if (pos.equals("noun")) {
                n_synonym_list_.put(word, list);
            } else if (pos.equals("adjective")) {
                a_synonym_list_.put(word, list);
            }
        }
        return list;
    }

    public ArrayList getSynonymAllSenses(String word) {
        ArrayList list = new ArrayList();
        try {
            PointerTargetNodeList synonym;
            IndexWordSet iws = lookupAllIndexWords(word);
            IndexWord[] iw_array = iws.getIndexWordArray();

            for (int j = 0; j < iw_array.length; j++) {
                IndexWord iw = iw_array[j];
                Synset[] synsets = iw.getSenses();
                for (int i = 0; i < synsets.length && i < 2; i++) //no more than the first 2 senses
                {
                    Synset synset = synsets[i];
                    if (isBadSense(synset)) {
                        continue;
                    }

                    synonym = pu_.getSynonyms(synset);
                    if (synonym.isEmpty()) {
                        synonym = pu_.getDirectHypernyms(synset);
                    }
                    Set set = getLemmas(synonym);
                    if (set != null) {
                        list.addAll(set);
                    }
                    //			    else System.out.println("getLemmas got null");
                }
            }

        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        return list;
    }

    /**
     * get derived word between noun and verb
     */
    public ArrayList getDerived(String word) {
        ArrayList list = new ArrayList();
        try {
            PointerTargetNodeList derived;
            IndexWordSet iws = lookupAllIndexWords(word);
            IndexWord[] iw_array = iws.getIndexWordArray();

            for (int j = 0; j < iw_array.length && j < 2; j++) {
                IndexWord iw = iw_array[j];
                Synset[] synsets = iw.getSenses();
                for (int i = 0; i < synsets.length; i++) //no more than the first 2 senses
                {
                    Synset synset = synsets[i];
                    System.out.println("synset: " + synset);
                    derived = pu_.getDerived(synset);
                    if (derived == null) {
                        continue;
                    }
//                    System.out.println("derived: " + derived);
                    Set set = getLemmas(derived);
                    if (set != null) {
                        list.addAll(set);
                    }
                }
            }

        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        return list;
    }
    
    /**
     * Get all antonyms of given word
     * @param word String word
     * @return List of Antonyms
     */
    public ArrayList getAntonyms(String word) {
        ArrayList list = new ArrayList();
        try {
            PointerTargetNodeList antonym;
            IndexWordSet iws = lookupAllIndexWords(word);
            IndexWord[] iw_array = iws.getIndexWordArray();

            for (int j = 0; j < iw_array.length && j < 2; j++) {
                IndexWord iw = iw_array[j];
                Synset[] synsets = iw.getSenses();
                for (Synset synset : synsets) {
                    System.out.println("synset: " + synset);
                    antonym = pu_.getAntonyms(synset);
                    if (antonym == null) {
                        continue;
                    }
Set set = getLemmas(antonym);
if (set != null) {
    list.addAll(set);
}
                } //no more than the first 2 senses
            }

        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        return list;
    }

    public ArrayList getLemmas(String word, String pos) {

        POS _pos = POS.getPOSForLabel(pos);

        if (lemmas_.keySet().contains(word)) {
            HashMap lemma = (HashMap) lemmas_.get(word);
            if (lemma.keySet().contains(_pos)) {
                return (ArrayList) lemma.get(_pos);
            }
        }

        ArrayList lemmas = new ArrayList();


        try {

            IndexWordSet iws = lookupAllIndexWords(word);

            if (_pos != null) {
                if (iws.isValidPOS(_pos)) {
                    lemmas.add(iws.getIndexWord(_pos).getLemma());
                    //			System.out.println("The lemmas of " + word + "(" + pos + ") are: " + lemmas);
                    HashMap lemma = null;
                    if (lemmas_.keySet().contains(word)) {
                        lemma = (HashMap) lemmas_.get(word);
                    } else {
                        lemma = new HashMap();
                    }
                    lemma.put(_pos, lemmas);
                    lemmas_.put(word, lemma);
                    return lemmas;
                } else {
                    lemmas.add("nolemma");
                    return lemmas;
                }
            } else {
                /*
                 * if (iws.isValidPOS(VERB)) {
                 * lemmas.add(iws.getIndexWord(VERB).getLemma());
                 *
                 * }
                 */
                if (iws.isValidPOS(NOUN)) {
                    lemmas.add(iws.getIndexWord(NOUN).getLemma());
                } else {
                    lemmas.add("nolemma");
                }
                /*
                 * if (iws.isValidPOS(ADJECTIVE)) {
                 * lemmas.add(iws.getIndexWord(ADJECTIVE).getLemma()); } if
                 * (!((iws.isValidPOS(ADJECTIVE)) || (iws.isValidPOS(NOUN)))) {
                 * lemmas.add("nolemma"); }
                 *
                 * if (iws.isValidPOS(ADVERB)) {
                 * lemmas.add(iws.getIndexWord(ADVERB).getLemma());
                 *
                 * }
                 */
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return lemmas;
    }

    public ArrayList getDirectHyponym(String word, String pos) {
        ArrayList list = new ArrayList();
        try {
            POS _pos = POS.getPOSForLabel(pos);
            PointerTargetNodeList dir_hyponym;

            if (_pos != null) {
                //		System.out.println("_pos is not null");
                IndexWord iw = lookupIndexWord(_pos, word);
                Synset[] synsets = iw.getSenses();
                for (int i = 0; i < synsets.length; i++) {
                    Synset synset = synsets[i];
                    dir_hyponym = pu_.getDirectHyponyms(synset);
                    Set set = getLemmas(dir_hyponym);

                    if (set != null) {
                        list.addAll(set);
                    }
                }
            } else {
                IndexWordSet iws = lookupAllIndexWords(word);
                IndexWord[] iw_array = iws.getIndexWordArray();

                for (int j = 0; j < iw_array.length; j++) {
                    IndexWord iw = iw_array[j];
                    _pos = iw.getPOS();
                    Synset[] synsets = iw.getSenses();
                    for (int i = 0; i < synsets.length; i++) {
                        Synset synset = synsets[i];
                        dir_hyponym = pu_.getDirectHyponyms(synset);
                        Set set = getLemmas(dir_hyponym);
                        if (set != null) {
                            list.addAll(set);
                        }
                        //			    else System.out.println("getLemmas got null");
                    }
                }
            }


        } catch (JWNLException e) {
            e.printStackTrace();
            list.clear();
            return null;
        } catch (Exception ie) {
            ie.printStackTrace();
            //System.out.println(ie);
        }

        //System.out.println("**********");
        //System.out.println("'"+word+"'s"+" Synonym List: "+list);

        return list;
    }
    /*
     * public String getLemma(String phrase, String pos) {
     *
     * try { POS _pos = POS.getPOSForLabel(pos); DefaultMorphologicalProcessor
     * dmp = new DefaultMorphologicalProcessor(); if(_pos != null){ return
     * dmp.lookupBaseForm(_pos, phrase).getLemma(); } else { return null; }
     * }catch (Exception e) { e.printStackTrace(); return null; }
     *
     * String [] words = phrase.split(" "); for (int i = 0; i < words.length;
     * i++) { System.out.print( " " + words[i]); } System.out.println(); BitSet
     * bt = new BitSet(words.length); //	bt.set(0, words.length - 1, true); //
     * ArrayList words = new ArrayList(); String lemma =
     * net.didion.jwnl.dictionary.morph.Util.getLemma(words, bt, "_");
     * System.out.println(phrase + "'s lemma is: " + lemma); return lemma; }
     */

    /**
     * *****************getLemma*********************************************
     */
    public ArrayList getLemmasWithTp(String word) {
        try {
            if (w_lemmas_.containsKey(word)) {
                return (ArrayList) w_lemmas_.get(word);
            }
            IndexWordSet iws = lookupAllIndexWords(word);
            IndexWord[] iw_array = iws.getIndexWordArray();
            ArrayList lemmas = new ArrayList();
            for (int i = 0; i < iw_array.length; i++) {
                IndexWord iw = iw_array[i];
                String lemma = iw.getLemma();
                POS pos = iw.getPOS();
                if (pos.equals(NOUN)) {
                    lemmas.add(lemma + "_N");
                } else if (pos.equals(VERB)) {
                    lemmas.add(lemma + "_V");
                }
                if (pos.equals(ADJECTIVE)
                        || pos.equals(ADVERB)) {
                    lemmas.add(lemma + "_A");
                }
                //get derived
                PointerTargetNodeList ptnl = pu_.getDerived(iw.getSense(1));
                for (int j = 0; j < ptnl.size(); j++) {
                    PointerTargetNode ptn = (PointerTargetNode) ptnl.get(j);
                    net.didion.jwnl.data.Word w = ptn.getWord();
                    lemma = w.getLemma();
                    pos = iw.getPOS();
                    if (pos.equals(NOUN)) {
                        lemmas.add(lemma + "_N");
                    } else if (pos.equals(VERB)) {
                        lemmas.add(lemma + "_V");
                    }
                    if (pos.equals(ADJECTIVE)
                            || pos.equals(ADVERB)) {
                        lemmas.add(lemma + "_A");
                    }
                }
            }
            w_lemmas_.put(word, lemmas);
            return lemmas;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList getSenses(PointerTargetNodeList ptnl) {
        ArrayList set = new ArrayList();
        // ptnl.print();
        Iterator iter = ptnl.iterator();
        PointerTargetNode ptn;
        Synset syn;
        Word[] words;

        while (iter.hasNext()) {
            ptn = (PointerTargetNode) iter.next();
            syn = ptn.getSynset();
            if (syn != null) {
                set.add(syn);
            }
        }  // the end of the while loop.

        return set;
    }

    public Set getSynsets(PointerTargetNodeList ptnl) {
        Set set = new TreeSet();
        // ptnl.print();
        Iterator iter = ptnl.iterator();
        PointerTargetNode ptn;
        Synset syn;
        Word[] words;

        while (iter.hasNext()) {
            ptn = (PointerTargetNode) iter.next();
            syn = ptn.getSynset();
            set.add(syn);
        }
        return set;
    }

    public ArrayList getLemmas(net.didion.jwnl.data.Word[] words) {
        ArrayList set = new ArrayList();
        for (int j = 0; j < words.length; j++) {
            set.add(words[j].getLemma());
            //System.out.println("word: " + words[j].getLemma());
        }
        return set;
    }

    public Set getLemmas(PointerTargetNodeList ptnl) {
        Set set = new TreeSet();
//        System.out.println("ptnl: " + ptnl.size()); 
//        ptnl.print();
        Iterator iter = ptnl.iterator();
        PointerTargetNode ptn;
        Synset syn;
        net.didion.jwnl.data.Word[] words;

        while (iter.hasNext()) {
            ptn = (PointerTargetNode) iter.next();
            syn = ptn.getSynset();
            if (syn != null) {
                words = syn.getWords();
                for (int j = 0; j < words.length; j++) {
                    set.add(words[j].getLemma());
//                    System.out.println("word: " + words[j].getLemma());
                }
            } else {
                if (ptn.getWord() != null) {
                    set.add(ptn.getWord().getLemma());
                    // System.out.println("lemma from getWord() method: " + ptn.getWord().getLemma());
                } else {
                    set = null;
                }
            }
        }  // the end of the while loop.

        return set;
    }

    public Set getLemmas(ArrayList ptnls) {
        Set set = new TreeSet();
        // ptnl.print();
        Iterator iter = ptnls.iterator();
        PointerTargetNode ptn;
        Synset syn;
        net.didion.jwnl.data.Word[] words;

        while (iter.hasNext()) {
            PointerTargetNodeList ptnl = (PointerTargetNodeList) iter.next();
            for (int i = 0; i < ptnl.size(); i++) {
                ptn = (PointerTargetNode) ptnl.get(i);
                syn = ptn.getSynset();
                if (syn != null) {
                    words = syn.getWords();
                    for (int j = 0; j < words.length; j++) {
                        set.add(words[j].getLemma());
                        //System.out.println("word: " + words[j].getLemma());
                    }
                } else {
                    if (ptn.getWord() != null) {
                        set.add(ptn.getWord().getLemma());
                        // System.out.println("lemma from getWord() method: " + ptn.getWord().getLemma());
                    } else {
                        set = null;
                    }
                }
            }
        }  // the end of the while loop.

        return set;
    }

    /**
     * return the lemma which synset is the first one of it's synet list
     */
    public Set getHiLemmas(PointerTargetNodeList ptnl) throws Exception {
        Set set = new TreeSet();
        // ptnl.print();
        Iterator iter = ptnl.iterator();
        PointerTargetNode ptn;
        Synset syn;
        net.didion.jwnl.data.Word[] words;

        while (iter.hasNext()) {
            ptn = (PointerTargetNode) iter.next();
            syn = ptn.getSynset();
            if (syn != null) {
                words = syn.getWords();
                for (int j = 0; j < words.length; j++) {
                    String lemma = words[j].getLemma();
                    POS _pos = words[j].getPOS();
                    IndexWord iw = lookupIndexWord(_pos, lemma);
                    if (iw == null) {
                        continue;
                    }
                    Synset[] senses = iw.getSenses();
                    if (senses.length > 0) {
                        if (syn.equals(senses[0])) {
                            set.add(words[j].getLemma());
                        }
                    }
                    //System.out.println("word: " + words[j].getLemma());
                }
            } else {
                if (ptn.getWord() != null) {
                    set.add(ptn.getWord().getLemma());
                    // System.out.println("lemma from getWord() method: " + ptn.getWord().getLemma());
                } else {
                    set = null;
                }
            }
        }  // the end of the while loop.

        return set;
    }
    public HashMap lemma_for_hype_ = new HashMap();

    /**
     * check whether word1 is the hypernym of word2
     */
    public boolean isHypernym(String word1, String word2) {
        ArrayList hypernyms = getHypernym(word2);
        ArrayList hypernyms_word1 = getHypernym(word1);
//            System.out.println("The hypernyms of " + word2 + " are: " + hypernyms);
        String lemma = (String) lemma_for_hype_.get(word1);
        String lemma_word2 = (String) lemma_for_hype_.get(word2);
        if (lemma == null || lemma_word2 == null) {
            lemma = getLemma(word1);
            lemma_word2 = getLemma(word2);
            lemma_for_hype_.put(word1, lemma);
            lemma_for_hype_.put(word2, lemma_word2);
        }

        if (hypernyms.contains(lemma) || hypernyms_word1.contains(lemma_word2)) {
            return true;
        }
        return false;
    }

    /**
     * Check word2 is the hyponym of word1
     *
     * @param word2
     * @param word1
     * @return
     * @throws Exception
     */
//    public boolean isHyponym(String word2,String word1){
//        ArrayList hyponyms=getHypernym(word2);
//        String lemma=(String) lemma_for_hype_.get(word1);
//        if(lemma==null){
//            lemma=getLemma(word1);
//            lemma_for_hype_.put(word1, lemma);
//        }
//        
//        if (hyponyms.contains(lemma)){
//            return true;
//        }
//        return false;
//    }
    public boolean isHypernym(Synset sense1,
            String word2) throws Exception {
        ArrayList senses = getSenses(word2, sense1.getPOS());
        for (int i = 0; i < senses.size(); i++) {
            Synset sense2 = (Synset) senses.get(i);
            if (isHypernym(sense1, sense2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHypernym(Synset sense1,
            String word2, int num) throws Exception {
        ArrayList senses = getSenses(word2, sense1.getPOS());
        for (int i = 0; i < senses.size() && i < num; i++) {
            Synset sense2 = (Synset) senses.get(i);
            if (isHypernym(sense1, sense2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHypernym(Synset sense1,
            Synset sense2) throws Exception {
        //	RelationshipList rls = rf_.findRelationships(sense1, sense2, PointerType.HYPERNYM, 5);
        PointerTargetTree ptt = pu_.getHypernymTree(sense2);
        List ptnls = ptt.toList();
        for (int i = 0; i < ptnls.size(); i++) {
            PointerTargetNodeList ptnl = (PointerTargetNodeList) ptnls.get(i);
            for (int j = 0; j < ptnl.size(); j++) {
                PointerTargetNode ptn = (PointerTargetNode) ptnl.get(j);
                if (sense1.equals(ptn.getSynset())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDirHypernym(Synset sense1,
            Synset sense2) throws Exception {
        //	RelationshipList rls = rf_.findRelationships(sense1, sense2, PointerType.HYPERNYM, 5);
        PointerTargetNodeList ptnl = pu_.getDirectHypernyms(sense2);
        for (int j = 0; j < ptnl.size(); j++) {
            PointerTargetNode ptn = (PointerTargetNode) ptnl.get(j);
            if (sense1.equals(ptn.getSynset())) {
                return true;
            }
        }
        return false;
    }

    public boolean isHyponym(Synset sense1,
            Synset sense2) throws Exception {
        //	RelationshipList rls = rf_.findRelationships(sense1, sense2, PointerType.HYPERNYM, 5);
        PointerTargetTree ptt = pu_.getHyponymTree(sense2);
        List ptnls = ptt.toList();
        for (int i = 0; i < ptnls.size(); i++) {
            PointerTargetNodeList ptnl = (PointerTargetNodeList) ptnls.get(i);
            for (int j = 0; j < ptnl.size(); j++) {
                PointerTargetNode ptn = (PointerTargetNode) ptnl.get(j);
                if (sense1.equals(ptn.getSynset())) {
                    return true;
                }
            }
        }
        return false;
    }

//    public boolean isHyponym(String word1, String word2){
//	    ArrayList hyponyms = getHypernym(word2);
//	    
////            System.out.println("The hypernyms of " + word2 + " are: " + hypernyms);
//	    String lemma = (String)lemma_for_hype_.get(word1);
//	    if (lemma == null) {
//		lemma = getLemma(word1);
//		lemma_for_hype_.put(word1, lemma);
//	    }
//
//
//	    if (hypernyms.contains(lemma)) {
//		return true;
//	    }
//	    return false;
//    }
    public boolean isDirHyponym(Synset sense1,
            Synset sense2) throws Exception {
        PointerTargetNodeList ptnl = pu_.getDirectHyponyms(sense2);
        for (int j = 0; j < ptnl.size(); j++) {
            PointerTargetNode ptn = (PointerTargetNode) ptnl.get(j);
            if (sense1.equals(ptn.getSynset())) {
                return true;
            }
        }
        return false;
    }

    public boolean isHypernym(String word1, String word2, String pos) {
        ArrayList hypernyms = getHypernym(word2, pos);

        if (hypernyms == null) {
            return false;
        }
        String lemma = getLemma(word1, pos);

        if (hypernyms.contains(lemma)) {
            return true;
        }
        return false;
    }

    public String isHypernymWhich(String word1, String word2, String pos) {
        ArrayList hypernyms = getHypernym(word2, pos);

        if (hypernyms == null) {
            return null;
        }
        String lemma = getLemma(word1, pos);

        if (hypernyms.contains(lemma)) {
            return word1;
        }
        return null;
    }

    /**
     * check the hypernym relation between phrase
     */
    public boolean isHypernymPhr(String word1, String word2, String pos) {
        String[] words = word2.split("_");
        String single_word = words[0];
        ArrayList single_hyps = getHypernymPhr(single_word, pos);
        ArrayList hypernyms = getHypernymPhr(word2, pos);

        if (single_hyps != null
                && single_hyps.size() > 0
                && words.length > 1
                && hypernyms.contains(single_hyps.get(0))) {
            return false;
        }
        if (hypernyms == null) {
            return false;
        }
        String lemma = getLemma(word1, pos);

        if (hypernyms.contains(lemma)) {
            return true;
        }
        return false;
    }

    /**
     * we have to filter out the bad sense first
     */
    public boolean isHypernymNoBS(String word1, String word2, String pos) {
        ArrayList hypernyms = getHypernymNoBS(word2, pos);

        String lemma = getLemma(word1, pos);

        if (hypernyms.contains(lemma)) {
            return true;
        }
        return false;
    }

    public boolean isHypernym(String word1, String word2, String pos, int arrange) {
        ArrayList hypernyms = getHypernym(word2, pos, arrange);

        String lemma = getLemma(word1, pos);

        if (hypernyms.contains(lemma)) {
            return true;
        }
        return false;
    }

    /**
     * **** find hypernyms ************************
     */
    public ArrayList getHypernym(String word) {
        try {
            if (hyper_noun_lists_.containsKey(word)) {
                return (ArrayList) hyper_noun_lists_.get(word);
            }

            ArrayList list = new ArrayList();
            IndexWordSet iws = lookupAllIndexWords(word);

            Set set = new TreeSet();
            if (iws.isValidPOS(NOUN)) {
                IndexWord noun = iws.getIndexWord(NOUN);
                PointerTargetTree ptt = pu_.getHypernymTree(noun.getSense(1));
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                /*
                 * if (noun.getSenseCount() > 1) {
                 *
                 * ptt = pu_.getHypernymTree(noun.getSense(2)); word_list =
                 * ptt.getRootNode().getSynset().getWords(); pttList =
                 * (ArrayList) ptt.toList(); iter0 = pttList.iterator();
                 * while(iter0.hasNext()) { PointerTargetNodeList ptnl =
                 * (PointerTargetNodeList)iter0.next();
                 * set.addAll(getLemmas(ptnl)); } }
                 */
            }
            list.addAll(set);

            // testing the unique.
	    /*
             * Iterator iter = list.iterator(); System.out.println("checking the
             * unique."); while(iter.hasNext()) { System.out.println("unique: "
             * + (String)iter.next()); }
             */
            //	    System.out.println("WORD: " + word);
            //	    System.out.println("Hypernym List: "+list);
            hyper_noun_lists_.put(word, list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList getHypernym(String word, String pos) {
        try {
            if (hyper_noun_lists_.containsKey(word)
                    && pos.equals("noun")) {
                return (ArrayList) hyper_noun_lists_.get(word);
            }
            if (hyper_adj_lists_.containsKey(word)
                    && pos.equals("adjective")) {
                return (ArrayList) hyper_adj_lists_.get(word);
            }
            if (hyper_verb_lists_.containsKey(word)
                    && pos.equals("verb")) {
                return (ArrayList) hyper_verb_lists_.get(word);
            }
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
                hyper_noun_lists_.put(word, list);
            } else if (pos.equals("verb")) {
                _pos = VERB;
                hyper_verb_lists_.put(word, list);
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
                hyper_adj_lists_.put(word, list);
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            boolean compared = false;
            ArrayList senses = new ArrayList(Arrays.asList(iw.getSenses()));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getHypernymTree(sense);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * the hypernym has to be the exact phrase instead of one of words
     */
    public ArrayList getHypernymPhr(String word, String pos) {
        try {
            String word1 = word.replaceAll(" ", "_");
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            } else if (pos.equals("verb")) {
                _pos = VERB;
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            boolean compared = false;
            ArrayList senses = new ArrayList(Arrays.asList(iw.getSenses()));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getHypernymTree(sense);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                ArrayList lemmas = new ArrayList();
                lemmas.addAll(getLemmas(pttList));
                if (!compared
                        && (!lemmas.contains(word)
                        && !lemmas.contains(word1))) {
                    return new ArrayList();
                }
                compared = true;
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * the hypernym has to be the exact phrase instead of one of words
     */
    public ArrayList getHypernymPhrSs(String word, String pos) {
        try {
            String word1 = word.replaceAll(" ", "_");
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            } else if (pos.equals("verb")) {
                _pos = VERB;
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            boolean compared = false;
            ArrayList senses = new ArrayList(Arrays.asList(iw.getSenses()));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getHypernymTree(sense);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                ArrayList lemmas = new ArrayList();
                lemmas.addAll(getLemmas(pttList));
                if (!compared
                        && (!lemmas.contains(word)
                        && !lemmas.contains(word1))) {
                    return new ArrayList();
                }
                compared = true;
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    ArrayList ss = getSenses(ptnl);
                    if (ss != null
                            && ss.size() > 0) {
                        list.addAll(ss);
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * the hypernym has to be the exact phrase instead of one of words
     */
    public ArrayList getHiHypernymPhr(String word, String pos) {
        try {
            String word1 = word.replaceAll(" ", "_");
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            } else if (pos.equals("verb")) {
                _pos = VERB;
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            boolean compared = false;
            ArrayList senses = new ArrayList();
            senses.add(iw.getSense(1));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getHypernymTree(sense);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                ArrayList lemmas = new ArrayList();
                lemmas.addAll(getLemmas(pttList));
                if (!compared
                        && (!lemmas.contains(word)
                        && !lemmas.contains(word1))) {
                    return new ArrayList();
                }
                compared = true;
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * the hypernym has to be the exact phrase instead of one of words
     */
    public ArrayList getHyponymPhr(String word, String pos) {
        try {
            String word1 = word.replaceAll(" ", "_");
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            } else if (pos.equals("verb")) {
                _pos = VERB;
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            boolean compared = false;
            ArrayList senses = new ArrayList(Arrays.asList(iw.getSenses()));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getHyponymTree(sense);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                ArrayList lemmas = new ArrayList();
                lemmas.addAll(getLemmas(pttList));
                if (!compared
                        && (!lemmas.contains(word)
                        && !lemmas.contains(word1))) {
                    return new ArrayList();
                }
                compared = true;
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * the hypernym has to be the exact phrase instead of one of words
     */
    public ArrayList getHiHyponymPhr(String word, String pos) {
        try {
            String word1 = word.replaceAll(" ", "_");
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            } else if (pos.equals("verb")) {
                _pos = VERB;
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            boolean compared = false;
            ArrayList senses = new ArrayList();
            senses.add(iw.getSense(1));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getHyponymTree(sense);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                ArrayList lemmas = new ArrayList();
                lemmas.addAll(getLemmas(pttList));
                if (!compared
                        && (!lemmas.contains(word)
                        && !lemmas.contains(word1))) {
                    return new ArrayList();
                }
                compared = true;
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * the hypernym has to be the exact phrase instead of one of words
     */
    public ArrayList getSynonymPhr(String word, String pos) {
        try {
            String word1 = word.replaceAll(" ", "_");
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            } else if (pos.equals("verb")) {
                _pos = VERB;
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            boolean compared = false;
            ArrayList senses = new ArrayList(Arrays.asList(iw.getSenses()));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getSynonymTree(sense, 1);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                ArrayList lemmas = new ArrayList();
                lemmas.addAll(getLemmas(pttList));
                if (!compared
                        && (!lemmas.contains(word)
                        && !lemmas.contains(word1))) {
                    return new ArrayList();
                }
                compared = true;
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * the hypernym has to be the exact phrase instead of one of words
     */
    public ArrayList getHiSynonymPhr(String word, String pos) {
        try {
            String word1 = word.replaceAll(" ", "_");
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            } else if (pos.equals("verb")) {
                _pos = VERB;
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            boolean compared = false;
            ArrayList senses = new ArrayList();
            senses.add(iw.getSense(1));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getSynonymTree(sense, 1);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                ArrayList lemmas = new ArrayList();
                lemmas.addAll(getLemmas(pttList));
                if (!compared
                        && (!lemmas.contains(word)
                        && !lemmas.contains(word1))) {
                    return new ArrayList();
                }
                compared = true;
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * with no bad sense, such speech attack for attack event
     */
    public ArrayList getHypernymNoBS(String word, String pos) {
        try {
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            } else if (pos.equals("verb")) {
                _pos = VERB;
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            ArrayList senses = new ArrayList(Arrays.asList(iw.getSenses()));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    //System.out.println("find a bad sense: " + sense);
                    continue;
                }
                PointerTargetTree ptt = pu_.getHypernymTree(sense);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList getHypernymSs(String word, String pos) {
        try {
            ArrayList list = new ArrayList();
            POS _pos = POS.getPOSForLabel(pos);
            ArrayList senses = getSenses(word, _pos);
            if (senses == null
                    || senses.size() == 0) {
                return list;
            }
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getHypernymTree(sense);
                ArrayList pttList = (ArrayList) ptt.toList();
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    ArrayList ss = getSenses(ptnl);
                    if (ss != null
                            && ss.size() > 0) {
                        list.addAll(ss);
                    }
                }
                //		list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList getSenses(String word, String pos) {
        POS pos_ = getPOS(pos, word);
        if (pos_ == null
                || word == null
                || word.length() == 0) {
            return new ArrayList();
        }
        //System.err.println("get number of senses from " + word + "_" + pos_);
        return getSenses(word, pos_);
    }

    public ArrayList getSenses(String word, POS pos) {
        try {
            ArrayList senses = new ArrayList();
            IndexWord iw = null;

            String key = word + "_" + pos;
            if (senses_.containsKey(key)) {
                iw = (IndexWord) senses_.get(key);
                if (iw != null) {
                    //processed before
                    return new ArrayList(Arrays.asList(iw.getSenses()));
                }
            }
            if (iw == null) {
                iw = lookupIndexWord(pos, word);
            }
            if (iw == null) {
                //		System.out.println(word + " " + pos + " has no wordnet definition!!!");
                return senses;
            } else {
                senses_.put(key, iw);
            }

            senses = new ArrayList(Arrays.asList(iw.getSenses()));
            return senses;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList getHypernym(String word, String pos, int arrange) {
        try {
            String key = word + "_" + arrange;
            if (hyper_noun_lists_.containsKey(key)
                    && pos.equals("noun")) {
                return (ArrayList) hyper_noun_lists_.get(key);
            }
            if (hyper_adj_lists_.containsKey(key)
                    && pos.equals("adjective")) {
                return (ArrayList) hyper_adj_lists_.get(key);
            }
            if (hyper_verb_lists_.containsKey(key)
                    && pos.equals("verb")) {
                return (ArrayList) hyper_verb_lists_.get(key);
            }

            POS _pos = null;
            ArrayList list = new ArrayList();
            if (pos.equals("noun")) {
                _pos = NOUN;
                hyper_noun_lists_.put(key, list);
            } else if (pos.equals("verb")) {
                _pos = VERB;
                hyper_verb_lists_.put(key, list);
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
                hyper_adj_lists_.put(key, list);
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                return list;
            }

            Set set = new TreeSet();
            ArrayList senses = new ArrayList(Arrays.asList(iw.getSenses()));
            for (int i = 0; i < senses.size() && i < arrange; i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getHypernymTree(sense);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList getHyponym(String word, String pos) {
        try {

            if (hypo_noun_lists_.containsKey(word)
                    && pos.equals("noun")) {
                return (ArrayList) ((ArrayList) hypo_noun_lists_.get(word)).clone();
            }
            if (hypo_verb_lists_.containsKey(word)
                    && pos.equals("verb")) {
                return (ArrayList) ((ArrayList) hypo_verb_lists_.get(word)).clone();
            }
            //	    System.out.println("looking for the hyponym of " + word + " " + pos);
            POS _pos = POS.getPOSForLabel(pos);

            ArrayList list = new ArrayList();
            //	    IndexWordSet iws = lookupAllIndexWords(word);
            IndexWord iw = lookupIndexWord(_pos, word);

            Set set = new TreeSet();
            if (iw != null) {
                PointerTargetTree ptt = pu_.getHyponymTree(iw.getSense(1));
                int i = 1;
                int up_limmit = iw.getSenses().length;
                ArrayList pttList = new ArrayList();
                while (ptt != null
                        && i <= up_limmit
                        && i <= 3) {
                    ptt = pu_.getHyponymTree(iw.getSense(i));
                    net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                    if (word_list != null
                            && word_list.length > 0) {
                        pttList.addAll((ArrayList) ptt.toList());
                    }
                    i++;
                }
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
                if (pos.equals("noun")
                        || pos.equals("adjective")) {
                    hypo_noun_lists_.put(word, list);
                } else {
                    hypo_verb_lists_.put(word, list);
                }
            }
            // testing the unique.
	    /*
             * Iterator iter = list.iterator(); System.out.println("checking the
             * unique."); while(iter.hasNext()) { System.out.println("unique: "
             * + (String)iter.next()); }
             */
            //	    System.out.println("WORD: " + word);
            //	    System.out.println("Hyponym List: "+list);
	    /*
             * if (word.equals("shoot")) { System.out.println("The hyponym of
             * shoot is: " + list); } if (word.equals("fire")) {
             * System.out.println("The hyponym of fire is: " + list); }
             */
            return (ArrayList) list.clone();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList getHyponymNoBS(String word, String pos) {
        try {
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            } else if (pos.equals("verb")) {
                _pos = VERB;
            } else if (pos.equals("adjective")) {
                _pos = ADJECTIVE;
            }
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                //System.out.println(word + " " + _pos + " has no wordnet definition!!!");
                return list;
            }

            Set set = new TreeSet();
            ArrayList senses = new ArrayList(Arrays.asList(iw.getSenses()));
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (isBadSense(sense)) {
                    continue;
                }
                PointerTargetTree ptt = pu_.getHyponymTree(sense);
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList getHiHyponym(String word, String pos) {
        try {
            //	    System.out.println("looking for the hi_hyponym of " + word + " " + pos);
            POS _pos = POS.getPOSForLabel(pos);

            ArrayList list = new ArrayList();
            //	    IndexWordSet iws = lookupAllIndexWords(word);
            IndexWord iw = lookupIndexWord(_pos, word);

            Set set = new TreeSet();
            if (iw != null) {
                PointerTargetTree ptt = null;
                ptt = pu_.getHyponymTree(iw.getSense(1));
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                ArrayList pttList = (ArrayList) ptt.toList();
                Iterator iter0 = pttList.iterator();
                while (iter0.hasNext()) {
                    PointerTargetNodeList ptnl = (PointerTargetNodeList) iter0.next();
                    set.addAll(getHiLemmas(ptnl));
                }
                list.addAll(set);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * **** find Indirect hypernyms ************************
     */
    public ArrayList getIHypernym(String word) {
        //get the second level hypernyms
        try {
            if (hyper_noun_lists_.containsKey(word)) {
                return (ArrayList) hyper_noun_lists_.get(word);
            }

            ArrayList list = new ArrayList();
            IndexWordSet iws = lookupAllIndexWords(word);

            Set set = new TreeSet();
            if (iws.isValidPOS(NOUN)) {
                IndexWord noun = iws.getIndexWord(NOUN);
                PointerTargetTree ptt = pu_.getHypernymTree(noun.getSense(1));
                net.didion.jwnl.data.Word[] word_list = ptt.getRootNode().getSynset().getWords();
                list = getSecondLevelWords(ptt);
            }
            list.addAll(set);

            //System.out.println("The second level hypernym List: " + list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
     * find indirected hypernyms
     */

    public ArrayList getSecondLevelWords(PointerTargetTree ptt) {
        try {
            //return the second level (except root node) elements from
            ArrayList word_list = new ArrayList();
            PointerTargetTreeNode pttn = ptt.getRootNode();
            PointerTargetTreeNodeList pttnl = pttn.getChildTreeList();
            for (int i = 0; i < pttnl.size(); i++) {
                PointerTargetTreeNode pttn1 = (PointerTargetTreeNode) pttnl.get(i);
                PointerTargetTreeNodeList pttnl1 = pttn1.getChildTreeList();
                if (pttnl1 == null) {
                    return null;
                }
                for (int j = 0; j < pttnl1.size(); j++) {
                    //		System.out.println("The pointerTargetTreeNode is: " + pttn1.toString());
                    PointerTargetTreeNode pttn2 = (PointerTargetTreeNode) pttnl1.get(j);
                    net.didion.jwnl.data.Word[] word_l = pttn2.getSynset().getWords();
                    for (int k = 0; k < word_l.length; k++) {
                        word_list.add(word_l[k].getLemma());
                    }
                }
            }
            return word_list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadSynonymList(String file_name) {
        try {
            File file = new File(file_name);
            file.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            loadList(br, n_synonym_list_);
            synonym_file_ = file_name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHypernymList(String file_name) {
        try {
            File file = new File(file_name);
            file.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            loadList(br, hyper_noun_lists_);
            hypernym_file_ = file_name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadList(BufferedReader br,
            HashMap container) throws Exception {
        String a_line = null;
        while ((a_line = br.readLine()) != null) {
            if (a_line.trim().endsWith(":")) {
                String key = a_line.trim().substring(0, a_line.trim().length() - 1);
                a_line = br.readLine();
                if (a_line == null
                        || a_line.trim().length() == 0) {
                    //no definition
                    continue;
                } else {
                    a_line = a_line.replaceAll("\\[\\]", "");
                    String[] words = a_line.split("[, ]+");
                    container.put(key, new ArrayList(Arrays.asList(words)));
                }
            }
        }
    }

    public void writeListToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(synonym_file_)));
            writeListToFile(bw, n_synonym_list_);
            bw.close();
            bw = new BufferedWriter(new FileWriter(new File(hypernym_file_)));
            writeListToFile(bw, hyper_noun_lists_);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeListToFile(BufferedWriter bw,
            HashMap container) throws Exception {
        ArrayList keys = new ArrayList(container.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            bw.write(key + ":");
            bw.newLine();
            bw.write(((ArrayList) container.get(key)).toString());
            bw.newLine();
            bw.newLine();
        }
    }

    /**
     * set bad senses for triggers
     */
    public void initBadSenses() {
        //process conflict attack event
        try {
            ArrayList bad_senses = new ArrayList();
            POS _pos = POS.getPOSForLabel("noun");
            IndexWord iw = lookupIndexWord(_pos, "attack");
            bad_senses.add(iw.getSense(3)); //intense adverse criticism
            _pos = POS.getPOSForLabel("verb");
            iw = lookupIndexWord(_pos, "attack");
            bad_senses.add(iw.getSense(2)); //attack in speech or writing
            _pos = POS.getPOSForLabel("verb");
            iw = lookupIndexWord(_pos, "kill");
            bad_senses.addAll(Arrays.asList(iw.getSenses())); //attack in speech or writing
            _pos = POS.getPOSForLabel("noun");
            iw = lookupIndexWord(_pos, "kill");
            bad_senses.addAll(Arrays.asList(iw.getSenses())); //attack in speech or writing
            _pos = POS.getPOSForLabel("noun");
            iw = lookupIndexWord(_pos, "killing");
            bad_senses.addAll(Arrays.asList(iw.getSenses())); //attack in speech or writing
            bad_senses_.put("Conflict_Attack", bad_senses);
            bad_senses = new ArrayList();
            _pos = POS.getPOSForLabel("noun");
            iw = lookupIndexWord(_pos, "recompense");
            bad_senses.add(iw.getSense(2)); //intense adverse criticism
            _pos = POS.getPOSForLabel("verb");
            iw = lookupIndexWord(_pos, "recompense");
            bad_senses.add(iw.getSense(1)); //attack in speech or writing
            bad_senses_.put("Transaction_Transfer-Money", bad_senses);
        } catch (JWNLException je) {
            je.printStackTrace();
        }
    }

    /**
     * whether this sense or the hypernym of this sense contains bad senses
     */
    public boolean isBadSense(Synset sense) throws Exception {
        /*
         * String key = Bootstrap.type_ + "_" + Bootstrap.sub_type_; ArrayList
         * bad_senses = (ArrayList)bad_senses_.get(key); if (bad_senses == null)
         * { return false; } if (bad_senses.contains(sense)) { return true; }
         * PointerTargetTree ptt = pu_.getHypernymTree(sense); List ptnls =
         * ptt.toList(); for (int i = 0; i < ptnls.size(); i++) {
         * PointerTargetNodeList ptnl = (PointerTargetNodeList)ptnls.get(i); for
         * (int j = 0; j < ptnl.size(); j++) { PointerTargetNode ptn =
         * (PointerTargetNode)ptnl.get(j); for (int k = 0; k <
         * bad_senses.size(); k++) { Synset bad_sense =
         * (Synset)bad_senses.get(k); if
         * (sense.getWord(0).getLemma().equals("abuse")) {
         * //System.out.println("compare abuse with it's hypernym" + ptn);
         * //System.out.println("bad sense is: " + bad_sense); } if
         * (bad_sense.equals(ptn.getSynset())) { //	System.out.println("find a
         * bad one!!!!!!!!!!!!"); //	throwable_.printStackTrace(); return true;
         * } } } }
         */
        return false;
    }

    public POS getPOS(String pos,
            String cont) {
        return POS.getPOSForLabel(getPOS(pos));
    }

    public String getPOS(String pos) {
        if (pos.startsWith("N")) {
            pos = "noun";
            return pos;
        } else if (pos.startsWith("V")) {
            pos = "verb";
            return pos;
        } else if (pos.startsWith("A")) {
            pos = "adjective";
            return pos;
        }

        return pos;
    }

    /**
     * m2w: get the first sense of the index word, i.e. sslist[0] , for getting
     * offset
     *
     * @param s
     * @param pos
     * @return
     */
    public Synset getFirstSense(String s, POS pos) {
        try {
            IndexWord iw = null;

            String key = s + "_" + pos;
            if (firstSense_.containsKey(key)) {
                iw = (IndexWord) firstSense_.get(key);
                if (iw == null) {
                    //processed before
                    return null;
                }
            }
            if (iw == null) {
                iw = lookupIndexWord(pos, s);
            }
            firstSense_.put(key, iw);
            if (iw == null) {
                return null;
            }
            Synset sense = iw.getSenses()[0];
            return sense;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * m2w: get the word's first sense's offset
     *
     * @param s
     * @param pos
     * @return
     */
    public long getFirstSenseOffset(String s, POS pos) {
        Synset ss = this.getFirstSense(s, pos);
        if (ss == null) {
            return Long.MAX_VALUE;
        }
        long offset = ss.getOffset();
        return offset;
    }

    /**
     * m2w: generic verion of getSynset at which senses
     *
     * @param word
     * @param pos
     * @param M : front M senses
     * @return
     * @lastupdate 8/1/12 5:02 PM
     */
    public Synset getSynsetAt_Mse(String word, String pos, Integer M) {
        try {
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
            }
            if (pos.equals("verb")) {
                _pos = VERB;
            }
            if (pos.equals("adj")) {
                _pos = ADJECTIVE;
            }
            if (_pos != null) {
                IndexWord iw = lookupIndexWord(_pos, word);
                if (iw == null) {
                    return null;
                }
                Synset[] synsets = iw.getSenses();
                if (M <= synsets.length) {
                    return synsets[M - 1];
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * m2w: generic verion of getSynset
     *
     * @param word
     * @param pos
     * @param M : front M senses
     * @return
     * @lastupdate 8/1/12 5:02 PM
     */
    public ArrayList<Synset> getSynsetFront_Mses(String word, String pos, Integer M) {
        ArrayList<Synset> result = new ArrayList<Synset>();
        try {
            HashMap<String, ArrayList<Synset>> map = null;
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
                map = Synsets_noun;
            } else if (pos.equals("verb")) {
                _pos = VERB;
                map = Synsets_verb;
            } else if (pos.equals("adj")) {
                _pos = ADJECTIVE;
                map = Synsets_adj;
            } else {
                System.out.println("pos is not supported, plz check pos type or add current pos in");
            }

            if (map.containsKey(word)) {
                return map.get(word);
            } else {
                //getting the full list if don't have
                map.put(word, result);
                if (_pos != null) {
                    IndexWord iw = lookupIndexWord(_pos, word);
                    if (iw == null) {
                        return result;
                    }
                    Synset[] synsets = iw.getSenses();

                    for (int i = 0; i < M && i < synsets.length; i++) {//10/18/12 12:23 PM m2w
                        if (synsets[i] != null) {
                            result.add(synsets[i]);
                        }
                    }
                }
            }//closes else

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * m2w: get synonyms at M th sense of the word.
     *
     * @param s1
     * @param pos
     * @param M
     * @return
     */
    public ArrayList<String> getSynonymAt_Mse(String s1, String pos, Integer M) {
        ArrayList<String> synlist = new ArrayList<String>();
        HashMap<String, ArrayList<String>> map = null;
        if (M == 1 && pos.equals("noun")) {
            map = syn_noun_se1;
        }
        if (M == 1 && pos.equals("verb")) {
            map = syn_verb_se1;
        }
        if (M == 1 && pos.equals("adj")) {
            map = syn_adj_se1;
        }
        if (M == 2 && pos.equals("noun")) {
            map = syn_noun_se2;
        }
        if (M == 2 && pos.equals("verb")) {
            map = syn_verb_se2;
        }
        if (M == 2 && pos.equals("adj")) {
            map = syn_adj_se2;
        }
        if (map != null) {
            map.put(s1, synlist);
        }
        Synset ss = this.getSynsetAt_Mse(s1, pos, M);
        if (ss == null) {
            return synlist;
        }
        for (net.didion.jwnl.data.Word w : ss.getWords()) {
            synlist.add(w.getLemma());
        }

        HashSet<String> hs = new HashSet<String>();
        hs.addAll(synlist);
        synlist.clear();
        synlist.addAll(hs);
        return synlist;
    }

    /**
     * m2w: should be a faster version of getting synonyms.
     *
     * @param s1
     * @param pos
     * @param M
     * @return
     * @lastupdate 8/1/12 5:02 PM
     */
    public ArrayList<String> getSynonymFront_Mses(String s1, String pos, Integer M) {
        ArrayList<String> synlist = new ArrayList<String>();
        HashMap<String, ArrayList<String>> map = null;
        if (pos.equals("noun")) {
            map = syn_noun;
            if (map.containsKey(s1)) {
                return map.get(s1);
            }
        }
        if (pos.equals("verb")) {
            map = syn_verb;
            if (map.containsKey(s1)) {
                return map.get(s1);
            }
        }
        if (pos.equals("adj")) {
            map = syn_adj;
            if (map.containsKey(s1)) {
                return map.get(s1);
            }
        }
        map.put(s1, synlist);
        ArrayList<Synset> sslist = (ArrayList<Synset>) this.getSynsetAllSenses(s1, pos);
        for (Synset ss : sslist) {

            for (net.didion.jwnl.data.Word w : ss.getWords()) {
                synlist.add(w.getLemma());
            }
            M--;
            if (M == 0) {
                break;
            }
        }

        HashSet<String> hs = new HashSet<String>();
        hs.addAll(synlist);
        synlist.clear();
        synlist.addAll(hs);
        System.out.println("list: " + synlist);
        System.out.println("list size: " + synlist.size());
        return synlist;
    }

    /**
     * m2w: is syn new method, faster.
     *
     * @param s1
     * @param s2
     * @param pos
     * @param M
     * @return
     * @lastupdated 8/1/12 11:30 AM
     */
    public boolean isSynonymFront_Mses(String s1, String s2, String pos, Integer M) {
        ArrayList<String> s1list = this.getSynonymFront_Mses(s1, pos, M);
        if (s1list.contains(s2)) {
            return true;
        }
        return false;
    }

    /**
     * m2w: get hyper at nth level and mth sense.
     *
     * @param word
     * @param pos
     * @param N
     * @param M
     * @return
     * @lastupdate 8/2/12 12:07 PM
     */
    public ArrayList<String> getHyperAt_Nlvl_Mses(String word, String pos, Integer N, Integer M) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            HashMap map = null;
            POS _pos = null;
            if (M == 2 && N == 1) {
                if (pos.equalsIgnoreCase("noun")) {
                    _pos = NOUN;
                    map = this.hyp_noun_1stsense_dir;
                }
                if (pos.equalsIgnoreCase("verb")) {
                    _pos = VERB;
                    map = this.hyp_verb_1stsense_dir;
                }
                if (pos.equalsIgnoreCase("adj")) {
                    _pos = ADJECTIVE;
                    map = this.hyp_verb_1stsense_dir;
                }
            } else if (M == 1 && N == 1) {
                if (pos.equalsIgnoreCase("noun")) {
                    _pos = NOUN;
                    map = this.hyp_noun_2ndsense_dir;
                }
                if (pos.equalsIgnoreCase("verb")) {
                    _pos = VERB;
                    map = this.hyp_verb_2ndsense_dir;
                }
                if (pos.equalsIgnoreCase("adj")) {
                    _pos = ADJECTIVE;
                    map = this.hyp_adj_2ndsense_dir;
                }
            } else {
                _pos = this.getPOS(pos, "1");
            }

            if (map != null) {//if map matches.
                ArrayList<String> temp = (ArrayList<String>) map.get(word);
                if (map.containsKey(word)) {
                    return (ArrayList) map.get(word);
                }
                map.put(word, list);//put processed entry in map
            }
            //if no map matches
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                return list;
            }
            Set set = new TreeSet();
            Synset sense = this.getSynsetAt_Mse(word, pos, M);//get nth sense
            PointerTargetTree ptt = pu_.getHypernymTree(sense, N);
            ArrayList<PointerTargetNodeList> pttList = (ArrayList<PointerTargetNodeList>) ptt.toList();
            for (PointerTargetNodeList ptnl : pttList) {
                set.addAll(getLemmas(ptnl));
            }
            list.addAll(set);
            HashSet sss = new HashSet();
            sss.addAll(list);
            list.clear();
            list.addAll(sss);
            //remove syn.
            list.removeAll(this.getSynonymFront_Mses(word, pos, M));
            return list;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }

    }

    /**
     * m2w: generic version for get hypers.
     *
     * @param word
     * @param pos
     * @param N : front N levels
     * @param M : front M senses
     * @return
     */
    public ArrayList<String> getHypernymFront_Nlvl_Mses(String word, String pos, Integer N, Integer M) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            HashMap map = null;
            POS _pos = null;
            if (M == 2) {
                if (pos.equalsIgnoreCase("noun")) {
                    _pos = NOUN;
                    map = hyper_noun_maps_front2senses;
                }
                if (pos.equalsIgnoreCase("verb")) {
                    _pos = VERB;
                    map = hyper_verb_maps_front2senses;
                }
                if (pos.equalsIgnoreCase("adj")) {
                    _pos = ADJECTIVE;
                    map = hyper_adj_maps_front2senses;
                }
            } else {
                if (pos.equalsIgnoreCase("noun")) {
                    _pos = NOUN;
                    map = hyper_noun_lists_;
                }
                if (pos.equalsIgnoreCase("verb")) {
                    _pos = VERB;
                    map = hyper_verb_lists_;
                }
                if (pos.equalsIgnoreCase("adj")) {
                    _pos = ADJECTIVE;
                    map = hyper_adj_lists_;
                }
            }

//            if(map == null) return list;
            ArrayList<String> fullList = (ArrayList<String>) map.get(word);
            if (map.containsKey(word) && fullList.size() <= M) {
                return (ArrayList) map.get(word);
            }
            map.put(word, list);
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                return list;
            }
            Set set = new TreeSet();
            ArrayList<Synset> senses = this.getSynsetFront_Mses(word, pos, M);
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = senses.get(i);
                PointerTargetTree ptt = pu_.getHypernymTree(sense, N);
                ArrayList<PointerTargetNodeList> pttList = (ArrayList<PointerTargetNodeList>) ptt.toList();
                for (PointerTargetNodeList ptnl : pttList) {
                    set.addAll(getLemmas(ptnl));
                }
                list.addAll(set);
            }
            HashSet sss = new HashSet();
            sss.addAll(list);
            list.clear();
            list.addAll(sss);
            list.removeAll(this.getSynonymFront_Mses(word, pos, M));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * m2w: this method check if 2 words have the same direct hypernym.
     *
     * @param s1
     * @param s2
     * @param pos
     * @return
     * @lastupdate 8/1/12 4:04 PM
     */
    public String getSameDirHyperFront_Nlvl_Mses(String s1, String s2, String pos, Integer N, Integer M) {
        ArrayList<String> ls1 = this.getHypernymFront_Nlvl_Mses(s1, pos, N, M);
        ArrayList<String> ls2 = this.getHypernymFront_Nlvl_Mses(s2, pos, N, M);
        for (String hyp1 : ls1) {
            for (String hyp2 : ls2) {
                if (hyp1.equalsIgnoreCase(hyp2)) {
                    return hyp1;
                }
            }
        }
        return null;
    }

    /**
     * m2w: is hyper in front n levels and front m senses
     *
     * @param s1
     * @param s2
     * @param pos
     * @param N
     * @param M
     * @return
     * @lastupdate 8/2/12 12:08 PM
     */
    public boolean isHypernymFront_Nlvl_Mses(String s1, String s2, String pos, Integer N, Integer M) {
        ArrayList<String> s1list = this.getHypernymFront_Nlvl_Mses(s1.trim().toLowerCase(), pos, N, M);
        ArrayList<String> s2list = this.getHypernymFront_Nlvl_Mses(s2, pos, N, M);
        System.out.println("s1list: " + s1list);
        System.out.println("s2list: " + s2list);
        if (s1list.contains(s2)) {
            for (int i = 1; i < M; i++) {
                ArrayList<String> s2syn = this.getSynonymAt_Mse(s2, pos, i);
                if (!s2syn.isEmpty() && s1list.containsAll(s2syn)) {
                    return true;
                }
            }
        }
        if (s2list.contains(s1)) {
            for (int i = 1; i < M; i++) {
                ArrayList<String> s1syn = this.getSynonymAt_Mse(s1, pos, i);
                if (!s1syn.isEmpty() && s2list.containsAll(s1syn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * m2w: new generic method. return the higher level one in the 2 words.
     *
     * @param s1
     * @param s2
     * @param pos
     * @param N : front N levels
     * @param M : front M sensess
     * @return
     * @lastupdate 8/2/12 12:07 PM
     */
    public String isWhichHypernymFront_Nlvl_Mses(String s1, String s2, String pos, Integer N, Integer M) {
        ArrayList<String> s1list = this.getHypernymFront_Nlvl_Mses(s1, pos, N, M);
        ArrayList<String> s2list = this.getHypernymFront_Nlvl_Mses(s2, pos, N, M);
        if (s1list.contains(s2)) {
            for (int i = 1; i < M; i++) {
                ArrayList<String> s2syn = this.getSynonymAt_Mse(s2, pos, M);
                if (!s2syn.isEmpty() && s1list.containsAll(s2syn)) {
                    return s2;
                }
            }
        }
        if (s2list.contains(s1)) {
            for (int i = 1; i < M; i++) {
                ArrayList<String> s1syn = this.getSynonymAt_Mse(s1, pos, M);
                if (!s1syn.isEmpty() && s2list.containsAll(s1syn)) {
                    return s1;
                }
            }
        }
        return null;
    }

    /**
     * m2w: this method returns a ArrayList<String> which contains the meronym
     * of a certain word(part-whole relation).
     *
     * @param s
     * @return
     */
    public ArrayList<String> getMeronyms(String s, String pos, Integer M) {
//        System.out.println("word: " + s);
//        System.out.println("pos: "+pos);
//        System.out.println("M: " + M);
        try {
            if (mero_lists.containsKey(s)
                    && pos.equals("noun")) {
                return (ArrayList) mero_lists.get(s);
            }
            ArrayList list = new ArrayList();
            POS _pos = null;
            if (pos.equals("noun")) {
                _pos = NOUN;
                mero_lists.put(s, list);
            } else if (pos.equals("verb")) {
                return new ArrayList<String>();
            } else if (pos.equals("adjective")) {
                return new ArrayList<String>();
            }
            IndexWord iw = lookupIndexWord(_pos, s);
            if (iw == null) {
                return list;
            }
            ArrayList senses = this.getMeronyms(s, pos, M);
//            System.out.println("senses size: " + senses.size());
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = (Synset) senses.get(i);
                if (pu_ == null) {
                    System.out.println("pu is null!!!!!!!!!!!!!!!!!!!");
                    pu_ = PointerUtils.getInstance();
                }
                if (sense == null) {
                    System.out.println("sense is null!!!!!!!!!!!!!!!!!");
                    continue;
                }
                System.out.println(sense);
                PointerTargetNodeList ptnl = this.pu_.getMeronyms(sense);
                list.addAll(this.getLemmas(ptnl));
            }
            HashSet set = new HashSet();
            set.addAll(list);
            list.clear();
            list.addAll(set);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }

    }

    /**
     * m2w: generic version for get hypers.
     *
     * @param word
     * @param pos
     * @param N : front N levels
     * @param M : front M senses
     * @return
     * @lastupdate 8/21/12 1:09 PM
     */
    public ArrayList<String> getHypernymFront_Nlvl_Mses_actCtrl(String word, String pos, Integer N, Integer M, boolean excludeAction) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            HashMap map = null;
            POS _pos = null;
            if (M == 2) {
                if (pos.equalsIgnoreCase("noun")) {
                    _pos = NOUN;
                    map = hyper_noun_maps_front2senses_actCtrl;
                }
                if (pos.equalsIgnoreCase("verb")) {
                    _pos = VERB;
                    map = hyper_verb_maps_front2senses_actCtrl;
                }
                if (pos.equalsIgnoreCase("adj")) {
                    _pos = ADJECTIVE;
                    map = hyper_adj_maps_front2senses_actCtrl;
                }
            } else {
                if (pos.equalsIgnoreCase("noun")) {
                    _pos = NOUN;
                    map = hyper_noun_lists_actCtrl;
                } else if (pos.equalsIgnoreCase("verb")) {
                    _pos = VERB;
                    map = hyper_verb_lists_actCtrl;
                } else if (pos.equalsIgnoreCase("adj")) {
                    _pos = ADJECTIVE;
                    map = hyper_adj_lists_actCtrl;
                } else {
                    _pos = this.getPOS(pos, "1");
                }
            }

//            if(map == null) return list;
            ArrayList<String> fullList = (ArrayList<String>) map.get(word);
            if (map.containsKey(word) && fullList.size() <= M) {
                return (ArrayList) map.get(word);
            }
            map.put(word, list);
            IndexWord iw = lookupIndexWord(_pos, word);
            if (iw == null) {
                return list;
            }
            Set set = new TreeSet();
            ArrayList<Synset> senses = this.getSynsetFront_Mses(word, pos, M);
            for (int i = 0; i < senses.size(); i++) {
                Synset sense = senses.get(i);
                PointerTargetTree ptt = pu_.getHypernymTree(sense, N);
                ArrayList<PointerTargetNodeList> pttList = (ArrayList<PointerTargetNodeList>) ptt.toList();

                for (PointerTargetNodeList ptnl : pttList) {
                    set.addAll(getLemmas(ptnl));
                }
                if (excludeAction && set.contains("action")) {
                    continue;
                }
                list.addAll(set);
            }
            HashSet sss = new HashSet();
            sss.addAll(list);
            list.clear();
            list.addAll(sss);
            list.removeAll(this.getSynonymFront_Mses(word, pos, M));
            return list;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * m2w: should be a faster version of getting synonyms.
     *
     * @param s1
     * @param pos
     * @param M
     * @return
     * @lastupdate 8/21/12 1:09 PM
     */
    public ArrayList<String> getSynonymFront_Mses_actCtrl(String s1, String pos, Integer M, boolean excludeAction) {
        ArrayList<String> synlist = new ArrayList<String>();
        HashMap<String, ArrayList<String>> map = null;
        if (pos.equals("noun")) {
            map = syn_noun;
            if (map.containsKey(s1)) {
                return map.get(s1);
            }
        }
        if (pos.equals("verb")) {
            map = syn_verb;
            if (map.containsKey(s1)) {
                return map.get(s1);
            }
        }
        if (pos.equals("adj")) {
            map = syn_adj;
            if (map.containsKey(s1)) {
                return map.get(s1);
            }
        }
        map.put(s1, synlist);
        ArrayList<Synset> sslist = (ArrayList<Synset>) this.getSynsetAllSenses(s1, pos);
        for (Synset ss : sslist) {
            HashSet<String> subSet = new HashSet<String>();
            for (net.didion.jwnl.data.Word w : ss.getWords()) {
                subSet.add(w.getLemma());
            }
            if (excludeAction && subSet.contains("action")) {
                continue;
            }
            synlist.addAll(subSet);
        }

        HashSet<String> hs = new HashSet<String>();
        hs.addAll(synlist);
        synlist.clear();
        synlist.addAll(hs);
        return synlist;
    }

    /**
     * m2w: is syn new method, faster.
     *
     * @param s1
     * @param s2
     * @param pos
     * @param M
     * @return
     * @lastupdated 8/21/12 1:10 PM
     */
    public boolean isSynonymFront_Mses_actCtrl(String s1, String s2, String pos, Integer M, boolean excludeAction) {
        ArrayList<String> s1list = this.getSynonymFront_Mses_actCtrl(s1, pos, M, excludeAction);
        if (s1list.contains(s2)) {
            return true;
        }
        return false;
    }

    /**
     * m2w: new generic method. return the higher level one in the 2 words.
     *
     * @param s1
     * @param s2
     * @param pos
     * @param N : front N levels
     * @param M : front M sensess
     * @return
     * @lastupdate 8/2/12 12:07 PM
     */
    public String isWhichHypernymFront_Nlvl_Mses_actCtrl(String s1, String s2, String pos, Integer N, Integer M, boolean excludeAction) {
        ArrayList<String> s1list = this.getHypernymFront_Nlvl_Mses_actCtrl(s1, pos, N, M, excludeAction);
        ArrayList<String> s2list = this.getHypernymFront_Nlvl_Mses_actCtrl(s2, pos, N, M, excludeAction);
        if (s1list != null && s1list.contains(s2)) {
            for (int i = 1; i < M; i++) {
                ArrayList<String> s2syn = this.getSynonymAt_Mse_actCtrl(s2, pos, M, excludeAction);
                if (!s2syn.isEmpty() && s1list.containsAll(s2syn)) {
                    return s2;
                }
            }
        }
        if (s2list != null && s2list.contains(s1)) {
            for (int i = 1; i < M; i++) {
                ArrayList<String> s1syn = this.getSynonymAt_Mse_actCtrl(s1, pos, M, excludeAction);
                if (!s1syn.isEmpty() && s2list.containsAll(s1syn)) {
                    return s1;
                }
            }
        }
        return null;
    }

    public boolean isHyponymFront_Nlevel_Mses_actCtrl(String s1, String s2, String pos, Integer N, Integer M, boolean excludeAction) {
        ArrayList<String> s2list = this.getHypernymFront_Nlvl_Mses_actCtrl(s2, pos, N, M, excludeAction);
        if (s2list != null && s2list.contains(s1)) {
            for (int i = 1; i < M; i++) {
                ArrayList<String> s1syn = this.getSynonymAt_Mse_actCtrl(s1, pos, M, excludeAction);
                if (!s1syn.isEmpty() && s2list.containsAll(s1syn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * m2w: get synonyms at M th sense of the word.
     *
     * @param s1
     * @param pos
     * @param M
     * @return
     */
    public ArrayList<String> getSynonymAt_Mse_actCtrl(String s1, String pos, Integer M, boolean excludeAction) {
        ArrayList<String> synlist = new ArrayList<String>();
        HashMap<String, ArrayList<String>> map = null;
        if (M == 1 && pos.equals("noun")) {
            map = syn_noun_se1_actCtrl;
        }
        if (M == 1 && pos.equals("verb")) {
            map = syn_verb_se1_actCtrl;
        }
        if (M == 1 && pos.equals("adj")) {
            map = syn_adj_se1_actCtrl;
        }
        if (M == 2 && pos.equals("noun")) {
            map = syn_noun_se2_actCtrl;
        }
        if (M == 2 && pos.equals("verb")) {
            map = syn_verb_se2_actCtrl;
        }
        if (M == 2 && pos.equals("adj")) {
            map = syn_adj_se2_actCtrl;
        }
        if (map != null) {
            map.put(s1, synlist);
        }
        Synset ss = this.getSynsetAt_Mse(s1, pos, M);
        if (ss == null) {
            return synlist;
        }
        HashSet<String> subSet = new HashSet<String>();
        for (net.didion.jwnl.data.Word w : ss.getWords()) {
            subSet.add(w.getLemma());
            if (excludeAction && subSet.contains("action")) {
                continue;
            }
            synlist.addAll(subSet);
        }

        HashSet<String> hs = new HashSet<String>();
        hs.addAll(synlist);
        synlist.clear();
        synlist.addAll(hs);
        return synlist;
    }

    /**
     * m2w: this method check if 2 words have the same direct hypernym.
     *
     * @param s1
     * @param s2
     * @param pos
     * @return
     * @lastupdate 8/1/12 4:04 PM
     */
    public String getSameDirHyperFront_Nlvl_Mses_actCtrl(String s1, String s2, String pos, Integer N, Integer M, boolean excludeAction) {
        ArrayList<String> ls1 = this.getHypernymFront_Nlvl_Mses_actCtrl(s1, pos, N, M, excludeAction);
        ArrayList<String> ls2 = this.getHypernymFront_Nlvl_Mses_actCtrl(s2, pos, N, M, excludeAction);
        for (String hyp1 : ls1) {
            for (String hyp2 : ls2) {
                if (hyp1.equalsIgnoreCase(hyp2)) {
                    return hyp1;
                }
            }
        }
        return null;
    }

    /**
     * m2w: this returns the senses count, i.e. Familiarity & Polysemy Count. of
     * a word.
     *
     * @param word
     * @param pos
     * @return senses count
     * @lastupdate 2012-08-31
     */
    public int getPloysemyCount(String word, String pos) {
        return this.getSenses(word, pos).size();
    }

    /**
     * m2w: given an offset ,returns a synset
     *
     * @param pos
     * @param offset
     * @return
     * @lastupdate 9/7/12 1:06 PM
     */
    public Synset getSynsetAtOffset(String pos, long offset) {
        POS _pos = POS.getPOSForLabel(pos);
        try {
            return dico.getSynsetAt(_pos, offset);
        } catch (Exception e) {
            return null;
        }
    }

//-----------------m2w: sense-based calculation for clustering------------------
    public ArrayList<Synset> getSynonSynsetsFront_Mses_actCtrl(String word, String pos, Integer M, boolean doActCtrl) {
        //getSynsetFront_Mses already used hashmap. no need using again
        ArrayList<Synset> senseSet = new ArrayList<Synset>();
        senseSet.addAll(this.getSynsetFront_Mses(word, pos, M));
        //action control
//        if (doActCtrl) {
//            this.removeActionSynset(senseSet);
//        }
        return senseSet;
    }

    /**
     * m2w: this method returns a map of each sense's meronyms
     *
     * @param word
     * @param pos
     * @param M
     * @param doActCtrl
     * @return
     * @lastupdate 9/19/12 1:24 PM
     */
    public HashMap<Synset, HashSet<Synset>> getMeroSynsetsFront_Mses_actCtrl(String word, String pos, Integer M, boolean doActCtrl) {
        HashMap<String, HashMap<Synset, HashSet<Synset>>> map = null;
        HashMap<Synset, HashSet<Synset>> result = new HashMap<Synset, HashSet<Synset>>();
        //close hyper use different map
        if (pos.equalsIgnoreCase("noun")) {
            map = mero_word_noun_map_;
        } else if (pos.equalsIgnoreCase("verb")) {
            map = mero_word_verb_map_;
        } else if (pos.equalsIgnoreCase("adj")) {
            map = mero_word_adj_map_;
        } else {
            System.out.println("pos is not supported, plz check pos type or add current pos in");
        }

        if (map.containsKey(word)) {
            return map.get(word);
        } else {

            try {
                ArrayList<Synset> ssList = this.getSynsetFront_Mses(word, pos, M);
                for (Synset ss : ssList) {
                    HashSet<Synset> senseSet = new HashSet<Synset>();
                    PointerTargetNodeList ptnl = this.pu_.getMeronyms(ss);
                    senseSet.addAll(this.getSenses(ptnl));
                    if (senseSet.contains(ss)) {
                        senseSet.remove(ss);//removed the root
                    }
                    result.put(ss, senseSet);
                }

                //action control
                if (doActCtrl) {
                    for (HashSet<Synset> sss : result.values()) {
                        this.removeActionSynset(sss);
                    }
                }
                //save in map
                map.put(word, result);

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }//closes else
    }

    /**
     * m2w: this method returns a map of the sense's meronyms
     *
     * @param word
     * @param pos
     * @param M
     * @param doActCtrl
     * @return
     * @lastupdate 9/19/12 1:24 PM
     */
    public HashSet<Synset> getMeroSynsetsOfOffset_Mses_actCtrl(Synset sense, String pos, boolean doActCtrl) {

        HashMap<Synset, HashSet<Synset>> map = null;
        HashSet<Synset> result = new HashSet<Synset>();
        //close hyper use different map
        if (pos.equalsIgnoreCase("noun")) {
            map = mero_offset_noun_map_;
        } else if (pos.equalsIgnoreCase("verb")) {
            map = mero_offset_verb_map_;
        } else if (pos.equalsIgnoreCase("adj")) {
            map = mero_offset_adj_map_;
        } else {
            System.out.println("pos is not supported, plz check pos type or add current pos in");
        }

        if (map.containsKey(sense)) {
            return map.get(sense);
        } else {
            try {
                PointerTargetNodeList ptnl = this.pu_.getMeronyms(sense);
                result.addAll(this.getSenses(ptnl));
                if (result.contains(sense)) {
                    result.remove(sense);//removed the root
                }

                //action control
                if (doActCtrl) {
                    this.removeActionSynset(result);
                }
                //save in map
                map.put(sense, result);

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }//closes else
    }

    public HashMap<Synset, HashSet<Synset>> getHyperSynsetsFront_Nlvl_Mses_actCtrl(String word, String pos, Integer N, Integer M, boolean doActCtrl) throws Exception {
        throw new Exception("BOOM!");
        /* HashMap<String, HashMap<Synset, HashSet<Synset>>> map = null;
        HashMap<Synset, HashSet<Synset>> result = new HashMap<Synset, HashSet<Synset>>();
        //close hyper use different map
        if (M == SourceClusterGeneric.CLOSE_HYPER_SENSE_ENG && N == SourceClusterGeneric.CLOSE_HYPER_LEVEL_ENG) {
            if (pos.equalsIgnoreCase("noun")) {
                map = hyper_word_close_noun_map_;
            } else if (pos.equalsIgnoreCase("verb")) {
                map = hyper_word_close_verb_map_;
            } else if (pos.equalsIgnoreCase("adj")) {
                map = hyper_word_close_adj_map_;
            } else {
                System.out.println("pos is not supported, plz check pos type or add current pos in");
            }
        } else {
            if (pos.equalsIgnoreCase("noun")) {
                map = hyper_word_general_noun_map_;
            } else if (pos.equalsIgnoreCase("verb")) {
                map = hyper_word_general_verb_map_;
            } else if (pos.equalsIgnoreCase("adj")) {
                map = hyper_word_general_adj_map_;
            } else {
                System.out.println("pos is not supported, plz check pos type or add current pos in");
            }
        }

        if (map.containsKey(word)) {
            return map.get(word);
        } else {
            ArrayList<Synset> ssList = this.getSynsetFront_Mses(word, pos, M);

            try {
                for (Synset ss : ssList) {
                    HashSet<Synset> senseSet = new HashSet<Synset>();
                    PointerTargetTree ptt = pu_.getHypernymTree(ss, N);
                    ArrayList<PointerTargetNodeList> pttList = (ArrayList<PointerTargetNodeList>) ptt.toList();
                    for (PointerTargetNodeList ptnl : pttList) {
                        senseSet.addAll(getSenses(ptnl));
                    }
                    if (senseSet.contains(ss)) {
                        senseSet.remove(ss);//removed the root
                    }
                    result.put(ss, senseSet);
                }
                //action control
                if (doActCtrl) {
                    for (HashSet<Synset> sss : result.values()) {
                        this.removeActionSynset(sss);
                    }
                }
                //save in map
                map.put(word, result);

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }//closes else
        */
    }

    public HashSet<Synset> getHyperSynsetsOfSense_Nlvl_actCtrl(Synset sense, String pos, Integer N, boolean doActCtrl) throws Exception {
        throw new Exception();
        /* HashMap<Synset, HashSet<Synset>> map = null;
        //close hyper use different map
        if (N == SourceClusterGeneric.CLOSE_HYPER_LEVEL_ENG) {
            if (pos.equalsIgnoreCase("noun")) {
                map = hyper_offset_close_noun_map_;
            } else if (pos.equalsIgnoreCase("verb")) {
                map = hyper_offset_close_verb_map_;
            } else if (pos.equalsIgnoreCase("adj")) {
                map = hyper_offset_close_adj_map_;
            } else {
                System.out.println("pos is not supported, plz check pos type or add current pos in");
            }
        } else {
            if (pos.equalsIgnoreCase("noun")) {
                map = hyper_offset_general_noun_map_;
            } else if (pos.equalsIgnoreCase("verb")) {
                map = hyper_offset_general_verb_map_;
            } else if (pos.equalsIgnoreCase("adj")) {
                map = hyper_offset_general_adj_map_;
            } else {
                System.out.println("pos is not supported, plz check pos type or add current pos in");
            }
        }

        if (map.containsKey(sense)) {
            return map.get(sense);
        } else {
            try {
                HashSet<Synset> senseSet = new HashSet<Synset>();
                if (sense == null) {
                    return null;
                }
                PointerTargetTree ptt = pu_.getHypernymTree(sense, N);
                ArrayList<PointerTargetNodeList> pttList = (ArrayList<PointerTargetNodeList>) ptt.toList();
                for (PointerTargetNodeList ptnl : pttList) {
                    senseSet.addAll(getSenses(ptnl));
                }
                if (senseSet.contains(sense)) {
                    senseSet.remove(sense);//removed the root
                }                //action control
                if (doActCtrl) {
                    this.removeActionSynset(senseSet);
                }
                //add to map.
                map.put(sense, senseSet);

                return senseSet;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } 
        */
    }

    /**
     * m2w: this method is used by isHypon_2WordsFront_Nlvl_Mses_actCtrl()
     *
     * @param word
     * @param pos
     * @param N
     * @param M
     * @param doActCtrl
     * @return
     * @lastupdate 9/18/12 11:25 AM
     */
    public HashMap<Synset, HashSet<Synset>> getHyponSynsetsFront_Nlvl_Mses_actCtrl(String word, String pos, Integer N, Integer M, boolean doActCtrl) {
        HashMap<Synset, HashSet<Synset>> result = new HashMap<Synset, HashSet<Synset>>();
        ArrayList<Synset> ssList = this.getSynsetFront_Mses(word, pos, M);
        for (Synset ss : ssList) {
            HashSet<Synset> hypoSet = this.getHyponSynsetsOfSense_Nlvl_actCtrl(ss, pos, N, doActCtrl);
            if (doActCtrl) {
                this.removeActionSynset(hypoSet);
            }//action control
            result.put(ss, hypoSet);
        }
        return result;
    }

    public HashSet<Synset> getHyponSynsetsOfSense_Nlvl_actCtrl(Synset sense, String pos, Integer N, boolean doActCtrl) {
        HashMap<Synset, HashSet<Synset>> map = null;
        //close hyper use different map
        if (pos.equalsIgnoreCase("noun")) {
            map = hypo_noun_map_offset;
        } else if (pos.equalsIgnoreCase("verb")) {
            map = hypo_verb_map_offset;
        } else if (pos.equalsIgnoreCase("adj")) {
            map = hypo_adj_map_offset;
        } else {
            System.out.println("pos is not supported, plz check pos type or add current pos in");
        }
        if (map.containsKey(sense)) {
            return map.get(sense);
        } else {
            HashSet<Synset> senseSet = new HashSet<Synset>();
            try {
                PointerTargetTree ptt = pu_.getHyponymTree(sense, N);
                ArrayList<PointerTargetNodeList> pttList = (ArrayList<PointerTargetNodeList>) ptt.toList();
                for (PointerTargetNodeList ptnl : pttList) {
                    senseSet.addAll(getSenses(ptnl));
                }
                if (senseSet.contains(sense)) {
                    senseSet.remove(sense);//removed the root
                }                //action control
                if (doActCtrl) {
                    this.removeActionSynset(senseSet);
                }
                //add to map.
                map.put(sense, senseSet);

                return senseSet;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    /**
     * m2w: this method checks if s2 is a hyponym of s1, will returns the s2's
     * synset that is found in s1's hyponym set, return null if not found. not
     * in use
     *
     * @param s1
     * @param s2
     * @param pos
     * @param N : levels
     * @param M : senses
     * @param doActCtrl
     * @return : s2's synset, null if not found
     */
    public Synset isHypon_2WordsFront_Nlvl_Mses_actCtrl(String s1, String s2, String pos, Integer N, Integer M, boolean doActCtrl) {
        HashMap<Synset, HashSet<Synset>> hypoMap1 = this.getHyponSynsetsFront_Nlvl_Mses_actCtrl(s1, pos, N, M, doActCtrl);
        ArrayList<Synset> synoSet2 = this.getSynonSynsetsFront_Mses_actCtrl(s2, pos, M, doActCtrl);
        for (HashSet<Synset> hypoSet1 : hypoMap1.values()) {
            for (Synset ss2 : synoSet2) {
                if (hypoSet1.contains(ss2)) {
                    return ss2;
                }
            }
        }
        return null;

    }

    /**
     * m2w: this is used by checking pre-def-domains's hypos
     *
     * @param offset
     * @param s2
     * @param pos
     * @param N
     * @param M
     * @param doActCtrl
     * @return
     */
    public Synset isHypon_ofOffset_Nlvl_Mses_actCtrl(long offset, String s2, String pos, Integer N, Integer M, boolean doActCtrl) {
        Synset sense1 = this.getSynsetAtOffset(pos, offset);
        HashSet<Synset> hypoSet1 = this.getHyponSynsetsOfSense_Nlvl_actCtrl(sense1, pos, N, doActCtrl);
        ArrayList<Synset> synoSet2 = this.getSynonSynsetsFront_Mses_actCtrl(s2, pos, M, doActCtrl);
//        System.out.println("hypoSet1: " + hypoSet1);
//        System.out.println("synoSet2: " + synoSet2);
        for (Synset ss2 : synoSet2) {
            if (hypoSet1.contains(ss2)) {
//                System.out.println("contains syno in hyposets!!!!!!!!!!!!!!!!!!");
                return ss2;
            }
        }
        return null;
    }

    public Synset isSynon_2WordsFront_Mses_actCtrl(String s1, String s2, String pos, Integer M, boolean doActCtrl) {
        ArrayList<Synset> synoSet1 = this.getSynonSynsetsFront_Mses_actCtrl(s1, pos, M, doActCtrl);
        ArrayList<Synset> synoSet2 = this.getSynonSynsetsFront_Mses_actCtrl(s2, pos, M, doActCtrl);
        for (Synset ss1 : synoSet1) {
            if (synoSet2.contains(ss1)) {
                return ss1;
            }
        }
        return null;
    }

    public Synset isSynon_ofOffset_Front_Mses_actCtrl(long offset, String s2, String pos, Integer M, boolean doActCtrl) {
        Synset ss1 = this.getSynsetAtOffset(pos, offset);
        ArrayList<Synset> synoSet2 = this.getSynonSynsetsFront_Mses_actCtrl(s2, pos, M, doActCtrl);
        if (synoSet2.contains(ss1)) {
            return ss1;
        }
        return null;
    }

    public boolean isSynon_2Offsets_actCtrl(long offset1, long offset2, String pos) {
        Synset ss1 = this.getSynsetAtOffset(pos, offset1);
        Synset ss2 = this.getSynsetAtOffset(pos, offset2);
        if (ss1.equals(ss2)) {
            return true;
        }
        return false;
    }

    /**
     * m2w: this check if s2 is hyper of s1
     *
     * @param s1
     * @param s2
     * @param pos
     * @param N
     * @param M
     * @param doActCtrl
     * @return : if not hyper return null, if is return the sense that matches
     * @lastupdate 9/18/12 12:30 PM
     */
    public HashMap<String, Synset> isHyper_2WordsFront_Nlvl_Mses_actCtrl(String s1, String s2, String pos, Integer N, Integer M, boolean doActCtrl) throws Exception {
        HashMap<Synset, HashSet<Synset>> hyperMap1 = this.getHyperSynsetsFront_Nlvl_Mses_actCtrl(s1, pos, N, M, doActCtrl);
        ArrayList<Synset> synoSet2 = this.getSynonSynsetsFront_Mses_actCtrl(s2, pos, M, doActCtrl);
        HashMap<String, Synset> map = new HashMap<String, Synset>();
        for (Synset ss1 : hyperMap1.keySet()) {
            for (Synset ss2 : synoSet2) {
                if (hyperMap1.get(ss1).contains(ss2)) {
                    //                return ss2;
                    map.put(s1, ss1);
                    map.put(s2, ss2);
                    return map;
                }
            }
        }
        return null;
    }

    /**
     * m2w: this check if s2 is hyper of offset's sense
     *
     * @param s1
     * @param s2
     * @param pos
     * @param N
     * @param M
     * @param doActCtrl
     * @return : if not hyper return null, if is return the sense that matches
     * @lastupdate 9/18/12 12:30 PM
     */
    public Synset isHyper_ofOffset_Nlvl_Mses_actCtrl(long offset, String s2, String pos, Integer N, Integer M, boolean doActCtrl) throws Exception {
        Synset sense1 = this.getSynsetAtOffset(pos, offset);
        HashSet<Synset> hyperSet1 = this.getHyperSynsetsOfSense_Nlvl_actCtrl(sense1, pos, N, doActCtrl);
        ArrayList<Synset> synoSet2 = this.getSynonSynsetsFront_Mses_actCtrl(s2, pos, M, doActCtrl);
        for (Synset ss2 : synoSet2) {
            if (hyperSet1.contains(ss2)) {
                return ss2;
            }
        }
        return null;
    }

    public boolean isHyper_2Offsets_Nlvl_actCtrl(long offset1, long offset2, String pos, Integer N, boolean doActCtrl) throws Exception {
        Synset sense1 = this.getSynsetAtOffset(pos, offset1);
        Synset sense2 = this.getSynsetAtOffset(pos, offset2);
        HashSet<Synset> hyperSet1 = this.getHyperSynsetsOfSense_Nlvl_actCtrl(sense1, pos, N, doActCtrl);
        if (hyperSet1 == null) {
            return false;
        }
        if (hyperSet1.contains(sense2)) {
            return true;
        }
        return false;
    }

    /**
     * m2w: this check if s2 is mero of s1
     *
     * @param s1
     * @param s2
     * @param pos
     * @param N
     * @param M
     * @param doActCtrl
     * @return : if not mero return null, if is return the sense that matches
     * @lastupdate 9/19/12 1:42 PM
     */
    public HashMap<String, Synset> isMero_2WordsFront_Mses_actCtrl(String s1, String s2, String pos, Integer M, boolean doActCtrl) {
        HashMap<Synset, HashSet<Synset>> hyperMap1 = this.getMeroSynsetsFront_Mses_actCtrl(s1, pos, M, doActCtrl);
        ArrayList<Synset> synoSet2 = this.getSynonSynsetsFront_Mses_actCtrl(s2, pos, M, doActCtrl);
        HashMap<String, Synset> map = new HashMap<String, Synset>();
        for (Synset ss1 : hyperMap1.keySet()) {
            for (Synset ss2 : synoSet2) {
                if (hyperMap1.get(ss1).contains(ss2)) {
                    map.put(s1, ss1);
                    map.put(s2, ss2);
                    return map;
                }
            }
        }
        return null;
    }

    /**
     * m2w: this check if s2 is mero of offset's sense
     *
     * @param s1
     * @param s2
     * @param pos
     * @param N
     * @param M
     * @param doActCtrl
     * @return : if not mero return null, if is return the sense that matches
     * @lastupdate 9/19/12 1:42 PM
     */
    public Synset isMero_ofOffset_Mses_actCtrl(long offset, String s2, String pos, Integer M, boolean doActCtrl) {
        Synset sense1 = this.getSynsetAtOffset(pos, offset);
        HashSet<Synset> hyperSet1 = this.getMeroSynsetsOfOffset_Mses_actCtrl(sense1, pos, doActCtrl);
        ArrayList<Synset> synoSet2 = this.getSynonSynsetsFront_Mses_actCtrl(s2, pos, M, doActCtrl);
        for (Synset ss2 : synoSet2) {
            if (hyperSet1.contains(ss2)) {
                return ss2;
            }
        }
        return null;
    }

    public boolean isMero_2Offsets_actCtrl(long offset1, long offset2, String pos, boolean doActCtrl) {
        Synset sense1 = this.getSynsetAtOffset(pos, offset1);
        Synset sense2 = this.getSynsetAtOffset(pos, offset2);
        HashSet<Synset> hyperSet1 = this.getMeroSynsetsOfOffset_Mses_actCtrl(sense1, pos, doActCtrl);
        if (hyperSet1.contains(sense2)) {
            return true;
        }
        return false;
    }

    /**
     * m2w: this method removes all "action"'s synsets in the given senseSet
     *
     * @param senseSet
     * @lastupdate 9/17/12 11:43 AM
     */
    private void removeActionSynset(HashSet<Synset> senseSet) {
        ArrayList<Synset> sslist = this.getSenses("action", "noun");
        senseSet.removeAll(sslist);
    }

    /**
     * m2w: this method calculate which sense it is in all the senses of a
     * certain word and returns the sense number.
     *
     * @param word : the word we want to find the sense number
     * @param pos : pos
     * @param offset : the offset we want to check
     * @return : sense number, 0 if not found.
     * @lastupdate 9/17/12 11:42 AM
     */
    public int getWhichSenseNumber(String word, String pos, long offset) {
        ArrayList<Synset> ssList = this.getSynsetAllSenses(word, pos);//10/18/12 3:39 PM
        for (int i = 0; i < ssList.size(); i++) {
            Synset ss = ssList.get(i);
            if (ss.getOffset() == offset) {
                return i + 1;
            }
        }
        return 0;
    }

    public void cleanUp() {
        hyper_noun_lists_.clear();
        hyper_adj_lists_.clear();
        hyper_verb_lists_.clear();
        hypo_noun_lists_.clear();
        hypo_verb_lists_.clear();
        n_synonym_list_.clear();
        a_synonym_list_.clear();
        v_synonym_list_.clear();
        lemmas_.clear();
        lemma_.clear();
        mero_lists.clear();
        hyper_noun_maps_close_old.clear();
        hyper_adj_maps_close_old.clear();
        hyper_verb_maps_close_old.clear();
        front2Senses_.clear();
        firstSense_.clear();
        syn_verb.clear();
        syn_noun.clear();
        syn_adj.clear();
        Synsets_noun.clear();
        Synsets_verb.clear();
        Synsets_adj.clear();
        hyp_noun_1stsense_dir.clear();
        hyp_verb_1stsense_dir.clear();
        hyp_adj_1stsense_dir.clear();
        hyp_noun_2ndsense_dir.clear();
        hyp_verb_2ndsense_dir.clear();
        hyp_adj_2ndsense_dir.clear();
        syn_noun_se1.clear();
        syn_verb_se1.clear();
        syn_adj_se1.clear();
        syn_noun_se2.clear();
        syn_verb_se2.clear();
        syn_adj_se2.clear();

        hyper_noun_maps_front2senses_actCtrl.clear();
        hyper_adj_maps_front2senses_actCtrl.clear();
        hyper_verb_maps_front2senses_actCtrl.clear();
        syn_noun_se1_actCtrl.clear();
        syn_verb_se1_actCtrl.clear();
        syn_adj_se1_actCtrl.clear();
        syn_noun_se2_actCtrl.clear();
        syn_verb_se2_actCtrl.clear();
        syn_adj_se2_actCtrl.clear();
        hyper_noun_lists_actCtrl.clear();
        hyper_verb_lists_actCtrl.clear();
        hyper_adj_lists_actCtrl.clear();

        // m2w: sense-based clustering 9/18/12 11:28 AM
        hyper_word_close_noun_map_.clear();
        hyper_word_close_adj_map_.clear();
        hyper_word_close_verb_map_.clear();
        hyper_word_general_noun_map_.clear();
        hyper_word_general_adj_map_.clear();
        hyper_word_general_verb_map_.clear();
        hypo_noun_map_offset.clear();
        hypo_verb_map_offset.clear();
        hypo_adj_map_offset.clear();
        hyper_offset_close_noun_map_.clear();
        hyper_offset_close_adj_map_.clear();
        hyper_offset_close_verb_map_.clear();
        hyper_offset_general_noun_map_.clear();
        hyper_offset_general_adj_map_.clear();
        hyper_offset_general_verb_map_.clear();
        mero_word_noun_map_.clear();
        mero_word_adj_map_.clear();
        mero_word_verb_map_.clear();
        mero_offset_noun_map_.clear();
        mero_offset_adj_map_.clear();
        mero_offset_verb_map_.clear();
    }

    public int getHypoTreeSizeofOffsetAtLevel(long offset, String pos, int level) {
        try {

            Synset ss = this.getSynsetAtOffset(pos, offset);
            if (level == 1) {
                return this.pu_.getHyponymTree(ss, 1).toList().size();
            } else {
                int sizeToLevel = this.pu_.getHyponymTree(ss, level).toList().size();
                int sizeToPrevLevel = this.pu_.getHyponymTree(ss, level - 1).toList().size();
                int sizeAtLevel = sizeToLevel - sizeToPrevLevel;
                return sizeAtLevel;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getHypoTreeHeightofOffset(long offset, String pos) {
        try {
            Synset ss = this.getSynsetAtOffset(pos, offset);
            int wholeTreeSize = this.pu_.getHyponymTree(ss).toList().size();
            System.out.println("whole" + wholeTreeSize);
            int currSize = -1;
            int height = 1;
            while (currSize != wholeTreeSize) {
                currSize = this.pu_.getHyponymTree(ss, height++).toList().size();
//                System.out.println("---"+currSize);
            }

            return height;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        Wordnet w = new Wordnet();

//        Synset s = w.getSynsetAtOffset("noun", 3733281);
        System.out.println(w.getHyponym("tree", "noun"));
        System.out.println("derived: " + w.getDerived("refuel"));
//        System.out.println(w.getHyponym("restrain", "verb"));
//        System.out.println(w.getHyponym("restrain", "verb"));
        System.out.println(w.getBaseForm("higher", "adjective"));
        System.out.println(w.getHyponSynsetsFront_Nlvl_Mses_actCtrl("spread", "verb", 2, 4, false));
//        w.getVerbFrame("graduate", "verb");
//        w.getVerbFrame("attend", "verb");
//        w.getVerbFrame("kill", "verb");
//        w.getVerbFrame("enroll", "verb");
//        w.semRelated("graduate", "student", "left");
//        System.out.println(s);
//        System.out.println(w.getHyponSynsetsOfSense_Nlvl_actCtrl(s, "noun", 4, true));
        // Synset s = w.getSynsetAtOffset("noun", 3733281);
        // w.getVerbFrame("graduate", "verb");
        // w.getVerbFrame("attend", "verb");
        //  w.getVerbFrame("kill", "verb");
        //  w.getVerbFrame("enroll", "verb");
        // w.semRelated("graduate", "student", "left");
        System.out.println(w.getBaseForm("killing", "verb"));

        // System.out.println(w.getHyponSynsetsOfSense_Nlvl_actCtrl(s, "noun", 4, true));
//
//        String pos = "noun";
//        int N = 4;
//        int M = 2; 
        Synset carnivores1 = w.getSynsetAt_Mse("bleed", "verb", 1);

//        Synset carnivores2 = w.getSynsetAt_Mse("carnivores", "noun", 2);
//        Synset predator1 = w.getSynsetAt_Mse("predator", "noun", 1);
//        Synset predator2 = w.getSynsetAt_Mse("predator", "noun", 2);
        System.out.println(carnivores1);
//        System.out.println(carnivores2);
//        System.out.println(predator1);
//        System.out.println(predator2);
////        System.out.println(w.isSynon_2WordsFront_Mses_actCtrl("wolf", "wolves", "noun", 2, true));
//        System.out.println(w.getHyponSynsetsOfSense_Nlvl_actCtrl(predator1, "noun", 4, true));
//        System.out.println(w.getHyponSynsetsOfSense_Nlvl_actCtrl(predator2, "noun", 4, true));
//        System.out.println(w.isHypon_ofOffset_Nlvl_Mses_actCtrl(2152740, "carnivores", pos, N, 2, true));
////        System.out.println(w.gethyper);
//        System.out.println();
//        Synset ss = w.getSynsetAtOffset("noun", 14580897);
//        Synset ss = w.getFirstSense("meat", POS.NOUN);
//        System.out.println(w.getSynsetFront_Mses("meat", "noun", 2));
//        
//        System.out.println(ss);
//        
//        Date start = new Date();
//        double q  = Math.random() * 10;
//        IndexWord iw = w.lookupIndexWord(POS.NOUN, "meat");
//        for(int i = 0; i < 500; i++){
//            for(int j = i+1; j < 500; j++){
//                w.getSynsetFront_Mses(q + "", "noun", 2);
//            }
////            w.isSynon_2WordsFront_Mses_actCtrl("meat", "car", "noun", 2, true);
//        }
//        Date end = new Date();
//        System.out.println(end.getTime() - start.getTime());
        try {
//            PointerTargetTree ptt = w.pu_.getHyponymTree(ss);
//            System.out.println("size of whole list"+ptt.toList().size());
//            System.out.println();
//            PointerTargetTree ptt1 = w.pu_.getHyponymTree(ss, 1);
//            ArrayList<PointerTargetTreeNode> ptl1 = (ArrayList<PointerTargetTreeNode>)ptt1.toList();
//            System.out.println("size of front 1 level list"+ptt1.toList().size());
//            System.out.println("ptl1: " + ptl1.size());
//            
//            PointerTargetTree ptt2 = w.pu_.getHyponymTree(ss, 2);
//            ArrayList<PointerTargetTreeNode> ptl2 = (ArrayList<PointerTargetTreeNode>)ptt2.toList();
//            ptl2.removeAll(ptl1);
//            System.out.println("size of front 2 level list"+ptt2.toList().size());
//            System.out.println("ptl2: " + ptl2.size());
//            
//            
//            PointerTargetTree ptt3 = w.pu_.getHyponymTree(ss, 3);
//            ArrayList ptl3 = (ArrayList)ptt3.toList();
//            ptl3.removeAll(ptl2);
//            System.out.println("size of front 3 level list"+ptt3.toList().size());
//            System.out.println("ptl3: " + ptl3.size());
//            
//            
//            PointerTargetTree ptt4 = w.pu_.getHyponymTree(ss, 4);
//            ArrayList ptl4 = (ArrayList)ptt4.toList();
//            ptl4.removeAll(ptl3);
//            System.out.println("size of front 4 level list"+ptt4.toList().size());
//            System.out.println("ptl4: " + ptl4.size());
//            
//            
//            PointerTargetTree ptt5 = w.pu_.getHyponymTree(ss, 5);
//            ArrayList ptl5 = (ArrayList)ptt5.toList();
//            ptl5.removeAll(ptl4);
//            System.out.println("size of front 5 level list"+ptt5.toList().size());
//            System.out.println("ptl5: " + ptl5.size());
//            System.out.println(w.getHypoTreeSizeofOffsetAtLevel(7649854, "noun", 1));
//            System.out.println(w.getHypoTreeSizeofOffsetAtLevel(7649854, "noun", 2));
//            System.out.println(w.getHypoTreeSizeofOffsetAtLevel(7649854, "noun", 3));
//            System.out.println(w.getHypoTreeSizeofOffsetAtLevel(7649854, "noun", 4));
//            System.out.println(w.getHypoTreeSizeofOffsetAtLevel(7649854, "noun", 5));
//            System.out.println(w.getHypoTreeHeightofOffset(7649854, "noun"));
//            ptt.print();
//            PointerTargetTreeNode rootNode = (PointerTargetTreeNode)ptt.getRootNode();
//            PointerTargetTreeNodeList list = rootNode.getChildTreeList();
//            System.out.println("root children size "+list.size());
//            for(int i = 0; i < list.size(); i++){
//                PointerTargetTreeNode ptnTemp = (PointerTargetTreeNode)list.get(i);
//                ptnTemp.hasPointerTreeList();
//                boolean explorable = true;
////                while(explorable){
////                    
////                }
//            }
            //        System.out.println(w.lookupIndexWord(POS.NOUN, "body _part"));
            //        System.out.println(w.lookupIndexWord(POS.NOUN, "body part"));
            //        System.out.println(w.lookupIndexWord(POS.NOUN, "stuff_water"));
            //        System.out.println(w.lookupIndexWord(POS.NOUN, "stuff-water"));
            //        System.out.println(w.lookupIndexWord(POS.NOUN, "stuff water"));
            //        System.out.println(w.lookupIndexWord(POS.NOUN, "NATION-STATE"));
            //        System.out.println(w.lookupIndexWord(POS.NOUN, "NATION_STATE"));
            //        String prevOffset = "19000";
            //        for(long i = 19000; i < 124000; i++){
            //            Synset ss = w.getSynsetAtOffset("noun", i);
            //            if(ss == null) continue;
            //            String tempOffset = String.valueOf(ss.getOffset());
            //            if(prevOffset.contains(tempOffset)) continue;
            //            System.out.println("i: "+ i +" --- " +ss);
            //            prevOffset = tempOffset;
            ////            System.out.println(prevOffset +"--" +tempOffset);
            ////            System.out.println();
            //        }
            //        Synset ss = w.getSynsetAtOffset("noun", 14580897);
            //        Synset ss = w.getSynsetAt_Mse("stuff_water", "noun", 1);
            //        Synset ss = w.getSynsetAt_Mse("stuff-water", "noun", 2);
            //        System.out.println(ss);
            //        System.out.println(w.getSenses("tangle", "noun"));
            //        System.out.println(w.getSenses("tangle", "noun"));
        } catch (Exception ex) {
            Logger.getLogger(Wordnet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //==================================test=============================
    public HashMap hyper_noun_lists_ = new HashMap();
    public HashMap hyper_adj_lists_ = new HashMap();
    public HashMap hyper_verb_lists_ = new HashMap();
    public HashMap hypo_noun_lists_ = new HashMap();
    public HashMap hypo_verb_lists_ = new HashMap();
    /*
     * -----------------------m2w: for clustering------------------------------
     */
    //old maps
    private HashMap hyper_noun_maps_close_old = new HashMap();
    private HashMap hyper_adj_maps_close_old = new HashMap();
    private HashMap hyper_verb_maps_close_old = new HashMap();
    private HashMap mero_lists = new HashMap();
    private HashMap hyper_noun_maps_front2senses = new HashMap();
    private HashMap hyper_adj_maps_front2senses = new HashMap();
    private HashMap hyper_verb_maps_front2senses = new HashMap();
    private HashMap hyp_noun_1stsense_dir = new HashMap();
    private HashMap hyp_adj_1stsense_dir = new HashMap();
    private HashMap hyp_verb_1stsense_dir = new HashMap();
    private HashMap hyp_noun_2ndsense_dir = new HashMap();
    private HashMap hyp_verb_2ndsense_dir = new HashMap();
    private HashMap hyp_adj_2ndsense_dir = new HashMap();
    private HashMap front2Senses_ = new HashMap();
    private HashMap firstSense_ = new HashMap();
    private HashMap syn_verb = new HashMap();
    private HashMap syn_noun = new HashMap();
    private HashMap syn_adj = new HashMap();
    private HashMap syn_verb_se1 = new HashMap();
    private HashMap syn_noun_se1 = new HashMap();
    private HashMap syn_adj_se1 = new HashMap();
    private HashMap syn_verb_se2 = new HashMap();
    private HashMap syn_noun_se2 = new HashMap();
    private HashMap syn_adj_se2 = new HashMap();
    private HashMap Synsets_noun = new HashMap();
    private HashMap Synsets_verb = new HashMap();
    private HashMap Synsets_adj = new HashMap();
    //-----------------------sense-based-calculation----------------------------
    //meromaps:
    private HashMap<String, HashMap<Synset, HashSet<Synset>>> mero_word_noun_map_ = new HashMap<String, HashMap<Synset, HashSet<Synset>>>();
    private HashMap<String, HashMap<Synset, HashSet<Synset>>> mero_word_adj_map_ = new HashMap<String, HashMap<Synset, HashSet<Synset>>>();
    private HashMap<String, HashMap<Synset, HashSet<Synset>>> mero_word_verb_map_ = new HashMap<String, HashMap<Synset, HashSet<Synset>>>();
    private HashMap<Synset, HashSet<Synset>> mero_offset_noun_map_ = new HashMap<Synset, HashSet<Synset>>();
    private HashMap<Synset, HashSet<Synset>> mero_offset_adj_map_ = new HashMap<Synset, HashSet<Synset>>();
    private HashMap<Synset, HashSet<Synset>> mero_offset_verb_map_ = new HashMap<Synset, HashSet<Synset>>();
    //hypermaps:
    private HashMap<String, HashMap<Synset, HashSet<Synset>>> hyper_word_close_noun_map_ = new HashMap<String, HashMap<Synset, HashSet<Synset>>>();
    private HashMap<String, HashMap<Synset, HashSet<Synset>>> hyper_word_close_adj_map_ = new HashMap<String, HashMap<Synset, HashSet<Synset>>>();
    private HashMap<String, HashMap<Synset, HashSet<Synset>>> hyper_word_close_verb_map_ = new HashMap<String, HashMap<Synset, HashSet<Synset>>>();
    private HashMap<String, HashMap<Synset, HashSet<Synset>>> hyper_word_general_noun_map_ = new HashMap<String, HashMap<Synset, HashSet<Synset>>>();
    private HashMap<String, HashMap<Synset, HashSet<Synset>>> hyper_word_general_adj_map_ = new HashMap<String, HashMap<Synset, HashSet<Synset>>>();
    private HashMap<String, HashMap<Synset, HashSet<Synset>>> hyper_word_general_verb_map_ = new HashMap<String, HashMap<Synset, HashSet<Synset>>>();
    private HashMap<Synset, HashSet<Synset>> hyper_offset_close_noun_map_ = new HashMap<Synset, HashSet<Synset>>();
    private HashMap<Synset, HashSet<Synset>> hyper_offset_close_adj_map_ = new HashMap<Synset, HashSet<Synset>>();
    private HashMap<Synset, HashSet<Synset>> hyper_offset_close_verb_map_ = new HashMap<Synset, HashSet<Synset>>();
    private HashMap<Synset, HashSet<Synset>> hyper_offset_general_noun_map_ = new HashMap<Synset, HashSet<Synset>>();
    private HashMap<Synset, HashSet<Synset>> hyper_offset_general_adj_map_ = new HashMap<Synset, HashSet<Synset>>();
    private HashMap<Synset, HashSet<Synset>> hyper_offset_general_verb_map_ = new HashMap<Synset, HashSet<Synset>>();
    //hypomaps:
    private HashMap<Synset, HashSet<Synset>> hypo_noun_map_offset = new HashMap();
    private HashMap<Synset, HashSet<Synset>> hypo_verb_map_offset = new HashMap();
    private HashMap<Synset, HashSet<Synset>> hypo_adj_map_offset = new HashMap();
    //action control:
    private HashMap hyper_noun_maps_front2senses_actCtrl = new HashMap();
    private HashMap hyper_adj_maps_front2senses_actCtrl = new HashMap();
    private HashMap hyper_verb_maps_front2senses_actCtrl = new HashMap();
    private HashMap syn_verb_se1_actCtrl = new HashMap();
    private HashMap syn_noun_se1_actCtrl = new HashMap();
    private HashMap syn_adj_se1_actCtrl = new HashMap();
    private HashMap syn_verb_se2_actCtrl = new HashMap();
    private HashMap syn_noun_se2_actCtrl = new HashMap();
    private HashMap syn_adj_se2_actCtrl = new HashMap();
    public HashMap hyper_noun_lists_actCtrl = new HashMap();
    public HashMap hyper_adj_lists_actCtrl = new HashMap();
    public HashMap hyper_verb_lists_actCtrl = new HashMap();
    //used by deprecated method
    private HashMap hyper_noun_maps_front2sensesDir = new HashMap();
    private HashMap hyper_adj_maps_front2sensesDir = new HashMap();
    private HashMap hyper_verb_maps_front2sensesDir = new HashMap();
    /*
     * -----ends------m2w: for clustering
     */
    public final POS NOUN = POS.NOUN;
    public final POS VERB = POS.VERB;
    public final POS ADJECTIVE = POS.ADJECTIVE;
    public final POS ADVERB = POS.ADVERB;
    public HashMap n_synonym_list_ = new HashMap();
    public HashMap a_synonym_list_ = new HashMap();
    public HashMap v_synonym_list_ = new HashMap();
    public String synonym_file_ = null;
    public String hypernym_file_ = null;
    public HashMap lemmas_ = new HashMap(); //hashmap of hashmap
    public HashMap lemma_ = new HashMap();
    public HashMap w_lemmas_ = new HashMap();
    public HashMap senses_ = new HashMap();
    public HashMap bad_senses_ = new HashMap(); //key is type_sub-type and the value is arraylist of senses which shouldn't be used to for trigger
    RelationshipFinder rf_ = RelationshipFinder.getInstance();
    private HashMap<String, List> base_forms_ = new HashMap();
    public PointerUtils pu_ = PointerUtils.getInstance();
    private static HashMap<String, String> baseforms_ = new HashMap();

    public boolean semRelated(String rel, String concept, String position) {
        boolean related = false;
        ArrayList<String> rel_vframes = this.getVerbFrame(rel, "verb");
        System.out.println("rel: " + rel);
        System.out.println("position: " + position);
        for (String rel_vframe : rel_vframes) {
            System.out.println("rel_vframe: " + rel_vframe);
            String[] vf_items = rel_vframe.split(" ");
            String w_from_vf = vf_items[0];
            if (position.equals("right")) {
                if (vf_items.length == 3) {
                    w_from_vf = vf_items[2];
                } else if (vf_items.length == 2) {
                    w_from_vf = vf_items[1];
                }
            }
            System.out.println("w_from_vf: " + w_from_vf);

            if (!w_from_vf.startsWith("-")) {
                if (this.isHypernym(w_from_vf, concept, "noun")) {
                    return true;
                }
            }
        }
        return related;
    }
}