package wordproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author Code Monkey A
 */
public class SynsetWriter {

    public static void main(String[] args) throws FileNotFoundException, JWNLException {
        BasicConfigurator.configure();
        File inputFile = new File("T:\\Academics\\Research\\Scores.csv");
        //File inputFile = new File("/home/thomas/Desktop/Known.txt");
        Scanner sc = new Scanner(inputFile);
        ArrayList<Word> synonymList = new ArrayList<Word>();
        ArrayList<Word> antonymList = new ArrayList<Word>();
        ArrayList<Word> hyponymList = new ArrayList<Word>();
        Wordnet net = new Wordnet();

        File fileOut = new File("T:\\Academics\\Research\\Synset\\NewLexicon\\Words.txt");
        PrintWriter p = new PrintWriter(fileOut);

        sc.nextLine();

        int MINIMUM = 0;
        int MAXIMUM = 14876;
        int counter = 0;
        while (sc.hasNextLine() && counter < MINIMUM) {
            sc.nextLine();
            counter++;
        }
        System.out.println(sc.hasNextLine());
        while (sc.hasNextLine() && counter <= MAXIMUM) {
            //Look up word as various parts of speech
            String[] lineSplit = sc.nextLine().split(",");
            String word = lineSplit[0];
            System.out.println("Now Examining Word Number " + counter + " - " + word);
            Word currentWord = new Word(word.toLowerCase(), Double.parseDouble(lineSplit[1]));

            for (IndexWord iWord : currentWord.getIndexWords()) {
                if (iWord != null && Double.parseDouble(lineSplit[1]) != 0.1) {
                    p.println(word);
                    //System.out.println(word +": "+ net.getSynonym(word, iWord.getPOS().toString()));
                    //System.out.println(word +": "+ net.getDerived(word));
                    //Grab all of the strings of words out of the synset.
                    //String synString = iWord.getSenses().toString();
                    ArrayList synonyms = new ArrayList();
                    ArrayList antonyms = new ArrayList();
                    ArrayList hyponyms = new ArrayList();
                    String pos = iWord.getPOS().toString().toLowerCase().substring(6, iWord.getPOS().toString().length() - 1);
                    synonyms = net.getSynonym(word, iWord.getPOS().toString().toLowerCase());
                    antonyms = net.getAntonyms(word);
                    hyponyms = net.getHyponym(word, pos);
                    //String[] words = synString.substring(synString.indexOf("Words: ") + 7, synString.indexOf("--")).split(", ");
                    /* for (String currentWord : words) {
                    //Create new word objects and store them in the list with the parent word as an arg
                    synonymList.add(new Word(currentWord, new Word(lineSplit[0], Double.parseDouble(lineSplit[1]))));
                } */

                    for (Object obj : synonyms) {
                        Word tempWord = new Word(obj.toString().toLowerCase(), new Word(lineSplit[0], Double.parseDouble(lineSplit[1])));
                        synonymList.add(tempWord);
                    }
                    for (Object obj : antonyms) {
                        Word tempWord = new Word(obj.toString().toLowerCase(), new Word(lineSplit[0], Double.parseDouble(lineSplit[1])));
                        antonymList.add(tempWord);
                    }
                    for (Object obj : hyponyms) {
                        Word tempWord = new Word(obj.toString().toLowerCase(), currentWord);
                        IndexWord tempIndexWord = tempWord.getHighestPOS();
                            if (tempIndexWord != null && willWeKeepThisWord(iWord, tempIndexWord)) {
                                hyponymList.add(tempWord);
                            }
                    }

                } else {
                    //System.out.println("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!");
                }
            }

            counter++;
        }

        p.close();

        // Sort the list
        Collections.sort(synonymList);
        Collections.sort(antonymList);
        Collections.sort(hyponymList);

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

        for (Word word : hyponymList) {
            if (word.getWord().endsWith(" ")) {
                word.setWord(word.getWord().substring(0, word.getWord().length() - 1));
            }
        }

        double scale = Math.pow(10, 3);
        ArrayList<Word> condensedSynList = scoreWords(synonymList);
        ArrayList<Word> condensedAntList = scoreWords(antonymList);
        ArrayList<Word> condensedHypList = scoreWords(hyponymList);

        for (Word word : condensedSynList) {
            word.setScore(Math.round(word.getScore() * scale) / scale);
            //System.out.println(word.getParent().getWord() + " " + word.getParent().getScore() + " :Syn Word: " + word.getWord() + " " + word.getScore());

        }

        for (Word word : condensedAntList) {
            word.setScore(1 - word.getScore());
            word.setScore(Math.round(word.getScore() * scale) / scale);
            //System.out.println(word.getParent().getWord() + " " + word.getParent().getScore() + " :Ant Word: " + word.getWord() + " " + word.getScore());
        }

        for (Word word : condensedHypList) {
            word.setScore(Math.round(word.getScore() * scale) / scale);
            //System.out.println(word.getParent().getWord() + " " + word.getParent().getScore() + " :Syn Word: " + word.getWord() + " " + word.getScore());

        }

        ArrayList<Word> sharedWords = new ArrayList<Word>();

        for (Word word : condensedSynList) {
            int index = binarySearch(condensedAntList, 0, condensedAntList.size() - 1, word);
            if (index != -1) {
                //System.out.println(word.getWord() +" Syn Score: "+ word.getScore() + " Syn Parent: "+ word.getParent().getWord() +" Ant Score: "+ condensedAntList.get(index).getScore() +" Ant Parent: "+ condensedAntList.get(index).getParent().getWord());
                sharedWords.add(word);
            }
        }

        System.out.println("Total Syn " + condensedSynList.size());
        System.out.println("Total Ant " + condensedAntList.size());
        System.out.println("Total Hyp " + condensedHypList.size());
        System.out.println("Naughty Words " + sharedWords.size());

        printToFile(condensedSynList, "Synonyms.txt");
        printToFile(condensedAntList, "Antonyms.txt");
        printToFile(condensedHypList, "Hyponyms.txt");
        printToFile(sharedWords, "Overlapped Words.txt");
    }

