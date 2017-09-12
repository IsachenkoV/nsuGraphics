package ru.nsu.ccfit.g12201.isachenko.cg.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.geometry.Point2D;

public class Model {

    public BufferedImage canvas, legend;
    private Graphics2D gCanvas, gLegend;

    private Point canvasInitialSize = new Point(400, 400);
    private Point legendInitialSize = new Point(80, 400);
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Double> userIsolines = new ArrayList<>();
    private ArrayList <Point2D> ans = new ArrayList<>();

    private final int mask = 0b11111111;

    private Function f = new Function(0, 400, 0, 400);
    private int n, k, m, isolineColor;
    public boolean isGridEnabled = false, isIsolinesEnabled = false, isInterpolationEnabled = false, isDitheringEnabled = false;

    public Model(String filePath) throws IOException {
        Scanner scanner = new Scanner(Paths.get(filePath));
        k = scanner.nextInt();
        m = scanner.nextInt();
        n = scanner.nextInt();
        for (int i = 0; i <= n; i++) {
            int r = scanner.nextInt();
            int g = scanner.nextInt();
            int b = scanner.nextInt();
            int a = 255;

            colors.add(((a << 24) + (b << 16) + (g << 8) + r));
        }

        int r = scanner.nextInt();
        int g = scanner.nextInt();
        int b = scanner.nextInt();
        int a = 255;
        isolineColor = ((a << 24) + (b << 16) + (g << 8) + r);

        renderLegend(legendInitialSize);
        renderCanvas(canvasInitialSize);
        isGridEnabled = false;
    }

    public void renderLegend(Point sz) {
        legend = new BufferedImage(sz.x, sz.y, BufferedImage.TYPE_4BYTE_ABGR);
        for (int k = 0; k <= n; k++) {
            int start = k * (sz.y / (n + 1));
            int end  = (k + 1) * (sz.y / (n + 1));
            for (int i = 0; i < sz.x; i++) {
                for (int j = start; j < end; j++) {
                    legend.setRGB(i, j, colors.get(k));
                }
            }
        }

        if (isInterpolationEnabled || isDitheringEnabled)
        {
            int d = (sz.y / (n + 1));
            for (int k = 0; k < n; k++) {
                int start = k * d;
                int end  = (k + 1) * d;
                for (int i = 0; i < sz.x; i++) {
                    for (int j = start; j < end; j++) {
                        double u = j, u1 = start, u2 = end + 1.0;
                        int c1 = colors.get(k), c2 = colors.get(k + 1);
                        int     b1 = mask & (c1) >> 16,
                                g1 = mask & (c1) >> 8,
                                r1 = mask & (c1);

                        int     b2 = mask & (c2) >> 16,
                                g2 = mask & (c2) >> 8,
                                r2 = mask & (c2);

                        int r = (int) (r1 * (u2 - u) / (u2 - u1) + r2 * (u - u1) / (u2 - u1));
                        int g = (int) (g1 * (u2 - u) / (u2 - u1) + g2 * (u - u1) / (u2 - u1));
                        int b = (int) (b1 * (u2 - u) / (u2 - u1) + b2 * (u - u1) / (u2 - u1));

                        legend.setRGB(i, j, (255 << 24) + (b << 16) + (g << 8) + r);
                    }
                }
            }
        }

        gLegend = legend.createGraphics();
        double delta = (f.fMax - f.fMin) / (n + 1.0);

        for (int k = 1; k <= n; k++) {
            int py  = k * (sz.y / (n + 1));
            double value = f.fMin + delta * k;
            gLegend.drawString(Double.toString(BigDecimal.valueOf(value).setScale(5,BigDecimal.ROUND_HALF_DOWN).doubleValue()), 0, py);
        }
    }

