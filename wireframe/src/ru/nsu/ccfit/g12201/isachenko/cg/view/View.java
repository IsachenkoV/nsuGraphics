package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Figure;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Владимир on 17.04.2015.
 */
public class View {
    private int frameWidth = 800;
    private int frameHeight = 600;
    private int firstLocation = 30;

    private JFrame mainWindow = new JFrame("WF");
    private Canvas canvas = new Canvas();
    private Menu menu = new Menu(mainWindow);

    public View(Controller c) {
        c.bindView(this);

        mainWindow.setSize(frameWidth, frameHeight);
        mainWindow.setLocation(firstLocation, firstLocation);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new BorderLayout());

        mainWindow.add(canvas, BorderLayout.CENTER);
        mainWindow.add(menu, BorderLayout.NORTH);
        canvas.setController(c);
        menu.setController(c);

        mainWindow.setVisible(true);
    }

    public int[] closeParamsDialog()
    {
        return menu.paramsDialog.closeDialog();
    }

    public Figure closeAddingModelDialog()
    {
        return menu.addingModelDialog.closeDialog();
    }

    public void drawImage(BufferedImage image) {
        canvas.setImage(image);
    }
}
