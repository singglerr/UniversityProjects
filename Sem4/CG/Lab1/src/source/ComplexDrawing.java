package source;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * Created by Данил on 09.03.2018.
 */

public class ComplexDrawing extends Drawing {

    public ComplexDrawing(GraphicsContext context, double width, double height){
        super(context, width, height);
    }

    public void drawPoint(Point3D point3D) {
//        Point2D[] points = converter.convert(point3D);
//        double radius = 5;
//        context.setFill(Color.BLUE);
//        int i = 1;
//        for (Point2D point : points){
//            context.fillOval(point.getX() - radius, point.getY() - radius, radius*2, radius*2);
//            context.fillText("T" + i++, point.getX() + 3, point.getY() - 3);
//            strokeLine(point, new Point2D(point.getX(), center.getY()));
//            strokeLine(point, new Point2D(center.getX(), point.getY()));
//        }
//        int sign = (point3D.getY() >= 0) ? 1 : -1;
//        radius = Math.abs(point3D.getY());
//        context.strokeArc(center.getX() - radius,center.getY() - radius, radius*2, radius*2, -90 * sign, 90, ArcType.OPEN);
    }

    @Override
    public void drawCoordinateSystem(Point2D center, Point2D... semiaxis) {
        String[] names = {"X", "Z", "Y",  "Y"};
        int i = 0;
        context.setFill(Color.BLACK);
        for (Point2D point : semiaxis){
            strokeLine(center, point);
            if (i % 3 == 0)
                fillText(names[i++], point, Orientation.UPRIGHT);
            else
                fillText(names[i++], point, Orientation.DOWNLEFT);
        }
    }
}