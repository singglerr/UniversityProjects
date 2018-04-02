package sample.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Interfaces.AbstractFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;


public class Rectangle extends Shape {
    private double width, height;
    private final Point[] vertices = new Point[4];

    public Rectangle(double canvasWidth, double canvasHeight){
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public Rectangle(Point[] points, double canvasWidth, double canvasHeight){
        setRectangle(points[0], points[1]);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public void setRectangle(Point point1, Point point2){
        vertices[0] =  new Point(Math.min(point1.x, point2.x), Math.min(point1.y, point2.y));
        vertices[2] =  new Point(Math.max(point1.x, point2.x), Math.max(point1.y, point2.y));
        vertices[1] =  new Point(vertices[0].x, vertices[2].y);
        vertices[3] =  new Point(vertices[2].x, vertices[0].y);
        width = vertices[0].distanceTo(vertices[3]);
        height = vertices[0].distanceTo(vertices[1]);
        minSize = Math.min(width, height);
        center.x = (vertices[0].x + vertices[3].x) / 2;
        center.y = (vertices[0].y + vertices[1].y) / 2;
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

        double[] xPoints = new double[4];
        double[] yPoints = new double[4];
        int i = 0;
        for (Point c : vertices){
            xPoints[i] = c.x;
            yPoints[i] = c.y;
            i++;
        }
        if (isMarked()) {
            gc.setFill(Color.BLUE);
        } else {
            gc.setFill(getFillColor());
        }
        gc.fillPolygon(xPoints, yPoints, 4);
        gc.setStroke(Color.BLACK);
        gc.strokePolygon(xPoints, yPoints, 4);
    }

    public void rotate(double dangle) {
        angle = (angle + dangle) % (Math.PI * 2);
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

    public void resize(double ds) {
        for (Point p : vertices){
            p.changeDistanceTo(center, ds);
        }
        width = vertices[0].distanceTo(vertices[3]);
        height = vertices[0].distanceTo(vertices[1]);
        minSize = Math.min(width, height);
    }


    public void moveToward(Point point, double distance) {
        Point dp = center.clone();
        dp.changeDistanceTo(point, -distance);
        move(dp.x - center.x, dp.y - center.y);
    }

    public boolean isIntersectingBounds() {
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
        boolean b3 = sign(point, vertices[2], vertices[3]) >= 0;
        boolean b4 = sign(point, vertices[3], vertices[0]) >= 0;
        return ((b1 == b2) && (b2 == b3) & (b3 == b4));
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
        String obj = "Rectangle, " + "x = " + center.x + ", y = " + center.y + ", width = " + width + ", height = "
                + height + ", angle = " + angle + ", Color = " + getFillColor().toString() +  ", isMarked = "
                + isMarked() + "\n";
        try {
            writer.write(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(AbstractFactory factory, String info, BufferedReader reader) {
        String[] sections = info.split(", ");
        center.x = Double.parseDouble(sections[1].split(" ")[2]);
        center.y = Double.parseDouble(sections[2].split(" ")[2]);
        width = Double.parseDouble(sections[3].split(" ")[2]);
        height = Double.parseDouble(sections[4].split(" ")[2]);
        double dangle = Double.parseDouble(sections[5].split(" ")[2]);
        setFillColor(Color.valueOf(sections[6].split(" ")[2]));
        setMarked(Boolean.parseBoolean(sections[7].split(" ")[2]));
        vertices[0] = new Point(-width/2, -height/2);
        vertices[1] = new Point(-width/2, height/2);
        vertices[2] = new Point(width/2, height/2);
        vertices[3] = new Point(width/2, -height/2);
        for (Point p : vertices){
            p.x += center.x;
            p.y += center.y;
        }
        minSize = Math.min(width, height);
        rotate(dangle);
    }

    @Override
    public String toString() {
        return "Rectangle";
    }
}
