package sample.Interfaces;

public interface Observable {
    void addObserver(Observer observer);
    void removerObserver(Observer observer);
    void notifyObserver();
}
