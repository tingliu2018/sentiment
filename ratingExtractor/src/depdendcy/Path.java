/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package depdendcy;

import java.util.ArrayList;

/**
 *
 * @author ting
 */
public class Path extends ArrayList<Node> {

    //@Override
    public String toString(Node con_head, Node rel_node) {
        StringBuffer out = new StringBuffer();
        ArrayList processed_nodes = new ArrayList();
        for (int i = 0; i < size(); i++) {
            Node node = get(i);
            if (processed_nodes.contains(node)) {
                remove(node);
                i--;
                continue;   
            }
            processed_nodes.add(node);
            if (!node.equals(con_head) && !node.equals(rel_node)) {
                out.append(node.getName() + "/" + node.getPos() + " ");
            } else if (!node.equals(rel_node)) {
                out.append("CONCEPT/" + node.getPos() + " ");
            }
        }
        return out.toString().trim();
    }

    public String toExactString(Node con_head, Node rel_node) {
        StringBuffer out = new StringBuffer();
        ArrayList processed_nodes = new ArrayList();
        for (int i = 0; i < size(); i++) {
            Node node = get(i);
            if (processed_nodes.contains(node)) {
                remove(node);
                i--;
                continue;   
            }
            processed_nodes.add(node);
            if (!node.equals(con_head) && !node.equals(rel_node)) {
                out.append(node.getName() + " ");
            } else if (!node.equals(rel_node)) {
                ;//out.append("CONCEPT" + " ");
            }
        }
        return out.toString().trim();
    }


    public String getPOS(String pos) {
        if (pos.startsWith("N")) {
            return "noun";
        } else if (pos.startsWith("V")) {
            return "verb";
        } else if (pos.startsWith("A")) {
            return "adj";
        } else {
            return "noun";
        }
    }


    public boolean contains(String type, String content) {
        for (int i = 0; i < size(); i++) {
            Node node = get(i);
            if (type.equals(this.POS)) {
                if (node.getPos().equals(content)) {
                    return true;
                }
            } else {
//                System.out.println("path node: " + node.getLemmaWord() + "---" + node.getName());
                if ((node.getLemmaWord() != null
                        && node.getLemmaWord().trim().equals(content))
                        || node.getName().trim().equals(content)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final long serialVersionUID = 9114711482163825155L;
    public static final String POS = "pos";
    public static final String WORD = "word";
}
