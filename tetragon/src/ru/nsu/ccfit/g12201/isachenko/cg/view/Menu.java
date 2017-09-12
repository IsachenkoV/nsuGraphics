package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Владимир on 20.05.2015.
 */
public class Menu extends JPanel {
    private JLabel miniMap;
    private JPanel functions;

    private JButton addConvex = new JButton("Добавить выпуклый");
    private JButton addNonconvex = new JButton("Добавить невыпуклый");
    private JButton clear = new JButton("Очистить");

    private JCheckBox filter = new JCheckBox("Включить фильтрацию");

    Menu()
    {
        setLayout(new BorderLayout());
        setBorder(new EtchedBorder());
        miniMap = new JLabel(new ImageIcon(new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR)));

        functions = new JPanel();
        functions.setLayout(new GridLayout(4, 1, 5, 10));
        functions.add(addConvex);
        functions.add(addNonconvex);
        functions.add(filter);
        functions.add(clear);

        add(miniMap, BorderLayout.NORTH);
        add(functions, BorderLayout.CENTER);
    }

    public void setController(Controller controller) {
        addConvex.addActionListener(controller);
        addNonconvex.addActionListener(controller);
        clear.addActionListener(controller);
        filter.addActionListener(controller);
    }

    public void setImage(BufferedImage image) {
        miniMap.setIcon(new ImageIcon(image));
        miniMap.repaint();
    }
}
