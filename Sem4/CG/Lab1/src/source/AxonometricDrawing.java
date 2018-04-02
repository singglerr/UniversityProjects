package source;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Point3DBuilder;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AxonometricDrawing extends Drawing {

    public AxonometricDrawing(GraphicsContext context, double width, double height){
        super(context, width, height);
    }

    public void drawPoint(Point3D point3D) {

    }

    @Override
    public void drawCoordinateSystem(Point2D center, Point2D... semiaxis) {
        String[] names = {"X", "Y", "Z"};
        int i = 0;
        for (Point2D point : semiaxis){
            if (i % 2 == 0) {
                fillText(names[i/2], point, Orientation.DOWNLEFT);
            }
            i++;
            strokeLine(center, point);
        }
    }
}