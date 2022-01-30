package Pack.Controller;

import Pack.Model.Node;
import Pack.Model.Tree;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class mainPageController {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  ITEMS
    @FXML
    private Pane mainPane;

    @FXML
    private TextField Xcomponent = null;

    @FXML
    private TextField Ycomponent = null;

    @FXML
    private TextField Zcomponent = null;

    @FXML
    private Button insert;

    @FXML
    private Button remove;

    @FXML
    private Button findNear;

    @FXML
    private Canvas canv;

    @FXML
    private Canvas canv2;

    @FXML
    private Label conditionLabel;

    @FXML
    Button show;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //points would save in this file
    static File file = new File("C:\\FilePath\\repairPoints.txt");

    //also points in file would save in this 2d array
    static ArrayList<ArrayList<Double>> points ;

    //this GraphicsContext is for canvas1 for drawing nodes of tree
    GraphicsContext graphicsContext;

    //this GraphicsContext is for canvas2 for drawing lines of tree
    GraphicsContext graphicsContext2;

    //if user click on find nearest this boolean is kinda permission for showing nearest node
    boolean wantNear =false;

//----------------------------------------------------------------------------------------------------------------------

    //this function reads the points in the file
    public void readFromFile() throws IOException {
        //creating a buffer reader
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String str;
        String sum = "";

        //all lines would save as a string in "sum"
        while ((str = bufferedReader.readLine()) != null){ sum += str; }

        points = new ArrayList<ArrayList<Double>>();

        if(!sum.equals("")) {
            //splitting
            String[] pointsSTR = sum.split("//");
            String[][] pointsSTR2 = new String[pointsSTR.length][3];
            Double[][] pointsDBL = new Double[pointsSTR.length][3];

            for (int i = 0; i < pointsSTR.length; i++) {
                pointsSTR2[i] = pointsSTR[i].split(",");
            }

            //converting string array to double array
            for (int i = 0; i < pointsSTR.length; i++) {
                for (int j = 0; j < pointsSTR2[0].length; j++) {
                    pointsDBL[i][j] = Double.valueOf(pointsSTR2[i][j]);
                }
            }

            //converting double array to 2d double arraylist (just for making our work easier)
            for (int i = 0; i < pointsDBL.length; i++) {
                ArrayList<Double> array = new ArrayList<>();
                for (int j = 0; j < pointsSTR2[0].length; j++) {
                    array.add(pointsDBL[i][j]);
                }
                points.add(array);
            }
        }
    }



    //this function add new point to the file
    public void writeInFile() throws IOException {
        //first save all of text in a string
        String str;
        String sum = "";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while ((str = bufferedReader.readLine()) != null){ sum += str; }

        //the format of saving in file is like this: x1,y1,z1//x2,y2,z2
        String point = Xcomponent.getText().trim() + "," + Ycomponent.getText().trim() + "," + Zcomponent.getText().trim();

        String[] arr = sum.split("//");
        String text="";
        for (int i = 0; i <arr.length ; i++) {

            if (arr[i].equals(point))
                text = "Point is already \nexisted!";
        }

        if(!text.equals("")) {

            conditionLabel.setText(text);
        }
        else {
            if (!sum.equals(""))
                sum += "//" + point;
            else
                sum += point;

            //now rewrite all points in file
            FileWriter fWriter = new FileWriter(file);
            fWriter.write(sum);
            fWriter.close();

            conditionLabel.setText("Point Added:\n" + point);

        }
    }





    //this function search in file and find the point and remove it from there
    public void removeFromFile() throws IOException {

        //the format of saving in file is like this: x1,y1,z1//x2,y2,z2
        String point = Xcomponent.getText().trim() + "," + Ycomponent.getText().trim() + "," + Zcomponent.getText().trim();

        //first we need to read file
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String str;
        String sum = "";

        while ((str = bufferedReader.readLine()) != null){ sum += str; }

        //splitting points
        String[] pointsSTR = sum.split("//");
        sum = "";

        //we rewrite file and when reached to the point, we would ignore it
        int pointNotFound = 0;
        for (int i = 0; i <pointsSTR.length ; i++) {

            if(!pointsSTR[i].equals(point)) {
                sum += pointsSTR[i];
                pointNotFound++;

                if(i != pointsSTR.length-1)
                    sum += "//";
            }
        }
        //preventing probable format error in file
        if(sum.endsWith("//"))
            sum = sum.substring(0, sum.length()-2);

        if(pointNotFound == pointsSTR.length)
            conditionLabel.setText("Point Not Found!");
        else
            conditionLabel.setText("Point Removed:\n" + point);

        if (pointsSTR.length == 0)
            conditionLabel.setText("Tree Is Empty!");

        //rewriting
        FileWriter fWriter = new FileWriter(file);
        fWriter.write(sum);
        fWriter.close();
        readFromFile();

        //clearing 2 canvas completely
        graphicsContext = canv.getGraphicsContext2D();
        graphicsContext2 = canv2.getGraphicsContext2D();

        graphicsContext.clearRect(0, 0, canv.getWidth(), canv.getHeight());
        graphicsContext2.clearRect(0,0,canv2.getWidth(),canv2.getHeight());

        //drawing new tree
        Tree t = myTree();
    }


//----------------------------------------------------------------------------------------------------------------

    //this function disable buttons when textFields are empty
    public void checkXYZ(){
        if(Xcomponent.getText().trim().equals("") || Ycomponent.getText().trim().equals("") || Zcomponent.getText().trim().equals("") ){
            insert.setDisable(true);
            remove.setDisable(true);
            findNear.setDisable(true);
        }else{
            insert.setDisable(false);
            remove.setDisable(false);
            findNear.setDisable(false);
        }
    }




    //when user click insert
    public void insertNode() throws IOException {
        readFromFile();
        writeInFile();
        Xcomponent.setText("");
        Ycomponent.setText("");
        Zcomponent.setText("");
        Tree t = myTree();
        drawTree(t.root);
    }




    //when user click remove
    public void removeNode() throws IOException {
        removeFromFile();
        Xcomponent.setText("");
        Ycomponent.setText("");
        Zcomponent.setText("");
    }




    //when user click on find
    public void find() throws IOException {

        wantNear =true;
        readFromFile();
        Tree t = myTree();
        Xcomponent.setText("");
        Ycomponent.setText("");
        Zcomponent.setText("");

        int delay = 500;
        for (int i = 0; i < Tree.path.size() ; i++ )
        {
            if ( Tree.path.get(i) != null )
            {
                setTimer(Tree.path.get(i) , delay , Color.web("#ffcccc") );
                delay +=  1000;

                if ( i > 0 )
                {
                    setTimer(Tree.path.get(i-1) , delay-1000 , Color.web("#9999ff") );
                }
                if ( i == Tree.path.size()-1 )
                {
                    setTimer(Tree.path.get(i) , delay , Color.web("#c171c1"));
                }
            }

        }
    }



//----------------------------------------------------------------------------------------------------------------------

    //this function create a tree with points and draw the nearest point to pahpad in different color(if you want)
    public Tree myTree() {
        Tree tree = null;
        try {
            //collecting points
            readFromFile();

            //creating tree
            tree = new Tree();
            tree.createTree(points);

            double x, y, z;
            x = Double.parseDouble(Xcomponent.getText().trim());
            y = Double.parseDouble(Ycomponent.getText().trim());
            z = Double.parseDouble(Zcomponent.getText().trim());

            ArrayList<Double> xyz = new ArrayList<>();
            xyz.add(x);
            xyz.add(y);
            xyz.add(z);

            Node pahpad = new Node(xyz);

            //nearest repair shop to pahpad
            Node near = tree.search(new Node(xyz));

            conditionLabel.setText("Your Point:\n" + pahpad.coordinate.toString());

            //draw
            drawTree(tree.root);
            Tree.path.add(near);


        } catch (Exception e) {
        }
        return tree;
    }


//----------------------------------------------------------------------------------------------------------------------


    public void setTimer(Node node, int delay, Color color)
    {
        Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        try {
                            drawNode( node.coordinate.toString() , node.x , node.y , color );
                        } catch (Exception e) {}
                    }
                });
            }
        }, delay);
    }


