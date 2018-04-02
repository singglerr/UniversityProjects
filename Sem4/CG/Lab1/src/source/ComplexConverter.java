package source;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/**
 * Created by Данил on 09.03.2018.
 */
public class ComplexConverter {
    private Point2D center;

    public ComplexConverter(Point2D center){
        this.center = center;
    }


    public Point2D[] convert(Point3D point3D) {
        Point2D[] res = new Point2D[3];
        double x = point3D.getX(),
               y = point3D.getY(),
               z = point3D.getZ();

        res[0] = new Point2D(center.getX() - x, center.getY() + y);
        res[1] = new Point2D(center.getX() - x, center.getY() - z);
        res[2] = new Point2D(center.getX() + y, center.getY() - z);
        return res;
    }
}
