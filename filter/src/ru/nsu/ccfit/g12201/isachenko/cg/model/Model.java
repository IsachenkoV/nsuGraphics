package ru.nsu.ccfit.g12201.isachenko.cg.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Владимир on 09.03.2015.
 */
public class Model {

    private final int initialWidth = 240, initialHeight = 535;
    private final int blue = Color.BLUE.getRGB();
    private final int mask = 0b11111111;

    private boolean filter = false;
    private BufferedImage srcImage, dstImage;

    public BufferedImage srcCanvas = new BufferedImage(initialWidth, initialHeight, BufferedImage.TYPE_4BYTE_ABGR), dstCanvas;

    public static enum Filters{
        BLACK_AND_WHITE, EDGE, BLUR, SHARPEN, IDENTICAL, NEGATIVE, EMBOSS, WATERCOLOR, GAMMA, MATRIX;
        public int[][][] matrices = {
                { //BLACK_AND_WHITE
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                { //EDGE
                        {0, -1, 0},
                        {-1, 4, -1},
                        {0, -1, 0}
                },
                { //BLUR
                        {0, 1, 0},
                        {1, 2, 1},
                        {0, 1, 0}
                },
                { //SHARPEN
                        {0, -1, 0},
                        {-1, 5, -1},
                        {0, -1, 0}
                },
                { //IDENTICAL
                        {0, 0, 0},
                        {0, 1, 0},
                        {0, 0, 0}
                },
                { //NEGATIVE
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                { //EMBOSS
                        {0, 1, 0},
                        {-1, 0, 1},
                        {0, -1, 0}
                },
                { //WATERCOLOR
                        {0, -1, 0},
                        {-1, 5, -1},
                        {0, -1, 0}
                },
                { //GAMMA
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                { //MATRIX
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                }
        };

        public int[] divisors = {1, 1, 6, 1, 1, 1, 1, 1, 1, 1};
        public int[] offsets = {0, 0, 0, 0, 0, 0, 128, 0, 0, 0};
    }

    public Model(String imagePath) throws IOException {
        openNewFile(imagePath);
    }

    public void saveFile(String path) throws IOException {
        ImageIO.write(dstImage, "png", new File(path));
    }

    public void openNewFile(String path) throws IOException {
        srcImage = ImageIO.read(new File(path));
        dstImage = ImageIO.read(new File(path));
        renderCanvas(new Point(srcCanvas.getWidth(), srcCanvas.getHeight()));
    }

    public void applyFilter(Filters f) {
        filter = true;
        switch (f)
        {
            case BLACK_AND_WHITE:
                setCurImageBlackWhite();
                break;
            case BLUR:
                matrixTransform(f.matrices[Filters.BLUR.ordinal()], f.divisors[Filters.BLUR.ordinal()], f.offsets[Filters.BLUR.ordinal()]);
                break;
            case SHARPEN:
                matrixTransform(f.matrices[Filters.SHARPEN.ordinal()], f.divisors[Filters.SHARPEN.ordinal()], f.offsets[Filters.SHARPEN.ordinal()]);
                break;
            case IDENTICAL:
                matrixTransform(f.matrices[Filters.IDENTICAL.ordinal()], f.divisors[Filters.IDENTICAL.ordinal()], f.offsets[Filters.IDENTICAL.ordinal()]);
                break;
            case NEGATIVE:
                setCurImageNegative();
                break;
            case EMBOSS:
                matrixTransform(f.matrices[Filters.EMBOSS.ordinal()], f.divisors[Filters.EMBOSS.ordinal()], f.offsets[Filters.EMBOSS.ordinal()]);
                break;
            case WATERCOLOR:
                setCurImageWaterColor(f);
                break;
            case EDGE:
                matrixTransform(f.matrices[Filters.EDGE.ordinal()], f.divisors[Filters.EDGE.ordinal()], f.offsets[Filters.EDGE.ordinal()]);
                break;
        }
    }

    private void setCurImageWaterColor(Filters f) {
        int x = srcImage.getWidth();
        int y = srcImage.getHeight();
        BufferedImage curImage = new BufferedImage(x, y, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {

                int curRGB = srcImage.getRGB(i, j);
                int a = mask & (curRGB) >> 24;
                int     defB = mask & (curRGB) >> 16,
                        defG = mask & (curRGB) >> 8,
                        defR = mask & (curRGB);

                int[] arrayB = new int[25];
                int[] arrayG = new int[25];
                int[] arrayR = new int[25];
                int c = 0;
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dy = -2; dy <= 2; dy++) {
                        int curX = i + dx, curY = j + dy;
                        if (curX < 0 || curX >= x || curY < 0 || curY >= y)
                        {
                            arrayB[c] = defB;
                            arrayG[c] = defG;
                            arrayR[c] = defR;
                        }
                        else
                        {
                            int pointRGB = srcImage.getRGB(curX, curY);
                            arrayB[c] = mask & (pointRGB) >> 16;
                            arrayG[c] = mask & (pointRGB) >> 8;
                            arrayR[c] = mask & (pointRGB);
                        }
                        c++;
                    }
                }
                Arrays.sort(arrayB);
                Arrays.sort(arrayG);
                Arrays.sort(arrayR);
                int res = (a << 24) + (arrayB[12] << 16) + (arrayG[12] << 8) + arrayR[12];
                curImage.setRGB(i, j, res);
            }
        }
        int[][] matrix = f.matrices[f.ordinal()];
        int divisor = f.divisors[f.ordinal()];
        int offset = f.offsets[f.ordinal()];
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                int curRGB = curImage.getRGB(i, j);
                int a = mask & (curRGB) >> 24;

                int     defB = mask & (curRGB) >> 16,
                        defG = mask & (curRGB) >> 8,
                        defR = mask & (curRGB);

                int sumB = 0;
                int sumG = 0;
                int sumR = 0;
                for (int dx = -1; dx <= 1; dx++)
                {
                    for (int dy = -1; dy <= 1; dy++)
                    {
                        int curX = i + dx, curY = j + dy;
                        if (curX < 0 || curX >= x || curY < 0 || curY >= y)
                        {
                            sumB += defB * matrix[dx + 1][dy + 1];
                            sumG += defG * matrix[dx + 1][dy + 1];
                            sumR += defR * matrix[dx + 1][dy + 1];
                        }
                        else
                        {
                            int pointRGB = curImage.getRGB(curX, curY);
                            sumB += (mask & (pointRGB) >> 16) * matrix[dx + 1][dy + 1];
                            sumG += (mask & (pointRGB) >> 8) * matrix[dx + 1][dy + 1];
                            sumR += (mask & (pointRGB)) * matrix[dx + 1][dy + 1];
                        }
                    }
                }
                sumB = sumB / divisor + offset;
                sumG = sumG / divisor + offset;
                sumR = sumR / divisor + offset;

                sumB = sumB < 0 ? 0 : sumB;
                sumB = sumB > 255 ? 255 : sumB;
                sumG = sumG < 0 ? 0 : sumG;
                sumG = sumG > 255 ? 255 : sumG;
                sumR = sumR < 0 ? 0 : sumR;
                sumR = sumR > 255 ? 255 : sumR;

                int result = (a << 24) + (sumB << 16) + (sumG << 8) + sumR;
                dstImage.setRGB(i, j, result);
            }
        }