//----------------------------------------------------------------------------------------------

    //button "show" is enable only for first time you click on one of buttons
    boolean firstVisit =false;


    public void showw() {
        wantNear =false;
        firstVisit = true;
        Tree t = myTree();
        drawTree(t.root);

        if(points.size() == 0){
            conditionLabel.setText("Tree Is Empty!");
        }

        if(firstVisit){
            show.setDisable(true);
        }
    }

//---------------------------------------------------------------------

    public void drawNode(String text, double x, double y, Color color){
        graphicsContext = canv.getGraphicsContext2D();
        graphicsContext.drawImage(createNodeImage(text,color), x, y);
    }

//---------------------------------------------------------------------

    public WritableImage createNodeImage(String coordinates, Color color) {

        StackPane sPane = new StackPane();
        sPane.setPrefSize(26, 26);

        Circle c = new Circle(40);
        c.setStroke(Color.web("#D6E5FA"));
        c.setFill(color);
        c.setStrokeWidth(5);
        sPane.getChildren().add(c);
        Text txtNum = new Text(coordinates);
        txtNum.setFill(Color.WHITE);
        sPane.getChildren().add(txtNum);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        return sPane.snapshot(parameters, null);
    }

//--------------------------------------------------------------------------------

    public void drawTree(Node root){
        drawTree(root, 900,10,450);
    }

    public void drawTree(Node node, double x, double y, double temp){

        if (node == null) { return; }


        if(temp<20){
            temp *= 5;
        }

        drawTree(node.leftChild, x-temp,y+100,temp/2);


        node.x = x;
        node.y = y;
        drawNode(node.coordinate.toString(),x ,y,Color.web("#d4779b"));

        if(node.leftChild != null)
            drawLine(node.x+50, node.y+50, x-temp+50,y+150);
        if(node.rightChild != null)
            drawLine(node.x+50, node.y+50, x+temp+50,y+150);


        drawTree(node.rightChild,x+temp, y+100,temp/2);
    }

//--------------------------------------------------------------------------------

    public void drawLine(double x1,double y1,double x2,double y2){
        graphicsContext2 = canv2.getGraphicsContext2D();
        graphicsContext2.setStroke(Color.web("#4c367c"));
        graphicsContext2.strokeLine(x1,y1,x2,y2);
        graphicsContext2.strokeLine(x1,y1+1,x2,y2+1);
        graphicsContext2.strokeLine(x1,y1-1,x2,y2-1);
    }

}
