package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.*;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Владимир on 19.05.2015.
 */
public class View {
    private int frameWidth = 800;
    private int frameHeight = 600;
    private int firstLocation = 30;

    private JFrame mainWindow = new JFrame("Tetragon");
    private Canvas canvas = new Canvas();
    private Menu menu = new Menu();

    public View(Controller c) {
        c.bindView(this);

        mainWindow.setSize(frameWidth, frameHeight);
        mainWindow.setLocation(firstLocation, firstLocation);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new BorderLayout());

        mainWindow.add(canvas, BorderLayout.CENTER);
        mainWindow.add(menu, BorderLayout.EAST);

        canvas.setController(c);
        menu.setController(c);
        mainWindow.setVisible(true);

        mainWindow.addComponentListener(c);
    }

    public void drawImage(BufferedImage image) {
        canvas.setImage(image);
    }
    public void drawMiniImage(BufferedImage image) {menu.setImage(image);}

    public Point getCanvasCurrentSize() {
        return new Point(canvas.getWidth() - 5, canvas.getHeight() - 5);
    }


}
