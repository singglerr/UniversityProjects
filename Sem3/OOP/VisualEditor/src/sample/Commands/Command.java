package sample.Commands;

import sample.Shape.Shape;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Данил on 23.12.2017.
 */
public abstract class Command {
    protected List<Shape> shapes;
    protected boolean success = false;

    public abstract void execute();
    public abstract void undo();
    public abstract Command clone();
    public void setShapes(List<Shape> shapes){
        this.shapes = shapes;
    }
    public boolean isSuccess(){
        return success;
    }
}
