/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;

/**
 * This class is an arraylist of different instances of Student Rate that are stored
 * in an arraylist, and contains all of the student rates for a single professor
 * @author ting
 */
public class StudentRates extends ArrayList<StudentRate> {

    @Override
    /**
     * This method returns the student rate for a professor at the given index in the array list.
     * @return - the student rate at the index in the array list.
     */
    public StudentRate get(int index) {
        return (StudentRate)super.get(index); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("StudentRates{\n");
        for (StudentRate studentRate: this) {
            out.append(studentRate.toString()+"\n");
        }
        return out.append("}").toString();
    }
    
}
