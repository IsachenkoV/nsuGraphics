package ru.nsu.ccfit.g12201.isachenko.cg.model;

/**
 * Created by Владимир on 04.03.2015.
 */
public class Path {

    private Point startPoint, endPoint, canvasSize;
    private int index;
    private double allPhi;

    Path(Point canvasSize, int c)
    {
        index = c;
        allPhi = Math.random() * 5;
        startPoint = new Point(0, 0);
        endPoint = new Point(0, 0);
        update(canvasSize);
    }

    public void update(Point canvasSize)
    {
        this.canvasSize = canvasSize;
        int a = Math.min(canvasSize.x, canvasSize.y) * 4 / 9;
        int step = a / 4;
        startPoint.x = ((canvasSize.x - a) / 2);
        startPoint.y = ((canvasSize.y - a) / 2);
        int c = 0;
        boolean flag = false;
        for (int x = 0; x < a - step / 2; x+= step) {

            if (flag)
                break;

            for (int y = 0; y < a - step / 2; y += step) {
                if (c == index) {
                    startPoint.x += x;
                    startPoint.y += y;
                    flag = true;
                    break;
                }
                c++;
                if (c == index) {
                    startPoint.x += x + step - 1;
                    startPoint.y += y + step - 1;
                    flag = true;
                    break;
                }
                c++;
            }
        }
       // System.out.println(startPoint.x + " " + startPoint.y);
    }

    public Point getCurPosition(Point sz, int time)
    {
        return startPoint;
    }

    public double getCurPhi(Point sz, int time)
    {
        return time * allPhi / 180;
    }
}
