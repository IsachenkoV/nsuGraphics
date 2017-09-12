package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.model.*;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Point;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Владимир on 09.03.2015.
 */
public class DrawPanel extends JPanel {

    private BufferedImage srcImage, dstImage;
    private JPanel srcPanel = new JPanel(), dstPanel = new JPanel();
    private JLabel srcImageLabel, dstImageLabel = new JLabel();
    private Border bl = BorderFactory.createLineBorder(Color.black);

    DrawPanel()
    {
        setLayout(new GridLayout(1, 2));
        setBorder(new EtchedBorder());

        srcPanel.setLayout(new BorderLayout());
        dstPanel.setLayout(new BorderLayout());
        srcPanel.setBorder(new TitledBorder(bl, "Исходное"));
        dstPanel.setBorder(new TitledBorder(bl, "Результат"));

        srcImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        dstImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);

        srcImageLabel = new JLabel(new ImageIcon(srcImage));
        dstImageLabel = new JLabel(new ImageIcon(dstImage));
        srcPanel.add(srcImageLabel);
        dstPanel.add(dstImageLabel);

        add(srcPanel);
        add(dstPanel);
    }

    public void setImages(BufferedImage src, BufferedImage dst)
    {
        srcImage = src;
        dstImage = dst;
        srcImageLabel.setIcon(new ImageIcon(srcImage));
        dstImageLabel.setIcon(new ImageIcon(dstImage));

        srcImageLabel.repaint();
        dstImageLabel.repaint();
    }

    public Point getSizes()
    {
        return new Point(srcImageLabel.getWidth(), srcImageLabel.getHeight());
    }
}
