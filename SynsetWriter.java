package parsertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.dictionary.Dictionary;

/**
 *
 * @author Thomas Talasco
 */
public class SynsetWriter {

    public static void main(String[] args) throws FileNotFoundException, JWNLException {
        File inputFile = new File("/home/thomas/Desktop/UnknownScores.txt");
        Scanner sc = new Scanner(inputFile);

        ArrayList<Word> wordList = new ArrayList<Word>();

        Dictionary dictionary = Dictionary.getDefaultResourceInstance();

        while (sc.hasNextLine()) {
            String[] lineSplit = sc.nextLine().split(" ");
            String word = lineSplit[0];
            IndexWord iWord = dictionary.getIndexWord(POS.NOUN, word);
            if (iWord == null) {
                iWord = dictionary.getIndexWord(POS.VERB, word);
                if (iWord == null) {
                    iWord = dictionary.getIndexWord(POS.ADJECTIVE, word);
                    if (iWord == null) {
                        iWord = dictionary.getIndexWord(POS.ADVERB, word);
                    }
                }
            }
            if (iWord != null && Double.parseDouble(lineSplit[1]) != 0.1) {
                String synString = iWord.getSenses().toString();
                String[] words = synString.substring(synString.indexOf("Words: ") + 7, synString.indexOf("--")).split(", ");
                for (String currentWord : words) {
                    wordList.add(new Word(currentWord, new Word(lineSplit[0], Double.parseDouble(lineSplit[1]))));
                }
            } else {
                //System.out.println("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!");
            }
        }

        Collections.sort(wordList);
        
        for(Word word : wordList){
            //System.out.println(word.getWord());
        }

        ArrayList<Word> condensedList = new ArrayList<Word>();
        int i = 0, j, count;
        double sum, avg;
        while (i < wordList.size()) {
            j = i;
            sum = 0;
            count = 1;
            while (j < wordList.size() && wordList.get(j).equals(wordList.get(i))) {
                sum += wordList.get(j).getScore();
                j++;
                count++;
            }
            avg = sum / count;
            condensedList.add(new Word(wordList.get(i).getWord(), avg));
            condensedList.get(condensedList.size() - 1).setParent(wordList.get(i).getParent());
            i++;
        }

        for (Word word : condensedList) {
            if(word.getScore() != word.getParent().getScore()){
            System.out.println(word.getParent().getWord() + " " + word.getParent().getScore() + " :Syn Word: " + word.getWord() + " " + word.getScore());
            }
        }

    }
}
