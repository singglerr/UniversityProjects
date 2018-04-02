package source;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../res/sample.fxml"));
        primaryStage.setTitle("ИиКГ Лабораторная работа №1 Усов Д.В. МО-214");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1024, 786));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
