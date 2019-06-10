package parsertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Thomas Talasco
 */

public class SynsetWriter {

    public static void main(String[] args) throws FileNotFoundException, JWNLException {
        BasicConfigurator.configure();
        File inputFile = new File("/home/thomas/Desktop/UnknownScores.txt");
        Scanner sc = new Scanner(inputFile);
        ArrayList<Word> wordList = new ArrayList<Word>();
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();
        Wordnet net = new Wordnet();

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
                
                //System.out.println(word +": "+ net.getSynonym(word, iWord.getPOS().toString()));
                //System.out.println(word +": "+ net.getDerived(word));
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
            if(word.getWord().endsWith(" ")){
                word.setWord(word.getWord().substring(0, word.getWord().length()-1));
            }
        }

        ArrayList<Word> condensedList = new ArrayList<Word>();
        int i = 0, j;
        double sum, avg, count;
        while (i < wordList.size()) {
            j = i+1;
            sum = wordList.get(i).getScore();
            count = 1;
            //System.out.println(wordList.get(i).getWord());
            while (j < wordList.size() && wordList.get(j).getWord().equals(wordList.get(i).getWord())) {
                sum += wordList.get(j).getScore();
                count++;
                //System.out.println("\t"+ wordList.get(j).getWord() +" "+ count);
                j++;
            }
            avg = sum / count;
            condensedList.add(new Word(wordList.get(i).getWord(), avg));
            condensedList.get(condensedList.size() - 1).setParent(wordList.get(i).getParent());
            i = j;
        }

        
        for (Word word : condensedList) {
            if(word.getScore() != word.getParent().getScore()){
                System.out.println(word.getParent().getWord() + " " + word.getParent().getScore() + " :Syn Word: " + word.getWord() + " " + word.getScore());
            }
        }

    }
}
