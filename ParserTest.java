package parsertest;

import java.util.Collection;
import java.util.List;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ParserTest {

    public static void main(String[] args) {
        try {
        String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        JOptionPane.showMessageDialog(null, "Choose file to read");
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        String filename = "";
        if(returnVal == JFileChooser.APPROVE_OPTION){
            filename = fc.getSelectedFile().getAbsolutePath();
        }
        JOptionPane.showMessageDialog(null, "Choose file to write to");
        String writetofile = "";
        returnVal = fc.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            writetofile = fc.getSelectedFile().getAbsolutePath();
        }
        PrintWriter pw = new PrintWriter(writetofile);
        LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
        TreebankLanguagePack tlp = lp.treebankLanguagePack(); // a PennTreebankLanguagePack for English
        GrammaticalStructureFactory gsf = null;
        if (tlp.supportsGrammaticalStructures()) {
            gsf = tlp.grammaticalStructureFactory();
        }
        for(List<HasWord> sentence : new DocumentPreprocessor(filename)) {
            Tree parse = lp.apply(sentence);
            int i = 1;
            while (parse.getNodeNumber(i) != null) {
                //Filter for noun phrases
                if (parse.getNodeNumber(i).toString().contains("NP") 
                        && !parse.getNodeNumber(i).toString().contains("S") 
                        && !parse.getNodeNumber(i).toString().contains("ROOT") 
                        && !parse.getNodeNumber(i).toString().contains("V")) { //Print
                    StringBuffer temp = new StringBuffer(parse.getNodeNumber(i).getLeaves().toString()); //Temporary buffer for each stiring used for cleaning
                    for(int j = 0; j < temp.length(); j++){ //Clean up string, remove brackets and commas
                        if(temp.charAt(j) == ',' || temp.charAt(j) == '[' || temp.charAt(j) == ']'){
                            temp.deleteCharAt(j);
                        }
                    }
                    pw.println(temp.toString()); //Write cleaned string to file
                    //System.out.println(temp.toString());
                    //System.out.println(parse.getNodeNumber(i).getLeaves().toString());
                }
                i++;
            }

            if (gsf != null) {
                GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                Collection tdl = gs.typedDependenciesCCprocessed();
                //System.out.println();
            }
        }
        pw.close();
    } catch(IOException e){
        e.printStackTrace();
    }
    }
}


