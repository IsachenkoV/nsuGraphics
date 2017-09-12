package ru.nsu.ccfit.g12201.isachenko.cg.model;

/**
 * Created by Владимир on 20.05.2015.
 */
public class Line {
    public double a, b, c;

    Line(Point s, Point t)
    {
        a = s.y - t.y;
        b = t.x - s.x;
        c = -a*s.x - b*s.y;
        norm();
    }

    private void norm()
    {
        double z = Math.sqrt(a*a + b*b);
        if (Math.abs(z) > 1e-9)
        {
            a /= z;
            b /= z;
            c /= z;
        }
    }

    public double dist(Point p) {
        return a * p.x + b * p.y + c;
    }
}
