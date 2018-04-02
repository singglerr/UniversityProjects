package sample.Shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Interfaces.MoveListener;
import sample.Interfaces.Saveable;
import sample.Interfaces.Sticky;

/**
 * Created by Данил on 08.11.2017.
 */
public abstract  class Shape implements Saveable, MoveListener{
    protected double angle;
    private Color fillColor = Color.GREEN;
    private boolean isMarked = false;
    protected double canvasWidth, canvasHeight;
    protected double minSize;
    protected Point center = new Point(0,0);
    protected Sticky observable;

    @Override
    public void setObservable(Sticky observable) {
        if (this.observable != null)
            this.observable.removeMoveListener(this);
        this.observable = observable;
    }

    @Override
    public void removeObservable() {
        observable = null;
    }

    @Override
    public Sticky getObservable() {
        return observable;
    }

    public abstract void move(double dx, double dy);
    public abstract void moveToward(Point point, double distance);
    public abstract void draw(GraphicsContext gc);
    public abstract void resize(double dr);
    public abstract void rotate(double dangle);
    public abstract void rotateFrom(Point point, double dangle);
    public abstract boolean isIntersectingBounds();
    public abstract boolean isIntersectingWith(Shape shape);
    public abstract boolean contains(Point point);
    public void setFillColor(Color color){
        fillColor = color;
    }
    public Color getFillColor() {
        return fillColor;
    }
    public void setMarked(boolean marked){
        isMarked = marked;
    }
    public boolean isMarked(){
        return isMarked;
    }
    public double getMinSize(){
        return minSize;
    }
    public double getCenterX(){
        return center.x;
    }
    public double getCenterY(){
        return center.y;
    }
}
