package ratingextractor;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

/**
 *
 * @author Yuwei Chen
 */
public class NameExtractor2 extends HTMLEditorKit.ParserCallback {
    //global string array to store all names
    StringBuffer s;
    private static String[] urls = new String[1532];
    
    public static void main(String[] args) throws FileNotFoundException {
        //
        File out = new File("C:\\Users\\Owner\\Desktop\\data.txt");
        Scanner in = new Scanner(out);
        for(int i=0; i<urls.length-1;i++){
            String s2 = in.nextLine();
            urls[i]= s2;
        }
        File output = new File("C:\\Users\\Owner\\Desktop\\Data\\output1.txt\\");
        PrintWriter p = new PrintWriter(output);
        try {
        for(int r=10;r<11;r++){
            //uses url and input stream to read in the data from weddingvendors.com
            String webPage = "﻿http://www.weddingvendors.com/baby-names/origin/african/boy/";
            URL url = new URL("http://www.weddingvendors.com/baby-names/origin/african/girl/page-7/");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            //convert the data into an array of chars then a string.
            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();
            Scanner sc = new Scanner(result);
            //parses through the text to find the title and names of all participants. 
            while(sc.hasNextLine()){
                String input = sc.nextLine();
                if(input.contains("<Title>")==true){
                    System.out.println("asd");
                    p.print(input);
                }
                else if (input.contains("actual-name")==true){
                    int sIndex = input.indexOf("/\">");
                    int eIndex = input.indexOf("</a>");
                    String finalOut = input.substring(sIndex+3, eIndex);
                    p.print(finalOut+ "\n");
                }
           }
           }
            p.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
