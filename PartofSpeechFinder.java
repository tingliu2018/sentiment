package parsertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

/**
 *
 * @author Thomas
 */
public class PartofSpeechFinder {

    public static void main(String[] args) throws JWNLException, FileNotFoundException {
        File lexicon = new File("/media/thomas/ESD-USB/newFiles/Scores.txt");
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();
        Scanner sc = new Scanner(lexicon);

        //Output files
        PrintWriter[] pw = new PrintWriter[4];
        pw[0] = new PrintWriter("/media/thomas/ESD-USB/Parts of Speech/Noun.txt");
        pw[1] = new PrintWriter("/media/thomas/ESD-USB/Parts of Speech/Verb.txt");
        pw[2] = new PrintWriter("/media/thomas/ESD-USB/Parts of Speech/Adjective.txt");
        pw[3] = new PrintWriter("/media/thomas/ESD-USB/Parts of Speech/Adverb.txt");
        IndexWord[] iWords = new IndexWord[4];
        
        sc.nextLine(); //Skip header line

        //Lookup all possible POS of a word, print it to the appropriate files
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String word = line.substring(0, line.indexOf(" "));
            iWords[0] = dictionary.getIndexWord(POS.NOUN, word);
            iWords[1] = dictionary.getIndexWord(POS.VERB, word);
            iWords[2] = dictionary.getIndexWord(POS.ADJECTIVE, word);
            iWords[3] = dictionary.getIndexWord(POS.ADVERB, word);

            for (int i = 0; i < iWords.length; i++) {
                if (iWords[i] != null) {
                    pw[i].println(iWords[i].getLemma());
                }

            }
        }
        
        for (int i = 0; i < pw.length; i++) {
            pw[i].close();
        }

    }
}
