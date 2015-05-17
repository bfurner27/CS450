package classifier;


import java.util.LinkedList;
import java.util.List;

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
    private T data;
    private int attributeIndx;
    private double check;
    private List<Node> children;

    public int getAttributeIndx() {
        return attributeIndx;
    }

    public void setAttributeIndx(int attributeIndx) {
        this.attributeIndx = attributeIndx;
    }

    public double getCheck() {
        return check;
    }

    public void setCheck(double check) {
        this.check = check;
    }
    
    public Node() {
        children = new LinkedList<>();
        data = null;
        attributeIndx = 0;
        check = -1.0;

    }
    
    public Node(T data) {
        children = new LinkedList<>();
        this.data = data;        
        attributeIndx = 0;
        check = -1.0;
    }
    
    public Node(int numChildren) {
        children = new LinkedList<>();
        data = null;
        for (int i = 0; i < numChildren; i++) {
            children.add(new Node(numChildren));
        }
        attributeIndx = 0;
        check = -1.0;
    }
    
    public List<Node> getChildren() {
        return children;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public void setChildren(List<Node> children) {
        this.children = children;
    }
    
    public void addChild (T data) {
        children.add(new Node(data));
    }
}
