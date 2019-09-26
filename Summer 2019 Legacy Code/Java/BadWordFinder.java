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

/**
 *
 * @author Code Monkey A
 */
public class BadWordFinder {

    public static void main(String[] args) throws FileNotFoundException, JWNLException {

        File[] countFiles = {
            new File("/media/thomas/ESD-USB/Combined Files/KnownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownGood.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownAverage.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownPoor.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownAwful.txt")
        };

        File ourScores = new File("/media/thomas/ESD-USB/newFiles/Scores.txt");
        File lexiconScores = new File("/media/thomas/ESD-USB/newFiles/valenceScores.txt");

        ArrayList<Word> ourWords = new ArrayList();
        ArrayList<Word> lexWords = new ArrayList();
        ArrayList<Word> badWords = new ArrayList();
        ArrayList<Word> countedWords = new ArrayList();
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();

        Scanner sc = new Scanner(ourScores);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] lineSplit = line.split(" ");
            ourWords.add(new Word(lineSplit[0], Double.parseDouble(lineSplit[lineSplit.length - 1])));
        }

        sc.close();

        sc = new Scanner(lexiconScores);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] lineSplit = line.split(" ");
            lexWords.add(new Word(lineSplit[0], Double.parseDouble(lineSplit[lineSplit.length - 1])));
        }

        sc.close();

        for (int i = 0; i < ourWords.size(); i++) {
            int index = linearSearch(lexWords, ourWords.get(i));
            if (index != -1) {
                IndexWord iWord = dictionary.getIndexWord(POS.ADVERB, ourWords.get(i).getWord());
                if (iWord != null) {
                    if (ourWords.get(i).getScore() >= 0.7 && lexWords.get(index).getScore() >= 0.7) {
                        badWords.add(new Word(ourWords.get(i).getWord(), ourWords.get(i).getScore(), lexWords.get(index).getScore()));
                    }
                    if (ourWords.get(i).getScore() <= 0.3 && lexWords.get(index).getScore() <= 0.3) {
                        badWords.add(new Word(ourWords.get(i).getWord(), ourWords.get(i).getScore(), lexWords.get(index).getScore()));
                    } else if (Math.abs(ourWords.get(i).getScore() - lexWords.get(index).getScore()) <= 0.1) {
                        badWords.add(new Word(ourWords.get(i).getWord(), ourWords.get(i).getScore(), lexWords.get(index).getScore()));
                    }
                    //badWords.add(new Word(ourWords.get(i).getWord(), ourWords.get(i).getScore(), lexWords.get(index).getScore()));
                }
            }
        }

        for (int i = 0; i < countFiles.length; i++) {
            File countFile = countFiles[i];
            sc = new Scanner(countFile);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] lineSplit = line.split(" ");
                countedWords.add(new Word(lineSplit[0], 0.0));
                countedWords.get(countedWords.size() - 1).setCount(Double.parseDouble(lineSplit[lineSplit.length - 1]));
            }
            for (Word word : badWords) {
                int index = linearSearch(countedWords, word);
                if (index != -1) {
                    switch (i) {
                        case 0:
                            word.setAwesomeCount(countedWords.get(index).getCount());
                            break;
                        case 1:
                            word.setGoodCount(countedWords.get(index).getCount());
                            break;
                        case 2:
                            word.setAverageCount(countedWords.get(index).getCount());
                            break;
                        case 3:
                            word.setPoorCount(countedWords.get(index).getCount());
                            break;
                        case 4:
                            word.setAwfulCount(countedWords.get(index).getCount());
                            break;
                        default:
                            System.out.println("BAD VALUE FOR i");
                            break;
                    }
                } else {
                    System.out.println(word.getWord() + " NOT FOUND!");
                }
            }
            countedWords.clear();
        }

        PrintWriter p = new PrintWriter("/media/thomas/ESD-USB/newFiles/goodAdverbWords.csv");
        p.println("Word\tOur Score\tLex Score\tTotal Count\tAwesome Count\tGood Count\tAverage Count\tPoor Count\tAwful Count");
        for (Word word : badWords) {
            p.println(word.getWord() + "\t" + word.getScore() + "\t" + word.getLexScore() + "\t" + word.getTotalCount() + "\t" + word.getAwesomeCount() + "\t" + word.getGoodCount() + "\t" + word.getAverageCount() + "\t" + word.getPoorCount() + "\t" + word.getAwfulCount());
        }

        p.close();
    }

    public static int linearSearch(ArrayList<Word> list, Word searchWord) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getWord().equals(searchWord.getWord())) {
                return i;
            }
        }
        return -1;
    }
}
