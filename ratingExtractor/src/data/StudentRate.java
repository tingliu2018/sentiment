/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class goes over a single student rating about a single professor
 * @author ting
 */
public class StudentRate implements Serializable {
    private String rating; //awful, awesome
    private String date;
    private double overall;
    private double difficulty;
    private Course course;
    private ArrayList<Tag> tags;
    private String comment;
    private int helpful;
    private int nohelp;

    /**
     * This constructor initializes a student rating of a professor
     * @param rating - the rating the student has given a professor, ranging from awful to awesome
     * @param date - the date the review was made
     * @param overall - The overall score they gave the professor, from 0 to 5
     * @param difficulty - how difficult the student found the course
     * @param course - the name of the course the student took
     * @param tags -the tags the student gave the professor.
     * @param comment - he comment the student gave the professor.
     * @param helpful - whether people found the rating to be helpful.
     * @param nohelp - whether people found the rating to be of no help.
     */
    public StudentRate(String rating, String date, double overall, double difficulty, Course course, ArrayList<Tag> tags, String comment, int helpful, int nohelp) {
        this.rating = rating;
        this.date = date;
        this.overall = overall;
        this.difficulty = difficulty;
        this.course = course;
        this.tags = tags;
        this.comment = comment;
        this.helpful = helpful;
        this.nohelp = nohelp;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getOverall() {
        return overall;
    }

    public void setOverall(double overall) {
        this.overall = overall;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getHelpful() {
        return helpful;
    }

    public void setHelpful(int helpful) {
        this.helpful = helpful;
    }

    public int getNohelp() {
        return nohelp;
    }

    public void setNohelp(int nohelp) {
        this.nohelp = nohelp;
    }

    @Override
    public String toString() {
        return "Rate{" + "\nrating=" + rating + "\ndate=" + date + "\noverall=" + overall + "\ndifficulty=" + difficulty + "\ncourse=" + course + "\ntags=" + tags + "\ncomment=" + comment + "\nhelpful=" + helpful + "\nnohelp=" + nohelp + "\n}";
    }
    
}
