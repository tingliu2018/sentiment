/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Code Monkey
 */
public class ReallyPoorlyNamedClassThatWellTotallyRememberWhatItDoes {

    public static void main(String[] args) throws FileNotFoundException, JWNLException {
        BasicConfigurator.configure();
        File inputFile = new File("/media/thomas/ESD-USB/Synset/Unknown/Unknown.txt");
        //File inputFile = new File("/home/thomas/Desktop/Known.txt");
        Scanner sc = new Scanner(inputFile);
        ArrayList<Word> expansionList = new ArrayList<Word>();
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();
        Wordnet net = new Wordnet();

        File output = new File("/media/thomas/ESD-USB/Synset/Unknown/expansionWords.txt");
        PrintWriter p = new PrintWriter(output);

        while (sc.hasNextLine()) {
            //Look up word as various parts of speech
            String[] lineSplit = sc.nextLine().split(" ");
            String word = lineSplit[0];
            if (lineSplit.length == 0) {
                System.out.println("Everything is pain!");
                System.exit(8);
            }
            if (lineSplit.length < 3) {
                System.out.println(word);
                IndexWord iWord = dictionary.getIndexWord(POS.ADJECTIVE, word);
                if (iWord == null) {
                    iWord = dictionary.getIndexWord(POS.ADVERB, word);
                    if (iWord == null) {
                       
                    } else {
                        expansionList.add(new Word(word, Double.parseDouble(lineSplit[1])));
                    }
                } else {
                    expansionList.add(new Word(word, Double.parseDouble(lineSplit[1])));
                }
            }
        }
        for (int i = 0; i < expansionList.size(); i++) {
            System.out.println("There are words");
            p.println(expansionList.get(i).getWord() + " " + expansionList.get(i).getScore());
        }
        p.close();
    }
}
