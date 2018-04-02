package Controllers;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Commands.Command;
import sample.Commands.MoveShape;
import sample.Commands.ResizeShape;
import sample.Commands.RotateShape;
import sample.Interfaces.Sticky;
import sample.Shape.*;
import sample.MyStorage;
import sample.Shape.Factory.AbstractShapeFactory;
import sample.Shape.Factory.BaseShapeFactory;
import sun.security.provider.SHA;

import javax.swing.text.Style;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class Controller {
    private static final double DANGLE = Math.PI/90;

    @FXML
    private Canvas canvas;

    @FXML
    private ListView<TypeOfShape> listView;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TreeView<Shape> treeView;

    private MultipleSelectionModel<TreeItem<Shape>> selectionModel;
    private Stage primaryStage;
    private Map<KeyCode, Command> keyCodeCommandMap = new HashMap<>();
    private Command mouseButtonCommand;
    private MyStorage<Shape> storage = new MyStorage<>();
    private Stack<Command> commandStack = new Stack<>();
    private List<Shape> selectedShapes = new LinkedList<>();
    private AbstractShapeFactory shapeFactory;
    private TypeOfShape selectedType = TypeOfShape.Circle;
    private int countOfClicks = 0;
    private Stack<Point> points = new Stack<>();


    public Controller(){
        storage.addObserver(() -> {
            treeView.getRoot().getChildren().clear();
            rebuildTree(storage.toList(), treeView.getRoot());
        });


        //Действия над фигурами
        keyCodeCommandMap.put(KeyCode.UP, new MoveShape(0,-2));
        keyCodeCommandMap.put(KeyCode.DOWN, new MoveShape(0,2));
        keyCodeCommandMap.put(KeyCode.LEFT, new MoveShape(-2,0));
        keyCodeCommandMap.put(KeyCode.RIGHT, new MoveShape(2,0));
        keyCodeCommandMap.put(KeyCode.ADD, new ResizeShape(2));
        keyCodeCommandMap.put(KeyCode.SUBTRACT, new ResizeShape(-2));
        keyCodeCommandMap.put(KeyCode.PAGE_UP, new RotateShape(DANGLE));
        keyCodeCommandMap.put(KeyCode.PAGE_DOWN, new RotateShape(-DANGLE));

        //
        keyCodeCommandMap.put(KeyCode.Z, new Command() {
            @Override
            public void execute() {
                if (!commandStack.isEmpty()){
                    commandStack.pop().undo();
                }
            }

            @Override
            public void undo() {
            }

            @Override
            public Command clone() {
                try{
                    return (Command) getClass().getDeclaredConstructors()[0].newInstance(Controller.this);
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

        });
        keyCodeCommandMap.put(KeyCode.DELETE, new Command() {

            @Override
            public void execute() {
                for (Shape shape : shapes){
                    storage.remove(shape);
                    unselectShape(shape);
                }
                success = true;
            }

            @Override
            public void undo() {
                for (Shape shape : shapes) {
                    storage.addEnd(shape);
                }
            }

            @Override
            public Command clone() {
                try{
                    return (Command) getClass().getDeclaredConstructors()[0].newInstance(Controller.this);
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        });

        mouseButtonCommand = new Command() {
            private boolean controlDown;
            private Shape prevCurrent;

            public void setControlDown(boolean controlDown){
                this.controlDown = controlDown;
            }

            @Override
            public void execute() {
                Shape shape = clickedOnShape(points.lastElement());
                if (shape == null){
                    Shape newShape = null;
                    switch (selectedType){
                        case StickyCircle:
                            newShape = shapeFactory.getShape("StickyCircle");
                            ((StickyCircle) newShape).setCircle(points.pop());
                            break;
                        case Circle:
                            newShape = shapeFactory.getShape("Circle");
                            ((Circle) newShape).setCircle(points.pop());
                            break;
                        case Triangle:
                            if (countOfClicks == 2){
                                newShape = shapeFactory.getShape("Triangle");
                                ((Triangle) newShape).setTriangle(points.pop(), points.pop(), points.pop());
                                countOfClicks = 0;
                                //points.removeAllElements();
                            }
                            else {
                                countOfClicks++;
                            }
                            break;
                        case Rectangle:
                            if (countOfClicks == 1){
                                newShape = shapeFactory.getShape("Rectangle");
                                ((Rectangle) newShape).setRectangle(points.pop(), points.pop());
                                countOfClicks = 0;
                                //points.removeAllElements();
                            }
                            else {
                                countOfClicks++;
                            }
                            break;
                    }
                    if (newShape != null){
                        unselectAll();
                        shapes.add(newShape);
                        success = true;
                        newShape.setFillColor(colorPicker.getValue());
                        storage.addEnd(newShape);
                        selectShape(newShape);
                    }
                }
                else {
                    points.pop();
                    countOfClicks = 0; // Тест
                    if (controlDown){
                        if (shape.isMarked()){
                            unselectShape(shape);
                        }
                        else {
                            selectShape(shape);
                        }
                    }
                    else {
                        unselectAll();
                        selectShape(shape);
                    }
                }
            }

            @Override
            public void undo() {
                if (success) {
                    storage.remove(shapes.get(0));
                    //unselectShape(shape);
                }
            }

            @Override
            public Command clone() {
                try{
                    return (Command) getClass().getDeclaredConstructors()[0].newInstance(Controller.this);
                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    private void selectShape(Shape shape) {
        selectionModel.select(findTreeItem(shape));
    }

    private void unselectShape(Shape shape) {
        TreeItem<Shape> item = findTreeItem(shape);
        int index = selectionModel.getSelectedItems().indexOf(item);
        selectionModel.clearSelection(index);
    }

    private TreeItem<Shape> findTreeItem(Shape shape) {
        for (TreeItem<Shape> item : treeView.getRoot().getChildren()){
            if (item.getValue() == shape){
                return item;
            }
        }
        return null;
    }

    private void unselectAll(){
        selectionModel.clearSelection();
    }

    public void initialize()
    {
        TreeItem<Shape> root = new TreeItem<>(new GroupOfShapes(){
            @Override
            public String toString() {
                return "Объекты на холсте";
            }
        });
        root.setExpanded(true);
        treeView.setRoot(root);
        colorPicker.setValue(Color.WHITE);
        colorPicker.setOnHidden(event -> {
            if (!selectedShapes.isEmpty()){
                for (Shape shape : selectedShapes)
                    shape.setFillColor(colorPicker.getValue());
                redraw();
            }
        });
        listView.setFocusTraversable(false);
        listView.getItems().addAll(TypeOfShape.values());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().select(TypeOfShape.Circle);
        listView.setOnMouseClicked(event -> {
            selectedType = listView.getSelectionModel().getSelectedItem();
            countOfClicks = 0;
            points.removeAllElements();
        });
        treeView.setFocusTraversable(false);
        shapeFactory = new BaseShapeFactory(canvas.getWidth(), canvas.getHeight());
        selectionModel = treeView.getSelectionModel();
        treeView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<Shape>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<Shape>> c) {
                for (Shape shape : selectedShapes){
                    shape.setMarked(false);
                }
                selectedShapes.clear();
                for (TreeItem<Shape> item : selectionModel.getSelectedItems()){
                    item.getValue().setMarked(true);
                    selectedShapes.add(item.getValue());
                }
                redraw();
            }
        });
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        redraw();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            Point click = new Point(mouseEvent.getX(), mouseEvent.getY());
            points.push(click);
            Command command = mouseButtonCommand.clone();
            command.setShapes(new LinkedList<>());
            try {
                Method setControlDown = command.getClass().getDeclaredMethod("setControlDown", boolean.class);
                setControlDown.invoke(command, mouseEvent.isControlDown());
                command.execute();
                if (command.isSuccess()){
                    commandStack.push(command);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            redraw();
        }
    }

    public void Exit(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    private Shape clickedOnShape(Point click){
        for(storage.end(); !storage.isBOF(); storage.previous()){
            Shape shape = storage.getObject();
            if (shape.contains(click))
                return shape;
        }
        return null;
    }

    private void redraw(){
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.LIGHTGRAY);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (storage.first(); !storage.isEOL(); storage.next()){
            storage.getObject().draw(context);
        }
    }

    public void onClickClear(ActionEvent actionEvent) {
        selectedShapes.clear();
        countOfClicks = 0;
        points.removeAllElements();
        storage.removeAll();
        treeView.getRoot().getChildren().clear();
        redraw();
    }

    public void showAbout(ActionEvent actionEvent) {

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("../res/About.fxml"));
            Stage stage = new Stage();
            stage.setHeight(240.0);
            stage.setWidth(458.0);
            stage.setResizable(false);
            stage.setTitle("About");
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage.getScene().getWindow());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleKeyEvent(KeyEvent event){
        Command cloneableCommand = keyCodeCommandMap.get(event.getCode());
        if (cloneableCommand == null)
            return;
        Command command = cloneableCommand.clone();
        if (event.isControlDown() && event.getCode() == KeyCode.Z){
            command.execute();
        }
        else if (event.getCode() != KeyCode.Z && !selectedShapes.isEmpty()){
            command.setShapes(new LinkedList<>(selectedShapes));
            boolean isMove = false;
            switch (event.getCode()){
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    isMove = true;
            }
            command.execute();
            if (command.isSuccess()) {
                commandStack.push(command);
                if (isMove)
                    checkCollide();
            }
        }
        redraw();
    }

    private void checkCollide(){
        for (Shape selected : selectedShapes){
            if (selected instanceof Sticky){
                Sticky sticky = (Sticky) selected;
                for (storage.first(); !storage.isEOL(); storage.next()){
                    Shape shape = storage.getObject();
                    if (!selectedShapes.contains(shape) && !sticky.containsMoveListener(shape) && selected.isIntersectingWith(shape)){
                        sticky.addMoveListener(shape);
                    }
                }
            }
            else {
                Sticky observable = selected.getObservable();
                if (observable != null && !selectedShapes.contains((Shape) observable)){
                    observable.removeMoveListener(selected);
                }
            }
        }
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.getScene().setOnKeyPressed(event -> handleKeyEvent(event));
    }

    public void group(ActionEvent actionEvent) {
        if (!selectedShapes.isEmpty()) {
            GroupOfShapes group = new GroupOfShapes();
            List<Shape> list = new LinkedList<>(selectedShapes);
            for (Shape shape: list){
                group.addShape(shape);
                storage.remove(shape);
            }
            unselectAll();
            storage.addEnd(group);
            selectShape(group);
            redraw();
        }
    }

    public void ungroup(ActionEvent actionEvent) {
        if (!selectedShapes.isEmpty()){
            List<Shape> list = new LinkedList<>(selectedShapes);
            for (Shape shape : list){
                if (shape instanceof GroupOfShapes){
                    storage.remove(shape);
                    for (Shape child : ((GroupOfShapes) shape).getChilds()){
                        storage.addEnd(child);
                    }
                }
            }
            unselectAll();
            redraw();
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            File file = new File("config.txt");
            file.createNewFile();
            storage.saveToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(ActionEvent actionEvent) {
        storage.removeAll();
        unselectAll();
        try {
            File file = new File("config.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while (reader.ready()){
                String line = reader.readLine();
                String[] arr = line.split(", ");
                Shape shape = shapeFactory.getShape(arr[0]);
                shape.load(shapeFactory, line, reader);
                storage.addEnd(shape);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        for (TreeItem<Shape> item : treeView.getRoot().getChildren()){
            if (item.getValue().isMarked()){
                selectionModel.select(item);
            }
        }
        redraw();
    }

    private void rebuildTree(List<Shape> list, TreeItem<Shape> root) {
        for (Shape shape : list){
            TreeItem<Shape> item = new TreeItem<>(shape);
            if (shape instanceof GroupOfShapes){
                GroupOfShapes group = (GroupOfShapes) shape;
                rebuildTree(group.getChilds(), item);
            }
            root.getChildren().add(item);
        }
    }
}