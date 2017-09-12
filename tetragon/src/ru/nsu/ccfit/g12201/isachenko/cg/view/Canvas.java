package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.*;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Владимир on 17.04.2015.
 */
public class Canvas extends JPanel {
    BufferedImage image;
    JLabel imageLabel;

    Canvas()
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBackground(Color.DARK_GRAY);
        image = new BufferedImage(2048, 1024, BufferedImage.TYPE_4BYTE_ABGR);
        imageLabel = new JLabel(new ImageIcon(image));

        add(imageLabel);
    }

    public void setImage(BufferedImage img)
    {
        image = img;
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.repaint();
    }

    public void setController(Controller controller) {
        imageLabel.addMouseListener(controller);
    }
}
