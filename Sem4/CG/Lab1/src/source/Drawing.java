package source;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public abstract class Drawing {
    GraphicsContext context;
    double width, height;       // Drawing dimensions

    Drawing(GraphicsContext context, double width, double height){
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

    public void drawPoint(Point2D point, Color color, String name){
        context.setFill(color);
        double radius = 5;
        context.fillOval(point.getX() - radius, point.getY() - radius, radius*2, radius*2);
        fillText(name, point, Orientation.UPRIGHT);
    }

    public void fillText(String text, Point2D point, Orientation orientation){
        Point2D textPoint;
        double offset = 5;
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
            default:
                textPoint = point;
        }
        context.setFill(Color.BLACK);
        context.fillText(text, textPoint.getX(), textPoint.getY());
    }

    public abstract void drawCoordinateSystem(Point2D center, Point2D... semiaxis);
}
