package sample.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Interfaces.AbstractFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by Данил on 08.11.2017.
 */
public class Circle extends Shape {
    private double radius = 15;

    public Circle(double canvasWidth, double canvasHeight){
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public Circle(Point center, double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        setCircle(center);
    }

    public void setCircle(Point center){
        if (center.x < radius)
            this.center.x = radius;
        else if (center.x > canvasWidth - radius)
            this.center.x = canvasWidth - radius;
        else
            this.center.x = center.x;

        if (center.y < radius)
            this.center.y = radius;
        else if (center.y > canvasHeight - radius)
            this.center.y = canvasHeight - radius;
        else
            this.center.y = center.y;
        minSize = radius;
    }

    public void rotate(double dangle) {
    }

    public void rotateFrom(Point point, double dangle) {
        double sina = Math.sin(dangle),
                cosa = Math.cos(dangle);
        double x = center.x - point.x, y = center.y - point.y;
        center.x = x * cosa + y * sina + point.x;
        center.y = x * (-sina) + y * cosa + point.y;
    }

    public boolean isIntersectingBounds() {
        if (center.x < radius || center.y < radius || center.x > canvasWidth - radius || center.y > canvasHeight - radius)
            return true;
        else return false;
    }

    public boolean contains(Point point) {
        if ((Math.pow((point.x - center.x), 2) + Math.pow((point.y - center.y), 2)) <= Math.pow(radius, 2))
            return true;
        else return false;
    }

    public void move(double dx, double dy) {
        center.x += dx;
        center.y += dy;
    }

    public void resize(double dr) {
        radius += dr;
        minSize = radius;
    }

    public void moveToward(Point point, double distance) {
        center.changeDistanceTo(point, -distance);
    }

    public void draw(GraphicsContext gc) {
        if (isMarked())
            gc.setFill(Color.BLUE);
        else
            gc.setFill(getFillColor());
        gc.fillOval(center.x - radius, center.y - radius, radius*2, radius*2);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(center.x - radius, center.y - radius, radius*2, radius*2);
    }

    public void save(BufferedWriter writer) {
        String obj = "Circle, " + "x = " + center.x + ", y = " + center.y + ", radius = " + radius + ", Color = "
                      + getFillColor().toString() +  ", isMarked = " + isMarked() + "\n";
        try {
            writer.write(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(AbstractFactory factory, String info, BufferedReader reader) {
        String[] sections = info.split(", ");
        center.x = Double.parseDouble(sections[1].split(" ")[2]);
        center.y = Double.parseDouble(sections[2].split(" ")[2]);
        radius = Double.parseDouble(sections[3].split(" ")[2]);
        setFillColor(Color.valueOf(sections[4].split(" ")[2]));
        setMarked(Boolean.parseBoolean(sections[5].split(" ")[2]));
        minSize = radius;
    }


    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public boolean isIntersectingWith(Shape shape) {
        if (shape instanceof Circle){
            Circle circle = (Circle) shape;
            return radius + circle.getRadius() > center.distanceTo(new Point(circle.getCenterX(), circle.getCenterY()));
        }
        return false;
    }

    @Override
    public String toString() {
        return "Circle";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Circle){
            Circle circle = (Circle) obj;
            if (center.x == circle.getCenterX() && center.y == circle.getCenterY() && radius == circle.getRadius())
                return true;
        }
        return false;
    }
}