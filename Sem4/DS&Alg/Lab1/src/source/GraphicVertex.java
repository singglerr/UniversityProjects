package source;

import javafx.geometry.Point2D;
import source.graph.Vertex;

public class GraphicVertex extends Vertex{
    private double x, y;

    public GraphicVertex(double x, double y, String name){
        super(name);
        this.x = x;
        this.y = y;
    }

    public Point2D getPoint(){
        return new Point2D(x, y);
    }

    public void moveTo(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
