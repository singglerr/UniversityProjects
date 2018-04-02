package sample.Shape.Factory;

import sample.Interfaces.AbstractFactory;
import sample.Shape.Shape;

public interface AbstractShapeFactory  extends AbstractFactory {
    Shape getShape(String info);
}