        renderCanvas(new Point(srcCanvas.getWidth(), srcCanvas.getHeight()));
    }

    public void pixelCopy(boolean b) {
        int x = srcImage.getWidth();
        int y = srcImage.getHeight();
        if (b)
        {
            for (int i = 0; i < x; i++)
            {
                for (int j = 0; j < y; j++)
                {
                    dstImage.setRGB(i, j, srcImage.getRGB(i, j));
                }
            }
        }
        else
        {
            for (int i = 0; i < x; i++)
            {
                for (int j = 0; j < y; j++)
                {
                    srcImage.setRGB(i, j, dstImage.getRGB(i, j));
                }
            }
        }
        renderCanvas(new Point(srcCanvas.getWidth(), srcCanvas.getHeight()));
    }

    public void renderCanvas(Point size)    {
        Point srcImageSize = new Point(srcImage.getWidth(), srcImage.getHeight());
        Point startCanvas, startImage, endImage;

        startCanvas = new Point((size.x - srcImageSize.x)/2, (size.y - srcImageSize.y)/2);
        startImage = new Point();
        endImage = srcImageSize;

        if (size.x < srcImageSize.x && size.y < srcImageSize.y)
        {
            startCanvas = new Point();
            startImage = new Point((0 - size.x + srcImageSize.x)/2, (0 - size.y + srcImageSize.y)/2);
            endImage = new Point(startImage.x + size.x, startImage.y + size.y);
        }

        if (size.x < srcImageSize.x && size.y >= srcImageSize.y)
        {
            startCanvas = new Point(0, (size.y - srcImageSize.y)/2);
            startImage = new Point((srcImageSize.x - size.x)/2, 0);
            endImage = new Point(startImage.x + size.x, startImage.y + srcImageSize.y);
        }

        if (size.x >= srcImageSize.x && size.y < srcImageSize.y)
        {
            startCanvas = new Point((size.x - srcImageSize.x)/2, 0);
            startImage = new Point(0, (srcImageSize.y - size.y)/2);
            endImage = new Point(startImage.x + srcImageSize.x, startImage.y + size.y);
        }

        if (!filter) // resize!
        {
            srcCanvas = new BufferedImage(size.x, size.y, BufferedImage.TYPE_4BYTE_ABGR);
            for (int i = 0; i < size.x; i++)
            {
                for (int j = 0; j < size.y; j++)
                {
                    srcCanvas.setRGB(i, j, blue);
                }
            }
            for (int i = startImage.x; i < endImage.x; i++)
            {
                for (int j = startImage.y; j < endImage.y; j++)
                {
                    int x = startCanvas.x + i - startImage.x;
                    int y = startCanvas.y + j - startImage.y;
                    srcCanvas.setRGB(x, y, srcImage.getRGB(i, j));
                }
            }
        }

        //render dst canvas
        dstCanvas = new BufferedImage(size.x, size.y, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < size.x; i++)
        {
            for (int j = 0; j < size.y; j++)
            {
                dstCanvas.setRGB(i, j, blue);
            }
        }

        for (int i = startImage.x; i < endImage.x; i++)
        {
            for (int j = startImage.y; j < endImage.y; j++)
            {
                int x = startCanvas.x + i - startImage.x;
                int y = startCanvas.y + j - startImage.y;
                dstCanvas.setRGB(x, y, dstImage.getRGB(i, j));
            }
        }
        filter = false;
    }

    private void setCurImageBlackWhite() {
        int x = srcImage.getWidth();
        int y = srcImage.getHeight();
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                int curRGB = srcImage.getRGB(i, j);
                int     a = mask & (curRGB) >> 24,
                        b = mask & (curRGB) >> 16,
                        g = mask & (curRGB) >> 8,
                        r = mask & (curRGB);
                double result = 0.299 * r + 0.587 * g + 0.114 * b;
                int newRGB = (a << 24) + (((int)result) << 16) + (((int)result) << 8) + ((int)result);
                dstImage.setRGB(i, j, newRGB);
            }
        }
        renderCanvas(new Point(srcCanvas.getWidth(), srcCanvas.getHeight()));
    }

    private void setCurImageNegative() {
        int x = srcImage.getWidth();
        int y = srcImage.getHeight();
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                int curRGB = srcImage.getRGB(i, j);
                int     a = mask & (curRGB) >> 24,
                        b = mask & (curRGB) >> 16,
                        g = mask & (curRGB) >> 8,
                        r = mask & (curRGB);
                int newRGB = (a << 24) + ((255 - b) << 16) + ((255 - g) << 8) + (255 - r);
                dstImage.setRGB(i, j, newRGB);
            }
        }
        renderCanvas(new Point(srcCanvas.getWidth(), srcCanvas.getHeight()));
    }

    public void applyGammaCorrection(double gamma) {
        int x = srcImage.getWidth();
        int y = srcImage.getHeight();
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                int curRGB = srcImage.getRGB(i, j);
                int     a = mask & (curRGB) >> 24,
                        b = mask & (curRGB) >> 16,
                        g = mask & (curRGB) >> 8,
                        r = mask & (curRGB);

                int result;
                b = (int)(Math.pow( b/255.0, gamma ) * 255);
                g = (int)(Math.pow( g/255.0, gamma ) * 255);
                r = (int)(Math.pow( r/255.0, gamma ) * 255);
                result = (a << 24) + (b << 16) + (g << 8) + r;
                dstImage.setRGB(i, j, result);
            }
        }
        renderCanvas(new Point(srcCanvas.getWidth(), srcCanvas.getHeight()));
    }

    public void applyEdgeSecretion(int threshold) {
        int x = srcImage.getWidth();
        int y = srcImage.getHeight();
        applyFilter(Filters.EDGE);
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                int curRGB = dstImage.getRGB(i, j);
                int     a = mask & (curRGB) >> 24,
                        b = mask & (curRGB) >> 16,
                        g = mask & (curRGB) >> 8,
                        r = mask & (curRGB);
                double result = 0.299 * r + 0.587 * g + 0.114 * b;

                if ((int) result > threshold)
                {
                    dstImage.setRGB(i, j, Color.WHITE.getRGB());
                }
                else
                {
                    dstImage.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        renderCanvas(new Point(srcCanvas.getWidth(), srcCanvas.getHeight()));
    }

    public void matrixTransform(int[][] matrix, int divisor, int offset) {
        int x = srcImage.getWidth();
        int y = srcImage.getHeight();
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                int curRGB = srcImage.getRGB(i, j);
                int a = mask & (curRGB) >> 24;

                int     defB = mask & (curRGB) >> 16,
                        defG = mask & (curRGB) >> 8,
                        defR = mask & (curRGB);

                int sumB = 0;
                int sumG = 0;
                int sumR = 0;
                for (int dx = -1; dx <= 1; dx++)
                {
                    for (int dy = -1; dy <= 1; dy++)
                    {
                        int curX = i + dx, curY = j + dy;
                        if (curX < 0 || curX >= x || curY < 0 || curY >= y)
                        {
                            sumB += defB * matrix[dx + 1][dy + 1];
                            sumG += defG * matrix[dx + 1][dy + 1];
                            sumR += defR * matrix[dx + 1][dy + 1];
                        }
                        else
                        {
                            int pointRGB = srcImage.getRGB(curX, curY);
                            sumB += (mask & (pointRGB) >> 16) * matrix[dx + 1][dy + 1];
                            sumG += (mask & (pointRGB) >> 8) * matrix[dx + 1][dy + 1];
                            sumR += (mask & (pointRGB)) * matrix[dx + 1][dy + 1];
                        }
                    }
                }
                sumB = sumB / divisor + offset;
                sumG = sumG / divisor + offset;
                sumR = sumR / divisor + offset;

                sumB = sumB < 0 ? 0 : sumB;
                sumB = sumB > 255 ? 255 : sumB;
                sumG = sumG < 0 ? 0 : sumG;
                sumG = sumG > 255 ? 255 : sumG;
                sumR = sumR < 0 ? 0 : sumR;
                sumR = sumR > 255 ? 255 : sumR;

                int result = (a << 24) + (sumB << 16) + (sumG << 8) + sumR;
                dstImage.setRGB(i, j, result);
            }
        }

        renderCanvas(new Point(srcCanvas.getWidth(), srcCanvas.getHeight()));
    }

}