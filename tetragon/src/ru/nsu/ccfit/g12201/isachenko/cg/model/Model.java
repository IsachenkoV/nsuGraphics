package ru.nsu.ccfit.g12201.isachenko.cg.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Владимир on 19.05.2015.
 */
public class Model {
    private final int mask = 0b11111111;
    private BufferedImage image = new BufferedImage(2048, 1024, BufferedImage.TYPE_4BYTE_ABGR);
    private Graphics graphics = image.createGraphics();
    private ArrayList<Tetragon> tetragons = new ArrayList<Tetragon>();
    private ArrayList<Point> lastAdded = new ArrayList<Point>();

    private BufferedImage[] mipMaps = new BufferedImage[11];
    private boolean isFilter;
    private BufferedImage miniImage;

    public Model(String imagePath) throws IOException {
/*        Point[] ps = new Point[4];
        ps[0] = new Point(10, 10);
        ps[1] = new Point(400, 10);
        ps[2] = new Point(210, 210);
        ps[3] = new Point(10, 400);
        tetragons.add(new Tetragon(ps));
*/
        isFilter = false;
        renderMipMaps(imagePath);
        redraw();
    }

    private void renderMipMaps(String imagePath) throws IOException {
        mipMaps[10] = ImageIO.read(new File(imagePath));
        for (int i = 9; i >= 1; i--)
        {
            int size = (1 << i);
            mipMaps[i] = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
            for (int x = 0; x < size; x++)
            {
                for (int y = 0; y < size; y++)
                {
                    int[] rgb = new int[4];
                    rgb[0] = mipMaps[i + 1].getRGB(2 * x, 2 * y);
                    rgb[1] = mipMaps[i + 1].getRGB(2 * x + 1, 2 * y);
                    rgb[2] = mipMaps[i + 1].getRGB(2 * x, 2 * y + 1);
                    rgb[3] = mipMaps[i + 1].getRGB(2 * x + 1, 2 * y + 1);

                    int a = 0, r = 0, g = 0, b = 0;
                    for (int k = 0; k < 4; k++)
                    {
                        a += mask & (rgb[k] >> 24);
                        r += mask & (rgb[k]) >> 16;
                        g += mask & (rgb[k]) >> 8;
                        b += mask & (rgb[k]);
                    }

                    int aRES = a / 4;
                    int rRES = r / 4;
                    int gRES = g / 4;
                    int bRES = b / 4;

                    int res = (aRES << 24) + (rRES << 16) + (gRES << 8) + bRES;
                    mipMaps[i].setRGB(x, y, res);
                }
            }
        }
    }

    public boolean addPointToCanvas(Point p) {
        if (lastAdded.size() == 3)
        {
            if (intersect(lastAdded.get(0), lastAdded.get(1), lastAdded.get(2), p) || intersect(lastAdded.get(0), p, lastAdded.get(1), lastAdded.get(2)))
                return false;
        }

        lastAdded.add(p);

        if (lastAdded.size() == 4)
        {
            Point[] ps = new Point[4];
            lastAdded.toArray(ps);
            tetragons.add(new Tetragon(ps));
            lastAdded.clear();
            redraw();
        }

        graphics.setColor(Color.RED);
        for (int i = 0; i < lastAdded.size() - 1; i++)
        {
            Point a = lastAdded.get(i);
            Point b = lastAdded.get(i + 1);
            graphics.drawLine(a.x, a.y, b.x, b.y);
        }

        graphics.setColor(Color.BLUE);
        for (Point p1: lastAdded)
        {
            graphics.fillOval(p1.x, p1.y, 6, 6);
        }

        return true;
    }

    private boolean intersectOnCoord(int a, int b, int c, int d) {
        int ax = Math.min(a, b);
        int bx = Math.max(a, b);
        int cx = Math.min(c, d);
        int dx = Math.max(c, d);
        return (Math.max(ax, cx) <= Math.min(bx, dx));
    }

    private boolean between(int a, int b, int c)
    {
        return (a <= c && c <= b) || (b <= c && c <= a);
    }

    private boolean intersect(Point a, Point b, Point c, Point d) {
        if (!intersectOnCoord(a.x, b.x, c.x, d.x) || !intersectOnCoord(a.y, b.y, c.y, d.y))
            return false;

        Line m = new Line(a, b);
        Line n = new Line(c, d);

        double det = m.a*n.b-m.b*n.a;
        Point res;

        if (Math.abs(det) < 1e-9) // ||
        {
            return false;
        } else
        {
            res = new Point( (int) ((n.c*m.b - m.c*n.b) / det), (int) ((n.a*m.c - m.a*n.c) / det));
        }

        return between(a.x, b.x, res.x) && between(a.y, b.y, res.y) && between(c.x, d.x, res.x) && between(c.y, d.y, res.y);
    }

