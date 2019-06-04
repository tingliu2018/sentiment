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

/**
 *
 * @author thomas
 */
public class FrequencyFinder {

    public static void main(String[] args) throws FileNotFoundException {

        File[] files = {
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAverage.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAwful.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownGood.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownPoor.txt")};
        ArrayList<Word> totalWords = new ArrayList<Word>();

        ArrayList[] ratingsLists = {
            new ArrayList<Word>(),
            new ArrayList<Word>(),
            new ArrayList<Word>(),
            new ArrayList<Word>(),
            new ArrayList<Word>()
        };

        String[] outputStrings = new String[5];

        double percentage = 0.8;
        //read in the files into the arraylist
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            parseFile(file, totalWords, ratingsLists[i]);
        }

        for (int i = 0; i < totalWords.size(); i++) {
            totalWords.get(i).setCount((int) (totalWords.get(i).getCount() * 0.8));
        }

        //get each list from the array
        for (int j = 0; j < ratingsLists.length; j++) {
            ArrayList<Word> currentList = ratingsLists[j];
            //get each word in each list
            for (int i = 1; i < currentList.size(); i++) {
                //linear search of totalwords
                for (int k = 0; k < ratingsLists.length; k++) {
                    if (currentList.get(i).getWord().equals(totalWords.get(k))) {
                        if (currentList.get(i).getCount() >= totalWords.get(k).getCount()) {
                            outputStrings[j] += currentList.get(i).getWord() + " " + currentList.get(i).getCount() + " " + totalWords.get(k).getCount() * 1.25 + "\n";
                        }
                    }
                }
            }
        }
        File[] outputFiles = {
            new File("/media/thomas/ESD-USB/Frequency/UnknownAverage.txt"),
            new File("/media/thomas/ESD-USB/Frequency/UnknownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Frequency/UnknownAwful.txt"),
            new File("/media/thomas/ESD-USB/Frequency/UnknownGood.txt"),
            new File("/media/thomas/ESD-USB/Frequency/UnknownPoor.txt")};

        PrintWriter p = new PrintWriter("");

        for (int i = 0; i < outputFiles.length; i++) {
            p = new PrintWriter(outputFiles[i]);
            p.print(outputStrings[i]);
            p.flush();
        }
        p.close();
    }

    public static void parseFile(File file, ArrayList<Word> totalWords, ArrayList<Word> ratingWords) throws FileNotFoundException {
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
                    }
                }
                if (!flag) {
                    totalWords.add(new Word(word, Integer.parseInt(lineSplit[lineSplit.length - 1])));
                }
                ratingWords.add(new Word(word, Integer.parseInt(lineSplit[lineSplit.length - 1])));
            } else {

                boolean flag = false;
                //addings to the totalWords
                for (int i = 0; i < totalWords.size(); i++) {
                    if (lineSplit[0].equals(totalWords.get(i).getWord())) {
                        for (int j = 0; j < lineSplit.length; j++) {
                            System.out.print(lineSplit[j] + " ");
                        }
                        System.out.println("");
                        totalWords.get(i).setCount(totalWords.get(i).getCount() + Integer.parseInt(lineSplit[lineSplit.length - 1]));
                        flag = true;
                    }
                }
                if (!flag) {
                    totalWords.add(new Word(lineSplit[0], Integer.parseInt(lineSplit[lineSplit.length - 1])));
                }
                ratingWords.add(new Word(lineSplit[0], Integer.parseInt(lineSplit[lineSplit.length - 1])));
            }
        }
    }

}
