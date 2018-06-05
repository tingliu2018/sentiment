/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package depdendcy;

import java.io.*;
import java.util.*;

/**
 *
 * @author Logan Brandt
 */
public class CommentExtractor {

    static int awesomeCounter = 0;
    static int wonderfulCounter = 0;
    static int goodCounter = 0;
    static int badCounter = 0;
    static int awfulCounter = 0;
    static int horribleCounter = 0;

    public static void extractWords(String sentence) {
        if (sentence.contains("awesome professor")) {
            awesomeCounter++;
        } else if (sentence.contains("wonderful professor")) {
            wonderfulCounter++;
        } else if (sentence.contains("good professor")) {
            goodCounter++;
        } else if (sentence.contains("bad professor")) {
            badCounter++;
        } else if (sentence.contains("awful professor")) {
            awfulCounter++;
        } else if (sentence.contains("horrible professor")) {
            horribleCounter++;
        }
    }

    public static void main(String args[]) throws FileNotFoundException {
        String str;
        File inFile = new File("C:\\Users\\Logan Brandt\\Documents\\internship\\ratingExtractor\\ratingExtractor\\sentiment\\ratingExtractor\\output.txt");
        File outFile = new File("Count.txt");
        PrintWriter pw = new PrintWriter(outFile);
        Scanner sc = new Scanner(inFile);
        while (sc.hasNextLine()) {
            str = sc.nextLine();
            extractWords(str);
        }
        pw.println("awesome: " + awesomeCounter);
        pw.println("good: " + goodCounter);
        pw.println("wonderful: " + wonderfulCounter);
        pw.println("bad: " + badCounter);
        pw.println("awful: " + awfulCounter);
        pw.println("horrible: " + horribleCounter);
        pw.flush();
        pw.close();
        sc.close();
    }
}
