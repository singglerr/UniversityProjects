package source;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.ArcType;

import java.util.HashMap;
import java.util.Map;

public class Model {
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

    public Model(Canvas canvasAxon, Canvas canvasComp){
        axonConverter = new AxonometricConverter(new Point2D(canvasAxon.getWidth() / 2, canvasAxon.getHeight() / 2));
        compConverter = new ComplexConverter(new Point2D(canvasComp.getWidth() / 2, canvasComp.getHeight() / 2));
        axonDrawer = new Drawer(canvasAxon.getGraphicsContext2D(), canvasAxon.getWidth(), canvasAxon.getHeight());
        complexDrawer = new Drawer(canvasComp.getGraphicsContext2D(), canvasComp.getWidth(), canvasComp.getHeight());
        calculatePoints();
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
        Point2D[] T = compConverter.convert(mainPoint);
        Point2D convCenter = compConverter.convert(center)[0];
        for (Point2D point : T){
            complexDrawer.strokeLine(point, new Point2D(point.getX(), convCenter.getY()));
            complexDrawer.strokeLine(point, new Point2D(convCenter.getX(), point.getY()));
        }
        int sing = (mainPoint.getY() >= 0) ? 1 : -1;
        complexDrawer.strokeArc(convCenter, mainPoint.getY() * 2 * sing, mainPoint.getY() * 2 * sing,
                -90 * sing, 90, ArcType.OPEN, Config.STROKECOLOR);
        complexDrawer.drawPoint(T[0], Config.XY);
        complexDrawer.fillText("T1", T[0], 1, Orientation.UPRIGHT);
        complexDrawer.drawPoint(T[1], Config.XZ);
        complexDrawer.fillText("T2", T[1], 1, Orientation.UPRIGHT);
        complexDrawer.drawPoint(T[2], Config.ZY);
        complexDrawer.fillText("T3", T[2], 1, Orientation.UPRIGHT);
        Point2D Tx = compConverter.convert(points.get("Tx"))[1];
        Point2D Ty = compConverter.convert(points.get("Ty"))[0];
        Point2D Tz = compConverter.convert(points.get("Tz"))[1];
        complexDrawer.drawPoint(Tx, Config.TX);
        complexDrawer.fillText("Tx", Tx, 1, Orientation.UPRIGHT);
        complexDrawer.drawPoint(Ty, Config.TY);
        complexDrawer.fillText("Ty", Ty, 1, Orientation.UPRIGHT);
        complexDrawer.drawPoint(Tz, Config.TZ);
        complexDrawer.fillText("Tz", Tz, 1, Orientation.UPRIGHT);
    }

    private void renderAxonometric() {
        axonDrawer.clear();
        drawAxonCoordSystrem();
        Point2D Tx = axonConverter.convert(points.get("Tx"));
        Point2D Ty = axonConverter.convert(points.get("Ty"));
        Point2D Tz = axonConverter.convert(points.get("Tz"));
        Point2D XY = axonConverter.convert(points.get("XY"));
        Point2D XZ = axonConverter.convert(points.get("XZ"));
        Point2D ZY = axonConverter.convert(points.get("ZY"));
        Point2D point = axonConverter.convert(mainPoint);
        axonDrawer.strokeLine(point, XY);
        axonDrawer.strokeLine(point, XZ);
        axonDrawer.strokeLine(point, ZY);
        axonDrawer.strokeLine(XY, Tx);
        axonDrawer.strokeLine(XY, Ty);
        axonDrawer.strokeLine(XZ, Tx);
        axonDrawer.strokeLine(XZ, Tz);
        axonDrawer.strokeLine(ZY, Tz);
        axonDrawer.strokeLine(ZY, Ty);
        axonDrawer.drawPoint(point, Config.T);
        axonDrawer.fillText("T", point, 1, Orientation.UPRIGHT);
        axonDrawer.drawPoint(Tx, Config.TX);
        axonDrawer.fillText("Tx", Tx, 1, Orientation.UPRIGHT);
        axonDrawer.drawPoint(Ty, Config.TY);
        axonDrawer.fillText("Ty", Ty, 1, Orientation.UPRIGHT);
        axonDrawer.drawPoint(Tz, Config.TZ);
        axonDrawer.fillText("Tz", Tz, 1, Orientation.UPRIGHT);
        axonDrawer.drawPoint(XY, Config.XY);
        axonDrawer.fillText("T1", XY, 1, Orientation.UPRIGHT);
        axonDrawer.drawPoint(XZ, Config.XZ);
        axonDrawer.fillText("T2", XZ, 1, Orientation.UPRIGHT);
        axonDrawer.drawPoint(ZY, Config.ZY);
        axonDrawer.fillText("T3", ZY, 1, Orientation.UPRIGHT);
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
        complexDrawer.fillText("X", OX, 1, Orientation.UPRIGHT);
        complexDrawer.fillText("Z", OZ, 1, Orientation.DOWNRIGHT);
        complexDrawer.fillText("Y", OY1, 1, Orientation.DOWNLEFT);
        complexDrawer.fillText("Y", OY2, 1, Orientation.UPLEFT);
    }

    private void drawAxonCoordSystrem(){
        Point2D convCenter = axonConverter.convert(center);
        int i = 0;
        String[] axis = {
                "X",
                "Y",
                "Z"
        };
        for (Point3D p : coordSystem){
            Point2D convPoint = axonConverter.convert(p);
            axonDrawer.strokeLine(convCenter, convPoint);
            if (i % 2 == 0){
                axonDrawer.fillText(axis[i/2], convPoint, 1, Orientation.UPRIGHT);
            }
            i++;
        }
    }
}