    public void renderCanvas(Point sz) {
        canvas = new BufferedImage(sz.x, sz.y, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < sz.x; i++)
        {
            for (int j = 0; j < sz.y; j++)
            {
                double t = f.getValue(i, j, sz);

                double delta = (f.fMax - f.fMin) / (n + 1.0);
                int k = 0;
                double thr = f.fMin + k * delta;
                while (t >= thr)
                {
                    k++;
                    thr = f.fMin + k * delta;
                }

                if (k == colors.size() + 1)
                    k--;

                if (k == 0)
                    k++;
                canvas.setRGB(i, j, colors.get(k - 1));
            }
        }

        gCanvas = canvas.createGraphics();

        if (isDitheringEnabled)
            drawDithering();

        if (isInterpolationEnabled)
            drawInterpolation();

        if (isIsolinesEnabled)
            drawIsolines();

        if (isGridEnabled)
            drawGrid();
    }

    public double getFunctionValue(int x, int y) {
        if (x < 0 || y < 0 || x >= canvas.getWidth() || y >= canvas.getHeight())
            return 0;
        return f.getValue(x, y, new Point(canvas.getWidth(), canvas.getHeight()));
    }

    public void setFunctionParams(int[] array) {
        f = new Function(array[0], array[1], array[2], array[3]);
        k = array[4];
        m = array[5];
        renderCanvas(new Point(canvas.getWidth(), canvas.getHeight()));
        renderLegend(new Point(legend.getWidth(), legend.getHeight()));
    }

    public void drawGrid() {
        gCanvas.setColor(Color.WHITE);
        int cW = canvas.getWidth();
        int cH = canvas.getHeight();

        double dx = f.sz.x / (k - 1.0);
        double dy = f.sz.y / (m - 1.0);

        for (int i = 0; i < k; i++)
        {
            double x = f.a + i * dx;
            int x1 = (int) (((x - f.a) * cW) / (f.sz.x + 0.0));
            if (x1 < 0)
                x1 = 0;
            if (x1 >= cW)
                x1 = cW - 1;

            gCanvas.drawLine(x1, 0, x1, cH - 1);
        }

        for (int i = 0; i < m; i++)
        {
            double y = f.c + i * dy;
            int y1 = (int) (((y - f.c) * cH) / (f.sz.y + 0.0));
            if (y1 < 0)
                y1 = 0;
            if (y1 >= cH)
                y1 = cH - 1;

            gCanvas.drawLine(0, y1, cW - 1, y1);
        }
    }

