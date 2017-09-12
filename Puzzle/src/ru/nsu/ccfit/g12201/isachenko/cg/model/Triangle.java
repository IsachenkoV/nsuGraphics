package ru.nsu.ccfit.g12201.isachenko.cg.model;

import java.awt.image.BufferedImage;

/**
 * Created by Владимир on 25.02.2015.
 */
public class Triangle {
    private int opaquePixels = 0, totalPixels = 0, edgePixels = 0;
    private final double sn = Math.sin(Math.toRadians(90));
    private final double tg45 = Math.tan(Math.toRadians(45));
    private Point[] positionOnRI = new Point[3];
    public Path path;

    Triangle(Point a, Point b, Point c)
    {
        positionOnRI[0] = a;
        positionOnRI[1] = b;
        positionOnRI[2] = c;
    }

    void updatePosOnRI(Point a, Point b, Point c)
    {
        positionOnRI[0] = a;
        positionOnRI[1] = b;
        positionOnRI[2] = c;
    }

    void updatePath(Point canvasSize)
    {
        path.update(canvasSize);
    }

    void draw(BufferedImage canvas, BufferedImage RI, int time)
    {
        Point curPosition = path.getCurPosition(new Point(canvas.getWidth(), canvas.getHeight()), time);
        int sz1 = Math.abs(positionOnRI[1].x - positionOnRI[0].x);
        int sz2 = sz1;
        double phi = path.getCurPhi(new Point(canvas.getWidth(), canvas.getHeight()), time);

        int deg = ((int) Math.toDegrees(phi)) % 360;
        int k = deg / 90;
        deg %= 90;
        double alp = Math.toRadians(deg);
        double tg = Math.tan(alp / 2.0);

        if (positionOnRI[1].x > positionOnRI[0].x) {
            for (int i = 0; i <= sz1; i++) {
                for (int j = 0; j <= sz2; j++) {
                    int x = i;
                    int y = j;
//                    x = (int) (Math.cos(phi) * i + Math.sin(phi) * j);
//                    y = (int) (Math.cos(phi) * j - Math.sin(phi) * i);

                    x = (int)(i - tg*j);
                    y = j;
                    y = (int)(Math.sin(alp) * x + y);
                    x = (int)(x - tg * y);

                    for (int c = 0; c < k; c++)
                    {
                        x =  x - (int)(tg45*y);
                        y = (int) (sn * x + y);
                        x =  x - (int)(tg45 * y);
                    }

                    int xsrc = i + positionOnRI[0].x;
                    int ysrc = j + positionOnRI[0].y;
                    int xd = x + curPosition.x;
                    int yd = y + curPosition.y;
                    canvas.setRGB(xd, yd, RI.getRGB(xsrc, ysrc));
                    totalPixels++;
                    if ((RI.getRGB(xsrc, ysrc)>>24) > 0)
                        opaquePixels++;
                }
                sz2--;
            }
        } else
        {
            for (int j = 0; j <= sz1; j++) {
                for (int i = 0; i <= sz2; i++) {
                    int x = i;
                    int y = j;
//                    x = (int) (Math.cos(phi) * i + Math.sin(phi) * j);
//                    y = (int) (Math.cos(phi) * j - Math.sin(phi) * i);

                    x = (int)(i - tg*j);
                    y = j;
                    y = (int)(Math.sin(alp) * x + y);
                    x = (int)(x - tg * y);

                    for (int c = 0; c < k; c++)
                    {
                        x =  x - (int)(tg45*y);
                        y = (int) (sn * x + y);
                        x =  x - (int)(tg45 * y);
                    }
                    int xsrc = positionOnRI[0].x - i;
                    int ysrc = positionOnRI[0].y - j;
                    int xd = curPosition.x - x;
                    int yd = curPosition.y - y;

                    canvas.setRGB(xd, yd, RI.getRGB(xsrc, ysrc));
                    totalPixels++;
                    if ((RI.getRGB(xsrc, ysrc)>>24) > 0)
                        opaquePixels++;
                }
                sz2--;
            }
        }
    }
}
