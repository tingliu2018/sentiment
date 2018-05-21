/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratingextractor;

import java.io.Serializable;

/**
 *
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



    public Course(String name, String credit, String textbook, String grade, String attendance, String takeAgain) {
        this.name = name;
        this.credit = credit.substring(credit.indexOf(":") + 1).trim(); //remove For Credit: 
        this.textbook = textbook.substring(textbook.indexOf(":") + 1).trim(); //remove Textbook Used:
        this.grade = grade.substring(grade.indexOf(":") + 1).trim();//remove Grade Received:
        this.attendance = attendance.substring(attendance.indexOf(":") + 1).trim();//remove Attendance:
        this.takeAgain = takeAgain.substring(takeAgain.indexOf(":") + 1).trim();//remove Would Take Again:
//        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getTextbook() {
        return textbook;
    }

    public void setTextbook(String textbook) {
        this.textbook = textbook;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getTakeAgain() {
        return takeAgain;
    }

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
    public String toString() {
        return "Course{" + "name=" + name + ", credit=" + credit + ", textbook=" + textbook + ", grade=" + grade + ", attendance=" + attendance + ", takeAgain=" + takeAgain + '}';
    }
    
}