    private void drawIsolines() {
        gCanvas.setColor(new Color(isolineColor));
        int cW = canvas.getWidth();
        int cH = canvas.getHeight();

        double dx = f.sz.x / (k - 1.0);
        double dy = f.sz.y / (m - 1.0);
        double delta = (f.fMax - f.fMin) / (n + 1.0);
        ArrayList <Double> zz = new ArrayList<>(userIsolines);
        for (int s = 1; s <= n; s++) {
            double value = f.fMin + delta * s;
            zz.add(value);
        }

        for (Double value: zz)
        {
            for (int i = 0; i < k - 1; i++) {
                for (int j = 0; j < m - 1; j++) {
                    ans.clear();

                    getAns(i, j, value, dx, dy);

                    if (ans.size() == 2 || ans.size() == 3)
                    {
                        Point2D p1 = ans.get(0), p2 = ans.get(1);
                        int xA1 = (int) (((p1.getX() - f.a) * cW) / (f.sz.x + 0.0));
                        int yA1 = (int) (((p1.getY() - f.c) * cH) / (f.sz.y + 0.0));
                        int xA2 = (int) (((p2.getX() - f.a) * cW) / (f.sz.x + 0.0));
                        int yA2 = (int) (((p2.getY() - f.c) * cH) / (f.sz.y + 0.0));
                        gCanvas.drawLine(xA1, yA1, xA2, yA2);
                    }

                    if (ans.size() == 4)
                    {
                        double x0 = f.a + i * dx;
                        double y0 = f.c + j * dy;
                        Point2D p0 = new Point2D(x0, y0), pCenter = new Point2D(x0 + dx/2.0, y0 + dy/2.0);
                        double val0 = f.getValue(p0), valCenter = f.getValue(pCenter);

                        boolean sign1 = val0 < value, sign2 = valCenter < value;

                        if (sign1 == sign2)
                        {
                            Point2D p1 = ans.get(0), p2 = ans.get(1);
                            int xA1 = (int) (((p1.getX() - f.a) * cW) / (f.sz.x + 0.0));
                            int yA1 = (int) (((p1.getY() - f.c) * cH) / (f.sz.y + 0.0));
                            int xA2 = (int) (((p2.getX() - f.a) * cW) / (f.sz.x + 0.0));
                            int yA2 = (int) (((p2.getY() - f.c) * cH) / (f.sz.y + 0.0));
                            gCanvas.drawLine(xA1, yA1, xA2, yA2);

                            p1 = ans.get(2); p2 = ans.get(3);
                            xA1 = (int) (((p1.getX() - f.a) * cW) / (f.sz.x + 0.0));
                            yA1 = (int) (((p1.getY() - f.c) * cH) / (f.sz.y + 0.0));
                            xA2 = (int) (((p2.getX() - f.a) * cW) / (f.sz.x + 0.0));
                            yA2 = (int) (((p2.getY() - f.c) * cH) / (f.sz.y + 0.0));
                            gCanvas.drawLine(xA1, yA1, xA2, yA2);
                        }
                        else
                        {
                            Point2D p1 = ans.get(0), p2 = ans.get(3);
                            int xA1 = (int) (((p1.getX() - f.a) * cW) / (f.sz.x + 0.0));
                            int yA1 = (int) (((p1.getY() - f.c) * cH) / (f.sz.y + 0.0));
                            int xA2 = (int) (((p2.getX() - f.a) * cW) / (f.sz.x + 0.0));
                            int yA2 = (int) (((p2.getY() - f.c) * cH) / (f.sz.y + 0.0));
                            gCanvas.drawLine(xA1, yA1, xA2, yA2);

                            p1 = ans.get(1); p2 = ans.get(2);
                            xA1 = (int) (((p1.getX() - f.a) * cW) / (f.sz.x + 0.0));
                            yA1 = (int) (((p1.getY() - f.c) * cH) / (f.sz.y + 0.0));
                            xA2 = (int) (((p2.getX() - f.a) * cW) / (f.sz.x + 0.0));
                            yA2 = (int) (((p2.getY() - f.c) * cH) / (f.sz.y + 0.0));
                            gCanvas.drawLine(xA1, yA1, xA2, yA2);
                        }
                    }
                }
            }
        }
    }

    private void getAns(int i, int j, double value, double dx, double dy)  {
        double x0 = f.a + i * dx;
        double y0 = f.c + j * dy;

        double x1 = x0 + dx;
        double y1 = y0 + dy;

        Point2D ps[] = new Point2D[4];
        ps[0] = new Point2D(x0, y0);
        ps[1] = new Point2D(x1, y0);
        ps[2] = new Point2D(x1, y1);
        ps[3] = new Point2D(x0, y1);

        double f1 = f.getValue(ps[0]), f2 = f.getValue(ps[1]);
        double fMin = Math.min(f1, f2), fMax = Math.max(f1, f2);
        if (!(fMin > value || fMax < value))
        {
            double x = dx * (value - fMin) / (fMax - fMin);
            if (f1 < f2)
            {
                ans.add(new Point2D(ps[0].getX() + x, ps[0].getY()));
            }
            else
            {
                ans.add(new Point2D(ps[1].getX() - x, ps[0].getY()));
            }
        }

        f1 = f.getValue(ps[1]);  f2 = f.getValue(ps[2]);
        fMin = Math.min(f1, f2); fMax = Math.max(f1, f2);
        if (!(fMin > value || fMax < value))
        {
            double y = dy * (value - fMin) / (fMax - fMin);
            if (f1 < f2)
            {
                ans.add(new Point2D(ps[1].getX(), ps[1].getY() + y));
            }
            else
            {
                ans.add(new Point2D(ps[2].getX(), ps[2].getY() - y));
            }
        }

        f1 = f.getValue(ps[3]);  f2 = f.getValue(ps[2]);
        fMin = Math.min(f1, f2); fMax = Math.max(f1, f2);
        if (!(fMin > value || fMax < value))
        {
            double x = dx * (value - fMin) / (fMax - fMin);
            if (f1 < f2)
            {
                ans.add(new Point2D(ps[3].getX() + x, ps[3].getY()));
            }
            else
            {
                ans.add(new Point2D(ps[2].getX() - x, ps[2].getY()));
            }
        }

        f1 = f.getValue(ps[0]);  f2 = f.getValue(ps[3]);
        fMin = Math.min(f1, f2); fMax = Math.max(f1, f2);
        if (!(fMin > value || fMax < value))
        {
            double y = dy * (value - fMin) / (fMax - fMin);
            if (f1 < f2)
            {
                ans.add(new Point2D(ps[0].getX(), ps[0].getY() + y));
            }
            else
            {
                ans.add(new Point2D(ps[3].getX(), ps[3].getY() - y));
            }
        }
    }

