package sample.Commands;


import sample.Shape.Shape;

/**
 * Created by Данил on 23.12.2017.
 */
public class ResizeShape extends Command {
    private double ds;

    public ResizeShape(double ds) {
        this.ds = ds;
    }

    @Override
    public void execute() {
//        if (shape != null){
//            shape.resize(ds);
//            if (shape.isIntersectingBounds() || (shape.getMinSize() < 10 && ds < 0)) {
//                undo();
//                success = false;
//            }
//            else
//                success = true;
//        }
        if (shapes != null && !shapes.isEmpty()){
            int size = shapes.size();
            int i = 0;
            boolean increment = true;
            while (i >= 0 && i < size){
                Shape shape = shapes.get(i);
                if (increment) {
                    shape.resize(ds);
                }
                if (increment && (shape.isIntersectingBounds() || (shape.getMinSize() < 10 && ds < 0))){
                    success = false;
                    increment = false;
                }
                if (increment)
                    i++;
                else {
                    shape.resize(-ds);
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
                shape.resize(-ds);
        }
    }

    @Override
    public Command clone() {
        Command command = new ResizeShape(ds);
        return command;
    }
}
