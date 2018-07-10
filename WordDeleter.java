/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Aidan Canavan
 */
public class WordDeleter {
    
    private static String fileName;
    private static List<String> commonWords = new ArrayList<>(Arrays.asList("the", "him", "his", "her", "he", "she", "a", "an", "of", "to", "this", 
            "and", "that", "not", "on", "as", "you", "at", "from", "for", "over", "after", "about", "up", "my"));
    
    public static void main(String[] args)throws FileNotFoundException{
    WordDeleter wd = new WordDeleter("C:\\Users\\Aidan Canavan\\Desktop\\MaleGoodPhrase.txt");
    wd.deleteWords("myOutput");
    }
    
    
    public WordDeleter(String fileName){
    this.fileName = fileName;
    }
    public void deleteWords(String outputFileName) throws FileNotFoundException{
    File inputFile = new File(fileName);
    
    File outputFile =  new File(outputFileName+".txt");
    PrintWriter pr = new PrintWriter(outputFile);
    Scanner sc = new Scanner(inputFile);
    int c =0;
    while(sc.hasNextLine()){
        c++;
        System.out.println(c);
    String nounPhrase = sc.nextLine();
        //System.out.println(nounPhrase);
    ArrayList<String> nounPhraseList= new ArrayList<String>();
            nounPhraseList.addAll(makeList(nounPhrase+" "));
            //System.out.println(nounPhraseList);
            int size =nounPhraseList.size();
            ArrayList<String> tempList = new ArrayList<String>();
            tempList.addAll(nounPhraseList);
            
    for(int p=0;p<tempList.size();p++){
        
        String word = tempList.get(p);
    if (commonWords.contains(word)){nounPhraseList.remove(word);}
    }
        //System.out.println(nounPhraseList);
    String toOutput="";
    
    for(String myWord:nounPhraseList){
    toOutput= toOutput+" "+myWord;
    }
    pr.println(toOutput);
    }
    pr.close();
    }
    
    
    public ArrayList makeList(String phrase){
        String word="";
        ArrayList<String> output = new ArrayList<String>();
    for(int i=0;i<phrase.length()-1;i++){
    String str = phrase.substring(i,i+1);
    if(str.equals(" ")){
        //System.out.println(word);
        output.add(word);
        str ="";
        word="";
        
    
    }
    else{
    word =word+str;
    }
    
    }
        //System.out.println(output);
    return output;
    }
    
    
    }

