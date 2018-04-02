package source;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.ArcType;

import java.util.HashMap;
import java.util.Map;

public class Model3D {
    private final double length = 256 * Config.SCALE;
    private Point3D[] coordSystem = {
            new Point3D(length, 0, 0),
            new Point3D(-length, 0, 0),
            new Point3D(0, length, 0),
            new Point3D(0, -length, 0),
            new Point3D(0, 0, length),
            new Point3D(0, 0, -length)
    };
    private Point3D center = new Point3D(0,0,0);
    private Map<String, Point3D> points = new HashMap<>();
    private Drawer axonDrawer;
    private Drawer complexDrawer;
    private AxonometricConverter axonConverter;
    private ComplexConverter compConverter;
    private Point3D mainPoint = new Point3D(0,0,0);

    public Model3D(Canvas canvasAxon, Canvas canvasComp){
        axonConverter = new AxonometricConverter(new Point2D(canvasAxon.getWidth() / 2, canvasAxon.getHeight() / 2));
        compConverter = new ComplexConverter(new Point2D(canvasComp.getWidth() / 2, canvasComp.getHeight() / 2));
        axonDrawer = new Drawer(canvasAxon.getGraphicsContext2D(), canvasAxon.getWidth(), canvasAxon.getHeight());
        complexDrawer = new Drawer(canvasComp.getGraphicsContext2D(), canvasComp.getWidth(), canvasComp.getHeight());
        render();
    }

    public void updateAngles(double alpha, double beta, double gamma){
        axonConverter.updateAngles(alpha, beta, gamma);
        renderAxonometric();
    }

    public void updatePoint(double x, double y, double z){
        mainPoint = new Point3D(x * Config.SCALE, y * Config.SCALE, z * Config.SCALE);
        calculatePoints();
        render();
    }

    private void calculatePoints(){
        points.put("Tx", new Point3D(mainPoint.getX(), 0, 0));
        points.put("Ty", new Point3D(0, mainPoint.getY(), 0));
        points.put("Tz", new Point3D(0, 0, mainPoint.getZ()));
        points.put("XY", new Point3D(mainPoint.getX(), mainPoint.getY(), 0));
        points.put("XZ", new Point3D(mainPoint.getX(), 0, mainPoint.getZ()));
        points.put("ZY", new Point3D(0, mainPoint.getY(), mainPoint.getZ()));
    }

    private void render() {
        renderAxonometric();
        renderComplex();
    }

    private void renderComplex() {
        complexDrawer.clear();
        drawComplexCoordSystrem();
        Point2D[] points = compConverter.convert(mainPoint);
        Point2D convCenter = compConverter.convert(center)[0];
        for (Point2D point : points){
            complexDrawer.strokeLine(point, new Point2D(point.getX(), convCenter.getY()));
            complexDrawer.strokeLine(point, new Point2D(convCenter.getX(), point.getY()));
        }
        int sing = (mainPoint.getY() >= 0) ? 1 : -1;
        complexDrawer.strokeArc(convCenter, mainPoint.getY() * 2 * sing, mainPoint.getY() * 2 * sing,
                -90 * sing, 90, ArcType.OPEN, Config.STROKECOLOR);
        complexDrawer.drawPoint(points[0], Config.XY);
        complexDrawer.drawPoint(points[1], Config.XZ);
        complexDrawer.drawPoint(points[2], Config.ZY);

    }

    private void renderAxonometric() {

    }

    private void drawComplexCoordSystrem(){
        Point2D convCenter = compConverter.convert(center)[0];
        Point2D OX = compConverter.convert(coordSystem[0])[1];
        Point2D OY1 = compConverter.convert(coordSystem[2])[2];
        Point2D OY2 = compConverter.convert(coordSystem[2])[0];
        Point2D OZ = compConverter.convert(coordSystem[4])[1];
        complexDrawer.strokeLine(convCenter, OX);
        complexDrawer.strokeLine(convCenter, OY1);
        complexDrawer.strokeLine(convCenter, OY2);
        complexDrawer.strokeLine(convCenter, OZ);
    }

    private void drawAxonCoordSystrem(){

    }
}