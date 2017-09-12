package ru.nsu.ccfit.g12201.isachenko.cg.model;

/**
 * Created by Владимир on 21.05.2015.
 */
public class Box {
    public int minX, maxX, minY, maxY;

    Box(Point[] ps)
    {
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;

        for (int i = 0; i < 4; i++)
        {
            minX = Math.min(minX, ps[i].x);
            maxX = Math.max(maxX, ps[i].x);
            minY = Math.min(minY, ps[i].y);
            maxY = Math.max(maxY, ps[i].y);
        }
    }

    Box(Point a, Point b, Point c)
    {
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;

        minX = Math.min(minX, a.x);
        minX = Math.min(minX, b.x);
        minX = Math.min(minX, c.x);

        minY = Math.min(minY, a.y);
        minY = Math.min(minY, b.y);
        minY = Math.min(minY, c.y);

        maxX = Math.max(maxX, a.x);
        maxX = Math.max(maxX, b.x);
        maxX = Math.max(maxX, c.x);

        maxY = Math.max(maxY, a.y);
        maxY = Math.max(maxY, b.y);
        maxY = Math.max(maxY, c.y);
    }
}
