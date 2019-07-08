package parsertest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author thomas
 */
public class CombineFiles {

    public static void main(String[] args) throws FileNotFoundException {

        File[] femaleKnownFiles = {
            new File("/media/thomas/ESD-USB/Need Score/FemaleAverageKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleAwesomeKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleGoodKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemalePoorKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleAwfulKnownOutput.txt"),};

        File[] maleKnownFiles = {
            new File("/media/thomas/ESD-USB/Need Score/MaleAverageKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleAwesomeKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleGoodKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MalePoorKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleAwfulKnownOutput.txt")};

        File[] femaleUnknownFiles = {
            new File("/media/thomas/ESD-USB/Need Score/FemaleAverageUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleAwesomeUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleGoodUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemalePoorUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleAwfulUnknownOutput.txt"),};

        File[] maleUnknownFiles = {
            new File("/media/thomas/ESD-USB/Need Score/MaleAverageUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleAwesomeUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleGoodUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MalePoorUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleAwfulUnknownOutput.txt")};

        ArrayList<Word> maleWords = new ArrayList<Word>();
        ArrayList<Word> femaleWords = new ArrayList<Word>();
        ArrayList<Word> combinedWords = new ArrayList<Word>();

        String[] outputKnownStrings = {"KnownAverage","KnownAwesome","KnownGood","KnownPoor","KnownAwful",};
        String[] outputUnknownStrings = {"UnknownAverage","UnknownAwesome","UnknownGood","UnknownPoor","UnknownAwful"};
        
        for (int i = 0; i < maleKnownFiles.length; i++) {
            femaleWords = parseFile(femaleKnownFiles[i]);
            maleWords = parseFile(maleKnownFiles[i]);
            combinedWords = combineFiles(femaleWords, maleWords);
            printFile(combinedWords,outputKnownStrings[i]);
            femaleWords.clear();
            maleWords.clear();
            combinedWords.clear();
        }
        for (int i = 0; i < maleUnknownFiles.length; i++) {
            femaleWords = parseFile(femaleUnknownFiles[i]);
            maleWords = parseFile(femaleUnknownFiles[i]);
            combinedWords = combineFiles(femaleWords, maleWords);
            printFile(combinedWords,outputUnknownStrings[i]);
            femaleWords.clear();
            maleWords.clear();
            combinedWords.clear();
        }
        
    }

    public static ArrayList<Word> combineFiles(ArrayList<Word> femaleWords, ArrayList<Word> maleWords) {
        ArrayList<Word> combinedWords = femaleWords;
        boolean flag = false;
        for (int i = 0; i < maleWords.size(); i++) {
            for (int j = 0; j < combinedWords.size(); j++) {
                if (combinedWords.get(j).getWord().equals(maleWords.get(i).getWord())) {
                    combinedWords.get(j).setCount(combinedWords.get(j).getCount() + maleWords.get(i).getCount());
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                combinedWords.add(maleWords.get(i));
            } else {
                flag = false;
            }
        }
        return combinedWords;
    }

    public static void printFile(ArrayList<Word> words, String fileName) throws FileNotFoundException {
        String filePath = "/media/thomas/ESD-USB/Combined Files/" + fileName + ".txt";

        PrintWriter pw = new PrintWriter(filePath);
        //System.out.println(words.toString());
        for (int j = 0; j < words.size(); j++) {
            pw.println(words.get(j).getWord() + " " + words.get(j).getCount());
        }
        pw.close();
        //System.out.println(words.toString());
    }

    public static ArrayList<Word> parseFile(File file) throws FileNotFoundException {
        Scanner fileRead = new Scanner(file);
        //read file

        ArrayList<Word> words = new ArrayList<Word>();
        while (fileRead.hasNextLine()) {
            //splits file line into word and it's count
            String line = fileRead.nextLine();
            String[] splitLine = line.split(" ");
            if (splitLine.length < 3) {
                words.add(new Word(splitLine[0], Integer.parseInt(splitLine[1])));
            } else {
                String word = "";
                for (int j = 0; j < splitLine.length - 1; j++) {
                    word += splitLine[j];
                }
                words.add(new Word(word, Integer.parseInt(splitLine[splitLine.length - 1])));
            }
        }
        return words;
    }

}
