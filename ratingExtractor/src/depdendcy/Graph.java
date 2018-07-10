/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package depdendcy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This method creates a structure that connects all the words in a sentence by 
 * creating a node for each of the words, and edges to connect the words together
 * in a parent child kind of relationship.
 * @author cslin
 */
public class Graph implements Serializable {

    //private ArrayList<Node> listSourceNode = new ArrayList<Node>();
    //private ArrayList<Node> listDestNode = new ArrayList<Node>();
    private ArrayList<Node> listNode = new ArrayList<Node>();
    private ArrayList<Edge> listEdge = new ArrayList<Edge>();
    private int iNode = 0, iEdge = 0;
    private ArrayList<Edge> outEdge = null;
    //private ArrayList<Edge> inEdge = new ArrayList<Edge>();
    private ArrayList<Edge> inEdge = null;
    private final static long serialVersionUID = -3059622786385294161L; //7370388788340660600L;//4176635012854306031L;//7370388788340660600L;//7370388788340660600L;//

    /**
     * This method checks to see if the edge already exists in the graph. If it does
     * not, it adds it to listEdge. Otherwise, it does not. It also checks to see 
     * if the nodes are in listNode yet. If they are not, it adds them to list node
     * @param sNode - The first node of the edge.
     * @param dNode - The second node of the edge.
     * @param relation - The relationship between the two nodes. 
     */
    public void checkNewEdge(Node sNode, Node dNode, String relation) {
        boolean bFst = false, bSnd = false;
        Node node1 = null, node2 = null;

        //check if the nodes exist
        for (int i = 0; i < listNode.size(); i++) {
            Node node = listNode.get(i);

            if (sNode.getId() == node.getId()) {
                bFst = true;
                node1 = node;
            }

            if (dNode.getId() == node.getId()) {
                bSnd = true;
                node2 = node;
            }
        }
        if (!bFst) {
            listNode.add(iNode, sNode);
            iNode++;
            node1 = sNode;
        }
        if (!bSnd) {
            listNode.add(iNode, dNode);
            iNode++;
            node2 = dNode;
        }

        //Update the parent node
        Edge edge = new Edge(node2, 1, relation);
        node1.adjacencies.add(edge);
        for (int i = 0; i < listNode.size(); i++) {
            Node node = listNode.get(i);
            if (node.getId() == node1.getId()) {
                listNode.set(i, node1);
            }
        }

        // add the new edge to Graph
        Edge newedge = new Edge(node1, node2, relation);
        listEdge.add(iEdge, newedge);
        iEdge++;
    }

    /**
     * This method gets the list of nodes this graph has
     * @return - listNode
     */
    public ArrayList<Node> getNodes() {
        return listNode;
    }

    /**
     * This method gets a node based on the id, or place in the sentence of the 
     * node. The root word is 0. 
     * @param id - place in sentence word was located.
     * @return - node at the certain id.
     */
    public Node getNode(int id) {

        for (int i = 0; i < listNode.size(); i++) {
            if ((listNode.get(i)).getId() == id) {
                return listNode.get(i);
            }
        }
        return null;
    }
    //added by TL

    /**
     * This method gets the node based on the name of the word being searched for.
     * @param name - word being searched for.
     * @return - the node attached to the word.
     */
    public Node getNode(String name) {

        for (int i = 0; i < listNode.size(); i++) {
            if ((listNode.get(i)).equal(name)) {
                return listNode.get(i);
            }
        }
        return null;
    }

    public Node getStemmedNode(String stem) {

        for (int i = 0; i < listNode.size(); i++) {
            if ((listNode.get(i)).getLemmaWord().equalsIgnoreCase(stem)) {
                return listNode.get(i);
            }
        }
        return null;
    }//added by Zumrut 10/28/2013

    /**
     * This method gets the list of edges this graph has
     * @return listEdge
     */
    public ArrayList<Edge> getEdges() {
        return listEdge;
    }