    private boolean inside(Point p, Point a, Point b, Point c) {
        long triangleS = Math.abs(Point.vMul(Point.sub(b, a), Point.sub(b, c)));

        long res = Math.abs(Point.vMul(Point.sub(p, a), Point.sub(p, b))) +
                  Math.abs(Point.vMul(Point.sub(p, b), Point.sub(p, c))) +
                  Math.abs(Point.vMul(Point.sub(p, a), Point.sub(p, c)));

        return triangleS == res;
    }

    private void redraw() {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        for (Tetragon t: tetragons)
        {
            Point a, b, c, d;
            if (Math.signum(Point.vMul(Point.sub(t.ps[0], t.ps[1]), Point.sub(t.ps[2], t.ps[1]))) != Math.signum(Point.vMul(Point.sub(t.ps[0], t.ps[3]), Point.sub(t.ps[2], t.ps[3]))))
            {
                a = t.ps[1];
                b = t.ps[2];
                c = t.ps[0];
                d = t.ps[3];
            }
            else
            {
                a = t.ps[0];
                b = t.ps[1];
                c = t.ps[3];
                d = t.ps[2];
            }

            boolean isConvex = true;
            if (Math.signum(Point.vMul(Point.sub(t.ps[0], t.ps[1]), Point.sub(t.ps[2], t.ps[1]))) == Math.signum(Point.vMul(Point.sub(t.ps[0], t.ps[3]), Point.sub(t.ps[2], t.ps[3])))
                || Math.signum(Point.vMul(Point.sub(t.ps[1], t.ps[0]), Point.sub(t.ps[3], t.ps[0]))) == Math.signum(Point.vMul(Point.sub(t.ps[1], t.ps[2]), Point.sub(t.ps[3], t.ps[2]))))
            {
                isConvex = false;
            }

            long s = (Math.abs(Point.vMul(Point.sub(b, a), Point.sub(b, c))) + Math.abs(Point.vMul(Point.sub(b, d), Point.sub(b, c)))) / 2;

            if (isConvex) {
                SofLE system = new SofLE(t.ps);
                double coef[] = system.solve();
                Box box = new Box(t.ps);
                for (int i = box.minX; i <= box.maxX; i++) {
                    for (int j = box.minY; j <= box.maxY; j++) {
                        if (inside(new Point(i, j), a, b, c) || inside(new Point(i, j), b, c, d)) {
                            double u, v;
                            u = (coef[0] * i + coef[1] * j + coef[2]) / (coef[6] * i + coef[7] * j + coef[8]);
                            v = (coef[3] * i + coef[4] * j + coef[5]) / (coef[6] * i + coef[7] * j + coef[8]);

                            int level0 = 0, level1 = 0;
                            for (level1 = 1; level1 < 11; level1++)
                                if ((1 << level1) * (1 << level1) > s)
                                    break;
                            if (level1 > 10)
                                level1 = 10;
                            level0 = level1 - 1;

                            if (!isFilter) {
                                int size = (1 << level1);
                                int x = (int) (Math.abs(u) * size);
                                int y = (int) (Math.abs(v) * size);
                                if (x < size && y < size && x >= 0 && y >= 0)
                                    image.setRGB(i, j, mipMaps[level1].getRGB(x, y));
                            } else {
                                image.setRGB(i, j, filtration(level0, level1, u, v, s));
                            }
                        }
                    }
                }
            }
            else
            {
                { //FIRST
                    SofLE system = new SofLE(a, b, c, 1);
                    double[] coef = system.solve();

                    Box box = new Box(a, b, c);
                    for (int i = box.minX; i <= box.maxX; i++) {
                        for (int j = box.minY; j <= box.maxY; j++) {
                            if (inside(new Point(i, j), a, b, c)) {
                                double u, v;
                                u = (coef[0] * i + coef[1] * j + coef[2]) / (coef[6] * i + coef[7] * j + coef[8]);
                                v = (coef[3] * i + coef[4] * j + coef[5]) / (coef[6] * i + coef[7] * j + coef[8]);

                                int level0 = 0, level1 = 0;
                                for (level1 = 1; level1 < 11; level1++)
                                    if ((1 << level1) * (1 << level1) > s)
                                        break;
                                if (level1 > 10)
                                    level1 = 10;
                                level0 = level1 - 1;

                                if (!isFilter) {
                                    int size = (1 << level1);
                                    int x = (int) (Math.abs(u) * size);
                                    int y = (int) (Math.abs(v) * size);
                                    if (x < size && y < size && x >= 0 && y >= 0)
                                        image.setRGB(i, j, mipMaps[level1].getRGB(x, y));
                                } else {
                                    image.setRGB(i, j, filtration(level0, level1, u, v, s));
                                }
                            }
                        }
                    }
                }
                { //SECOND
                    SofLE system = new SofLE(d, b, c, 0);
                    double[] coef = system.solve();

                    Box box = new Box(d, b, c);
                    for (int i = box.minX; i <= box.maxX; i++) {
                        for (int j = box.minY; j <= box.maxY; j++) {
                            if (inside(new Point(i, j), b, c, d)) {
                                double u, v;
                                u = (coef[0] * i + coef[1] * j + coef[2]) / (coef[6] * i + coef[7] * j + coef[8]);
                                v = (coef[3] * i + coef[4] * j + coef[5]) / (coef[6] * i + coef[7] * j + coef[8]);

                                int level0 = 0, level1 = 0;
                                for (level1 = 1; level1 < 11; level1++)
                                    if ((1 << level1) * (1 << level1) > s)
                                        break;
                                if (level1 > 10)
                                    level1 = 10;
                                level0 = level1 - 1;

                                if (!isFilter) {
                                    int size = (1 << level1);
                                    int x = (int) (Math.abs(u) * size);
                                    int y = (int) (Math.abs(v) * size);
                                    if (x < size && y < size && x >= 0 && y >= 0)
                                        image.setRGB(i, j, mipMaps[level1].getRGB(x, y));
                                } else {
                                    image.setRGB(i, j, filtration(level0, level1, u, v, s));
                                }
                            }
                        }
                    }
                }
            }
            graphics.setColor(Color.RED);
            for (int i = 0; i < 4; i++)
            {
                graphics.drawLine(t.ps[i].x, t.ps[i].y, t.ps[(i + 1)%4].x, t.ps[(i + 1)%4].y);
            }
            graphics.setColor(Color.BLUE);
            for (Point p: t.ps)
            {
                graphics.fillOval(p.x, p.y, 6, 6);
            }
            graphics.setColor(Color.BLACK);
            int asd = 0;
            for (Point p: t.ps)
            {
                graphics.drawString(asd + "", p.x, p.y);
                asd++;
            }
        }
    }

