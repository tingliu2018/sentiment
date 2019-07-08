package parsertest;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.trees.Tree;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Thomas Talasco
 */
public class DatasetBuilder {
    
    protected static final int NUMCOMMENTS = 1000;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File lexicon = new File("/media/thomas/ESD-USB/Lexicon.csv");
        Scanner sc = new Scanner(lexicon);
        ArrayList<Word> words = new ArrayList();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] lineSplit = line.split("\t");
            if (lineSplit.length >= 2) {
                words.add(new Word(lineSplit[0], Double.parseDouble(lineSplit[1])));
            }
        }
        File dictionary = new File("/media/thomas/ESD-USB/dataset/dictionary.txt");
        File labels = new File("/media/thomas/ESD-USB/dataset/sentiment_labels.txt");
        
        PrintWriter dictWriter = new PrintWriter(dictionary);
        PrintWriter labelWriter = new PrintWriter(labels);
        
        for (int i = 0; i < words.size(); i++) {
            dictWriter.println(words.get(i).getWord() + "|" + (i + 1));
            labelWriter.println((i + 1) + "|" + words.get(i).getScore());
        }
        
        dictWriter.close();
        labelWriter.close();
        
        File[] rawFemaleFiles = {
            new File("/media/thomas/ESD-USB/FemaleAverageTags.txt"),
            new File("/media/thomas/ESD-USB/FemaleAwfulTags.txt"),
            new File("/media/thomas/ESD-USB/FemaleGoodTags.txt"),
            new File("/media/thomas/ESD-USB/FemalePoorTags.txt"),
            new File("/media/thomas/ESD-USB/FemaleAwesomeTags.txt")};
        
        File[] rawMaleFiles = {
            new File("/media/thomas/ESD-USB/MaleAverageTags.txt"),
            new File("/media/thomas/ESD-USB/MaleAwfulTags.txt"),
            new File("/media/thomas/ESD-USB/MaleGoodTags.txt"),
            new File("/media/thomas/ESD-USB/MalePoorTags.txt"),
            new File("/media/thomas/ESD-USB/MaleAwesomeTags.txt")};
        
        File masterFile = new File("/media/thomas/ESD-USB/dataset/masterFile.txt");
        File femaleFile = new File("/media/thomas/ESD-USB/dataset/femaleSnippets.txt");
        File maleFile = new File("/media/thomas/ESD-USB/dataset/maleSnippets.txt");
        
        fileWriter(rawMaleFiles, masterFile, maleFile);
        fileWriter(rawFemaleFiles, masterFile, femaleFile);
        datasetSentences(masterFile);
    }
    
    public static void fileWriter(File[] arr, File masterFile, File outputFile) throws FileNotFoundException, IOException {
        Scanner sc;
        Random r = new Random();
        ArrayList<String> sentences = new ArrayList();
        ArrayList<Integer> numbers = new ArrayList();
        int index;
        
        PrintWriter masterWriter = new PrintWriter(masterFile);
        PrintWriter outputWriter = new PrintWriter(outputFile);
        
        for (int i = 0; i < arr.length; i++) {
            sc = new Scanner(arr[i]);
            while (sc.hasNextLine()) {
                sentences.add(sc.nextLine());
            }
            for (int j = 0; j < NUMCOMMENTS && j < sentences.size(); j++) {
                index = r.nextInt(sentences.size());
                if (!numbers.contains(index)) {
                    numbers.add(index);
                    outputWriter.println(sentences.get(index));
                    masterWriter.println(sentences.get(index));
                }
            }
            numbers.clear();
            sentences.clear();
        }
        
        masterWriter.close();
        outputWriter.close();
        
        ConstituencyParse cp = new ConstituencyParse("/media/thomas/ESD-USB/dataset/sents.toks", "/media/thomas/ESD-USB/dataset/parents.txt", false);
        
        List<HasWord> tokens = null;
        sc = new Scanner(masterFile);
        
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
        
        ConstituencyParse cp2 = new ConstituencyParse("/media/thomas/ESD-USB/dataset/sents.toks", "/media/thomas/ESD-USB/dataset/dparents.txt", false);
        
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
    
    public static void datasetSentences(File masterFile) throws FileNotFoundException {
        File output = new File("/media/thomas/ESD-USB/dataset/datasetSentences.txt");
        File dataSplitFile = new File("/media/thomas/ESD-USB/dataset/datasetSplit.txt");
        Scanner sc = new Scanner(masterFile);
        PrintWriter pw = new PrintWriter(output);
        PrintWriter pw1 = new PrintWriter(dataSplitFile);
        pw.println("sentence_index\tsentence");
        pw1.println("sentence_index,splitset_label");
        int counter = 1;
        while (sc.hasNextLine()) {
            pw.println(counter + "\t" + sc.nextLine());
            pw1.println(counter + "," + 2);
            counter++;
        }
        pw.close();
        pw1.close();
    }
}
