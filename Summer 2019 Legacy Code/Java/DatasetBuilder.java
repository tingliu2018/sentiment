package parsertest;

import edu.stanford.nlp.trees.Tree;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.process.DocumentPreprocessor;
import java.io.Reader;
import java.io.StringReader;

/**
 * Builds all the dataset files with the exception of STree and SOStr which must
 * be made manually by replacing spaces with | in parents.txt and in the
 * masterFile
 * 
 * There are 10 types of people those who understand this code and those who don't
 *
 * @author Code Monkey A & B
 */
public class DatasetBuilder {

    /*
    Number of randomly chosen comments
     */
    protected static final int NUMCOMMENTS = 10;
    protected static final String ROOT = "/media/thomas/ESD-USB/"; //This is where your files are stored
    protected static final String RAWDIR = ROOT + "rawfiles/"; //Raw files subdirectory
    protected static final String DATADIR = ROOT + "dataset/";

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File lexicon = new File(ROOT + "Lexicon.csv");
        Scanner sc = new Scanner(lexicon);
        ArrayList<Word> words = new ArrayList();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] lineSplit = line.split("\t");
            if (lineSplit.length >= 2) {
                words.add(new Word(lineSplit[0], Double.parseDouble(lineSplit[1])));
            }
        }
        File dictionary = new File(DATADIR + "dictionary.txt");
        File labels = new File(DATADIR + "sentiment_labels.txt");

        PrintWriter dictWriter = new PrintWriter(dictionary);
        PrintWriter labelWriter = new PrintWriter(labels);

        labelWriter.println("phrase ids|sentiment values");
        for (int i = 0; i < words.size(); i++) {
            dictWriter.println(words.get(i).getWord() + "|" + (i));
            labelWriter.println((i) + "|" + words.get(i).getScore());
        }

        dictWriter.close();
        labelWriter.close();

        File[] rawFemaleFiles = {
            new File(RAWDIR + "FemaleAverageTags.txt"),
            new File(RAWDIR + "FemaleAwfulTags.txt"),
            new File(RAWDIR + "FemaleGoodTags.txt"),
            new File(RAWDIR + "FemalePoorTags.txt"),
            new File(RAWDIR + "FemaleAwesomeTags.txt")};

        File[] rawMaleFiles = {
            new File(RAWDIR + "MaleAverageTags.txt"),
            new File(RAWDIR + "MaleAwfulTags.txt"),
            new File(RAWDIR + "MaleGoodTags.txt"),
            new File(RAWDIR + "MalePoorTags.txt"),
            new File(RAWDIR + "MaleAwesomeTags.txt")};

        File masterFile = new File(DATADIR + "masterFile.txt"); //Contains all randomly selected comments
        File femaleFile = new File(DATADIR + "femaleSnippets.txt"); //Contains n/2 randomly selected female comments
        File maleFile = new File(DATADIR + "maleSnippets.txt"); //Contains n/2 randomly selected male comments

        fileWriter(rawMaleFiles, masterFile, maleFile);
        fileWriter(rawFemaleFiles, masterFile, femaleFile);
        datasetSentences(masterFile);
        parentWriter(masterFile);
        // This code is bad
        vocabWriter(new File(DATADIR + "sents.toks"));
        sentenceParser(masterFile);
        try {
        Runtime r = Runtime.getRuntime();
        Process p = r.exec("python SBuilder.py"); //Create STree and SOStr, requires python file in same directory
        } catch(Exception e){
            System.out.println("Failed to run SBuilder.py, is it in the correct directory?");
            e.printStackTrace();
        }
    }

    public static void vocabWriter(File tokens) throws FileNotFoundException {
        File[] files = {new File(DATADIR + "vocab.txt"), new File(DATADIR + "vocab-cased.txt")};
        PrintWriter[] pw = {new PrintWriter(files[0]), new PrintWriter(files[1])};

        Scanner sc = new Scanner(tokens);

        //Write vocab word files, one token per line
        while (sc.hasNextLine()) {
            String[] lineSplit = sc.nextLine().split(" ");
            for (String word : lineSplit) {
                for (PrintWriter p : pw) {
                    p.println(word);
                }
            }
        }

        for (PrintWriter p : pw) {
            p.close();
        }
    }

    public static void fileWriter(File[] arr, File masterFile, File outputFile) throws FileNotFoundException, IOException {
        Scanner sc;
        Random r = new Random();
        ArrayList<String> comments = new ArrayList();
        ArrayList<Integer> numbers = new ArrayList(); //Ensures no duplicate comments picked, stores indices
        int index;

        FileWriter fw = new FileWriter(masterFile, true); //Append to masterFile don't overwrite
        PrintWriter masterWriter = new PrintWriter(fw);
        PrintWriter outputWriter = new PrintWriter(outputFile);

        // Read all files and store all comments in an ArrayList
        for (int i = 0; i < arr.length; i++) {
            sc = new Scanner(arr[i]);
            while (sc.hasNextLine()) {
                comments.add(sc.nextLine());
            }
        }
        // Randomly choose comments
        for (int i = 0; i < NUMCOMMENTS / 2 && i < comments.size(); i++) {
            index = r.nextInt(comments.size());
            if (!numbers.contains(index)) {
                numbers.add(index);
                outputWriter.println(comments.get(index));
                masterWriter.println(comments.get(index));
            }
        }
        numbers.clear();
        comments.clear();

        masterWriter.close();
        outputWriter.close();

    }

    /**
     * Does spooky stuff to make treeee files, ask Dr. Liu if you are missing
     * the java files from the LSTM repo, that's not our code and therefore does
     * not have code monkey approval
     *
     * @param masterFile File to write to, will contian all comments
     * @throws IOException don't do this
     */
    public static void parentWriter(File masterFile) throws IOException {

        ConstituencyParse cp = new ConstituencyParse(DATADIR + "sents.toks", DATADIR + "parents.txt", false);

        List<HasWord> tokens = null;
        Scanner sc = new Scanner(masterFile);

        ArrayList<List<HasWord>> tokenList = new ArrayList<List<HasWord>>();

        while (sc.hasNextLine()) {
            tokens = cp.sentenceToTokens(sc.nextLine());
            tokenList.add(tokens);
        }

        ArrayList<Tree> trees = new ArrayList<Tree>();

        for (List<HasWord> token : tokenList) {
            cp.printTokens(token);
            trees.add(cp.parse(token));
        }

        ArrayList<int[]> parentList = new ArrayList<int[]>();
        ArrayList<int[]> dparentList = new ArrayList<int[]>();

        for (Tree tree : trees) {
            int[] parents = cp.constTreeParents(tree);
            parentList.add(parents);
        }

        for (int[] parent : parentList) {
            cp.printParents(parent);
        }

        ConstituencyParse cp2 = new ConstituencyParse(DATADIR + "sents.toks", DATADIR + "dparents.txt", false);

        for (int i = 0; i < trees.size(); i++) {
            int[] dparents = cp.depTreeParents(trees.get(i), tokenList.get(i));
            dparentList.add(dparents);

        }

        for (int[] dparent : dparentList) {
            cp2.printParents(dparent);
        }

        cp.close();
        cp2.close();
    }

    /**
     * Determines the ratio of sentences that go to the three folders(Train,Dev,Test).
     * Also writes datasetSentences.txt and datasetSplit.txt
     *
     * @param masterFile Output file
     * @throws FileNotFoundException don't do this
     */
    public static void datasetSentences(File masterFile) throws FileNotFoundException {
        File output = new File(DATADIR + "datasetSentences.txt");
        File dataSplitFile = new File(DATADIR + "datasetSplit.txt");
        Scanner sc = new Scanner(masterFile);
        PrintWriter sentenceWriter = new PrintWriter(output);
        PrintWriter splitWriter = new PrintWriter(dataSplitFile);
        sentenceWriter.println("sentence_index\tsentence");
        splitWriter.println("sentence_index,splitset_label");
        int counter = 1;
        while (sc.hasNextLine()) {
            sentenceWriter.println(counter + "\t" + sc.nextLine());
            splitWriter.println(counter + "," + 2); //MAY NEED REVISION TO SPLIT PROPERLY
            counter++;
        }
        sentenceWriter.close();
        splitWriter.close();
    }

    /**
     * Separate comments with multiple sentences in to separate comments of single sentences.
     * 
     * @param file
     * @throws FileNotFoundException 
     */
    public static void sentenceParser(File file) throws FileNotFoundException {
        //List of sentences that is to be printed
        List<String> sentenceList = new ArrayList<String>();
        Scanner sc = new Scanner(file);
        //goes through the original file
        while (sc.hasNextLine()) {
            String paragraph = sc.nextLine();
            //reads in the comments through standford parser
            Reader reader = new StringReader(paragraph);
            DocumentPreprocessor dp = new DocumentPreprocessor(reader);
            //splits the comments and adds them to our list of sentences
            for (List<HasWord> sentence : dp) {
                // SentenceUtils not Sentence
                String sentenceString = SentenceUtils.listToString(sentence);
                sentenceList.add(sentenceString);
            }
        }
        //overwrites the file with sentences seperated
        PrintWriter p = new PrintWriter(file);
        for (String sentence : sentenceList) {
            p.println(sentence);
        }
        p.close();
    }
}
