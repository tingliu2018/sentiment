package hello.world;

import java.util.Collection;
import java.util.List;
import java.io.StringReader;
import java.util.ArrayList;

import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

public class Demo {

    public static void main(String[] args) {
        String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        //String filename = "C:\\Users\\Thomas\\Desktop\\HelloWorld.txt";
        String filename = "C:\\Users\\Thomas\\Desktop\\FemaleAwesomeCommentsPunc.txt";
        LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
        TreebankLanguagePack tlp = lp.treebankLanguagePack(); // a PennTreebankLanguagePack for English
        GrammaticalStructureFactory gsf = null;
        if (tlp.supportsGrammaticalStructures()) {
            gsf = tlp.grammaticalStructureFactory();
        }
        for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
            Tree parse = lp.apply(sentence);
            int i = 1;
            while (parse.getNodeNumber(i) != null) {
                //System.out.println("Examining: "+ parse.getNodeNumber(i));
                if (parse.getNodeNumber(i).toString().contains("NP") 
                        && !parse.getNodeNumber(i).toString().contains("S") 
                        && !parse.getNodeNumber(i).toString().contains("ROOT") 
                        && !parse.getNodeNumber(i).toString().contains("V")) {
                    System.out.println(parse.getNodeNumber(i).getLeaves());
                }
                i++;
            }
            //System.out.println(parse.getNodeNumber(4));
            //parse.pennPrint();

            if (gsf != null) {
                GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                Collection tdl = gs.typedDependenciesCCprocessed();
                //System.out.println(tdl);
                System.out.println();
            }
        }
    }
}
