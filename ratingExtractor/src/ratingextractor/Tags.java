/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ratingextractor;

import java.util.ArrayList;
import utils.Out;

/**
 *
 * @author ting
 */
public class Tags extends ArrayList {

    @Override
    public Tag get(int index) {
        return (Tag)super.get(index); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static Tags parseTags(String tagContent) {
        String[] items = tagContent.split("[()]+");
        Tags tags = new Tags();
//        Out.print(tagContent.length());
        if (tagContent.length() == 0) {
            return tags;
        }
        for (int i = 0; i < items.length; i++) {
            String tag = items[i];
            int freq = Integer.parseInt(items[++i]);
            tags.add(new Tag(tag, freq));
        }
        return tags;
    } 
}
