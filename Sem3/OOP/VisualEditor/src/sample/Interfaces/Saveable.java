package sample.Interfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface Saveable {
    void save(BufferedWriter writer);
    void load(AbstractFactory factory, String info, BufferedReader reader);
}
