/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

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
import ratingextractor.StudentRate;
import ratingextractor.StudentRates;

/**
 *
 * @author tliu and Yuwei Chen
 */
public class ReadObject {

    private static String[] s = new String[114360];
    private static int counter = 0;
    private static File output = new File("C:\\Users\\Owner\\Desktop\\Data\\data2.txt");

    public Professor readObjectFromFile(String filepath, PrintWriter p) {
        try {
            //reads object file and outputs it as a professor
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            System.out.println("The Object has been read from the file");
            objectIn.close();
            //downcast to Student Rate to grab rating of every rating
            Professor input = (Professor) obj;
            StudentRates ratings = input.getRatings();
            for (int i = 0; i < ratings.size(); i++) {
                StudentRate r = ratings.get(i);
                String rating = r.getRating();
            //filter all comments thats not awesome or awful
                if (rating.equals("awesome")) {
                    p.print(r.toString());
                } else if (rating.equals("awful")) {
                    p.print(r.toString());
                }
            }
            p.flush();
            return input;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        ReadObject rObj = new ReadObject();
        //sets path to get test data
        PrintWriter p = new PrintWriter(output);
        Path dir = Paths.get("C:\\Users\\Owner\\Desktop\\ratingExtractor\\text\\sentiment6");
        //counter for array and array upper bound
        counter = 0;
        int i = 0;
        //gets all files name's in directory and stores them in an array
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
            rObj.readObjectFromFile(s[r], p).toString();
        }
    }
}
