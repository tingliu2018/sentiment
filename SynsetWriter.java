package parsertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
        File inputFile = new File("/media/thomas/ESD-USB/Synset/Unknown.txt");
        //File inputFile = new File("/home/thomas/Desktop/Known.txt");
        Scanner sc = new Scanner(inputFile);
        ArrayList<Word> synonymList = new ArrayList<Word>();
        ArrayList<Word> antonymList = new ArrayList<Word>();
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();
        Wordnet net = new Wordnet();

        while (sc.hasNextLine()) {
            //Look up word as various parts of speech
            String[] lineSplit = sc.nextLine().split(" ");
            String word = lineSplit[0];
            IndexWord iWord = dictionary.getIndexWord(POS.ADVERB, word);
            
            if (iWord != null && Double.parseDouble(lineSplit[1]) != 0.1) {
                //System.out.println(word +": "+ net.getSynonym(word, iWord.getPOS().toString()));
                //System.out.println(word +": "+ net.getDerived(word));
                //Grab all of the strings of words out of the synset.
                String synString = iWord.getSenses().toString();
                ArrayList antonyms = new ArrayList();
                ArrayList synonyms = new ArrayList();
                antonyms = net.getAntonyms(word);
                synonyms = net.getSynonym(word, iWord.getPOS().toString().toLowerCase());
                String[] words = synString.substring(synString.indexOf("Words: ") + 7, synString.indexOf("--")).split(", ");
                /* for (String currentWord : words) {
                    //Create new word objects and store them in the list with the parent word as an arg
                    synonymList.add(new Word(currentWord, new Word(lineSplit[0], Double.parseDouble(lineSplit[1]))));
                } */
                for (Object obj : synonyms) {
                    synonymList.add(new Word(obj.toString(), new Word(lineSplit[0], Double.parseDouble(lineSplit[1]))));
                }
                for (Object obj : antonyms) {
                    antonymList.add(new Word(obj.toString(), new Word(lineSplit[0], Double.parseDouble(lineSplit[1]))));
                }
            } else {
                //System.out.println("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!");
            }
        }

        // Sort the list
        Collections.sort(synonymList);
        Collections.sort(antonymList);

        // Clean up list remove any strings that end with blank characters
        for (Word word : synonymList) {
            if (word.getWord().endsWith(" ")) {
                word.setWord(word.getWord().substring(0, word.getWord().length() - 1));
            }
        }

        for (Word word : antonymList) {
            if (word.getWord().endsWith(" ")) {
                word.setWord(word.getWord().substring(0, word.getWord().length() - 1));
            }
        }
        
        double scale = Math.pow(10, 3);
        ArrayList<Word> condensedSynList = scoreWords(synonymList);
        ArrayList<Word> condensedAntList = scoreWords(antonymList);

        for (Word word : condensedSynList) {
            word.setScore(Math.round(word.getScore() * scale) / scale);
            //System.out.println(word.getParent().getWord() + " " + word.getParent().getScore() + " :Syn Word: " + word.getWord() + " " + word.getScore());

        }

        for (Word word : condensedAntList) {
            word.setScore(1 - word.getScore());
            word.setScore(Math.round(word.getScore() * scale) / scale);
            //System.out.println(word.getParent().getWord() + " " + word.getParent().getScore() + " :Ant Word: " + word.getWord() + " " + word.getScore());
        }
        
        ArrayList<Word> sharedWords = new ArrayList<Word>();
        
        for(Word word : condensedSynList){
            int index = binarySearch(condensedAntList, 0, condensedAntList.size()-1, word);
            if(index != -1){
                System.out.println(word.getWord() +" Syn Score: "+ word.getScore() + " Syn Parent: "+ word.getParent().getWord() +" Ant Score: "+ condensedAntList.get(index).getScore() +" Ant Parent: "+ condensedAntList.get(index).getParent().getWord());
                sharedWords.add(word);
            }
        }
        
        System.out.println("Total Syn "+ condensedSynList.size());
        System.out.println("Total Ant "+ condensedAntList.size());
        System.out.println("Naughty Words "+ sharedWords.size());
        
        printToFile(condensedSynList, "AdverbSyn.txt");
        printToFile(condensedSynList, "AdverbAnt.txt");
        printToFile(condensedSynList, "AdverbOverlap.txt");

    }

    /**
     * Takes in a sorted list of words and scores them based on the average of
     * the scores that they're passed with
     *
     * @param wordList List of Sorted Words with scores
     * @return Condensed List of words with each word from the arg list
     * appearing once with its score being the average of all its appearances in
     * wordList
     */
    public static ArrayList<Word> scoreWords(ArrayList<Word> wordList) {
        ArrayList<Word> condensedList = new ArrayList<Word>();
        int i = 0, j;
        double sum, avg, count;
        while (i < wordList.size()) {
            j = i + 1;
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
        return condensedList;
    }
    
    public static int binarySearch(ArrayList<Word> list, int l, int r, Word searchWord) 
    { 
        if (r >= l) { 
            int mid = l + (r - l) / 2; 
  
            // If the element is present at the 
            // middle itself 
            if (list.get(mid).getWord().equals(searchWord.getWord())) 
                return mid; 
  
            // If element is smaller than mid, then 
            // it can only be present in left subarray 
            if (list.get(mid).getWord().compareTo(searchWord.getWord()) > 0) 
                return binarySearch(list, l, mid - 1, searchWord); 
  
            // Else the element can only be present 
            // in right subarray 
            return binarySearch(list, mid + 1, r, searchWord); 
        } 
  
        // We reach here when element is not present 
        // in array 
        return -1; 
    }
    
    public static void printToFile(ArrayList<Word> words, String filename) throws FileNotFoundException{
        File file = new File("/media/thomas/ESD-USB/Synset/"+ filename);
        PrintWriter p = new PrintWriter(file);
        for(Word word : words){
            p.println(word.getWord() +" "+ word.getScore());
        }
        p.close();
    }
}
