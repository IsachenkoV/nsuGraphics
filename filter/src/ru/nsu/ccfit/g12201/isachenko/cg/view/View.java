package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.*;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Point;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Владимир on 09.03.2015.
 */
public class View {

    private int frameWidth = 800;
    private int frameHeight = 600;
    private int firstLocation = 30;

    private JFrame mainWindow = new JFrame("Filter");
    private ControlPanel controlPanel = new ControlPanel(mainWindow);
    private DrawPanel drawPanel = new DrawPanel();

    public View(Controller c)
    {
        c.bindView(this);

        mainWindow.setSize(frameWidth, frameHeight);
        mainWindow.setLocation(firstLocation, firstLocation);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new BorderLayout());

        controlPanel.setListener(c);
        mainWindow.add(controlPanel, BorderLayout.EAST);
        mainWindow.add(drawPanel, BorderLayout.CENTER);
        mainWindow.addComponentListener(c);

        mainWindow.setVisible(true);
    }

    public Point getDrawPanelSize()
    {
        return drawPanel.getSizes();
    }

    public void rePaint(Model model)
    {
        drawPanel.setImages(model.srcCanvas, model.dstCanvas);
    }

    public void setSlidersDisable()
    {
        controlPanel.setSlidersDisable();
    }

    public int[] getMatrixParameters() {
        return controlPanel.getMatrixParameters();
    }
}
