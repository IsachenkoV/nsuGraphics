package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;

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
        this.setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());
        image = new BufferedImage(400, 400, BufferedImage.TYPE_4BYTE_ABGR);
        imageLabel = new JLabel(new ImageIcon(image));

        add(imageLabel, BorderLayout.CENTER);
    }

    public void setImage(BufferedImage img)
    {
        image = img;
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.repaint();
    }

    public void setController(Controller controller) {
        imageLabel.addMouseMotionListener(controller);
        imageLabel.addMouseListener(controller);
    }
}
