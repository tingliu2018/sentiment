package parsertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Thomas
 */
public class BadWordFinder {

    public static void main(String[] args) throws FileNotFoundException {

        File[] countFiles = {
            new File("/media/thomas/ESD-USB/Combined Files/KnownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownGood.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownAverage.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownPoor.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownAwful.txt")
            };

        File ourScores = new File("/media/thomas/ESD-USB/newFiles/Scores.txt");
        File lexiconScores = new File("/media/thomas/ESD-USB/newFiles/valenceScores.txt");
        File output = new File("/media/thomas/ESD-USB/newFiles/badWords.txt");

        ArrayList<Word> ourWords = new ArrayList();
        ArrayList<Word> lexWords = new ArrayList();
        ArrayList<Word> badWords = new ArrayList();
        ArrayList<Word> countedWords = new ArrayList();

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
            int index = binarySearch(lexWords, 0, lexWords.size(), ourWords.get(i));
            if ((ourWords.get(i).getScore() > .7 ^ lexWords.get(i).getScore() > .7) || (ourWords.get(i).getScore() > .3 ^ lexWords.get(i).getScore() > .3)) {
                badWords.add(new Word(ourWords.get(i).getWord(), ourWords.get(i).getScore(), lexWords.get(i).getScore()));
            }
        }

        for (int i = 0; i < countFiles.length; i++) {
            File countFile = countFiles[i];
            sc = new Scanner(countFile);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] lineSplit = line.split(" ");
                countedWords.add(new Word(lineSplit[0], 0.0));
                countedWords.get(countedWords.size()-1).setCount(Double.parseDouble(lineSplit[lineSplit.length-1]));
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
                    System.out.println(word.getWord() +" NOT FOUND!");
                }
            }
            countedWords.clear();

        }

        PrintWriter p = new PrintWriter("/media/thomas/ESD-USB/newFiles/badWords.txt");
        p.println("Word\tOur Score\tLex Score\tTotal Count\tAwesome Count\tGood Count\tAverage Count\tPoor Count\tAwful Count");
        for (Word word : badWords) {
            p.println(word.getWord() + "\t" + word.getScore() + "\t" + word.getLexScore() + "\t" + word.getTotalCount() + "\t" + word.getAwesomeCount() + "\t" + word.getGoodCount() + "\t" + word.getAverageCount() + "\t" + word.getPoorCount() + "\t" + word.getAwfulCount());
        }
    }
    
    public static int linearSearch(ArrayList<Word> list, Word searchWord){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getWord().equals(searchWord.getWord())){
                return i;
            }
        }
        return -1;
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

}
