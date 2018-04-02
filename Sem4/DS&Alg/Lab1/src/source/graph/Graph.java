package source.graph;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Данил on 24.03.2018.
 */

public class Graph {
    private List<Edge> edges = new ArrayList<>();
    private List<Vertex> vertices = new ArrayList<>();
    private final boolean isDirected;
    private boolean isChanged = false;
    private Double[][] adjacencyMatrix;

    public Graph(boolean isDirected){
        this.isDirected = isDirected;
    }

    public void addVertex(Vertex vertex){
        if (vertices.contains(vertex))
            return;
        isChanged = true;
        vertices.add(vertex);
    }

    public void addEdge(Edge edge){
        if (edges.contains(edge))
            return;
        isChanged = true;
        edges.add(edge);
    }

    public void removeEdge(Edge edge){
        boolean removed = edges.remove(edge);
        if (removed){
            isChanged = true;
        }
    }

    public void removeVertex(Vertex vertex){
        boolean removed = vertices.remove(vertex);
        if (removed) {
            isChanged = true;
            List<Edge> edgesToRemove = new LinkedList<>();
            for (Edge e : edges){
                if (e.getVertices().contains(vertex))
                    edgesToRemove.add(e);
            }
            edges.removeAll(edgesToRemove);
        }
    }

    public List<Edge> getEdges(){
        return new ArrayList<>(edges);
    }

    public List<Vertex> getVertices(){
        return new ArrayList<>(vertices);
    }

    public Double[][] getAdjacencyMatrix(){
        if (!isChanged)
            return adjacencyMatrix;
        int size = vertices.size();
        Double[][] result = new Double[size][size];
        for (Edge edge : edges){
            int indexLeft = vertices.indexOf(edge.getStartVertex());
            int indexRight = vertices.indexOf(edge.getEndVertex());
            result[indexLeft][indexRight] = edge.getWeight();
            if (!isDirected) {
                result[indexRight][indexLeft] = edge.getWeight();
            }
        }
        for (int i = 0; i < size; i++) {
            result[i][i] = Double.POSITIVE_INFINITY;
            for (int j = 0; j < size; j++) {
                if (result[i][j] == null) {
                    result[i][j] = 0d;
                }
            }
        }
        return result;
    }

    public List<Integer> breadthFirstSearch(Integer vertex) throws IndexOutOfBoundsException{
        Double[][] matrix = getAdjacencyMatrix();
        List<Integer> result = null;
        if (matrix != null){
            result = new ArrayList<>();
            int size = matrix.length;
            if (vertex >= size)
                throw  new IndexOutOfBoundsException(vertex + ", max index = " + size);
            Queue<Integer> queue = new LinkedList<>();
            boolean[] visited = new boolean[size];
            queue.add(vertex);
            while (!queue.isEmpty()){
                vertex = queue.poll();
                if (visited[vertex]) {
                    continue;
                }
                visited[vertex] = true;
                result.add(vertex);
                for (int i = 0; i < size; i++){
                    if (matrix[vertex][i] != 0d & matrix[vertex][i] != Double.POSITIVE_INFINITY){
                        queue.add(i);
                    }
                }
            }
        }
        return result;
    }

//    public Double[][] getAdjacencyMatrix(){
//        Double[][] res = new Double[vertices.size()][vertices.size()];
//        int size = vertices.size();
//        for (int i = 0; i < size; i++){
//            Vertex vert = vertices.get(i);
//            for (Vertex v : vert.getConnected()){
//                int index = vertices.indexOf(v);
//                res[i][index] = vert.getWeightTo(v);
//            }
//        }
//        return res;
//    }
}