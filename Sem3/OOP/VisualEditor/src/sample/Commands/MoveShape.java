package sample.Commands;


import sample.Shape.Shape;

/**
 * Created by Данил on 23.12.2017.
 */
public class MoveShape extends Command {
    private double dx, dy;

    public MoveShape(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute() {
        if (shapes != null && !shapes.isEmpty()){
//            shape.move(dx,dy);
//            if (shape.isIntersectingBounds()) {
//                undo();
//                success = false;
//            }
//            else
//                success = true;
            int size = shapes.size();
            int i = 0;
            boolean increment = true;
            while (i >= 0 && i < size){
                Shape shape = shapes.get(i);
                if (increment) {
                    shape.move(dx, dy);
                }
                if (increment && shape.isIntersectingBounds()){
                    success = false;
                    increment = false;
                }
                if (increment)
                    i++;
                else {
                    shape.move(-dx, -dy);
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
                shape.move(-dx, -dy);
        }
    }

    @Override
    public Command clone() {
       Command command = new MoveShape(dx, dy);
        return command;
    }
}