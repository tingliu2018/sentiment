/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class creates a professor object with information about them.
 * @author ting
 * 
 */
public class Professor implements Serializable {
    private String fName;
    private String lName;
    private School school;
    private double overall; // overall quality
    private StudentRates ratings;
    private Tags tags;
    private double difficulty;
    private String takeAgain;
    private String hotness;
    private String department;

    /**
     * This constructor initializes the professor class with the proper instance variable names
     * @param fName - the professor's first name.
     * @param lName - the professor's last name.
     * @param school - the school the professor teaches at
     * @param overall - the overall rating the professor has.
     * @param ratings - The ratings the students have given the professor
     * @param tags - What the students say about the professor from a list of tags on rmp.
     * @param difficulty - How difficult the students thought the courses this professor taught were. 
     * @param takeAgain - whether the student would take the course again.
     * @param hotness - if a student thinks a teacher's teaching style is considered "hot"
     * @param department - the department the professor teaches in
     */
    public Professor(String fName, String lName, School school, double overall, StudentRates ratings, Tags tags, double difficulty, String takeAgain, String hotness, String department) {
        this.fName = fName;
        this.lName = lName;
        this.school = school;
        this.overall = overall;
        this.ratings = ratings;
        this.tags = tags;
        this.difficulty = difficulty;
        this.takeAgain = takeAgain;
        this.hotness = hotness;
        this.department = department;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public double getOverall() {
        return overall;
    }

    public void setOverall(double overall) {
        this.overall = overall;
    }

    public StudentRates getRatings() {
        return ratings;
    }

    public void setRatings(StudentRates ratings) {
        this.ratings = ratings;
    }
    
    public void addRating(StudentRate rate) {
        this.ratings.add(rate);
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }
    
    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public String getTakeAgain() {
        return takeAgain;
    }

    public void setTakeAgain(String takeAgain) {
        this.takeAgain = takeAgain;
    }

    public String getHotness() {
        return hotness;
    }

    public void setHotness(String hotness) {
        this.hotness = hotness;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }    

    @Override
    public String toString() {
        return "Professor{\n" + "fName=" + fName + "\nlName=" + lName + "\ndepartment=" + department + "\nschool=" + school + "\noverall=" + overall + "\ntags=" + tags + "\ndifficulty=" + difficulty + "\ntakeAgain=" + takeAgain + "\nhotness=" + hotness + "\nratings=" + ratings + '}';
    }
    
}