    private void drawInterpolation() {
        int cW = canvas.getWidth();
        int cH = canvas.getHeight();
        Point sz = new Point(cW, cH);

        for (int i = 0; i < cW; i++)
        {
            for (int j = 0; j < cH; j++)
            {
                double t = f.getValue(i, j, sz);

                double delta = (f.fMax - f.fMin) / (n + 1.0);
                int k = 0;
                double thr = f.fMin + k * delta;
                while (t >= thr)
                {
                    k++;
                    thr = f.fMin + k * delta;
                }

                // value greater than maximum
                if (k == colors.size() + 1)
                {
                    k--;
                    thr -= delta;
                }
                // value lesser than minimum
                if (k == 0)
                {
                    k++;
                    thr += delta;
                }

                double u2 = thr, u1 = thr - delta;
                int c1, c2;

                // using all colors in interpolation
                k--;
                if (k < colors.size() - 2) {
                    c1 = colors.get(k);
                    c2 = colors.get(k + 1);
                }
                else
                {
                    u1 = f.fMax - delta * 2;
                    u2 = f.fMax;
                    c1 = colors.get(colors.size() - 2);
                    c2 = colors.get(colors.size() - 1);
                }

                /*
                // without last color
                   if (k == colors.size()) {
                    continue;
                    }
                    c1 = colors.get(k);
                    c2 = colors.get(k + 1);
                 */

                int     b1 = mask & (c1) >> 16,
                        g1 = mask & (c1) >> 8,
                        r1 = mask & (c1);

                int     b2 = mask & (c2) >> 16,
                        g2 = mask & (c2) >> 8,
                        r2 = mask & (c2);

                int r = (int) (r1 * (u2 - t) / (u2 - u1) + r2 * (t - u1) / (u2 - u1));
                int g = (int) (g1 * (u2 - t) / (u2 - u1) + g2 * (t - u1) / (u2 - u1));
                int b = (int) (b1 * (u2 - t) / (u2 - u1) + b2 * (t - u1) / (u2 - u1));

                canvas.setRGB(i, j, (255 << 24) + (b << 16) + (g << 8) + r);
            }
        }
    }

