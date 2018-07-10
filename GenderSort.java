/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratingextractor;

import java.io.File;
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
import ratingextractor.Professor;
import utils.ReadObject;

/**
 *
 * @author tliu and Yuwei Chen
 */
public class GenderSort {

    private static String[] s = new String[550000];
    private static int counter = 0;
    private static File male = new File("C:\\Users\\Owner\\Desktop\\obj\\FemaleAwfulTags2017.txt");
    private static File female = new File("C:\\Users\\Owner\\Desktop\\obj\\FemaleAwfulTags2016.txt");
    private static File female2 = new File("C:\\Users\\Owner\\Desktop\\obj\\FemaleAwfulTags2015.txt");
    private static File female3 = new File("C:\\Users\\Owner\\Desktop\\obj\\FemaleAwfulTags2014.txt");
    private static File female4 = new File("C:\\Users\\Owner\\Desktop\\obj\\MaleAwfulTags2017.txt");
    private static File male2 = new File("C:\\Users\\Owner\\Desktop\\obj\\MaleAwfulTags2016.txt");
    private static File male3 = new File("C:\\Users\\Owner\\Desktop\\obj\\MaleAwfulTags2015.txt");
    private static File male4 = new File("C:\\Users\\Owner\\Desktop\\obj\\MaleAwfulTags2014.txt");

    public static void readObjectFromFiles(String filepath, PrintWriter p, PrintWriter p2, PrintWriter p3, PrintWriter p4, PrintWriter p5, PrintWriter p6, PrintWriter p7, PrintWriter p8) {
        try {
            //reads object file and outputs it as a professor
            String maleString = "";
            String maleString2 = "";
            String maleString3 = "";
            String maleString4 = "";
            String femaleString = "";
            String femaleString2 = "";
            String femaleString3 = "";
            String femaleString4 = "";
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            //System.out.println("The Object has been read from the file");
            objectIn.close();
            //downcast to Student Rate to grab rating of every rating
            ratingextractor.Professor input = (Professor) obj;
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
                //System.out.println(date + tags);
                //if the rating is awesome and the comment has the words
                //  for (int j = 0; j < tags.size(); j++) {
                if (rating.contains("awful") == true) {
                    //if comments contain female pronouns and tag isn't empty print it to t
                    if (comment.contains("she") == true || comment.contains("She") == true || comment.contains("her") == true || comment.contains("Her") == true) {
                        if (!(tags.isEmpty())) {
                            if (date.substring(date.length() - 4).equals("2017")) {
                                femaleString += date + tags + "\n";
                            } else if (date.substring(date.length() - 4).equals("2016")) {
                                femaleString2 += date + tags + "\n";
                            } else if (date.substring(date.length() - 4).equals("2015")) {
                                femaleString3 += date + tags + "\n";
                            } else if (date.substring(date.length() - 4).equals("2014")) {
                                femaleString4 += date + tags + "\n";
                            }
                        }
                    } else if (comment.contains("he") == true || comment.contains("He") == true || comment.contains("his") == true || comment.contains("His") == true || comment.contains("Guy") == true || comment.contains("guy") == true) {
                        if (!(tags.isEmpty())) {
                            if (date.substring(date.length() - 4).equals("2017")) {
                                maleString += date + tags + "\n";
                            } else if (date.substring(date.length() - 4).equals("2016")) {
                                maleString2 += date + tags + "\n";
                            } else if (date.substring(date.length() - 4).equals("2015")) {
                                maleString3 += date + tags + "\n";
                            } else if (date.substring(date.length() - 4).equals("2014")) {
                                maleString4 += date + tags + "\n";
                            }
                        }
                    }
                }
                //  }

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
            p.print(femaleString);
            p2.print(femaleString2);
            p3.print(femaleString3);
            p4.print(femaleString4);
            p5.print(maleString);
            p6.print(maleString2);
            p7.print(maleString3);
            p8.print(maleString4);
            p.flush();
            p2.flush();
            p3.flush();
            p4.flush();
            p5.flush();
            p6.flush();
            p7.flush();
            p8.flush();

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        //sets path to get test data
        Path dir = Paths.get("C:\\Users\\Owner\\Desktop\\ratingExtractor\\text\\sentiment");
        //counter for array and array upper bound
        counter = 0;
        int i = 0;
        //gets all files name's in directory and stores them in an array
        PrintWriter p = new PrintWriter(female);
        PrintWriter p2 = new PrintWriter(female2);
        PrintWriter p3 = new PrintWriter(female3);
        PrintWriter p4 = new PrintWriter(female4);
        PrintWriter p5 = new PrintWriter(male);
        PrintWriter p6 = new PrintWriter(male2);
        PrintWriter p7 = new PrintWriter(male3);
        PrintWriter p8 = new PrintWriter(male4);

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
            readObjectFromFiles(s[r], p, p2, p3, p4, p5, p6, p7, p8);
        }
    }

}
