package ru.nsu.ccfit.g12201.isachenko.cg.model;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Владимир on 29.04.2015.
 */
public class Figure {

    final static double a[][] = {{-1, 3, -3, 1}, {3, -6, 3, 0}, {-3, 0, 3 , 0}, {1, 4, 1, 0}};
    static final Matrix Ms = new Matrix(a);

    public ArrayList<Point2D> points = new ArrayList<Point2D>();
    private int pCount;

    public Figure()
    {}

    void scanFigure(Scanner s)
    {
        pCount = s.nextInt();
        for (int i = 0; i < pCount; i++)
        {
            double x, y;
            x = s.nextDouble();
            y = s.nextDouble();
            points.add(new Point2D(x, y));
        }
    }

    public Point2D getPoint(double t)
    {
        int n = points.size() - 3;
        if (n < 0 || t > n)
            return null;

        double x, y;
        double T[] = new double[4];
        double Ax[] = new double[4];
        double Ay[] = new double[4];

        int k = (int) t;
        t -= k;

        T[3] = 1;
        for (int i = 3; i > 0; i--)
        {
            T[i - 1] = T[i] * t;
        }

        for (int i = 0; i < 4; i++)
        {
            Point2D pn = points.get(i + k);
            Ax[i] = pn.getX();
            Ay[i] = pn.getY();
        }

        Matrix CMs = Matrix.matrixScalarProduct(1.0/6.0, Ms);
        double[] P = Matrix.vectorMatrixProduct(T, CMs);

        x = Matrix.vectorVectorProduct(P, Ax);
        y = Matrix.vectorVectorProduct(P, Ay);

        return new Point2D(x, y);
    }

    public Point3D getSurfacePoint(double t, double u)
    {
        Point2D p = getPoint(t);
        return new Point3D(p.getX() * Math.cos(u), p.getX()*Math.sin(u), p.getY());
    }

    public int getPartsCount()
    {
        return points.size() - 3;
    }
}
