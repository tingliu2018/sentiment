/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsertest;

import java.io.File;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author thomas
 */
public class Hypernym {

    /**
     *
     * @param args Word to print hypernyms of, Output file
     */
    public static void main(String[] args) {
        ArrayList<IndexWord> knownWords = new ArrayList<IndexWord>();
        ArrayList<Integer> knownWordsCount = new ArrayList<Integer>();
        ArrayList<IndexWord> unknownWords = new ArrayList<IndexWord>();
        ArrayList<Integer> unknownWordsCount = new ArrayList<Integer>();
        try {
            String unknownOutputText = "";
            Dictionary dictionary = Dictionary.getDefaultResourceInstance();
            File knownFile = new File("PUT A FILE PATH HERE");
            Scanner reader = new Scanner(knownFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (!line.equals("")) {
                    StringBuffer problemFixer = new StringBuffer(line);
                    problemFixer.reverse();
                    //System.out.println(problemFixer);
                    int index = line.length() - problemFixer.indexOf(" ");
                    int count = Integer.parseInt(line.substring(index, line.length()));
                    String word = line.substring(0, index);
                    IndexWord iWord = dictionary.getIndexWord(POS.NOUN, word);
                    if (iWord == null) {
                        iWord = dictionary.getIndexWord(POS.VERB, word);
                        if (iWord == null) {
                            iWord = dictionary.getIndexWord(POS.ADJECTIVE, word);
                            if (iWord == null) {
                                iWord = dictionary.getIndexWord(POS.ADVERB, word);

                            }
                        }
                    }
                    if (iWord != null) {
                        knownWords.add(iWord);
                        knownWordsCount.add(count);
                    }
                }
            }
            File unknownFile = new File("PUT A FILE PATH HERE");
            reader = new Scanner(unknownFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] lineSplit = line.split(" ");
                IndexWord word = dictionary.getIndexWord(POS.NOUN, lineSplit[0]);
                if (word == null) {
                    word = dictionary.getIndexWord(POS.VERB, lineSplit[0]);
                    if (word == null) {
                        word = dictionary.getIndexWord(POS.ADJECTIVE, lineSplit[0]);
                        if (word == null) {
                            word = dictionary.getIndexWord(POS.ADVERB, lineSplit[0]);

                        }
                    }
                }
                if (word != null) {
                    unknownWords.add(word);
                    unknownWordsCount.add(Integer.parseInt(lineSplit[1]));
                }
            }
            int j = 0;
            boolean flag = false;
            for (IndexWord word : unknownWords) {
                //System.out.println(unknownWords.toString());
                if (word.getLemma() != null && !word.getLemma().equals("")) {
                    int i = 0;
                    for (IndexWord searchWord : knownWords) {
                        if (searchWord != null) {
                            if (word.getLemma().equals(searchWord.getLemma())) {
                                knownWordsCount.set(i, knownWordsCount.get(i) + unknownWordsCount.get(j));
                                flag = true;
                                break;
                            }
                        }
                        i++;
                    }
                    if (flag == false) {
                        unknownOutputText += unknownWords.get(j).getLemma() + ": " + unknownWordsCount.get(j) + "\n";
                    } else {
                        flag = false;
                    }
                }
                j++;
            }

            PrintWriter pw = new PrintWriter("PUT A FILE PATH HERE");
            pw.println(unknownOutputText);
            pw.flush();
            pw = new PrintWriter("PUT A FILE PATH HERE");
            int i = 0;
            for (IndexWord word : knownWords) {
                pw.println(word.getLemma() + " " + knownWordsCount.get(i));
                i++;
            }
            pw.close();
            //PointerTargetNodeList hypernyms = PointerUtils.getDirectHypernyms(word.getSenses().get(0));
            //System.out.println("Direct hypernyms of \"" + word.getLemma() + "\":");
            //hypernyms.toString(); Print to file
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//}
