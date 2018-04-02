package source;

import source.graph.Edge;

public class GraphicEdge extends Edge{
    private GraphicVertex left;
    private GraphicVertex right;

    public GraphicEdge(double weight, GraphicVertex left, GraphicVertex right, boolean isDirected){
        super(weight, left, right, isDirected);
        this.left = left;
        this.right = right;
    }

    public GraphicVertex getLeftGraphicVertex() {
        return left;
    }

    public GraphicVertex getRightGraphicVertex() {
        return right;
    }
}
