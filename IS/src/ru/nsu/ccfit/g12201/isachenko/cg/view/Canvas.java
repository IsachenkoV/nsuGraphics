package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {

    BufferedImage image, legend;
    JLabel imageLabel, legendLabel;

    Canvas()
    {
        this.setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());
        image = new BufferedImage(400, 400, BufferedImage.TYPE_4BYTE_ABGR);
        imageLabel = new JLabel(new ImageIcon(image));

        legend = new BufferedImage(50, 400, BufferedImage.TYPE_4BYTE_ABGR);
        legendLabel = new JLabel(new ImageIcon(legend));

        add(imageLabel, BorderLayout.CENTER);
        add(legendLabel, BorderLayout.EAST);
    }

    public void setImage(BufferedImage img)
    {
        image = img;
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.repaint();
    }

    public void setLegend(BufferedImage leg)
    {
        legend = leg;
        legendLabel.setIcon(new ImageIcon(legend));
        legendLabel.repaint();
    }

    public void setController(Controller controller) {
        imageLabel.addMouseMotionListener(controller);
        imageLabel.addMouseListener(controller);
    }
}
