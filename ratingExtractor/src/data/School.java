/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;

/**
 * This class give the name and location of the school a professor teaches at
 * @author ting
 */
public class School implements Serializable {
    private String name; 
    private String location;

    /**
     * This method initializes the school
     * @param name - the name of the school.
     * @param location  - the location where the school is located
     */
    public School(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "School{" + "name=" + name + ", location=" + location + '}';
    }
    
}
