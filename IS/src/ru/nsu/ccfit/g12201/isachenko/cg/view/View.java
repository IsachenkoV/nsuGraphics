package ru.nsu.ccfit.g12201.isachenko.cg.view;

import javax.swing.*;
import java.awt.*;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Point;

public class View {

    private int frameWidth = 800;
    private int frameHeight = 600;
    private int firstLocation = 30;

    private JFrame mainWindow = new JFrame("IS");
    private Canvas canvas = new Canvas();
    private StatusBar statusBar = new StatusBar();
    private ToolBar toolBar;

    public View(Controller c)
    {
        c.bindView(this);

        mainWindow.setSize(frameWidth, frameHeight);
        mainWindow.setLocation(firstLocation, firstLocation);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new BorderLayout());

        mainWindow.add(canvas);
        toolBar = new ToolBar(mainWindow);
        mainWindow.add(toolBar, BorderLayout.NORTH);
        mainWindow.add(statusBar, BorderLayout.SOUTH);

        toolBar.setController(c);
        canvas.setController(c);

        mainWindow.setVisible(true);
        mainWindow.addComponentListener(c);
    }

    public void rePaintImg(Model model)
    {
        canvas.setImage(model.canvas);
    }

    public void rePaintLegend(Model model) { canvas.setLegend(model.legend); }

    public Point getFrameSize()
    {
        return new Point(canvas.getWidth(), canvas.getHeight());
    }

    public void setStatusBarText(int x, int y, double value)
    {
        statusBar.setText("X: " + x + "; Y: " + y + "; Value: " + value );
    }

    public int[] getParams()
    {
        return toolBar.getParams();
    }
}
