package Pack.Model;

import java.util.ArrayList;
import static java.lang.Math.*;

public class Tree {
    public Node root = null;

    public Tree(){}

    //this part is for when we read spots of repair from file and want to add to tree
    //input is a 2d array witch contains all points

    public void createTree (ArrayList<ArrayList<Double>> points){
        //first point is root of tree
        root = new Node(points.get(0));

        for(int i=1 ; i<points.size() ; i++){
            ArrayList<Double> point = points.get(i);
            Node node = new Node(point);
            root.insert(node);
        }
    }


    //since we are using recursive function to search in our tree,
    //we use another method with same name and different inputs for first data
    //also this function returns the nearest repair spot to pahpad
    public Node search (Node pahpad){
        path = new ArrayList<>();
        return search(this.root,pahpad,0); }

    //we need to remember the path
    public static ArrayList<Node> path ;

    public Node search (Node root, Node pahpad, int depth){

        //is tree empty?
        //or maybe we reached to the end of a branch
        if(root == null)
            return null;

        //there could be a nearer spot in other branch
        //so we should check others too
        Node nextBranch;
        Node otherBranch;
        //boolean accc = false;

        //comparing pahpad coordinate with parent node
        //notice that our function is recursive and root changes
        if(pahpad.coordinate.getDepth(depth) < root.coordinate.getDepth(depth)){
            nextBranch = root.leftChild;
            otherBranch = root.rightChild;
        }
        else{
            nextBranch = root.rightChild;
            otherBranch = root.leftChild;
        }

        path.add(root);
        //tree would be searched and based on logic we designed
        //path.add(search(nextBranch, pahpad, depth + 1));
        Node temp = search(nextBranch, pahpad, depth + 1);
        //path.add(temp);

        //the best case of current branch, based on depth, saves here
        Node theBestOne = findClosest(temp, root, pahpad);

        //and this is distance between pahpad and found node
        double d = distance(pahpad.coordinate, theBestOne.coordinate);
        //double d = abs(pahpad.coordinate.getDepth(depth) - theBestOne.coordinate.getDepth(depth));

        //there might be a closer point in other branch
        double dd = abs(pahpad.coordinate.getDepth(depth) - root.coordinate.getDepth(depth));

        //if dd was closer, plan changes
        if(dd <= d){
            path.add(root);
            temp = search(otherBranch, pahpad, depth+1);

            theBestOne = findClosest(temp,theBestOne,pahpad);
        }

        //return the nearest one
        return theBestOne;
    }


    //we know that in a binary tree, each parent has 2 child
    //this function check witch child is closer to pahpad
    public Node findClosest(Node node1, Node node2, Node pahpad){
        if(node1 == null)
            return node2;
        if(node2 == null)
            return node1;

        //node1---------pahpad
        double distance1 = distance(node1.coordinate, pahpad.coordinate);

        //node2---------pahpad
        double distance2 = distance(node2.coordinate, pahpad.coordinate);

        if(distance1 < distance2)
            return node1;
        else
            return node2;
    }

//this function calculate the distance between two given point
    public double distance(Spot point1, Spot point2){
        double sum =0;

        for(int i=0 ; i<3 ; i++){
            double d = abs(point1.getDepth(i) - point2.getDepth(i));
            sum += pow(d,2);
        }
        return sqrt(sum);
    }
}