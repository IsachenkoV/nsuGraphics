package ru.nsu.ccfit.g12201.isachenko.cg.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Владимир on 02.03.2015.
 */
public class Model {

    private final int black = Color.BLACK.getRGB();
    private final int mask = 0b11111111;

    private final int imageInitialSize;
    private final BufferedImage initialImage;
    public BufferedImage renderedImage;
    public BufferedImage canvas = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);

    public boolean blendIsOn = false;
    public boolean filterIsOn = false;
    public Triangle[] tArray = new Triangle[32];

    public Model(String imagePath) throws IOException {
        initialImage = ImageIO.read(new File(imagePath));
        imageInitialSize = initialImage.getWidth();
        renderImage(new Point(imageInitialSize * 9 / 4, imageInitialSize * 9 / 4), 0);
    }

    public void renderImage(Point canvasSize, int t) {
        int size = Math.min(canvasSize.x, canvasSize.y) / 9 * 4;
        renderedImage = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);

        double div = (double) (imageInitialSize - 1) / (double) size;

        if (filterIsOn)
        {
            for (int i = 0; i < size - 1; i++)
            {
                for (int j = 0; j < size - 1; j++)
                {
                    int rgb[] = new int[4];
                    double dx = i * div - Math.floor(i * div);
                    double dy = j * div - Math.floor(j * div);

                    rgb[0] = initialImage.getRGB((int) (Math.floor(i * div)), (int) (Math.floor(j * div)));
                    rgb[1] = initialImage.getRGB((int) (Math.ceil(i * div)), (int) (Math.floor(j * div)));
                    rgb[2] = initialImage.getRGB((int) (Math.floor(i * div)), (int) (Math.ceil(j * div)));
                    rgb[3] = initialImage.getRGB((int) (Math.ceil(i * div)), (int) (Math.ceil(j * div)));

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

                    int aRES = (int) (a[0] * (1 - dx) * (1 - dy) + a[1] * dx * (1 - dy) + a[2] * (1 - dx) * dy + a[3] * dx * dy);
                    int rRES = (int) (r[0] * (1 - dx) * (1 - dy) + r[1] * dx * (1 - dy) + r[2] * (1 - dx) * dy + r[3] * dx * dy);
                    int gRES = (int) (g[0] * (1 - dx) * (1 - dy) + g[1] * dx * (1 - dy) + g[2] * (1 - dx) * dy + g[3] * dx * dy);
                    int bRES = (int) (b[0] * (1 - dx) * (1 - dy) + b[1] * dx * (1 - dy) + b[2] * (1 - dx) * dy + b[3] * dx * dy);

                    int res = (aRES << 24) + (rRES << 16) + (gRES << 8) + bRES;
                    renderedImage.setRGB(i, j, (res));
                }
            }
        }
        else
        {
            for (int i = 0; i < size; i++)
            {
                for (int j = 0; j < size; j++)
                {
                    renderedImage.setRGB(i, j, initialImage.getRGB( (int)(i * div),(int) (j * div) ) );
                }
            }
        }

        if (blendIsOn)
        {
            changeAlpha(size);
        }

        int c = 0;
        int step = size / 4;
        for (int x = 0; x < size - 1; x+= step)
        {
            for (int y = 0; y < size - 1; y += step)
            {
                if (tArray[c] == null) {
                    tArray[c] = new Triangle(new Point(x, y), new Point(x + step - 1, y), new Point(x, y + step - 1));
                    tArray[c].path = new Path(canvasSize, c);
                }
                else
                    tArray[c].updatePosOnRI(new Point(x, y),  new Point(x + step - 1, y), new Point(x, y + step - 1));
                //System.out.println(x + " " + y + " " + (x + step - 1) + " " + y + " " + x + " " +(y + step - 1));
                bresenham(x, y, x, y + step - 1);
                bresenham(x, y + step - 1, x + step - 1, y);
                bresenham(x + step - 1, y, x, y);
                c++;
                if (tArray[c] == null) {
                    tArray[c] = new Triangle(new Point(x + step - 1, y + step - 1), new Point(x, y + step - 1), new Point(x + step - 1, y));
                    tArray[c].path = new Path(canvasSize, c);
                } else
                    tArray[c].updatePosOnRI(new Point(x + step - 1, y + step - 1), new Point(x, y + step - 1), new Point(x + step - 1, y));
                //System.out.println((x + step - 1) + " " + (y + step - 1) +" " + (x) + " " + (y + step - 1) + " " + (x + step - 1) +" " + (y));
                bresenham(x, y + step - 1, x + step - 1, y);
                bresenham(x + step - 1, y, x + step - 1, y + step - 1);
                bresenham(x + step - 1, y + step - 1, x, y + step - 1);
                c++;
            }
        }

        renderCanvas(canvasSize, t);
    }

    private void changeAlpha(int size)
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                int srgb = renderedImage.getRGB(i, j);
                int drgb = renderedImage.getRGB(i, j);

                int[][] ar = new int[2][4]; // ar[][0] - A, ar[][1] - R, ar[][2] - G, ar[][3] - B
                for (int k = 0; k < 4; k++)
                {
                    ar[0][k] = mask & (srgb >> (32 - 8 * (k+1)));
                    ar[1][k] = mask & (drgb >> (32 - 8 * (k+1)));
                }

                double srca = ar[0][0] / 255.0;
                int res = 0b11111111000000000000000000000000;

                for (int k = 1; k < 4; k++)
                {
                    res += ((int) ((1.0 - srca) * ar[1][k] + srca * ar[0][k])) << (32 - 8 * (k+1));
                }

                renderedImage.setRGB(i, j, res);
            }
        }
    }

    void bresenham(int x0, int y0, int x1, int y1)
    {
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep)
        {
            int r = x0;
            x0 = y0;
            y0 = r;
            r = x1;
            x1 = y1;
            y1 = r;
        }

        if (x0 > x1)
        {
            int r = x0;
            x0 = x1;
            x1 = r;
            r = y0;
            y0 = y1;
            y1 = r;
        }

        int dx = x1 - x0;
        int dy = Math.abs(y1 - y0);
        int error = dx / 2;
        int ystep = (y0 < y1) ? 1 : -1;
        int y = y0;
        for (int x = x0; x <= x1; x++)
        {
            renderedImage.setRGB(steep ? y : x, steep ? x : y, black);
            error -= dy;
            if (error < 0)
            {
                y += ystep;
                error += dx;
            }
        }
    }

    public void renderCanvas(Point size, int t)
    {
        if (canvas.getWidth() != size.x || canvas.getHeight() != size.y)
        {
            for (int i = 0; i < 32; i++)
            {
                tArray[i].updatePath(size);
            }
        }

        canvas = new BufferedImage(size.x, size.y, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < 32; i++)
        {
            tArray[i].draw(canvas, renderedImage, t);
        }
    }
}
