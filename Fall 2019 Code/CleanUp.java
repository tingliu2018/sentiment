/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author thomas
 */
public class CleanUp {
    public static void main(String[] args) throws FileNotFoundException {
        File fileIn = new File("/media/thomas/ESD-USB/Synset/NewLexicon/Antonyms.txt");
        Scanner sc = new Scanner(fileIn);
        ArrayList<Word> Words = new ArrayList<Word>();
        while(sc.hasNextLine()) {
            String[] line = sc.nextLine().split(" ");
            Words.add(new Word(line[0], Double.parseDouble(line[1])));
            
        }
        ArrayList<Word> words = scoreWords(Words);
        for (int i = 0; i < words.size()-1; i++) {
                System.out.println(words.get(i));
                //System.out.println(words.get(i+1));
                System.out.println("");
        }
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
    
}
