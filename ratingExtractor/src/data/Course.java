/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;

/**
 * This class contains information about the name of the course that is being reviewed
 * @author ting
 */
public class Course implements Serializable {
    private String name;
    private String credit;
    private String textbook;
    private String grade;
    private String attendance;
    private String takeAgain;
    
//    private boolean online = false;

    /**
    * This is a constructor that initializes the current course. being reviewed 
    * @param name - the name of the course being reviewed
    * @param credit - whether credit was received for the course or not
    * @param textbook - whether a textbook was used or not
    * @param grade - what the student got for a grade in the class.
    * @param attendance - whether attendance is mandatory or not.
    * @param takeAgain - whether the student would take the class again or not
    */
    public Course(String name, String credit, String textbook, String grade, String attendance, String takeAgain) {
        this.name = name;
        this.credit = credit.substring(credit.indexOf(":") + 1).trim(); //remove For Credit: 
        this.textbook = textbook.substring(textbook.indexOf(":") + 1).trim(); //remove Textbook Used:
        this.grade = grade.substring(grade.indexOf(":") + 1).trim();//remove Grade Received:
        this.attendance = attendance.substring(attendance.indexOf(":") + 1).trim();//remove Attendance:
        this.takeAgain = takeAgain.substring(takeAgain.indexOf(":") + 1).trim();//remove Would Take Again:
//        this.online = online;
    }

    /**
     * This method returns the name of the course.
     * @return - name of the course.
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name of the course.
     * @param name - new name for the course.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns if the course was taken for credit.
     * @return - course taken for credit or not.
     */
    public String getCredit() {
        return credit;
    }
    
    /**
     * Sets whether the course was taken for credit or not.
     * @param credit - whether the course was taken for credit.
     */
    public void setCredit(String credit) {
        this.credit = credit;
    }

    /**
     * This method returns if a class requires the use of a textbook.
     * @return whether the class requires a textbook or not
     */
    public String getTextbook() {
        return textbook;
    }

    /**
     * This method sets whether a class needs a textbook or not.
     * @param textbook - if a textbook is required for a class
     */
    public void setTextbook(String textbook) {
        this.textbook = textbook;
    }

    /**
     * This method returns the grade the student received in their class
     * @return - the grade the student got in the class
     */
    public String getGrade() {
        return grade;
    }

    /**
     * This method sets the grade a student received in a course.
     * @param grade - the grade a student received in a course.
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * This method records if attendance is necessary for the class.
     * @return whether attendance is necessary
     */
    public String getAttendance() {
        return attendance;
    }

    /**
     * This method sets the attendance variable
     * @param attendance - whether attendance is mandatory or not
     */
    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    /**
     * This method determines if the student would take the class again if they could.
     * @return whether the student would take the class again.
     */
    public String getTakeAgain() {
        return takeAgain;
    }

    /**
     * Sets whether the person would take the class again.
     * @param takeAgain - if the student wants to take the class again.
     */
    public void setTakeAgain(String takeAgain) {
        this.takeAgain = takeAgain;
    }
//    /**
//     * Get the value of online
//     *
//     * @return the value of online
//     */
//    public boolean isOnline() {
//        return online;
//    }

//    /**
//     * Set the value of online
//     *
//     * @param online new value of online
//     */
//    public void setOnline(boolean online) {
//        this.online = online;
//    }

    @Override
    /**
     * This method converts the data about the course into a string.
     */
    public String toString() {
        return "Course{" + "name=" + name + ", credit=" + credit + ", textbook=" + textbook + ", grade=" + grade + ", attendance=" + attendance + ", takeAgain=" + takeAgain + '}';
    }
    
}
