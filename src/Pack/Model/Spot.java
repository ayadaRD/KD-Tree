package Pack.Model;
import java.util.ArrayList;

public class Spot {

    final ArrayList<Double> xyz;

    public Spot(ArrayList<Double> xyz){ this.xyz = xyz; }

    public Double getDepth(int depth){
        return xyz.get(depth % 3);
    }

    public String toString(){
        if(xyz != null)
            return "x : " + xyz.get(0) + "\ny : " + xyz.get(1) + "\nz : " + xyz.get(2);

        return "null";
    }
}