package sample.Shape.Factory;

import sample.Shape.*;

public class BaseShapeFactory implements AbstractShapeFactory{
    private double width, height;

    public BaseShapeFactory(double width, double height){
        this.width = width;
        this.height = height;
    }

    @Override
    public Object getObject(String info) {
        Object object = null;
        switch (info){
            case "StickyCircle":
                object = new StickyCircle(width, height);
                break;
            case "Circle":
                object = new Circle(width, height);
                break;
            case "Rectangle":
                object = new Rectangle(width, height);
                break;
            case "Triangle":
                object = new Triangle(width, height);
                break;
            case "GroupOfShapes":
                object = new GroupOfShapes();
                break;
        }
        return object;
    }

    public Shape getShape(String type){
        return (Shape) getObject(type);
    }
}
