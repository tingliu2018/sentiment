/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratingextractor;

import java.io.File;
import ratingextractor.Professor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import ratingextractor.StudentRate;
import ratingextractor.StudentRates;
import utils.ReadObject;

/**
 *
 * @author tliu and Yuwei Chen
 */
public class GenderSort {

    private static String[] s = new String[520000];
    private static int counter = 0;
    private static File male = new File("C:\\Users\\Owner\\Desktop\\Data\\MaleAwesomeTags2014.txt");
    private static File female = new File("C:\\Users\\Owner\\Desktop\\Data\\FemaleAwesomeTags2014.txt");
    private static String male2 = "";
    private static String female2 = "";

    public static Professor readObjectFromFiles(String filepath,PrintWriter p, PrintWriter p2) {
        try {
            //reads object file and outputs it as a professor
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            //  System.out.println("The Object has been read from the file");
            objectIn.close();
            //downcast to Student Rate to grab rating of every rating
            Professor input = (Professor) obj;
            StudentRates ratings = input.getRatings();
            //for loop looping through the array of ratings to find each comment
            //String fName = input.getfName();
            // String lName = input.getlName();
            for (int i = 0; i < ratings.size(); i++) {
                StudentRate r = ratings.get(i);
                String rating = r.getRating();
                String comment = r.getComment();
                String date = r.getDate();
                ArrayList<Tag> tags = new ArrayList<Tag>();
                tags = r.getTags();
                //if the rating is awesome and the comment has the words
                if (rating.contains("awesome") == true) {
                    //if comments contain female pronouns and tag isn't empty print it to t
                    if (comment.contains("she") == true || comment.contains("She") == true || comment.contains("her") == true || comment.contains("Her") == true) {
                        if (!(tags.isEmpty())) {
                            if (date.substring(date.length() - 4).equals("2014")) {
                                female2 += tags + "\n" + date;
                            }
                        }
                    } else if (comment.contains("he") == true || comment.contains("He") == true || comment.contains("his") == true || comment.contains("His") == true || comment.contains("Guy") == true || comment.contains("guy") == true) {
                        if (!(tags.isEmpty())) {
                            if (date.substring(date.length() - 4).equals("2014")) {
                                male2 += tags + "\n" + date;
                            }
                        }
                    }
                }

                /* else if (rating.contains("awful") == true) {
                    if (comment.contains("she") == true || comment.contains("She") == true || comment.contains("her") == true || comment.contains("Her") == true) {
                       if(!(tags.isEmpty())){
                        female2 += tags + "\n";
                        female2 += date + "\n";
                    }
                        //  female2 += comment + "\n";
                    } else if (comment.contains("he") == true || comment.contains("He") == true || comment.contains("his") == true || comment.contains("His") == true || comment.contains("Guy") == true || comment.contains("guy") == true) {
                       if(!(tags.isEmpty())){
                        male2 += tags + "\n";
                        male2 += date + "\n";
                    }
                        // male2 += comment + "\n";
                    }
                }*/
            }
            p2.print(female2);
            p.print(male2);
            p.flush();
            p2.flush();
            return input;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        //sets path to get test data
        Path dir = Paths.get("C:\\Users\\Owner\\Desktop\\ratingExtractor\\text\\sentiment");
        //counter for array and array upper bound
        counter = 0;
        int i = 0;
        //gets all files name's in directory and stores them in an array
        PrintWriter p = new PrintWriter(male);
        PrintWriter p2 = new PrintWriter(female);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                s[i] = file.toString();
                counter++;
                i++;
            }
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x);
        }
        // runs through all files in selected directory
        for (int r = 0; r < counter; r++) {
            System.out.println(r);
            readObjectFromFiles(s[r],p,p2);
        }
    }

}
