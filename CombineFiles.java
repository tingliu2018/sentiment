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
public class CombineFiles {

    public static void main(String[] args) throws FileNotFoundException {
        File[] knownFiles = {new File("/media/thomas/ESD-USB/Need Score/FemaleAverageKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleAwesomeKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleGoodKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemalePoorKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleAverageKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleAwesomeKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleGoodKnownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MalePoorKnownOutput.txt")};

        File[] unknownFiles = {new File("/media/thomas/ESD-USB/Need Score/FemaleAverageUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleAwesomeUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemaleGoodUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/FemalePoorUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleAverageUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleAwesomeUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MaleGoodUnknownOutput.txt"),
            new File("/media/thomas/ESD-USB/Need Score/MalePoorUnknownOutput.txt")};

        Scanner fileRead;
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<Integer> wordCounts = new ArrayList<Integer>();

        for (int i = 0; i < knownFiles.length / 2; i++) {
            fileRead = new Scanner(knownFiles[i]);
            parseLine(fileRead, words, wordCounts);
            fileRead = new Scanner(knownFiles[i + 4]);
            int index = 0;
            while (fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                String[] splitLine = line.split(" ");
                wordCounts.set(index, wordCounts.get(index) + Integer.parseInt(splitLine[splitLine.length - 1]));
                index++;
            }
            printFile(words, wordCounts, i, true);
        }

        for (int i = 0; i < unknownFiles.length / 2; i++) {
            fileRead = new Scanner(unknownFiles[i]);
            parseLine(fileRead, words, wordCounts);
            fileRead = new Scanner(knownFiles[i + 4]);
            while (fileRead.hasNextLine()) {
                String line = fileRead.nextLine();
                String[] splitLine = line.split(" ");
                int index = -1;
                for (int j = 0; j < words.size(); j++) {
                    if (words.get(j).equals(splitLine[0])) {
                        index = j;
                        break;
                    }
                }
                if (index == -1) {
                    if (splitLine.length > 2) {
                        String word = "";
                        for (int j = 0; j < splitLine.length - 1; j++) {
                            word += splitLine[j];
                        }
                        words.add(word);
                        wordCounts.add(Integer.parseInt(splitLine[splitLine.length - 1]));
                    } else {
                        words.add(splitLine[0]);
                        wordCounts.add(Integer.parseInt(splitLine[splitLine.length - 1]));
                    }
                } else {
                    wordCounts.set(index, wordCounts.get(index) + Integer.parseInt(splitLine[splitLine.length - 1]));
                }
            }
            printFile(words, wordCounts, i, false);

        }
    }

    public static void printFile(ArrayList<String> words, ArrayList<Integer> wordCounts, int i, boolean known) throws FileNotFoundException {
        String fileName = "txt.txt";
        if (known) {
            if (i < 1) {
                fileName = "/media/thomas/ESD-USB/Combined Files/KnownAverage.txt";
            } else if (i < 2) {
                fileName = "/media/thomas/ESD-USB/Combined Files/KnownAwesome.txt";
            } else if (i < 3) {
                fileName = "/media/thomas/ESD-USB/Combined Files/KnownGood.txt";
            } else if (i < 4) {
                fileName = "/media/thomas/ESD-USB/Combined Files/KnownPoor.txt";
            }
        } else {
            if (i < 1) {
                fileName = "/media/thomas/ESD-USB/Combined Files/UnknownAverage.txt";
            } else if (i < 2) {
                fileName = "/media/thomas/ESD-USB/Combined Files/UnknownAwesome.txt";
            } else if (i < 3) {
                fileName = "/media/thomas/ESD-USB/Combined Files/UnknownGood.txt";
            } else if (i < 4) {
                fileName = "/media/thomas/ESD-USB/Combined Files/UnknownPoor.txt";
            }
        }
        PrintWriter pw = new PrintWriter(fileName);
        for (int j = 0; j < words.size(); j++) {
            pw.println(words.get(j) + " " + wordCounts.get(j));
        }
        words.clear();
        wordCounts.clear();
    }

    public static void parseLine(Scanner fileRead, ArrayList<String> words, ArrayList<Integer> wordCounts) {
        while (fileRead.hasNextLine()) {
            String line = fileRead.nextLine();
            String[] splitLine = line.split(" ");
            if (splitLine.length < 3) {
                words.add(splitLine[0]);
                wordCounts.add(Integer.parseInt(splitLine[1]));
            } else {
                String word = "";
                for (int j = 0; j < splitLine.length - 1; j++) {
                    word += splitLine[j];
                }
                words.add(word);
                //System.out.println(splitLine[0] + " " + splitLine[1]);
                wordCounts.add(Integer.parseInt(splitLine[splitLine.length - 1]));
            }
        }
    }

}