    /**
     * This method gets the children of a node and stores them in an array list.
     * Is empty if the node is null or contains no children.
     * @param node - the node whose children are being searched for
     * @return - an array list of all the children of the input node
     */
    public ArrayList<Node> getChildren(Node node) {
        ArrayList<Node> children = new ArrayList<Node>();
        if (node == null) {
            return children;
        }

        synchronized (this) {
            int count = 0;

            for (int i = 0; i < listEdge.size(); i++) {
                Edge e = listEdge.get(i);
                Node nS = e.getParent();
                Node nD = e.getChild();

                if (nS.getId() == node.getId()) {
                    children.add(count, nD);
                    count++;
                }
            }
            return children;
        }

    }

    /**
     * This method takes a child node, and returns the parent of the node.
     * Is null if the node is the root or the node was null.
     * @param node - the node whose parent is being searched for.
     * @return - the parent of the inout node
     */
    public Node getParent(Node node) {
        Node par = null;
        if (node == null) {
            return par;
        }

        synchronized (this) {
            int count = 0;

            for (int i = 0; i < listEdge.size(); i++) {
                Edge e = listEdge.get(i);
                Node nS = e.getParent();
                Node nD = e.getChild();

                if (nD.getId() == node.getId()) {
                    return nS;
                }
            }
            return par;
        }
    }

    public ArrayList getParents(Node node) {
        ArrayList pars = new ArrayList();
        Node par = null;
        if (node == null) {
            return pars;
        }

        synchronized (this) {
            int count = 0;

            for (int i = 0; i < listEdge.size(); i++) {
                Edge e = listEdge.get(i);
                Node nS = e.getParent();
                Node nD = e.getChild();

                if (nD.getId() == node.getId()) {
                    if (!pars.contains(nS)) {
                        pars.add(nS);
                    }
                }
            }
            return pars;
        }
    }

    /**
     * Extract all edges starting from node
     * @param node - The node we are starting from.
     * @return - An array list of edges that go from that node down the tree.
     */
    public ArrayList<Edge> outFlow(Node node) {
        ArrayList<Edge> outEdge = new ArrayList<Edge>();
        if (node == null) {
            return outEdge;
        }

        synchronized (this) {
            int count = 0;

            for (int i = 0; i < listEdge.size(); i++) {
                Edge e = listEdge.get(i);
                Node nS = e.getParent();
                Node nD = e.getChild();

                if (nS.getId() == node.getId()) {
                    outEdge.add(count, e);
                    count++;
                }
            }
            return outEdge;
        }

    }

    /**
     * Extract all edges ending in node
     * @param node - The node we are starting from.
     * @return - An array list of edges that go from that node down the tree.
     */
    public ArrayList<Edge> inFlow(Node node) {
        ArrayList<Edge> inEdge = new ArrayList<Edge>();
        if (node == null) {
            return inEdge;
        }

        synchronized (this) {
            int count = 0;
            for (int i = 0; i < listEdge.size(); i++) {
                Edge e = listEdge.get(i);
                Node nS = e.getParent();
                Node nD = e.getChild();

                if (nD.getId() == node.getId()) {
                    inEdge.add(count, e);
                    count++;
                }
            }
            return inEdge;
        }
    }

    public void resetNodeInit() {
        for (int i = 0; i < listNode.size(); i++) {
            //listNode.get(i).minDist= Double.POSITIVE_INFINITY;
            listNode.get(i).minDist = Double.POSITIVE_INFINITY;
            listNode.get(i).previous = null;
        }
    }

    public ArrayList<Node> getSortedNodes() {
        ArrayList<Node> nodeInOrder = new ArrayList<Node>();

        resetNodeInit();

        for (int i = 0; i < listNode.size(); i++) {
            nodeInOrder.add(i, listNode.get(i));
        }

        for (int top = nodeInOrder.size() - 1; top > 0; top--) {
            for (int i = 0; i < top; i++) {
                if (nodeInOrder.get(i).getId() > nodeInOrder.get(i + 1).getId()) {
                    Node temp = nodeInOrder.get(i);
                    nodeInOrder.set(i, nodeInOrder.get(i + 1));
                    nodeInOrder.set(i + 1, temp);
                }
            }
        }
        return nodeInOrder;
    }

