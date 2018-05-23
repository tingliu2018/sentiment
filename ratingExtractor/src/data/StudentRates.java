/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;

/**
 *
 * @author ting
 */
public class StudentRates extends ArrayList<StudentRate> {

    @Override
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
