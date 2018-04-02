package source.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import source.Model;

import java.io.IOException;

public class Controller {

    @FXML
    public Canvas canvas;

    @FXML
    public TextArea textArea;

    @FXML
    public TextField countOfVertex;

    @FXML
    public TextField ratio;

    @FXML
    public Spinner<Integer> startVertex;

    private Model model;

    @FXML
    public void initialize(){
        model = new Model(canvas, textArea);
        canvas.setOnMouseClicked(event -> {
            Point2D point = new Point2D(event.getX(), event.getY());
            switch (event.getButton()){
                case PRIMARY:
                    model.mouseLeftClick(point);
                    break;
                case SECONDARY:
                    model.mouseRightClick(point);
                    break;
            }
        });

        startVertex.setEditable(true);
        startVertex.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1, 1));

        canvas.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                model.mouseDragged(new Point2D(event.getX(), event.getY()));
            }
        });
    }


    public void traverse(ActionEvent actionEvent) {
        model.search(startVertex.getValue());
    }

    public void generate(ActionEvent actionEvent) {
    }

    public void keyHandler(KeyEvent event){
        if (event.getCode().getName().equals("Delete")){
            model.delete();
        }
    }

    public void close(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void showAbout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../res/about.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.setTitle("About");
            stage.initOwner(((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}