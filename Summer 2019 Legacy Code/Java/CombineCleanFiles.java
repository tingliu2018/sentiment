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
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author thomas
 */
public class CombineCleanFiles {

    public static void main(String[] args) throws FileNotFoundException {
        File[] inputFiles = {
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownAverage.txt"),
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownAwful.txt"),
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownGood.txt"),
            new File("/media/thomas/ESD-USB/Cleaned Files/UnknownPoor.txt")
        };

        PrintWriter p = new PrintWriter("/media/thomas/ESD-USB/Synset/Unknown.txt");
        ArrayList<Word> words = new ArrayList();

        for (File file : inputFiles) {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String word = "";
                String line = sc.nextLine();
                String[] lineSplit = line.split(" ");
                for (int j = 0; j < lineSplit.length - 2; j++) {
                    word += lineSplit[j];
                }
                System.out.println(word);
                Double score = Double.parseDouble(lineSplit[lineSplit.length - 1]);
                boolean flag = false;
                for (Word search : words) {
                    if (search.getWord().equals(word)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    words.add(new Word(word, score));
                } else {
                    flag = false;
                }
            }
        }

        Collections.sort(words);

        for (Word word : words) {
            p.println(word.getWord() + " " + word.getScore());
        }

        p.close();
    }
}
