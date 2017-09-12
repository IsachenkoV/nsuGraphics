package ru.nsu.ccfit.g12201.isachenko.cg.model;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {

    public int figuresCount;
    private int n, m, k, a, b, c, d, zb, zf, sw, sh;
    private ArrayList <Figure> figures = new ArrayList<Figure>();


    private final Point3D camera = new Point3D(-10, 0, 0);
    private final Point3D cameraView = new Point3D(10, 0, 0);
    private final Point3D cameraUp = new Point3D(0, 1, 0);

    private BufferedImage image;

    private Matrix Mproj, Mcam, Mrotate;

    public Model(String filePath) throws IOException {
        Scanner scanner = new Scanner(Paths.get(filePath));
        n = scanner.nextInt();
        m = scanner.nextInt();
        k = scanner.nextInt();
        a = scanner.nextInt();
        b = scanner.nextInt();
        c = scanner.nextInt();
        d = scanner.nextInt();
        zb = scanner.nextInt();
        zf = scanner.nextInt();
        sw = scanner.nextInt();
        sh = scanner.nextInt();

        figuresCount = scanner.nextInt();
        for (int i = 0; i < figuresCount; i++)
        {
            Figure f = new Figure();
            f.scanFigure(scanner);
            figures.add(f);
        }
        image = new BufferedImage(500, 500, BufferedImage.TYPE_4BYTE_ABGR);

        initMatrices();
        initMproj();

        drawFigures();
    }

    private void initMatrices() {
        double[][] d = new double[4][4];
        d[0][2] = 1;
        d[1][1] = 1;
        d[2][0] = -1;
        d[2][3] = -10;
        d[3][3] = 1;

        Mcam = new Matrix(d);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (i == j)
                    d[i][j] = 1;
                else
                    d[i][j] = 0;
            }

        Mrotate = new Matrix(d);
    }

    private void initMproj() {
        double[][] d = new double[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                d[i][j] = 0;

        d[0][0] = 2.0 * zf / sw;
        d[1][1] = 2.0 * zf / sh;
        d[2][2] = zb / (zb - zf);
        d[2][3] = zf*zb / (zf - zb);
        d[3][2] = 1;

        Mproj = new Matrix(d);
    }

    private void initMrotate(double dx, double dy, double dz)
    {
        double d[][] = new double[4][4];

        Matrix Mx, My, Mz;

        zeroVector(d);
        d[0][0] = d[3][3] = 1;
        d[1][1] = Math.cos(dx);
        d[1][2] = -Math.sin(dx);
        d[2][1] = Math.sin(dx);
        d[2][2] = Math.cos(dx);
        Mx = new Matrix(d);

        zeroVector(d);
        d[0][0] = d[2][2] = Math.cos(dy);
        d[1][1] = d[3][3] = 1;
        d[0][2] = Math.sin(dy);
        d[2][0] = -Math.sin(dy);
        My = new Matrix(d);

        zeroVector(d);
        d[2][2] = d[3][3] = 1;
        d[0][0] = d[1][1] = Math.cos(dz);
        d[0][1] = -Math.sin(dz);
        d[1][0] = Math.sin(dz);
        Mz = new Matrix(d);

        Mrotate = Matrix.matrixProduct(Matrix.matrixProduct(Matrix.matrixProduct(Mx, My), Mz), Mrotate);
    }

    private void zeroVector(double[][] d)
    {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                d[i][j] = 0;
    }

    private void drawFigures() {
        Graphics g = image.createGraphics();
        g.fillRect(0, 0, 500, 500);
        g.setColor(Color.GREEN);
        g.drawRect(10, 10, 480, 480);

        g.setColor(Color.BLACK);

        for (int i = 0; i < figures.size(); i++)
        {
            BoundingBox b = new BoundingBox(figures.get(i), n*k, m*k);

            double[][] ar = new double[4][4];

            zeroVector(ar);
            ar[0][0] = 1.0 / (b.x.getY() - b.x.getX());
            ar[1][1] = 1.0 / (b.y.getY() - b.y.getX());
            ar[2][2] = 1.0 / (b.z.getY() - b.z.getX());
            ar[0][0] = 1.0;
            Matrix Mscale = new Matrix(ar);

            int partsCount = figures.get(i).getPartsCount();
            Point2D prev;
            for (double j = 0.0; j < partsCount; j += (partsCount + 0.0)/(n*k + 0.0))
            {
                prev = null;
                for (double k1 = 0.0; k1 < 2.0*Math.PI + 0.5; k1 += 2.0 * Math.PI / (m*k + 0.0))
                {
                    Point3D p = figures.get(i).getSurfacePoint(j, k1);
                    double[] d = new double[4];
                    double[] res;
                    d[0] = p.getX();
                    d[1] = p.getY();
                    d[2] = p.getZ();
                    d[3] = 1.0;
                    Mscale = Matrix.matrixProduct(Mscale, Mrotate);
                    Matrix op = Matrix.matrixProduct(Mcam, Mscale);
                    op = Matrix.matrixProduct(Mproj, op);
                    res = Matrix.matrixVectorProduct(op, d);

                    int x = (int) (240 + (res[0] * 240));
                    int y = (int) (240 + (res[1] * 240));

                    if (x >= 0 && x < 480 && y >= 0 && y < 480) {
                        if (prev != null)
                        {
                            g.drawLine((int)prev.getX(), (int)prev.getY(), x, y);
                        }
                        prev = new Point2D(x, y);

                        image.setRGB(x, y, Color.BLACK.getRGB());
                    }
                }
            }


            for (double k1 = 0.0; k1 < 2.0*Math.PI + 0.5; k1 += 2.0 * Math.PI / (m*k + 0.0))
            {
                prev = null;
                for (double j = 0.0; j < partsCount; j += (partsCount + 0.0)/(n*k + 0.0))
                {
                    Point3D p = figures.get(i).getSurfacePoint(j, k1);
                    double[] d = new double[4];
                    double[] res;
                    d[0] = p.getX();
                    d[1] = p.getY();
                    d[2] = p.getZ();
                    d[3] = 1.0;
                    Mscale = Matrix.matrixProduct(Mscale, Mrotate);
                    Matrix op = Matrix.matrixProduct(Mcam, Mscale);
                    op = Matrix.matrixProduct(Mproj, op);
                    res = Matrix.matrixVectorProduct(op, d);

                    int x = (int) (240 + (res[0] * 240));
                    int y = (int) (240 + (res[1] * 240));

                    if (x >= 0 && x < 480 && y >= 0 && y < 480) {
                        if (prev != null)
                        {
                            g.drawLine((int)prev.getX(), (int)prev.getY(), x, y);
                        }
                        prev = new Point2D(x, y);

                        image.setRGB(x, y, Color.BLACK.getRGB());
                    }
                }
            }

        }
    }

    public void setParams(int[] res) {
        n = res[0];
        m = res[1];
        k = res[2];
        a = res[3];
        b = res[4];
        c = res[5];
        d = res[6];
        zb = res[7];
        zf = res[8];
        sw = res[9];
        sh = res[10];

        initMproj();

        drawFigures();
    }

    public void addFigure(Figure figure) {
        figures.add(figure);
        drawFigures();
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void deletePrevModel() {
        if (figures.size() == 0)
            return;
        figures.remove(figures.size() - 1);
        drawFigures();
    }

    public void rotate(Point2D delta) {
        double dx, dy, dz = 0;
        dx = delta.getX() / zf;
        dy = delta.getY() / zf;
        initMrotate(dx, dy, dz);
        drawFigures();
    }
}
