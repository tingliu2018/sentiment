/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listtags;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import utils.ReadObject;
import data.Professor;
import data.Tags;
import data.Tag;


/**
 *
 * @author Aidan Canavan
 */
public class ListTags {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) throws FileNotFoundException{
    String name = "NE1";
    File dir = new File("C:\\Users\\Aidan Canavan\\Desktop\\RatingsSep\\"+name);
    File outputFile = new File("output.txt");
    //outputFile.renameTo(new File("C:\\Users\\Aidan Canavan\\Desktop\\RatingsSep\\"+name+".txt"));
    PrintWriter out = new PrintWriter(outputFile);
    File[] directoryListing = dir.listFiles();
    ReadObject rd = new ReadObject();
    ArrayList<String> tagList = new ArrayList<String>();
    ArrayList<Integer> tagNum = new ArrayList<Integer>();
    int counter =0;
    for(File f: directoryListing){
        Professor prof = rd.readObjectFromFile(f.getAbsolutePath());
        Tags myTags = prof.getTags();
        out.println(prof.getfName());
        System.out.println(prof.getfName());
        
        for(int p=0; p<myTags.size();p++){
            String str = myTags.get(p).getTag();
            if(str.substring(0,1).equals(" ")){
             out.println(str.substring(1,str.length()-1));
             str=str.substring(1,str.length()-1);
            }
            else{out.println(str);}
             //out.print(":");
            if(str.substring(str.length()-1,str.length()).equals(" ")){str=str.substring(0,str.length()-1);}
            if(!tagList.contains(str)){
            tagList.add(str);
            tagNum.add(myTags.get(p).getVotes());
            }
            else{
            int n = tagList.indexOf(str);
            int placehold = tagNum.get(n);
            tagNum.set(n,placehold+myTags.get(p).getVotes());
            }
            out.println(myTags.get(p).getVotes());
             //out.println("\n");
        }
        //System.out.println("\n");
        counter++;
        System.out.println(counter);
        
        
    
    }
    out.close();
    File output2 = new File(name+".txt");
    
    PrintWriter pw = new PrintWriter(output2);
    for(String s:tagList){
        pw.println(s+": "+tagNum.get(tagList.indexOf(s)));
        
    }
    pw.close();
    output2.renameTo(new File("C:\\Users\\Aidan Canavan\\Desktop\\RatingsSep\\"+name+".txt"));
    
    }
    
}
