/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;

/**
 *
 * @author ting
 */
public class Tag implements Serializable {
    private String tag;
    private int votes; //# of votes for this tag

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
