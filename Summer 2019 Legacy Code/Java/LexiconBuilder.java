package parsertest;

import edu.stanford.nlp.util.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author Code Monkey A
 */
public class LexiconBuilder {
    
    static ArrayList<Word> words = new ArrayList();
    public static void main(String[] args) throws FileNotFoundException {
        File originalLexicon = new File("/media/thomas/ESD-USB/Scores.csv");
        Scanner sc = new Scanner(originalLexicon);
        sc.nextLine();
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String word = line.split(",")[0].toLowerCase();
            words.add(new Word(word, Double.parseDouble(line.split(",")[1])));
            
                if(words.get(words.size() -1).getWord().equals("TRUE") || words.get(words.size() -1).getWord().equals("FALSE")){
                    System.out.println("This is garbage");
                }
        }
        
        File adjSyn = new File("/media/thomas/ESD-USB/Synset/Lexicon/AdjectiveSyn.txt");
        File adjAnt = new File("/media/thomas/ESD-USB/Synset/Lexicon/AdjectiveAnt.txt");
        File adverbAnt = new File("/media/thomas/ESD-USB/Synset/Lexicon/AdverbSyn.txt");
        File adverbSyn = new File("/media/thomas/ESD-USB/Synset/Lexicon/AdverbAnt.txt");
        File unknownWords = new File("/media/thomas/ESD-USB/Synset/Unknown/expansionWords.txt");
        
        readFile(adverbSyn);
        readFile(adverbAnt);
        readFile(adjSyn);
        readFile(adjAnt);
        readFile(unknownWords);
        
        Collections.sort(words);
        
        File output = new File("/media/thomas/ESD-USB/Lexicon.csv");
        PrintWriter p = new PrintWriter(output);
        
        for (int i = 0; i < words.size(); i++) {
            p.println(words.get(i).getWord() +"\t"+ words.get(i).getScore());
        }
        
        p.close();
        
    }
    
    public static void readFile(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            String word = line.split(" ")[0].toLowerCase();
            if(!(word.contains("_") || word.contains("(") || word.contains(")")) && !StringUtils.isNumeric(word)){
                Word newWord = new Word(word, Double.parseDouble(line.split(" ")[1]));
                if(!words.contains(newWord)){
                    words.add(newWord);
                }
            }
        }
    }
}