    private int filtration(int level0, int level1, double u, double v, long s)
    {
        int a[] = bilinearFiltration(level0, u, v);
        int b[] = bilinearFiltration(level1, u, v);

        double delta = Math.log(Math.sqrt(s)) / Math.log(2.0);

        delta = delta - Math.floor(delta);

        int array[] = new int[4];
        for (int i = 0; i < 4; i++)
        {
            array[i] = (int) (a[i] * (1.0 - delta) + b[i]*delta);
        }

        return (array[0] << 24) + (array[1] << 16) + (array[2] << 8) + array[3];
    }

    private int[] bilinearFiltration(int level, double u, double v)
    {
        int size = (1 << level);
        double x = u * size;
        double y = v * size;
        double dx = x - Math.floor(x);
        double dy = y - Math.floor(y);

        int rgb[] = new int[4];
        if (Math.ceil(x) >= 0 && Math.ceil(x) < size && Math.floor(x) >= 0 && Math.floor(x) < size &&
                Math.ceil(y) >= 0 && Math.ceil(y) < size && Math.floor(y) >= 0 && Math.floor(y) < size)
        {
            rgb[0] = mipMaps[level].getRGB((int) (Math.floor(x)), (int) (Math.floor(y)));
            rgb[1] = mipMaps[level].getRGB((int) (Math.ceil(x)) , (int) (Math.floor(y)));
            rgb[2] = mipMaps[level].getRGB((int) (Math.floor(x)), (int) (Math.ceil(y)));
            rgb[3] = mipMaps[level].getRGB((int) (Math.ceil(x)) , (int) (Math.ceil(y)) );
        }

        int[] a = new int[4];
        int[] r = new int[4];
        int[] g = new int[4];
        int[] b = new int[4];

        for (int k = 0; k < 4; k++)
        {
            a[k] = mask & (rgb[k] >> 24);
            r[k] = mask & (rgb[k]) >> 16;
            g[k] = mask & (rgb[k]) >> 8;
            b[k] = mask & (rgb[k]);
        }

        int res[] = new int[4];
        res[0] = (int) (a[0] * (1 - dx) * (1 - dy) + a[1] * dx * (1 - dy) + a[2] * (1 - dx) * dy + a[3] * dx * dy);
        res[1] = (int) (r[0] * (1 - dx) * (1 - dy) + r[1] * dx * (1 - dy) + r[2] * (1 - dx) * dy + r[3] * dx * dy);
        res[2] = (int) (g[0] * (1 - dx) * (1 - dy) + g[1] * dx * (1 - dy) + g[2] * (1 - dx) * dy + g[3] * dx * dy);
        res[3] = (int) (b[0] * (1 - dx) * (1 - dy) + b[1] * dx * (1 - dy) + b[2] * (1 - dx) * dy + b[3] * dx * dy);
        return res;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void addConvexTetragon(Point canvasCurrentSize) {
        Point ps[] = new Point[4];

        ps[0] = Point.getRandomPoint(canvasCurrentSize.x, canvasCurrentSize.y);
        ps[1] = Point.getRandomPoint(canvasCurrentSize.x, canvasCurrentSize.y);
        ps[2] = Point.getRandomPoint(canvasCurrentSize.x, canvasCurrentSize.y);

        while (true)
        {
            ps[3] = Point.getRandomPoint(canvasCurrentSize.x, canvasCurrentSize.y);
            if (!inside(ps[3], ps[0], ps[1], ps[2]) && !intersect(ps[0], ps[1], ps[2], ps[3]) && !intersect(ps[0], ps[3], ps[1], ps[2])) {
                if (Point.vMul(Point.sub(ps[1], ps[0]), Point.sub(ps[1], ps[3])) * Point.vMul(Point.sub(ps[1], ps[0]), Point.sub(ps[1], ps[2])) > 0 &&
                        Point.vMul(Point.sub(ps[2], ps[1]), Point.sub(ps[2], ps[0])) * Point.vMul(Point.sub(ps[2], ps[1]), Point.sub(ps[2], ps[3])) > 0 &&
                        Point.vMul(Point.sub(ps[3], ps[2]), Point.sub(ps[3], ps[0])) * Point.vMul(Point.sub(ps[3], ps[2]), Point.sub(ps[3], ps[1])) > 0 &&
                        Point.vMul(Point.sub(ps[0], ps[3]), Point.sub(ps[0], ps[1])) * Point.vMul(Point.sub(ps[0], ps[3]), Point.sub(ps[0], ps[2])) > 0) {
                    break;
                }
            }
        }

        tetragons.add(new Tetragon(ps));
        redraw();
    }

    public void addNonconvexTetragon(Point canvasCurrentSize) {
        Point ps[] = new Point[4];

        ps[0] = Point.getRandomPoint(canvasCurrentSize.x, canvasCurrentSize.y);
        ps[1] = Point.getRandomPoint(canvasCurrentSize.x, canvasCurrentSize.y);
        ps[2] = Point.getRandomPoint(canvasCurrentSize.x, canvasCurrentSize.y);

        while (true)
        {
            ps[3] = Point.getRandomPoint(canvasCurrentSize.x, canvasCurrentSize.y);
            if (inside(ps[3], ps[0], ps[1], ps[2]))
                break;
        }

        tetragons.add(new Tetragon(ps));
        redraw();
    }

    public void clearAll() {
        tetragons.clear();
        lastAdded.clear();
        redraw();
    }

    public void filterOn() {
        isFilter = !isFilter;
        redraw();
    }

    public BufferedImage getMiniImage(Point curSize) {
        int w, a;
        if (curSize == null)
        {
            a = miniImage.getWidth();
        }
        else {
            w = curSize.x - 16;
            a = (int) ((w / 4.56)) - 20;
        }

        long s = a * a;
        int level0 = 0, level1 = 0;
        for (level1 = 1; level1 < 11; level1++)
            if ( (1 << level1)*(1 << level1) > s)
                break;
        if (level1 > 10)
            level1 = 10;
        level0 = level1 - 1;

        miniImage = new BufferedImage(a, a, BufferedImage.TYPE_4BYTE_ABGR);

        for (int i = 0; i < a; i++)
        {
            for (int j = 0; j < a; j++)
            {
                double u = i / (a - 1.0);
                double v = j / (a - 1.0);
                if (!isFilter)
                {
                    int size = (1 << level1);
                    int x = (int)((u)*size);
                    int y = (int)((v)*size);
                    if (x < size && y < size && x >= 0 && y >= 0)
                        miniImage.setRGB(i, j, mipMaps[level1].getRGB(x, y));
                }
                else
                {
                    miniImage.setRGB(i, j, filtration(level0, level1, u, v, s));
                }
            }
        }

        return miniImage;
    }

    public BufferedImage getInitialMiniImage() {
        return mipMaps[7];
    }
}