    public static boolean willWeKeepThisWord(IndexWord wordOne, IndexWord wordTwo) {
        List<net.sf.extjwnl.data.Synset> senseOne = wordOne.getSenses();
        List<net.sf.extjwnl.data.Synset> senseTwo = wordTwo.getSenses();
        if (!senseTwo.get(0).containsWord(wordOne.getLemma())) {
            if (senseTwo.size() == 1) {
                return false;
            }
            if (!senseTwo.get(1).containsWord(wordOne.getLemma())) {
                return false;
            }
        }
        if (!senseOne.get(0).containsWord(wordTwo.getLemma())) {
            if (senseOne.size() == 1) {
                return false;
            }
            if (!senseOne.get(1).containsWord(wordTwo.getLemma())) {
                return false;
            }
        }
        return true;
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
            condensedList.get(condensedList.size() - 1).setPartOfSpeech(wordList.get(i).getPartOfSpeech());
            i = j;
        }
        return condensedList;
    }

    public static int binarySearch(ArrayList<Word> list, int l, int r, Word searchWord) {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            // If the element is present at the 
            // middle itself 
            if (list.get(mid).getWord().equals(searchWord.getWord())) {
                return mid;
            }

            // If element is smaller than mid, then 
            // it can only be present in left subarray 
            if (list.get(mid).getWord().compareTo(searchWord.getWord()) > 0) {
                return binarySearch(list, l, mid - 1, searchWord);
            }

            // Else the element can only be present 
            // in right subarray 
            return binarySearch(list, mid + 1, r, searchWord);
        }

        // We reach here when element is not present 
        // in array 
        return -1;
    }

    public static void printToFile(ArrayList<Word> words, String filename) throws FileNotFoundException {
        File file = new File("T:\\Academics\\Research\\Synset\\NewLexicon\\" + filename);
        PrintWriter p = new PrintWriter(file);
        for (Word word : words) {
            p.println(word.getWord() + " " + word.getScore() + " " + word.getPartOfSpeech());
        }
        p.close();
    }
}
