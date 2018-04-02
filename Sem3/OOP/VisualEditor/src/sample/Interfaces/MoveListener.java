package sample.Interfaces;

public interface MoveListener {
    void setObservable(Sticky observable);
    void removeObservable();
    Sticky getObservable();
    void move(double dx, double dy);
    boolean isIntersectingBounds();
}
