package source.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import source.*;

public class Controller {

    @FXML
    public Canvas canvasAxon;

    @FXML
    public Canvas canvasComplex;

    @FXML
    public Slider sliderX;

    @FXML
    public Slider sliderY;

    @FXML
    public Slider sliderZ;

    @FXML
    public Slider sliderAlpha;

    @FXML
    public Slider sliderBeta;

    @FXML
    public Slider sliderGamma;

    private Model model;


    @FXML
    public void initialize() {
        model = new Model(canvasAxon, canvasComplex);
        sliderX.setValue(64);
        sliderY.setValue(64);
        sliderZ.setValue(64);
        onCoordChanged(null);
    }

    public void close(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }



    public void reset(ActionEvent actionEvent) {
        sliderX.setValue(0);
        sliderY.setValue(0);
        sliderZ.setValue(0);
        sliderAlpha.setValue(0);
        sliderBeta.setValue(135);
        sliderGamma.setValue(270);
        onCoordChanged(null);
        onAngleChanged(null);
    }

    public void onCoordChanged(MouseEvent mouseEvent) {
        model.updatePoint(sliderX.getValue(), sliderY.getValue(), sliderZ.getValue());
    }

    public void onAngleChanged(MouseEvent mouseEvent) {
        model.updateAngles(sliderAlpha.getValue(), sliderBeta.getValue(), sliderGamma.getValue());
    }
}