/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import utils.ReadObject;
import data.Professor;

/**
 *
 * @author Aidan Canavan
 */
public class NameSearch {

    /**
     * @param args the command line arguments
     */
    private String countryName[] = new String[]{"african", "african-american", "american", "american-indian", "anglo-saxon", "arabic", "aramaic", "armenian", "arthurian-legend", "australian", "basque", "biblical", "cambodian", "catalan", "celtic", "contemporary", "cornish", "czech", "czechoslovakian", "danish", "dutch", "egyptian", "english", "farsi", "finnish", "french", "gaelic", "german", "greek", "gypsy", "hawaiian", "hebrew", "hindi", "hindu", "hungarian", "indian", "irish", "israeli", "italian", "japanese", "korean", "latin", "lithuanian", "maori", "native-american", "nigerian", "norse", "persian", "phoenician", "polish", "polynesian", "portuguese", "romanian", "russian", "sanskrit", "scandinavian", "scottish", "shakespearean", "singhalese", "slavic", "spanish", "swahili", "swedish", "teutonic", "thai", "turkish", "ukrainian", "vietnamese", "welsh", "yiddish"};
    private String notEnglish[] = new String[]{"african", "hindi", "hindu", "lithuanian", "maori", "nigerian", "persian", "portuguese", "spanish", "swahili", "vietnamese", "turkish", "ukrainian", "welsh", "yiddish", "teutonic", "thai", "basque", "arabic", "aramaic", "armenian", "egyptian", "phoenician", "indian", "japanese", "korean", "latin", "polynesian", "romanian", "russian", "sanskrit", "singhalese", "slavic", "swedish", "indian"};
    private ArrayList<String> myArray = new ArrayList<String>();

    public static void main(String[] args) throws FileNotFoundException {
        NameSearch ns = new NameSearch("C:\\Users\\Aidan Canavan\\Desktop\\sentiment\\names_with_countries.txt");
        //File outputFile = new File("swag222.txt");
        //System.out.println(outputFile.getPath());
        //PrintWriter out = new PrintWriter(outputFile);

        File dir = new File("C:\\Users\\Aidan Canavan\\Desktop\\Internship\\obj\\obj\\sentiment6");
        File[] directoryListing = dir.listFiles();
        int counter = 0;
        for (File child : directoryListing) {
            counter++;
            ReadObject re = new ReadObject();
            data.Professor prof = re.readObjectFromFile(child.getPath());
            System.out.println(counter);
            String country = ns.getCountry(prof.getfName());
            //System.out.println(child.getName());
            if(ns.englishSpeaker(country).equals("English")){
                child.renameTo(new File("C:\\Users\\Aidan Canavan\\Desktop\\Internship\\English\\"+child.getName()));
                //out.println(prof.getfName() + "," + ns.englishSpeaker(country));
            }
            else if(ns.englishSpeaker(country).equals("Not English")){
                child.renameTo(new File("C:\\Users\\Aidan Canavan\\Desktop\\Internship\\NotEnglish\\"+child.getName()));
                //out.println(prof.getfName() + "," + ns.englishSpeaker(country));
            }
            else{
            child.renameTo(new File("C:\\Users\\Aidan Canavan\\Desktop\\Internship\\Removed\\"+child.getName()));
            }
            
            //System.out.println(prof.getfName());
            //System.out.println(country);
            //System.out.println(ns.englishSpeaker(country));
            
        }
        //System.out.println(outputFile.getPath());
        //out.close();

    }

    public NameSearch(String fileName) throws FileNotFoundException {
        File namesAndCountries = new File(fileName);
        Scanner sc = new Scanner(namesAndCountries);
        String temp;
        while (sc.hasNextLine()) {
            temp = sc.nextLine();
            //System.out.println(temp);
            this.myArray.add(temp);
        }

    }

    public String getCountry(String name) {
        if (name.length() > 1) {
            name = name.substring(0, name.length() - 1);
        }
        String country = "";
        String temp = "";
        for (int p = 0; p < myArray.size(); p++) {
            temp = myArray.get(p);
            if (Arrays.asList(countryName).contains(temp)) {
                country = temp;
            }
            if (temp.equals(name)) {
                return country;
            }

        }
        return "Not Found";

    }

    public String englishSpeaker(String country) {
        if(country.equals("Not Found")){
            return "Remove";
        }
        else if (Arrays.asList(notEnglish).contains(country)) {
            return "Not English";
        } 
        
        else{return "English";}
    }
}