    public List<Node> subShortestPath(Node startNode, Node endNode) {

        resetNodeInit();
        computeAllPaths(startNode);

        List<Node> path = getPathToTarget(endNode);
        return path;
    }

    /**
     * Generate the shortest path between startNode and endNode Return the
     * visiting nodes
     *
     * @param startNode
     * @param endNode
     * @return
     */
    public List<Node> shortestPath(Node startNode, Node endNode) {

        resetNodeInit();
        computeAllPaths(startNode);

        List<Node> path = getPathToTarget(endNode);

        if (path.size() == 1) {
            List<Node> biPath = subShortestPath(endNode, startNode);
            System.out.println("--------- Generate Shortest Path ---------");
            System.out.println("From (" + endNode.getName() + "-" + endNode.getId()
                    + ")-->(" + startNode.getName() + "-" + startNode.getId() + ")");
            System.out.print("Path: ");
            for (int i = 0; i < biPath.size(); i++) {
                System.out.print(biPath.get(i).getName() + "-" + biPath.get(i).getId() + "; ");
            }
            System.out.println();

            return biPath;
        } else {
            System.out.println("--------- Generate Shortest Path ---------");
            System.out.println("From (" + startNode.getName() + "-" + startNode.getId()
                    + ")-->(" + endNode.getName() + "-" + endNode.getId() + ")");
            System.out.print("Path: ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i).getName() + "-" + path.get(i).getId() + "; ");
            }
            System.out.println();

            return path;
        }
    }

    public static List<Node> getPathToTarget(Node target) {
        List<Node> path = new ArrayList<Node>();
        for (Node vertex = target; vertex != null; vertex = vertex.previous) {
            path.add(vertex);
        }

        Collections.reverse(path);
        return path;
    }

    public void computeAllPaths(Node source) {
        source.minDist = 0.0;
        PriorityQueue<Node> nodeQueue = new PriorityQueue<Node>();
        nodeQueue.add(source);

        while (!nodeQueue.isEmpty()) {
            Node s = nodeQueue.poll();

            // Visit each edge starting from s
            for (Edge e : s.adjacencies) {
                Node d = e.getChild();
                double weight = e.getWeight();
                double distFromU = s.minDist + weight;

                if (distFromU < d.minDist) {
                    nodeQueue.remove(d);

                    d.minDist = distFromU;
                    d.previous = s;
                    nodeQueue.add(d);

                }

            }
        }
        System.out.println();
    }

