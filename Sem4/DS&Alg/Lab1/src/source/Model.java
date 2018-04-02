package source;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import source.graph.Edge;
import source.graph.Graph;
import source.graph.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Model {
    private Graph graph = new Graph(false);
    private Drawer drawer;
    private List<GraphicVertex> vertices = new ArrayList<>();
    private List<GraphicEdge> edges = new ArrayList<>();
    private TextArea textArea;
    private List<GraphicVertex> selectedVertices = new ArrayList<>();
    private boolean creatingEdges = false;
    private boolean wasDragged = false;

    public Model(Canvas canvas, TextArea textArea){
        drawer = new Drawer(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight());
        this.textArea = textArea;
        render();
    }

    //TODO - Логика выделения/создания вершин
    public void mouseLeftClick(Point2D point){
        if (wasDragged) {
            wasDragged = false;
            return;
        }
        if (creatingEdges){
            selectedVertices.clear();
            creatingEdges = false;
        }
        GraphicVertex vertex = isClickedOnVertex(point);
        if (vertex == null){
            vertex = new GraphicVertex(point.getX(), point.getY(), "");
            vertices.add(vertex);
            graph.addVertex(vertex);
        }
        else if (!selectedVertices.contains(vertex)){
            if (selectedVertices.size() == 2){
                selectedVertices.clear();
            }
            selectedVertices.add(vertex);
        }
        else {
            selectedVertices.remove(vertex);
        }
        render();
    }

    //TODO - Перемещение вершины (протестировать)
    public void mouseDragged(Point2D point){
        wasDragged = true;
        GraphicVertex vertex = isClickedOnVertex(point);
        if (vertex != null){
            vertex.moveTo(point.getX(), point.getY());
            render();
        }
    }

    //TODO - Логика создания рёбер
    public void mouseRightClick(Point2D point){
        GraphicVertex vertex = isClickedOnVertex(point);
        if (vertex != null) {
            if (creatingEdges){
                GraphicEdge edge = new GraphicEdge(1, selectedVertices.remove(0), vertex, false);
                if (edges.contains(edge)){
                    creatingEdges = !creatingEdges;
                    selectedVertices.clear();
                    render();
                    return;
                }
                graph.addEdge(edge);
                edges.add(edge);
            }
            else {
                selectedVertices.clear();
                selectedVertices.add(vertex);
            }
            creatingEdges = !creatingEdges;
            render();
        }
    }

    public void delete(){
        if (selectedVertices.size() == 1){
            GraphicVertex vertex = selectedVertices.remove(0);
            graph.removeVertex(vertex);
            vertices.remove(vertex);
            synchronizeEdges();
        }
        else if (selectedVertices.size() == 2){
            GraphicEdge edgeToRemove = null;
            for (GraphicEdge edge : edges){
                List<Vertex> list = edge.getVertices();
                if (list.contains(selectedVertices.get(0)) && list.contains(selectedVertices.get(1))){
                    edgeToRemove = edge;
                    break;
                }
            }
            graph.removeEdge(edgeToRemove);
            edges.remove(edgeToRemove);
            selectedVertices.clear();
        }
        render();
    }

    private void synchronizeEdges(){
        edges.clear();
        for (Edge e : graph.getEdges()){
            edges.add((GraphicEdge) e);
        }
    }

    public void search(Integer vertex){
        List<Integer> list = null;
        try{
            list = graph.breadthFirstSearch(vertex - 1);
        }
        catch (IndexOutOfBoundsException e){
            println("Vertex not found");
        }
        if (list != null){
            int size = list.size();
            for (int i = 0; i < size; i++){
                if (i == size - 1){
                    print("" + (list.get(i) + 1));
                }
                else {
                    print((list.get(i) + 1) + ", ");
                }
            }
            println("");
        }
    }

    private void print(String s){
        StringBuilder builder = new StringBuilder(textArea.getText());
        builder.append(s);
        textArea.setText(builder.toString());
    }

    private void println(String s){
        StringBuilder builder = new StringBuilder(textArea.getText());
        builder.append(s);
        builder.append("\n");
        textArea.setText(builder.toString());
    }

    private GraphicVertex isClickedOnVertex(Point2D point){
        for (GraphicVertex vertex : vertices){
            if (Math.pow(point.getX() - vertex.getX(), 2) + Math.pow(point.getY() - vertex.getY(), 2) < Math.pow(Config.RADIUS, 2))
                return vertex;
        }
        return null;
    }


    //TODO - Доделать отрисовку рёбер
    private void render(){
        drawer.clear();
        int i = 1;
        for (GraphicEdge edge : edges){
            drawer.strokeLine(edge.getLeftGraphicVertex().getPoint(), edge.getRightGraphicVertex().getPoint());
        }
        for (GraphicVertex vertex : vertices){
            drawer.drawPoint(vertex.getPoint(), Config.FILLCOLOR);
            drawer.fillText(i++ + "", vertex.getPoint(), 0,Orientation.CENTER);
        }
        for (GraphicVertex vertex : selectedVertices){
            drawer.strokePoint(vertex.getPoint(), Config.STROKECOLOR);
        }

    }
}
