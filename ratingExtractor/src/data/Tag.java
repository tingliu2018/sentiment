/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;

/**
 * This method contains a single tag for a professor, and how many students voted for them on the tag.
 * @author ting
 */
public class Tag implements Serializable {
    private String tag;
    private int votes; //# of votes for this tag

    /**
     * This method creates a new tag to give to a professor.
     * @param tag - the tag for a professor
     * @param votes - the number of votes the given tag has received.
     */
    public Tag(String tag, int votes) {
        this.tag = tag;
        this.votes = votes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Tag{" + "tag=" + tag + ", votes=" + votes + '}';
    }
    
}
