package sample.Commands;


import sample.Shape.Circle;
import sample.Shape.Shape;

/**
 * Created by Данил on 23.12.2017.
 */
public class RotateShape extends Command {
    double dangle;

    public RotateShape(double dangle) {
        this.dangle = dangle;
    }

    private int countOfCircles(){
        if (shapes == null)
            return -1;
        int res = 0;
        for (Shape shape : shapes)
            if (shape instanceof Circle)
                res++;
        return res;
    }

    @Override
    public void execute() {
//        if (shape != null) {
//            shape.rotate(dangle);
//            if (shape.isIntersectingBounds() || shape instanceof Circle) {
//                undo();
//                success = false;
//            }
//            else
//                success = true;
//        }
        if (countOfCircles() == shapes.size()){
            success = false;
            return;
        }
        if (shapes != null && !shapes.isEmpty()){
            int size = shapes.size();
            int i = 0;
            boolean increment = true;
            while (i >= 0 && i < size){
                Shape shape = shapes.get(i);
                if (increment) {
                    shape.rotate(dangle);
                }
                if (increment && shape.isIntersectingBounds()){
                    success = false;
                    increment = false;
                }
                if (increment)
                    i++;
                else {
                    shape.rotate(-dangle);
                    i--;
                }
            }
            if (increment) {
                success = true;
                return;
            }
        }
        success = false;
    }

    @Override
    public void undo() {
        if (shapes != null && !shapes.isEmpty() && success){
            for (Shape shape : shapes)
                shape.rotate(-dangle);
        }
    }

    @Override
    public Command clone() {
        Command command = new RotateShape(dangle);
        return command;
    }
}
