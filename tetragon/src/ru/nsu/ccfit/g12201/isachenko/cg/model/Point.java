package ru.nsu.ccfit.g12201.isachenko.cg.model;

/**
 * Created by Владимир on 20.05.2015.
 */
public class Point {

    int x, y;
    public Point()
    {
        x = y = 0;
    }

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static Point sub(Point a, Point b)
    {
        return new Point(a.x - b.x, a.y - b.y);
    }

    public static long vMul(Point a, Point b)
    {
        return a.x*b.y - a.y*b.x;
    }

    public static Point getRandomPoint(int maxX, int maxY)
    {
        int x = (int) (Math.random() * maxX);
        int y = (int) (Math.random() * maxY);
        return new Point(x, y);
    }
}
