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
public class CleanUp {

    protected static int FILTER = 100;

    public static void main(String[] args) throws FileNotFoundException {
        File[] combinedFiles = {
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAverage.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAwful.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownGood.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownPoor.txt")};

        File unknownScores = new File("/media/thomas/ESD-USB/Scores/UnknownScores.txt");

        Scanner sc = new Scanner(unknownScores);
        String line;
        String[] lineSplit;
        ArrayList<Word> unknownWords = new ArrayList();

        while (sc.hasNextLine()) {
            String word = "";
            line = sc.nextLine();
            lineSplit = line.split(" ");
            for (int j = 0; j < lineSplit.length - 1; j++) {
                word += lineSplit[j];
            }
            Double score = Double.parseDouble(lineSplit[lineSplit.length - 1]);
            unknownWords.add(new Word(word, score));
        }

        File[] outputFiles = {
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownAverage.txt"),
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownAwful.txt"),
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownGood.txt"),
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownPoor.txt")};

        PrintWriter[] p = new PrintWriter[outputFiles.length];

        for (int i = 0; i < combinedFiles.length; i++) {
            sc = new Scanner(combinedFiles[i]);
            p[i] = new PrintWriter(outputFiles[i]);
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                lineSplit = line.split(" ");
                if (Integer.parseInt(lineSplit[lineSplit.length - 1]) >= FILTER) {
                    int index = linearSearch(unknownWords, lineSplit[0]);
                    p[i].println(line +" "+ unknownWords.get(index).getScore());
                }
            }
            p[i].close();
        }
    }

    public static int linearSearch(ArrayList<Word> words, String word) {
        for (int i = 0; i < words.size(); i++) {
            if(words.get(i).getWord().equals(word)){
                return i;
            }
        }
        return -1;
    }
    

}
