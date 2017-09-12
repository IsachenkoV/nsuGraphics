package ru.nsu.ccfit.g12201.isachenko.cg.model;

/**
 * Created by Владимир on 20.05.2015.
 */
public class Tetragon {
    Point ps[] = new Point[4];
    public Tetragon(Point[] ps)
    {
        this.ps = ps.clone();
    }
}
