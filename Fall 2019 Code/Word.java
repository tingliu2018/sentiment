package wordproject;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

/**
 *
 * @author Code Monkey A
 * @author Code Monkey B
 */
public class Word implements Comparable {

    protected String word = null;
    protected double count = -1;
    protected double averageCount = 0;
    protected double awesomeCount = 0;
    protected double awfulCount = 0;
    protected double goodCount = 0;
    protected double poorCount = 0;
    protected double score = -1;
    protected double lexScore = -1;
    /* 0 = Adjective, 1 = Adverb, 3 = Noun, 4 = Verb */
    protected IndexWord[] iWords = new IndexWord[4];
    protected Word parent = null;
    protected String partOfSpeech = "";

    /**
     * Constructor for when a words appearances are being counted
     *
     * @param word The words string
     * @param count The current number of times the word has been seen across
     * all files
     */
    public Word(String word, int count) {
        this.word = word;
        this.count = count;
    }

    /**
     * Constructor for when a Word is used that has a sentiment score
     *
     * @param word The words string
     * @param score The sentiment score of the word
     */
    public Word(String word, double score) {
        this.word = word;
        this.score = score;
    }

    /**
     * Constructor for a word generated by another words Synset, score is set to
     * parents score
     *
     * @param word This words string
     * @param parent The parent word of which this word is part of its synset
     */
    public Word(String word, Word parent) {
        this.word = word;
        this.score = parent.getScore();
        this.parent = parent;
    }
    
    /**
     * Word object for when there is more than one score
     * @param word Word string
     * @param score Our Score which we generated previously
     * @param lexScore Lexicon Score
     */
    public Word(String word, double score, double lexScore) {
        this.word = word;
        this.score = score;
        this.lexScore = lexScore;
    }

    public String getWord() throws NullPointerException {
        if (word == null) {
            throw new NullPointerException();
        } else {
            return word;
        }
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getAverageCount() {
        return averageCount;
    }

    public void setAverageCount(double averageCount) {
        this.averageCount = averageCount;
    }

    public double getAwesomeCount() {
        return awesomeCount;
    }

    public void setAwesomeCount(double awesomeCount) {
        this.awesomeCount = awesomeCount;
    }

    public double getAwfulCount() {
        return awfulCount;
    }

    public void setAwfulCount(double awfulCount) {
        this.awfulCount = awfulCount;
    }

    public double getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(double goodCount) {
        this.goodCount = goodCount;
    }

    public double getPoorCount() {
        return poorCount;
    }

    public void setPoorCount(double poorCount) {
        this.poorCount = poorCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Word getParent() throws NullPointerException {
        if (parent == null) {
            throw new NullPointerException();
        } else {
            return parent;
        }
    }

    public void setParent(Word parent) {
        this.parent = parent;
    }

    public double getLexScore() {
        return lexScore;
    }

    public void setLexScore(double lexScore) {
        this.lexScore = lexScore;
    }
    
    public double getTotalCount(){
        return awesomeCount + goodCount + averageCount + poorCount + awfulCount; 
    }

    @Override
    public String toString() {
        return word + " " + count;
    }

    /**
    @Override
    public int compareTo(Object obj) {
        Word compareWord = (Word) obj;
        return (int) (compareWord.getCount() - this.count);
    }
    **/
    @Override
    public int compareTo(Object obj) {
        Word compareWord = (Word) obj;
        return this.word.compareTo(compareWord.getWord());
    }
    
    @Override
    public boolean equals(Object obj){
        Word compareWord = (Word) obj;
        return this.word.equals(compareWord.getWord());
    }
    
    public void dictionaryLookUp() throws JWNLException{
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();
        iWords[0] = dictionary.getIndexWord(POS.ADJECTIVE, this.word);
        iWords[1] = dictionary.getIndexWord(POS.ADVERB, this.word);
        iWords[2] = dictionary.getIndexWord(POS.VERB, this.word);
        iWords[3] = dictionary.getIndexWord(POS.NOUN, this.word);
    }
    
    public IndexWord[] getIndexWords(){
        return this.iWords;
    }
    
    public void setPartOfSpeech(String pos){
        this.partOfSpeech = pos;
    }
    
    public String getPartOfSpeech(){
        return this.partOfSpeech;
    }
}
