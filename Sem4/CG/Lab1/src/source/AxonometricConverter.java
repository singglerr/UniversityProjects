package source;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class AxonometricConverter{
    private static final double DEGREE = Math.PI/180;

    private double alphaAngle = 0, betaAngle = 135, gammaAngle = 270;
    private Point2D center;

    public AxonometricConverter(Point2D center){
        this.center = center;
    }

    public void updateAngles(double alphaAngle, double betaAngle, double gammaAngle){
        this.alphaAngle = alphaAngle;
        this.betaAngle = betaAngle;
        this.gammaAngle = gammaAngle;
    }

    public Point2D convert(Point3D point3D){
        double x = center.getX() - (point3D.getX() * Math.cos(DEGREE * alphaAngle) + point3D.getY() * Math.cos(DEGREE * betaAngle) +
                        point3D.getZ() * Math.cos(DEGREE * gammaAngle));
        double y = center.getY() + point3D.getX() * Math.sin(DEGREE * alphaAngle) + point3D.getY() * Math.sin(DEGREE * betaAngle) +
                    point3D.getZ() * Math.sin(DEGREE * gammaAngle);
        return new Point2D(x, y);
    }
}