    private void drawDithering() {
        drawInterpolation();
        int cW = canvas.getWidth();
        int cH = canvas.getHeight();

        for (int i = 0; i < cW; i++)
        {
            for (int j = 0; j < cH; j++)
            {
                int oldPixel = canvas.getRGB(i, j);
                int newPixel = getClosestPaletteColor(oldPixel);

                canvas.setRGB(i, j, newPixel);

                int     b1 = mask & (oldPixel) >> 16,
                        g1 = mask & (oldPixel) >> 8,
                        r1 = mask & (oldPixel);
                int     b2 = mask & (newPixel) >> 16,
                        g2 = mask & (newPixel) >> 8,
                        r2 = mask & (newPixel);
                int     errorB = b1 - b2,
                        errorG = g1 - g2,
                        errorR = r1 - r2;

                if (i < cW - 1)
                {
                    int cur = canvas.getRGB(i + 1, j);

                    int cls[] = new int[3];
                    cls[0] = mask & (cur) >> 16;
                    cls[1] = mask & (cur) >> 8;
                    cls[2] = mask & (cur);

                    cls[0] += errorB * 7 / 16;
                    cls[1] += errorG * 7 / 16;
                    cls[2] += errorR * 7 / 16;

                    for (int t = 0; t < 3; t++)
                    {
                        if (cls[t] < 0)
                            cls[t] = 0;
                        if (cls[t] > 255)
                            cls[t] = 255;
                    }
                    canvas.setRGB(i + 1, j, ((255 << 24) + (cls[0] << 16) + (cls[1] << 8) + cls[2]));
                }

                if (i > 0 && j < cH - 1)
                {
                    int cur = canvas.getRGB(i - 1, j + 1);

                    int cls[] = new int[3];
                    cls[0] = mask & (cur) >> 16;
                    cls[1] = mask & (cur) >> 8;
                    cls[2] = mask & (cur);

                    cls[0] += errorB * 3 / 16;
                    cls[1] += errorG * 3 / 16;
                    cls[2] += errorR * 3 / 16;

                    for (int t = 0; t < 3; t++)
                    {
                        if (cls[t] < 0)
                            cls[t] = 0;
                        if (cls[t] > 255)
                            cls[t] = 255;
                    }
                    canvas.setRGB(i - 1, j + 1, ((255 << 24) + (cls[0] << 16) + (cls[1] << 8) + cls[2]));
                }

                if (j < cH - 1)
                {
                    int cur = canvas.getRGB(i, j + 1);

                    int cls[] = new int[3];
                    cls[0] = mask & (cur) >> 16;
                    cls[1] = mask & (cur) >> 8;
                    cls[2] = mask & (cur);

                    cls[0] += errorB * 5 / 16;
                    cls[1] += errorG * 5 / 16;
                    cls[2] += errorR * 5 / 16;

                    for (int t = 0; t < 3; t++)
                    {
                        if (cls[t] < 0)
                            cls[t] = 0;
                        if (cls[t] > 255)
                            cls[t] = 255;
                    }
                    canvas.setRGB(i, j + 1, ((255 << 24) + (cls[0] << 16) + (cls[1] << 8) + cls[2]));
                }

                if (i < cW - 1 && j < cH - 1)
                {
                    int cur = canvas.getRGB(i + 1, j + 1);

                    int cls[] = new int[3];
                    cls[0] = mask & (cur) >> 16;
                    cls[1] = mask & (cur) >> 8;
                    cls[2] = mask & (cur);

                    cls[0] += errorB / 16;
                    cls[1] += errorG / 16;
                    cls[2] += errorR / 16;

                    for (int t = 0; t < 3; t++)
                    {
                        if (cls[t] < 0)
                            cls[t] = 0;
                        if (cls[t] > 255)
                            cls[t] = 255;
                    }
                    canvas.setRGB(i + 1, j + 1, ((255 << 24) + (cls[0] << 16) + (cls[1] << 8) + cls[2]));
                }
            }
        }
    }

    private int getClosestPaletteColor(int oldPixel) {
        int     b1 = mask & (oldPixel) >> 16,
                g1 = mask & (oldPixel) >> 8,
                r1 = mask & (oldPixel);

        int index = -1, minError = 255*255*3 + 1;
        for (int i = 0; i < colors.size(); i++) {
            int c2 = colors.get(i);
            int b2 = mask & (c2) >> 16,
                g2 = mask & (c2) >> 8,
                r2 = mask & (c2);

            int sum = (b1 - b2)*(b1 - b2) + (r1 - r2)*(r1 - r2) + (g1 - g2)*(g1 - g2);
            if (sum < minError)
            {
                index = i;
                minError = sum;
            }
        }
        return colors.get(index);
    }

    public void addIsoline(double v) {
        userIsolines.add(v);
        renderCanvas(new Point(canvas.getWidth(), canvas.getHeight()));
    }

    public void clearUserIsolines() {
        userIsolines.clear();
    }
}
