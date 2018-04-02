package sample.Interfaces;

public interface Sticky {
    void addMoveListener(MoveListener listener);
    boolean removeMoveListener(MoveListener listener);
    boolean containsMoveListener(MoveListener listener);
    void notifyAllListeners(double dx, double dy);
}
