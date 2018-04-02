package source;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.TextAlignment;

public class Drawer {
    private GraphicsContext context;
    private double width, height;       // Drawer dimensions

    Drawer(GraphicsContext context, double width, double height){
        this.context = context;
        this.width = width;
        this.height = height;
    }

    public void clear() {
        context.setFill(Color.LIGHTGRAY);
        context.fillRect(0,0, width, height);
    }

    public void strokeLine(Point2D point1, Point2D point2){
        context.setStroke(Color.BLACK);
        context.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
    }

    public void strokePoint(Point2D point2D, Color color){
        context.setStroke(color);
        context.strokeOval(point2D.getX() - Config.RADIUS, point2D.getY() - Config.RADIUS, Config.RADIUS * 2, Config.RADIUS * 2);
    }

    public void drawPoint(Point2D point, Color color){
        context.setFill(color);
        context.fillOval(point.getX() - Config.RADIUS, point.getY() - Config.RADIUS, Config.RADIUS*2, Config.RADIUS*2);
    }

    public void strokeArc(Point2D point, double width, double height, double startAngle, double arcExtent, ArcType arcType, Color color){
        context.setStroke(color);
        context.strokeArc(point.getX() - width/2, point.getY() - height/2, width, height, startAngle, 90, arcType);
    }

    public void fillText(String text, Point2D point,double offset, Orientation orientation){
        Point2D textPoint;
        switch (orientation){
            case UPRIGHT:
                context.setTextAlign(TextAlignment.LEFT);
                context.setTextBaseline(VPos.BOTTOM);
                textPoint = new Point2D(point.getX() + offset, point.getY() - offset);
                break;
            case UPLEFT:
                context.setTextAlign(TextAlignment.RIGHT);
                context.setTextBaseline(VPos.BOTTOM);
                textPoint = new Point2D(point.getX() - offset, point.getY() - offset);
                break;
            case DOWNLEFT:
                context.setTextAlign(TextAlignment.RIGHT);
                context.setTextBaseline(VPos.TOP);
                textPoint = new Point2D(point.getX() - offset, point.getY() + offset);
                break;
            case DOWNRIGHT:
                context.setTextAlign(TextAlignment.LEFT);
                context.setTextBaseline(VPos.TOP);
                textPoint = new Point2D(point.getX() + offset, point.getY() + offset);
                break;
            case CENTER:
                context.setTextAlign(TextAlignment.CENTER);
                context.setTextBaseline(VPos.CENTER);
                textPoint = point;
                break;
            default:
                textPoint = point;
        }
        context.setFill(Color.BLACK);
        context.fillText(text, textPoint.getX(), textPoint.getY());
    }
}
