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
    static int horribleProfCounter = 0;
    static int horribleCounter = 0;
    static int unfairCounter = 0;
    static int fairCounter = 0;
    static int loveCounter = 0;
    static int bestCounter = 0;
    static int worstCounter = 0;
    static int interestingCounter = 0;
    static int boringCounter = 0;
    static int hardCounter = 0;
    static int easyCounter = 0;

    public static void extractWords(String sentence) {
        if (sentence.contains("awesome professor") || sentence.contains("awesome teacher")) {
            awesomeCounter++;
        } else if (sentence.contains("wonderful professor") || sentence.contains("wonderful teacher")) {
            wonderfulCounter++;
        } else if (sentence.contains("good professor")  || sentence.contains("good teacher")) {
            goodCounter++;
        } else if (sentence.contains("bad professor")  || sentence.contains("bad teacher")) {
            badCounter++;
        } else if (sentence.contains("awful professor")  || sentence.contains("awful teacher")) {
            awfulCounter++;
        } else if (sentence.contains("horrible professor")  || sentence.contains("horrible teacher")) {
            horribleProfCounter++;
            horribleCounter++;
        }
        else if (sentence.contains("horrible")) {
            horribleCounter++;
        }
        else if (sentence.contains("unfair")) {
            unfairCounter++;
        }
        else if (sentence.contains("fair")) {
            fairCounter++;
        }
        else if (sentence.contains("love")) {
            loveCounter++;
        }
        else if (sentence.contains("best")) {
            bestCounter++;
        }
        else if (sentence.contains("worst")) {
            worstCounter++;
        }
        else if (sentence.contains("interesting")) {
            interestingCounter++;
        }
        else if (sentence.contains("boring") || sentence.contains("bored")) {
            boringCounter++;
        }
        else if (sentence.contains("hard")) {
            hardCounter++;
        }
        else if (sentence.contains("easy")) {
            easyCounter++;
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
        pw.println("awesome prof: " + awesomeCounter);
        pw.println("good prof: " + goodCounter);
        pw.println("wonderful prof: " + wonderfulCounter);
        pw.println("bad prof: " + badCounter);
        pw.println("awful prof: " + awfulCounter);
        pw.println("horrible prof: " + horribleProfCounter);
        pw.println("horrible: " + horribleCounter);
        pw.println("unfair: " + unfairCounter);
        pw.println("fair: " + fairCounter);
        pw.println("love: " + loveCounter);
        pw.println("best: " + bestCounter);
        pw.println("worst: " + worstCounter);
        pw.println("interesting: " + interestingCounter);
        pw.println("boring: " + boringCounter);
        pw.println("hard: " + interestingCounter);
        pw.println("easy: " + boringCounter);
        pw.flush();
        pw.close();
        sc.close();
    }
}

