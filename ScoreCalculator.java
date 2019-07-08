package parsertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Thomas Talasco, Yuwei Chen
 */
public class ScoreCalculator {

    protected static final double FILTER = 100.0;

    protected static ArrayList<Word> totalWords = new ArrayList<Word>();
    public static void main(String[] args) throws FileNotFoundException {

        /* Old array
        File[] files = {
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAverage.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAwful.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownGood.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownPoor.txt")};
         */
        File[] files = {
            new File("/media/thomas/ESD-USB/Combined Files/KnownAverage.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownAwful.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownGood.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownPoor.txt")};

        ArrayList[] ratingsLists = {
            new ArrayList<Word>(),
            new ArrayList<Word>(),
            new ArrayList<Word>(),
            new ArrayList<Word>(),
            new ArrayList<Word>()
        };

        String outputString = "";

        //read in the files into the arraylist
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            totalWords = parseFile(file, totalWords, ratingsLists[i]);
        }
        
        //get each list from the array    
        double scale = Math.pow(10, 3);
        for (int j = 0; j < totalWords.size(); j++) {
            double score = (totalWords.get(j).getAwesomeCount() * 0.9
                    + totalWords.get(j).getGoodCount() * 0.7
                    + totalWords.get(j).getAverageCount() * 0.5
                    + totalWords.get(j).getPoorCount() * 0.3
                    + totalWords.get(j).getAwfulCount() * 0.1) / totalWords.get(j).getCount();
            score = Math.round(score * scale) / scale;
            if (totalWords.get(j).getTotalCount() >= FILTER && score > 0) {
                outputString += totalWords.get(j).getWord() + " " + score + "\n";
            }
        }

        File outputFile = new File("/media/thomas/ESD-USB/newFiles/Scores.txt");

        PrintWriter p = new PrintWriter(outputFile);
        p.print(outputString);
        p.close();
    }

    /**
     * Updates totalwords by increasing the counts of the number of times a word
     * was seen in a particular file
     *
     * @param totalWords List of all words seen
     * @param file Current file being read
     * @param lineSplit Array of the line most recently read from the file that
     * splits along spaces
     * @param index The index of the object we're updating in totalWords
     * @return Updated totalWords list such that all counts are updated for file
     * specificity
     */
    public static ArrayList<Word> adjustCounts(ArrayList<Word> totalWords, File file, String[] lineSplit, int index) {
        switch (file.getName()) {
            case "UnknownAverage.txt":
            case "KnownAverage.txt":
                totalWords.get(index).setAverageCount(Integer.parseInt(lineSplit[lineSplit.length - 1]));
                break;
            case "UnknownAwesome.txt":
            case "KnownAwesome.txt":
                totalWords.get(index).setAwesomeCount(Integer.parseInt(lineSplit[lineSplit.length - 1]));
                break;
            case "UnknownAwful.txt":
            case "KnownAwful.txt":
                totalWords.get(index).setAwfulCount(Integer.parseInt(lineSplit[lineSplit.length - 1]));
                break;
            case "UnknownGood.txt":
            case "KnownGood.txt":
                totalWords.get(index).setGoodCount(Integer.parseInt(lineSplit[lineSplit.length - 1]));
                break;
            case "UnknownPoor.txt":
            case "KnownPoor.txt":
                totalWords.get(index).setPoorCount(Integer.parseInt(lineSplit[lineSplit.length - 1]));
                break;
            default:
                break;
        }
        return totalWords;
    }

    /**
     * Reads through a file and adds to the list of all words seen
     *
     * @param file File being read
     * @param totalWords List of words seen across all files
     * @param ratingWords List of words specific to the current file
     * @return An updated Total Words list with word
     * @throws FileNotFoundException
     */
    public static ArrayList<Word> parseFile(File file, ArrayList<Word> totalWords, ArrayList<Word> ratingWords) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String[] lineSplit = sc.nextLine().split(" ");
            if (lineSplit.length > 2) {
                String word = "";
                for (int j = 0; j < lineSplit.length - 1; j++) {
                    word += lineSplit[j];
                }
                boolean flag = false;
                for (int i = 0; i < totalWords.size(); i++) {
                    if (word.equals(totalWords.get(i).getWord())) {
                        totalWords.get(i).setCount(totalWords.get(i).getCount() + Integer.parseInt(lineSplit[lineSplit.length - 1]));
                        flag = true;
                        adjustCounts(totalWords, file, lineSplit, i);
                    }
                }
                if (!flag) {
                    totalWords.add(new Word(word, Integer.parseInt(lineSplit[lineSplit.length - 1])));
                    adjustCounts(totalWords, file, lineSplit, totalWords.size() - 1);
                }
                ratingWords.add(new Word(word, Integer.parseInt(lineSplit[lineSplit.length - 1])));
            } else {
                boolean flag = false;
                //addings to the totalWords
                for (int i = 0; i < totalWords.size(); i++) {
                    if (lineSplit[0].equals(totalWords.get(i).getWord())) {
                        totalWords.get(i).setCount(totalWords.get(i).getCount() + Integer.parseInt(lineSplit[lineSplit.length - 1]));
                        flag = true;
                        adjustCounts(totalWords, file, lineSplit, i);
                    }
                }
                if (!flag) {
                    totalWords.add(new Word(lineSplit[0], Integer.parseInt(lineSplit[lineSplit.length - 1])));
                    adjustCounts(totalWords, file, lineSplit, totalWords.size() - 1);
                }
                ratingWords.add(new Word(lineSplit[0], Integer.parseInt(lineSplit[lineSplit.length - 1])));
            }
        }
        return totalWords;
    }

}
