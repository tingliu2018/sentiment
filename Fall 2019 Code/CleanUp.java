/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordproject;

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
public class CleanUp {

    public static void main(String[] args) throws FileNotFoundException {
        String root = "T:\\Academics\\Research\\Synset\\NewLexicon";
        String[] directories = {root + "\\0-5000", root + "\\5001-10000", root + "\\10001-14876", root + "\\14878-21000"};
        String[] fileNames = {"\\Antonyms.txt", "\\Hyponyms.txt", "\\Synonyms.txt"};

        ArrayList<Word> antonyms = new ArrayList<Word>();
        ArrayList<Word> hyponyms = new ArrayList<Word>();
        ArrayList<Word> synonyms = new ArrayList<Word>();

        for (String directory : directories) {
            for (String fileName : fileNames) {
                File file = new File(directory + fileName);
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String[] lineSplit = sc.nextLine().split(" ");
                    switch (fileName) {
                        case "\\Antonyms.txt":
                            antonyms.add(new Word(lineSplit[0], Double.parseDouble(lineSplit[1])));
                            break;

                        case "\\Hyponyms.txt":
                            hyponyms.add(new Word(lineSplit[0], Double.parseDouble(lineSplit[1])));
                            break;

                        case "\\Synonyms.txt":
                            synonyms.add(new Word(lineSplit[0], Double.parseDouble(lineSplit[1])));
                            break;

                        default:
                            System.out.println("ERROR");
                            break;
                    }
                }
                sc.close();
            }
        }

        Collections.sort(antonyms);
        Collections.sort(hyponyms);
        Collections.sort(synonyms);

        ArrayList CondensedAntonyms = scoreWords(antonyms);
        ArrayList CondensedHyponyms = scoreWords(hyponyms);
        ArrayList CondensedSynonyms = scoreWords(synonyms);

        printToFile(CondensedAntonyms, "Antonyms.txt");
        printToFile(CondensedHyponyms, "Hyponyms.txt");
        printToFile(CondensedSynonyms, "Synonyms.txt");

    }

    public static ArrayList<Word> scoreWords(ArrayList<Word> wordList) {
        ArrayList<Word> condensedList = new ArrayList<Word>();
        int i = 0, j;
        double sum, avg, count;
        while (i < wordList.size()) {
            j = i + 1;
            sum = wordList.get(i).getScore();
            count = 1;
            //System.out.println(wordList.get(i).getWord());
            while (j < wordList.size() && wordList.get(j).getWord().equals(wordList.get(i).getWord())) {
                sum += wordList.get(j).getScore();
                count++;
                //System.out.println("\t"+ wordList.get(j).getWord() +" "+ count);
                j++;
            }
            avg = sum / count;
            condensedList.add(new Word(wordList.get(i).getWord(), avg));
            //condensedList.get(condensedList.size() - 1).setParent(wordList.get(i).getParent());
            //condensedList.get(condensedList.size() - 1).setPartOfSpeech(wordList.get(i).getPartOfSpeech());
            i = j;
        }
        return condensedList;
    }

    public static void printToFile(ArrayList<Word> words, String filename) throws FileNotFoundException {
        File file = new File("T:\\Academics\\Research\\Synset\\NewLexicon\\" + filename);
        PrintWriter p = new PrintWriter(file);
        for (Word word : words) {
            p.println(word.getWord() + " " + format(word.getScore()));
        }
        p.close();
    }

    public static String format(double number) {
        return String.format("%.3f", number);
    }

}
