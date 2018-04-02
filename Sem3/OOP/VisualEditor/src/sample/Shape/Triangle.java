package sample.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Interfaces.AbstractFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Triangle extends Shape {
    private Point[] vertices = new Point[3];

    public Triangle(double canvasWidth, double canvasHeight){
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public Triangle(Point[] points, double canvasWidth, double canvasHeight){
        setTriangle(points[0], points[1], points[2]);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public void setTriangle(Point p1, Point p2, Point p3){
        vertices[0] = p1;
        vertices[1] = p2;
        vertices[2] = p3;
        center.x = (vertices[0].x + vertices[1].x + vertices[2].x) / 3;
        center.y = (vertices[0].y + vertices[1].y + vertices[2].y) / 3;
        double min = Math.min(vertices[0].distanceTo(center), vertices[1].distanceTo(center));
        minSize = Math.min(min, vertices[2].distanceTo(center));
    }

    public void move(double dx, double dy) {
        center.x += dx;
        center.y += dy;
        for (Point c : vertices){
            c.x += dx;
            c.y += dy;
        }
    }

    public void draw(GraphicsContext gc) {

        double[] xPoint = new double[3];
        double[] yPoint = new double[3];
        for (int i = 0; i < 3; i++){
            xPoint[i] = vertices[i].x;
            yPoint[i] = vertices[i].y;
        }
        if (isMarked())
            gc.setFill(Color.BLUE);
        else
            gc.setFill(getFillColor());
        gc.fillPolygon(xPoint, yPoint, 3);
        gc.setStroke(Color.BLACK);
        gc.strokePolygon(xPoint, yPoint, 3);
    }

    public void resize(double dr) {
        for (Point c : vertices){
            c.changeDistanceTo(center, dr);
        }
        double min = Math.min(vertices[0].distanceTo(center), vertices[1].distanceTo(center));
        minSize = Math.min(min, vertices[2].distanceTo(center));
    }

    public void rotate(double dangle) {
        angle = (angle + dangle) % (Math.PI * 2);
        double[] oldX = new double[3];
        double[] oldY = new double[3];
        for (int i = 0; i < 3; i++) {
            oldX[i] = vertices[i].x;
            oldY[i] = vertices[i].y;
        }
        double sina = Math.sin(dangle),
                cosa = Math.cos(dangle);
        for (Point p : vertices) {
            double x = p.x - center.x, y = p.y - center.y;
            p.x = x * cosa + y * sina + center.x;
            p.y = x * (-sina) + y * cosa + center.y;
        }
    }

    public void rotateFrom(Point point, double dangle) {
        double sina = Math.sin(dangle),
                cosa = Math.cos(dangle);
        double x = center.x - point.x, y = center.y - point.y;
        center.x = x * cosa + y * sina + point.x;
        center.y = x * (-sina) + y * cosa + point.y;
        for (Point p : vertices) {
            x = p.x - point.x;
            y = p.y - point.y;
            p.x = x * cosa + y * sina + point.x;
            p.y = x * (-sina) + y * cosa + point.y;
        }
    }

    public void moveToward(Point point, double distance) {
        Point dp = center.clone();
        dp.changeDistanceTo(point, -distance);
        move(dp.x - center.x, dp.y - center.y);
    }

    public boolean isIntersectingBounds(){
        for (Point c : vertices)
            if (c.x < 0 || c.x > canvasWidth || c.y < 0 || c.y > canvasHeight)
                return true;
        return false;
    }

    private double sign(Point p1, Point p2, Point p3) {
        return (p1.x - p2.x) * (p2.y - p3.y) - (p3.x - p2.x) * (p2.y - p1.y);
    }

    public boolean contains(Point point) {
        boolean b1 = sign(point, vertices[0], vertices[1]) >= 0;
        boolean b2 = sign(point, vertices[1], vertices[2]) >= 0;
        boolean b3 = sign(point, vertices[2], vertices[0]) >= 0;
        return (b1 == b2) & (b2 == b3);
    }

    @Override
    public boolean isIntersectingWith(Shape shape) {
        if (shape instanceof Circle){
            Point point = new Point(shape.getCenterX(), shape.getCenterY());
            point.changeDistanceTo(center, -((Circle) shape).getRadius());
            return contains(point);
        }
        return false;
    }

    @Override
    public void save(BufferedWriter writer) {
        String obj = "Triangle" + ", vertex1(" + vertices[0].x + "," + vertices[0].y + ")"
                + ", vertex2(" + vertices[1].x + "," + vertices[1].y + ")"
                + ", vertex3(" + vertices[2].x + "," + vertices[2].y + ")"
                + ", angle = " + angle + ", Color = " + getFillColor().toString()
                +  ", isMarked = " + isMarked() + "\n";
        try {
            writer.write(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(AbstractFactory factory, String info, BufferedReader reader) {
        String[] arr = info.split(", ");
        for (int i = 0; i < 3; i++)
            vertices[i] = new Point(0,0);
        String vert1 = arr[1].split("\\(")[1];
        vertices[0].x = Double.parseDouble(vert1.split(",")[0]);
        vertices[0].y = Double.parseDouble(vert1.split(",")[1].replaceAll("\\)", ""));
        String vert2 = arr[2].split("\\(")[1];
        vertices[1].x = Double.parseDouble(vert2.split(",")[0]);
        vertices[1].y = Double.parseDouble(vert2.split(",")[1].replaceAll("\\)", ""));
        String vert3 = arr[3].split("\\(")[1];
        vertices[2].x = Double.parseDouble(vert3.split(",")[0]);
        vertices[2].y = Double.parseDouble(vert3.split(",")[1].replaceAll("\\)", ""));
        double dangle = Double.parseDouble(arr[4].split(" ")[2]);
        setFillColor(Color.valueOf(arr[5].split(" ")[2]));
        setMarked(Boolean.parseBoolean(arr[6].split(" ")[2]));
        center.x = (vertices[0].x + vertices[1].x + vertices[2].x) / 3;
        center.y = (vertices[0].y + vertices[1].y + vertices[2].y) / 3;
        double min = Math.min(vertices[0].distanceTo(center), vertices[1].distanceTo(center));
        minSize = Math.min(min, vertices[2].distanceTo(center));
        rotate(dangle);
    }

    @Override
    public String toString() {
        return "Triangle";
    }
}