    /**
     * Input a phrase(?), return the head Node.
     *
     * @param phrase
     * @return
     */
    //public Node getHead(String phrase){
    public Node getHead(ArrayList<Node> nodeInOrder, String phrase) {
        //ArrayList<Node> nodeInOrder = getSortedNodes();
        ArrayList<Node> nodePhrase = new ArrayList<Node>();

        String[] token = phrase.trim().split(" ");
        /*int iLen=0;
         for (int j=0; j<token.length; j++){
         for (int i=0; i<nodeInOrder.size(); i++){
         //use stemmed word to compare
         String stemToken=Util.stem(token[j]);
         String stemNodeInOrder=Util.stem(nodeInOrder.get(i).getName());
         if (stemToken.equalsIgnoreCase(stemNodeInOrder)){
         nodePhrase.add(iLen, nodeInOrder.get(i));
         iLen++;
         }
         }
         }*/
        boolean found = true;

        //find each word's node in the phrase
            for (int i = 0; i < nodeInOrder.size(); i++) {
                found = true;

                if (i + token.length > nodeInOrder.size()) {
                    return null;
                }


                if (found) {
                    for (int k = i; k < i + token.length; k++) {
                        nodePhrase.add(k - i, nodeInOrder.get(k));
                    }
                    break;
                }
            }

//     System.out.println("nodePhrase size: " + nodePhrase.size());
        ArrayList<Integer> iHeads = new ArrayList<Integer>();
        for (int i = 0; i < nodePhrase.size(); i++) {
            iHeads.add(i, 1);
        }

        for (int i = 0; i < nodePhrase.size(); i++) {
            for (int j = 0; j < nodePhrase.size(); j++) {
                if (i != j) {
                    for (int k = 0; k < listEdge.size(); k++) {
                        if (listEdge.get(k).getParent().getId() == nodePhrase.get(i).getId()
                                && listEdge.get(k).getChild().getId() == nodePhrase.get(j).getId()) {
                            iHeads.set(j, 0);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < nodePhrase.size(); i++) {
            if (iHeads.get(i) == (Integer) 1) {
                return nodePhrase.get(i);
            }
        }

        return null;
    }

    /**
     * Input a phrase(?), return the head Node.
     *
     * @param phrase
     * @return
     */
    public Node getHead(String phrase) {
//    public Node getHead(ArrayList<Node> nodeInOrder, String phrase) {
        ArrayList<Node> nodeInOrder = getSortedNodes();
        ArrayList<Node> nodePhrase = new ArrayList<Node>();

        String[] token = phrase.trim().split(" ");
        /*int iLen=0;
         for (int j=0; j<token.length; j++){
         for (int i=0; i<nodeInOrder.size(); i++){
         //use stemmed word to compare
         String stemToken=Util.stem(token[j]);
         String stemNodeInOrder=Util.stem(nodeInOrder.get(i).getName());
         if (stemToken.equalsIgnoreCase(stemNodeInOrder)){
         nodePhrase.add(iLen, nodeInOrder.get(i));
         iLen++;
         }
         }
         }*/
        boolean found = true;

        //find each word's node in the phrase
            for (int i = 0; i < nodeInOrder.size(); i++) {
                found = true;

                if (i + token.length > nodeInOrder.size()) {
                    return null;
                }


                if (found) {
                    for (int k = i; k < i + token.length; k++) {
                        nodePhrase.add(k - i, nodeInOrder.get(k));
                    }
                    break;
                }
            }

//     System.out.println("nodePhrase size: " + nodePhrase.size());
        ArrayList<Integer> iHeads = new ArrayList<Integer>();
        for (int i = 0; i < nodePhrase.size(); i++) {
            iHeads.add(i, 1);
        }

        for (int i = 0; i < nodePhrase.size(); i++) {
            for (int j = 0; j < nodePhrase.size(); j++) {
                if (i != j) {
                    for (int k = 0; k < listEdge.size(); k++) {
                        if (listEdge.get(k).getParent().getId() == nodePhrase.get(i).getId()
                                && listEdge.get(k).getChild().getId() == nodePhrase.get(j).getId()) {
                            iHeads.set(j, 0);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < nodePhrase.size(); i++) {
            if (iHeads.get(i) == (Integer) 1) {
                return nodePhrase.get(i);
            }
        }

        return null;
    }

    /**
     * get relation between two nodes if exists by TL
     */
    public String getRelation(Node n1, Node n2) {
        for (Edge e : listEdge) {
            if (e.contains(n1)
                    && e.contains(n2)) {
                return e.getRelation();
            }
        }
        return null;
    }
    
    public String getSubject(String phrase) {
        Node head = this.getHead(phrase);
        ArrayList<Node> children = this.getChildren(head);
        for (Node child: children) {
            if (this.getRelation(head, child).indexOf("subject") != -1) {
                return child.getName();
            }
        }
        return null;
    }

    public String getObject(String phrase) {
        Node head = this.getHead(phrase);
        ArrayList<Node> children = this.getChildren(head);
        for (Node child: children) {
            if (this.getRelation(head, child).indexOf("object") != -1) {
                return child.getName();
            }
        }
        return null;
    }

    public Path getNodes(Node ori_node_, Node con_head_) {
        Path path = new Path();
        boolean start = false;
        for (Node node : listNode) {
            if (node.equals(ori_node_)
                    || node.equals(con_head_)) {
                if (!start) {
                    start = true;
                    path.add(node);
                    continue;
                } else {
                    path.add(node);
                    break;
                }
            }
            if (start) {
                path.add(node);
            }
        }
        return path;
    }
}
