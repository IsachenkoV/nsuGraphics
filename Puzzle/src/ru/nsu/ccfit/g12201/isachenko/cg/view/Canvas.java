package ru.nsu.ccfit.g12201.isachenko.cg.view;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created by Владимир on 24.02.2015.
 */

public class Canvas extends JPanel {

    BufferedImage image;
    JLabel imageLabel;

    Canvas()
    {
        setLayout(new BorderLayout());
        image = new BufferedImage(128, 128, BufferedImage.TYPE_4BYTE_ABGR);
        imageLabel = new JLabel(new ImageIcon(image));
        add(imageLabel, BorderLayout.CENTER);
        setBorder(new EtchedBorder());
    }

    public void setImage(BufferedImage img)
    {
        image = img;
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.repaint();
    }
}
