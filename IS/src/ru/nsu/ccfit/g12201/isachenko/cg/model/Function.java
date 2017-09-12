package ru.nsu.ccfit.g12201.isachenko.cg.model;

import javafx.geometry.Point2D;

public class Function {

    public int a, b, c, d; //(b - a + 1)*(d - c + 1)
    public double fMin = 1e9, fMax = (-1) * 1e9;
    public Point sz;

    private double w, h;

    Function(int a, int b, int c, int d)
    {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        sz = new Point(b - a + 1, d - c + 1);
        w = (double) (sz.x);
        h = (double) (sz.y);

        for (int i = 0; i <= sz.x; i++)
        {
            for (int j = 0; j <= sz.y; j++)
            {
                fMin = Math.min(fMin, getValue(i, j, sz));
                fMax = Math.max(fMax, getValue(i, j, sz));
            }
        }
    }

    public double getValue(int x, int y, Point sz)
    {
        double xk = (x * w) / sz.x + a;
        double yk = (y * h) / sz.y + c;
        //return ( Math.pow( xk / 2, 3 ) - xk + Math.pow( yk * 15, 2 ) ) * Math.sin( yk / 100 );
        return xk * xk + 3 * xk + 5 * yk * yk - 2000;
        //return Math.cos(xk / 10.0) - Math.sin(yk / 10.0);
        //return  -((Math.cos((xk / 20)) + 2 * Math.sin((yk / 20))) * (xk * yk / 400));
    }

    public double getValue(Point2D p)
    {
        //return ( Math.pow( p.getX() / 2, 3 ) - p.getX() + Math.pow( p.getY()* 15, 2 ) ) * Math.sin( p.getY() / 100 );
        return p.getX() * p.getX() + 3 * p.getX() + 5 * p.getY() * p.getY() - 2000;
        //return Math.cos(p.getX() / 10.0) - Math.sin(p.getY() / 10.0);
        //return  -((Math.cos((p.getX() / 20)) + 2 * Math.sin((p.getY() / 20))) * (p.getX() * p.getY() / 400));
    }
}
