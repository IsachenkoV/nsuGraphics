package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.control.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Point;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Владимир on 24.02.2015.
 */
public class View {
    private int frameWidth = 508;
    private int frameHeight = 346;
    private int firstLocation = 30;

    private JFrame mainWindow = new JFrame("Puzzle");
    private StatusBar statusBar = new StatusBar();
    private ControlPanel controlPanel = new ControlPanel();
    private Canvas canvas = new Canvas();

    public View(Controller c)
    {
        c.bindView(this);
        mainWindow.setSize(frameWidth, frameHeight);
        mainWindow.setLocation(firstLocation, firstLocation);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new BorderLayout());

        statusBar.registerListener();
        controlPanel.registerListener(c);
        mainWindow.add(statusBar, BorderLayout.SOUTH);
        mainWindow.add(controlPanel, BorderLayout.EAST);
        mainWindow.add(canvas);
        mainWindow.addComponentListener(c);

        mainWindow.setVisible(true);
/*        System.out.println(canvas.getWidth() + " " + canvas.getHeight());
        System.out.println(controlPanel.getWidth() + " " + controlPanel.getHeight());
        System.out.println(statusBar.getWidth() + " " + statusBar.getHeight()); */
    }

    public void rePaint(Model model)
    {
        canvas.setImage(model.canvas);
    }

    public void setSliderState(int index)
    {
        controlPanel.setSliderState(index);
    }

    public Point getFrameSize()
    {
        return new Point(canvas.getHeight(), canvas.getWidth());
    }
}
