package source.graph;

import javax.swing.text.html.ListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Данил on 25.03.2018.
 */
public class Edge {
    private double weight;
    private Vertex start, end;
    private boolean isDirected;

    public Edge(double weight, Vertex start, Vertex end, boolean isDirected){
        this.weight = weight;
        this.start = start;
        this.end = end;
        this.isDirected = isDirected;
    }

    public double getWeight(){
        return weight;
    }

    public Vertex getStartVertex() {
        return start;
    }

    public Vertex getEndVertex() {
        return end;
    }

    public List<Vertex> getVertices() {
        List<Vertex> list = new ArrayList<>();
        list.add(start);
        list.add(end);
        return list;
    }

    public boolean isDirected() {
        return isDirected;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge){
            Edge edge = (Edge) obj;
            if (edge.isDirected && isDirected){
                return (edge.start == start && edge.end == end);
            }
            else {
                List<Vertex> list = edge.getVertices();
                return (list.contains(start) && list.contains(end));
            }
        }
        else
            return super.equals(obj);
    }
}
