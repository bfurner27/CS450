package classifier;


import java.util.LinkedList;
import java.util.List;
import weka.core.Attribute;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Benjamin
 * @param <T>
 */
public class Node<T> {
    private double data;
    private Attribute attributeIndx;
    private int checkIndx;
    private double checkIndxDouble;
    private List<Node> children;

    public Attribute getAttributeIndx() {
        return attributeIndx;
    }

    public void setAttributeIndx(Attribute attributeIndx) {
        this.attributeIndx = attributeIndx;
    }

    public int getCheckIndx() {
        return checkIndx;
    }

    public void setCheckIndx(int check) {
        this.checkIndx = check;
    }
    
    public Node() {
        children = new LinkedList<>();
        data = -1.0;
        attributeIndx = null;
        checkIndx = -1;
        checkIndxDouble = -1.0;

    }
    
    public Node(double data) {
        children = new LinkedList<>();
        this.data = data;        
        attributeIndx = null;
        checkIndx = -1;
        checkIndxDouble = -1.0;
    }
    
    public Node(int numChildren) {
        children = new LinkedList<>();
        data = -1.0;
        for (int i = 0; i < numChildren; i++) {
            children.add(new Node(numChildren));
        }
        attributeIndx = null;
        checkIndx = -1;
        checkIndxDouble = -1.0;
    }
    
    public List<Node> getChildren() {
        return children;
    }
    
    public double getData() {
        return data;
    }
    
    public void setData(double data) {
        this.data = data;
    }
    
    public void setChildren(List<Node> children) {
        this.children = children;
    }
    
    public void addChild(Node node) {
        children.add(node);
    }
    
    public double getCheckIndxDouble() {
        return checkIndxDouble;
    }

    public void setCheckIndxDouble(double checkIndxDouble) {
        this.checkIndxDouble = checkIndxDouble;
    }
    
    public void displayTree(String format) {
        for (Node child : children) {
            if (child != null)
            {
                child.displayTree(format += " ");
                if (child.getData() >= 0) {
                    System.out.println(format + "Leaf Node: " + child.getData());
                } else if (child.getCheckIndx() == -1 || child.getCheckIndxDouble() == -1.0) {
                    System.out.println(format + "Parent Node: " + child.getAttributeIndx().name());
                } else {
                    System.out.println(format + "tree child: " +  child.getAttributeIndx().name());
                }
            }
        }
    }
}
