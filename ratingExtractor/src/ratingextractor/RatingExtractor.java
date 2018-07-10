/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratingextractor;

import data.Tags;
import data.Tag;
import data.School;
import data.StudentRate;
import data.Professor;
import data.Course;
import data.StudentRates;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.*;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import utils.Out;

/**
 *
 * @author ting
 */
public class RatingExtractor {

    private String path = null;
    private String outPath = null;
    private ArrayList<String> problem_files = new ArrayList();

    public RatingExtractor(String path, String outPath) {
        this.path = path;
        this.outPath = outPath;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
//        String path = "C:\\NetBeans\\sentiment\\ratingExtractor\\data\\";
        String path = "C:\\research\\rateMyProfessors\\htmls\\";
//        String outPath = "C:\\NetBeans\\sentiment\\ratingExtractor\\obj\\";
        String outPath = "C:\\research\\rateMyProfessors\\objs\\";
        RatingExtractor re = new RatingExtractor(path, outPath);
        re.parse();
    }

    public void parse() throws IOException {
        File path = new File(this.path);
        File[] list = path.listFiles();
        for (File dir : list) {
            System.out.println(dir.getName());
            if (dir.isDirectory() && dir.getName().contains("sentiment")) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    try {
                        System.out.println("processing: " + file.getName());
                        Document doc = Jsoup.parse(file, "UTF-8");
                        if (!doc.text().contains("Professor in")) {
                            Out.print("skip " + file.getName());
                            continue;
                        }
                        Elements fName = doc.getElementsByClass("pfname");
                        System.out.println("first name: " + fName.text());
                        Elements lName = doc.getElementsByClass("plname");
                        Elements tags = doc.getElementsByClass("tag-box");
                        Tags tagList = Tags.parseTags(tags.text());
                        Elements from = doc.getElementsByClass("schoolname");
                        Elements departmentE = doc.getElementsByClass("result-title");
                        String department = departmentE.first().textNodes().get(0).toString().trim().substring("Professor in ".length());
                        Elements schoolName = doc.getElementsByClass("school");
                        School school = new School(schoolName.text(), from.text().substring(from.text().indexOf(schoolName.text()) + schoolName.text().length() + 1).trim());
                        Elements ratings = doc.getElementsByClass("grade");
                        List<String> texts = ratings.eachText();
                        double overall = Double.valueOf(ratings.get(0).text());
                        String takeAgain = ratings.get(1).text();
                        double difficulty = Double.valueOf(ratings.get(2).text());
                        Elements hotness = ratings.get(3).getElementsByAttribute("src");
                        String hot = "hot";
                        if (hotness.contains("cold")) {
                            hot = "cold";
                        }
                        Elements prfScores = doc.getElementsByClass("score"); //overall and level of difficulty
                        Elements ratingTypes = doc.getElementsByClass("rating-type"); //awesome, good, average...
                        Elements dates = doc.getElementsByClass("date");
                        Elements classNames = doc.getElementsByClass("name");
                        Elements classCredits = doc.getElementsByClass("credit");
                        Elements classAttendances = doc.getElementsByClass("attendance");
                        Elements classTextbooks = doc.getElementsByClass("textbook-used");
                        Elements classTakeAgains = doc.getElementsByClass("would-take-again");
                        Elements classGrades = doc.getElementsByClass("grade");
                        Elements individualTags = doc.getElementsByClass("tagbox");
                        Out.print(individualTags.size());
                        Elements comments = doc.getElementsByClass("commentsParagraph");
                        Elements helpfuls = doc.getElementsByClass("helpful");
                        Elements notHelpfuls = doc.getElementsByClass("nothelpful");
                        StudentRates studentRatings = new StudentRates();
                        for (int i = 0; i < ratingTypes.size(); i++) {
                            List<String> tagChildren = individualTags.get(i).children().eachText();
                            ArrayList<Tag> indTags = new ArrayList();
                            for (String tagChild : tagChildren) {
                                Tag aTag = new Tag(tagChild, 1);
                                indTags.add(aTag);
                            }
                            Course course = new Course(classNames.get(i).text(), classCredits.get(i).text(), classTextbooks.get(i).text(), classGrades.get(i + 4).text(), classAttendances.get(i).text(), classTakeAgains.get(i).text());
                            StudentRate studentRate = new StudentRate(ratingTypes.get(i).text(), dates.get(i).text(), Double.valueOf(prfScores.get(2 * i).text()), Double.valueOf(prfScores.get(2 * i + 1).text()), course, indTags, comments.get(i).text(), Integer.valueOf(helpfuls.get(i).getElementsByClass("count").text()), Integer.valueOf(notHelpfuls.get(i).getElementsByClass("count").text()));
                            studentRatings.add(studentRate);
                        }
                        Professor prf = new Professor(fName.text(), lName.text(), school, overall, studentRatings, tagList, difficulty, takeAgain, hotness.text(), department);
//                    Out.print(ratings.get(3).toString());
//                    Out.print(prfScores.size());
//                    Out.print(dates.size());
//                    Out.print(classNames.size());
//                    Out.print(classCredits.size());
//                    Out.print(classAttendances.size());
//                    Out.print(classTextbooks.size());
//                    Out.print(classTakeAgains.size());
//                    Out.print(classGrades.size());
//                    Out.print(individualTags.first().children().eachText().toString());
//                    Out.print(comments.size());
//                    Out.print(helpfuls.size());
//                    Out.print(notHelpfuls.size());
//                    Out.print(departmentE.first().textNodes().get(0).toString().trim().substring("Professor in ".length()));
//                    Out.print(helpfuls.get(0).getElementsByClass("count").text());
//                    Out.print(prf.toString());

//                    System.out.println("tags: " + tags);
//                        writeText(prf, dir, file);
                        writeObj(prf, dir, file);
                    } catch (Exception e) {
                        Out.print("failed for " + file.getName());
                        this.problem_files.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public void writeObj(Professor prf, File dir, File file) throws FileNotFoundException, IOException {
        String fileName = outPath + dir.getName() + "\\" + file.getName().replace("htm", "obj");
        fileName = fileName.replace("objl", "obj");
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(prf);
        oos.close();
    }

    public void writeText(Professor prf, File dir, File file) throws FileNotFoundException, IOException {
        String fileName = outPath + dir.getName() + "\\" + file.getName().replace("htm", "txt");
        fileName = fileName.replace("txtl", "txt");
        PrintWriter pw = new PrintWriter(fileName, "UTF-8");
        pw.print(prf.toString());
        pw.close();
    }
}
