package source.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Данил on 24.03.2018.
 */
public class Vertex {
    private String name;
//    private List<Vertex> connected = new ArrayList<>();
//    private List<Double> weightsToConnected = new ArrayList<>();

    public Vertex(String name){
        this.name = name;
    }

//    public void connect(Vertex vertex, Double weight){
//        connected.add(vertex);
//        weightsToConnected.add(weight);
//    }
//
//    public Double disconnect(Vertex vertex){
//        int index = connected.indexOf(vertex);
//        Double res = null;
//        if (index != -1) {
//            connected.remove(index);
//            res = weightsToConnected.get(index);
//            weightsToConnected.remove(index);
//        }
//        return res;
//    }
//
//    public Double getWeightTo(Vertex vertex){
//        int index = connected.indexOf(vertex);
//        if (index == -1)
//            return -1d;
//        return weightsToConnected.get(index);
//    }
//
//    public List<Vertex> getConnected(){
//        return connected;
//    }

    public String getName(){
        return name;
    }
}
