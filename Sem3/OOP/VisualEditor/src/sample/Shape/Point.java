package sample.Shape;

public class Point {
    public double x , y;

    public Point(double _x, double _y){
        x = _x;
        y = _y;
    }

    public boolean equals(Point point) {
        if (point.x == x && point.y == y)
            return true;
        else return false;
    }

    public void changeDistanceTo(Point point, double dr){
        double dx = point.x - x,
               dy = y - point.y;
        if (dx == 0){
            if (dy > 0)
                y += dr;
            else if (dy < 0)
                y -= dr;
            return;
        }
        if (dy == 0){
            if (dx > 0)
                x += dr;
            else if (dx < 0)
                x -= dr;
            return;
        }
        double nr = distanceTo(point) + dr;
        double k = dy / dx;
        double A = 1 + k*k,
               B = -k * (dy + k*x) - point.x,
               C =  point.x * point.x + (dy + k * x) * (dy + k * x)  - nr*nr;
        double D1 = B*B - A*C;
        double x1 = (-B + Math.sqrt(D1))/A,
               x2 = (-B - Math.sqrt(D1))/A;
        double y1 = y - k * x1 + k * x,
               y2 = y - k * x2 + k * x;
        if (((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y)) < ((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y))){
            x = x1;
            y = y1;
        }
        else {
            x = x2;
            y = y2;
        }
    }

    public double distanceTo(Point point){
        return Math.sqrt(squareDistanceTo(point));
    }
    public double squareDistanceTo(Point point){
        return (x - point.x)*(x - point.x) + (y - point.y)*(y - point.y);
    }
    public Point clone(){
        return new Point(x, y);
    }

}
