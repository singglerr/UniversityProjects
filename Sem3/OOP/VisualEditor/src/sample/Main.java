package sample;

import Controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Shape.Circle;
import sample.Shape.Point;
import sample.Shape.Shape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Visual editor");
        primaryStage.setScene(new Scene(root, 850, 673));
        primaryStage.setResizable(false);
        Controller controller  = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
