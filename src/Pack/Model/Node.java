package Pack.Model;

import java.util.ArrayList;

//this class is a single node of our tree

public class Node {
    //attributes
    public Node leftChild = null;
    public Node rightChild = null;
    public final Spot coordinate;
    public double x;
    public double y;

    //constructor
    public Node(ArrayList<Double> properties){
        this.coordinate = new Spot(properties);
    }

    //since we are using recursive function to add a node to our tree,
    //we use another method with same name and different inputs for first data

    public void insert(Node node){  this.insert(node, 0);  }

    public void insert(Node node, int coordinateCounter){
        // comparing property of root with new node
        if (node.coordinate.getDepth(coordinateCounter) < coordinate.getDepth(coordinateCounter) ){
            if(leftChild == null)
                leftChild = node;
            else
                leftChild.insert(node, coordinateCounter + 1);
        }
        else{
            if(rightChild == null)
                rightChild = node;
            else
                rightChild.insert(node, coordinateCounter + 1);
        }
    }
}