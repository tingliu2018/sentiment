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
import org.apache.log4j.BasicConfigurator;

/**
 * Parses a text file and pulls out noun phrases, if no args given then filepicker forces a choice of txt files for input and output
 * If 2 args then first arg Input text file, second arg output text file
 * @author Code Monkey A
 * @author Code Monkey B
 */
public class NounParser {

    public static void main(String[] args) {
        try {
            String fileInput = "";
            String fileOutput = "";
            BasicConfigurator.configure();
            if (args.length == 0) {
                JOptionPane.showMessageDialog(null, "Choose file to read");
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fileInput = fc.getSelectedFile().getAbsolutePath();
                }
                JOptionPane.showMessageDialog(null, "Choose file to write to");
                returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fileOutput = fc.getSelectedFile().getAbsolutePath();
                }
            } else if(args.length == 2){
                fileInput = args[0];
                fileOutput = args[1];
            } else {
                System.out.println("Requires 0 or 2 Args");
            }
            PrintWriter pw = new PrintWriter(fileOutput);
            String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
            LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
            TreebankLanguagePack tlp = lp.treebankLanguagePack(); // a PennTreebankLanguagePack for English
            GrammaticalStructureFactory gsf = null;
            if (tlp.supportsGrammaticalStructures()) {
                gsf = tlp.grammaticalStructureFactory();
            }
            for (List<HasWord> sentence : new DocumentPreprocessor(fileInput)) {
                Tree parse = lp.apply(sentence);
                int i = 1;
                while (parse.getNodeNumber(i) != null) {
                    //Filter for noun phrases
                    if (parse.getNodeNumber(i).toString().contains("NP")
                            && !parse.getNodeNumber(i).toString().contains("S")
                            && !parse.getNodeNumber(i).toString().contains("ROOT")
                            && !parse.getNodeNumber(i).toString().contains("V")) { //Print
                        StringBuffer temp = new StringBuffer(parse.getNodeNumber(i).getLeaves().toString()); //Temporary buffer for each stiring used for cleaning
                        for (int j = 0; j < temp.length(); j++) { //Clean up string, remove brackets and commas
                            if (temp.charAt(j) == ',' || temp.charAt(j) == '[' || temp.charAt(j) == ']') {
                                temp.deleteCharAt(j);
                            }
                        }
                        pw.println(temp.toString()); //Write cleaned string to file
                    }
                    i++;
                }

                if (gsf != null) {
                    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                    Collection tdl = gs.typedDependenciesCCprocessed();
                }
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
