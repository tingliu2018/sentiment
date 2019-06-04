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
public class WordCounter {

    public static void main(String[] args) throws FileNotFoundException {
        File[] files = {
            new File("/media/thomas/ESD-USB/Combined Files/KnownAverage.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownGood.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownPoor.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/KnownAwful.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAverage.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAwesome.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownGood.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownPoor.txt"),
            new File("/media/thomas/ESD-USB/Combined Files/UnknownAwful.txt")};

        ArrayList <Word> words = new ArrayList<Word> ();
        PrintWriter p = null;
        for(File file : files){
            Scanner sc = new Scanner(file);
            p = new PrintWriter("/media/thomas/ESD-USB/Words.txt");
            while(sc.hasNextLine()){
                String[] splitLine = sc.nextLine().split(" ");
                words.add(new Word(splitLine[0], Integer.parseInt(splitLine[1])));
            }
            Collections.sort(words);
            
            p.println(words.toString());
            System.out.println(words.toString());
            words.clear();
            p.flush();
        }
        p.close();
    }

}
