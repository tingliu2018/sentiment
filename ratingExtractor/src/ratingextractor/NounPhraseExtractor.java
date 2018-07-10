/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratingextractor;

import depdendcy.Graph;
import depdendcy.StanfordParser;
import depdendcy.WordDeleter;
import depdendcy.Stemmer;
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
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.BreakIterator;
import utils.Out;

/**
 *
 * @author Logan Brandt
 */
public class NounPhraseExtractor {

    private static String[] s = new String[550000];
    private static int counter = 0;
    private static WordDeleter wd = new WordDeleter();
    private static Stemmer st = new Stemmer();
    private static StanfordParser sp_ = new StanfordParser();
    private static String outPath = "C:\\Users\\Logan Brandt\\Documents\\internship\\ratingExtractor\\ratingExtractor\\text\\outputSentimentTest\\";

    public static void readObjectFromFiles(String filepath) {
        try {
            //reads object file and outputs it as a professor
            File f = new File(filepath);
            String name = f.getName();
            //System.out.println(name);
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            //System.out.println("The Object has been read from the file");
            objectIn.close();
            //downcast to Student Rate to grab rating of every rating
            Professor input = (Professor) obj;
            StudentRates ratings = input.getRatings();
            //StudentRate r = ratings.get(0);
            //System.out.println(r.getComment());
            //System.out.println(r.getNounPhrase());
            //for loop looping through the array of ratings to find each comment
            //String fName = input.getfName();
            // String lName = input.getlName();
            for (int i = 0; i < ratings.size(); i++) {//each individual comment
                ArrayList<String> finalNounPhrase = new ArrayList<>();
                StudentRate r = ratings.get(i);
                Graph g = new Graph();
                String rating = r.getRating();
                String comment = r.getComment();
                ArrayList<String> nounPhrases = getNounPhrases(comment);
                for (int j = 0; j < nounPhrases.size(); j++) {//each individual comment sentence
                    sp_.buildDependcyGraph(nounPhrases.get(j), g);
                    ArrayList<String> phrase = StanfordParser.getNounPhrases(g);
                    for (int k = 0; k < phrase.size(); k++) {//each individual noun phrase
                        String noun = wd.removeFromString(phrase.get(k));
                        noun = st.stemSentence(noun);
                        finalNounPhrase.add(noun);
                        //System.out.println(noun);
                    }
                    g = new Graph();
                }
                //printNounPhrases(finalNounPhrase);
                r.setNounPhrase(finalNounPhrase);
                String date = r.getDate();
                ArrayList<Tag> tags = new ArrayList<Tag>();
                tags = r.getTags();
                //ratings.set(i, r);
            }
            writeObj(input, name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static ArrayList<String> getNounPhrases(String comment) {
        ArrayList<String> answerArr = new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        iterator.setText(comment);
        int start = iterator.first();
        for (int end = iterator.next();
                end != BreakIterator.DONE;
                start = end, end = iterator.next()) {
            String sub = comment.substring(start, end);
            answerArr.add(sub);
        }
        return answerArr;
    }
    
    public static void writeObj(ratingextractor.Professor prof, String name) throws FileNotFoundException, IOException {
        String fileName = outPath + name;
        fileName = fileName.replace("objl", "obj");
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(prof);
        oos.close();
    }
    
    public static void printNounPhrases(ArrayList<String> nPhrase){
        for(int i = 0; i < nPhrase.size(); i++){
            System.out.println(nPhrase.get(i));
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //sets path to get test data
        Path dir = Paths.get("C:\\Users\\Logan Brandt\\Documents\\internship\\ratingExtractor\\ratingExtractor\\text\\sentiment1\\");
        //counter for array and array upper bound
        counter = 0;
        //gets all files name's in directory and stores them in an array

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                s[counter] = file.toString();
                counter++;
            }
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x);
        }
        // runs through all files in selected directory
        for (int r = 0; r < counter; r++) {//counter; r++) {
            readObjectFromFiles(s[r]);
            System.out.println(r);
        }
    }
}
