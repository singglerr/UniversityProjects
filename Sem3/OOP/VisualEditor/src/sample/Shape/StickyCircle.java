package sample.Shape;

import sample.Interfaces.MoveListener;
import sample.Interfaces.Sticky;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TransferQueue;

public class StickyCircle extends Circle implements Sticky{
    private List<MoveListener> listeners = new LinkedList<>();

    public StickyCircle(double canvasWidth, double canvasHeight){
        super(canvasWidth, canvasHeight);
    }

    public StickyCircle(Point center, double canvasWidth, double canvasHeight){
        super(center, canvasWidth,canvasHeight);
    }

    @Override
    public void addMoveListener(MoveListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            listener.setObservable(this);
        }
    }

    @Override
    public boolean removeMoveListener(MoveListener listener) {
        listener.removeObservable();
        return listeners.remove(listener);
    }

    @Override
    public void move(double dx, double dy) {
        super.move(dx, dy);
        notifyAllListeners(dx,dy);
    }

    @Override
    public boolean isIntersectingBounds() {
        for (MoveListener listener : listeners){
            if (listener.isIntersectingBounds())
                return true;
        }
        return super.isIntersectingBounds();
    }

    @Override
    public void notifyAllListeners(double dx, double dy) {
        for (MoveListener listener : listeners){
            listener.move(dx, dy);
        }
    }

    @Override
    public void save(BufferedWriter writer) {
        String obj = "StickyCircle, " + "x = " + center.x + ", y = " + center.y + ", radius = " + getRadius() + ", Color = "
                + getFillColor().toString() +  ", isMarked = " + isMarked() + "\n";
        try {
            writer.write(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean containsMoveListener(MoveListener listener) {
        return listeners.contains(listener);
    }

    @Override
    public boolean isIntersectingWith(Shape shape) {
        return shape.isIntersectingWith(this);
    }

    @Override
    public String toString() {
        return "StickyCircle";
    }
}
