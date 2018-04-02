package sample.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Interfaces.AbstractFactory;
import sample.Shape.Factory.AbstractShapeFactory;
import sample.Shape.Factory.BaseShapeFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Данил on 22.12.2017.
 */
public class GroupOfShapes extends Shape {
    private List<Shape> childs = new ArrayList<>();


    public void addShape(Shape shape){
        if (shape != null) {
            childs.add(shape);
            calculateCenter();
            calculateMinSize();
        }
    }

    public void removeShape(Shape shape){
        if (shape != null) {
            childs.remove(shape);
            calculateCenter();
            calculateMinSize();
        }
    }

    public List<Shape> getChilds(){
        return childs;
    }

    private void calculateMinSize(){
        minSize = Double.MAX_VALUE;
        for (Shape shape : childs){
            double shapeSize = shape.getMinSize();
            if (shapeSize < minSize)
                minSize = shapeSize;
        }
    }

    private void calculateCenter(){
        center.x = center.y = 0;
        for (Shape s : childs){
            center.x += s.getCenterX();
            center.y += s.getCenterY();
        }
        center.x = center.x / childs.size();
        center.y = center.y / childs.size();
    }

    public void move(double dx, double dy) {
        center.x += dx;
        center.y += dy;
        for (Shape shape : childs){
            shape.move(dx, dy);
        }
    }


    public void moveToward(Point point, double distance) {
        Point dp = center.clone();
        dp.changeDistanceTo(point, -distance);
        move(dp.x - center.x, dp.y - center.y);
    }

    public void draw(GraphicsContext gc) {
        for (Shape shape : childs){
            shape.draw(gc);
        }
    }

    public void resize(double dr) {
        for (Shape shape : childs){
            shape.moveToward(center, -2*dr);
            shape.resize(dr);
        }
        calculateMinSize();
    }

    public void rotate(double dangle) {
        for (Shape shape : childs)
            shape.rotateFrom(center, dangle);
    }

    public void rotateFrom(Point point, double dangle) {
        double sina = Math.sin(dangle),
                cosa = Math.cos(dangle);
        double x = center.x - point.x, y = center.y - point.y;
        center.x = x * cosa + y * sina + point.x;
        center.y = x * (-sina) + y * cosa + point.y;
        for (Shape shape : childs) {
            shape.rotateFrom(point, dangle);
        }
        //calculateCenter();
    }

    public boolean isIntersectingBounds() {
        for (Shape shape : childs){
            if (shape.isIntersectingBounds())
                return true;
        }
        return false;
    }

    public boolean contains(Point point) {
        for (Shape shape : childs){
            if (shape.contains(point))
                return true;
        }
        return false;
    }

    public void setFillColor(Color color) {
        super.setFillColor(color);
        for (Shape shape : childs){
            shape.setFillColor(color);
        }
    }

    public Color getFillColor() {
        return super.getFillColor();
    }

    public void setMarked(boolean marked) {
        super.setMarked(marked);
        for (Shape shape : childs){
            shape.setMarked(marked);
        }
    }

    public boolean isMarked() {
        return super.isMarked();
    }

    @Override
    public void save(BufferedWriter writer) {
        try {
            String out = "GroupOfShapes, " + "size = " + childs.size() + ", isMarked = " + isMarked() +" {\n";
            writer.write(out);
            for (Shape shape : childs){
                shape.save(writer);
            }
            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(AbstractFactory factory, String info, BufferedReader reader) {
        String[] arr = info.split(", ");
        boolean marked = Boolean.parseBoolean(arr[2].split(" ")[2]);
        setMarked(marked);
        if (factory instanceof AbstractShapeFactory) {
            AbstractShapeFactory shapeFactory = (AbstractShapeFactory) factory;
            int size = Integer.parseInt(arr[1].split(" ")[2]);
            try {
                for (int i = 0; i < size; i++) {
                    String line = reader.readLine();
                    Shape shape = shapeFactory.getShape(line.split(", ")[0]);
                    shape.load(factory, line, reader);
                    childs.add(shape);
                }
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            calculateCenter();
            calculateMinSize();
        }
    }

    @Override
    public boolean isIntersectingWith(Shape shape) {
        for (Shape child : childs){
            if (child.isIntersectingWith(shape))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "GroupOfShapes";
    }
}