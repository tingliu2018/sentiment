/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.process.DocumentPreprocessor;

/**
 *
 * @author thomas
 */
public class SentenceParser {
    
    public static void main(String[] args) throws FileNotFoundException {
        String filename = "/home/thomas/Desktop/totally not suspicious story from a creepy old man thats obviously not evil";
        File bestwordfile = new File(filename);
        sentenceParser(bestwordfile);
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

