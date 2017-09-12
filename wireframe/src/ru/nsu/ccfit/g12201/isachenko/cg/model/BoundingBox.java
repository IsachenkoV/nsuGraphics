package ru.nsu.ccfit.g12201.isachenko.cg.model;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/**
 * Created by Владимир on 06.05.2015.
 */
public class BoundingBox {
    public Point2D x, y, z;

    BoundingBox(Figure f, int k1, int k2) {
        x = new Point2D(1e9, -1e9);
        y = new Point2D(1e9, -1e9);
        z = new Point2D(1e9, -1e9);
        for (double j = 0.0; j < f.getPartsCount(); j += (f.getPartsCount() + 0.0) / (k1 + 0.0)) {
            for (double k = 0.0; k < 2.0 * Math.PI; k += 2.0 * Math.PI / (k2 + 0.0)) {
                Point3D p = f.getSurfacePoint(j, k);
                x = new Point2D(Math.min(x.getX(), p.getX()), Math.max(x.getY(), p.getX()));
                y = new Point2D(Math.min(y.getX(), p.getY()), Math.max(y.getY(), p.getY()));
                z = new Point2D(Math.min(z.getX(), p.getZ()), Math.max(z.getY(), p.getZ()));
            }
        }
    }
}