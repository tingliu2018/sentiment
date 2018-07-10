/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import data.Professor;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 *
 * @author tliu
 */
public class ReadObject {
    public Professor readObjectFromFile(String filepath) {
        try {
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            System.out.println("The Object has been read from the file");
            objectIn.close();
            return (Professor) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        ReadObject rObj = new ReadObject();
        Out.print(rObj.readObjectFromFile("C:\\NetBeans\\sentiment\\ratingExtractor\\obj\\sentiment\\sample.obj").toString());
    }
}
