/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package depdendcy;

import java.io.Serializable;

/**
 *
 * @author cslin
 */
public class Edge implements Serializable {

    private Node     parent, child;
    private String   relation;
    private double weight;

    public Edge( Node p, Node c, String r)
    {
        this.parent=p;
        this.child = c;
        this.relation = r;
        this.weight=1;
    }

    /**
     * Constructor for node's adjacencies
     */
    public Edge(Node argChild, double argWeight, String relation){
        this.child = argChild; this.weight = argWeight; this.relation=relation;
    }
    
    public Node getChild() {
        return child;
    }

    public String getRelation() {
        return relation;
    }

    public Node getParent() {
        return parent;
    }

    public double getWeight() {
        return weight;
    }
    
    public Node getTheOther(Node n) {
        if (n.equals(parent)) return child;
        else if (n.equals(child)) return parent;
        else return null;
    }
    
    public boolean contains(Node node) {
        if (parent.equals(node) ||
                child.equals(node)) {
            return true;
        }
        return false;
    }
    
}
