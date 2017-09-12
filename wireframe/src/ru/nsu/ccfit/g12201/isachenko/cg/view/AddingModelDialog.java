package ru.nsu.ccfit.g12201.isachenko.cg.view;

import javafx.geometry.Point2D;
import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Figure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class AddingModelDialog extends JDialog {
    private final int initialWidth = 400, initialHeight = 400;

    private JPanel panel;
    private JLabel label;
    private JButton ok = new JButton("Ok");
    private BufferedImage bi;
    private Graphics biGraphics;
    private Figure f;

    AddingModelDialog(Frame owner)
    {
        super(owner, "Add new model");

        setLayout(new BorderLayout());

        setSize(initialWidth, initialHeight);
        setAlwaysOnTop(true);
        setResizable(false);
        setLocation(100, 200);

        bi = new BufferedImage(300, 300, BufferedImage.TYPE_4BYTE_ABGR);
        biGraphics = bi.createGraphics();
        biGraphics.drawRect(0, 0, 300, 300);
        biGraphics.fillRect(0, 0, 300, 300);
        biGraphics.setColor(Color.red);

        panel = new JPanel();
        panel.setBackground(Color.lightGray);
        label = new JLabel(new ImageIcon(bi));
        panel.add(label);
        f = new Figure();

        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                biGraphics.setColor(Color.red);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    int x = e.getX();
                    int y = e.getY();
                    biGraphics.drawOval(x, y, 6, 6);
                    biGraphics.fillOval(x, y, 6, 6);
                    f.points.add(new Point2D(x, y));
                    drawBspline();
                    label.repaint();
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    int x = e.getX();
                    int y = e.getY();
                    boolean flag = false;
                    for (int i = 0; i < f.points.size(); i++)
                    {
                        Point2D p = f.points.get(i);
                        if ((p.getX() - x)*(p.getX() - x) + (p.getY() - y)*(p.getY() - y) <= 10.0)
                        {
                            flag = true;
                            f.points.remove(i);
                            break;
                        }
                    }
                    if (flag)
                        redraw();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        label.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                boolean flag = false;
                for (int i = 0; i < f.points.size(); i++)
                {
                    Point2D p = f.points.get(i);
                    if ((p.getX() - x)*(p.getX() - x) + (p.getY() - y)*(p.getY() - y) <= 10.0)
                    {
                        Point2D np = new Point2D(x, y);
                        f.points.set(i, np);
                        flag = true;
                        break;
                    }
                }

                if (flag)
                {
                    redraw();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        add(panel, BorderLayout.CENTER);
        add(ok, BorderLayout.SOUTH);
    }

    private void redraw()
    {
        biGraphics.setColor(Color.white);
        biGraphics.drawRect(0, 0, 300, 300);
        biGraphics.fillRect(0, 0, 300, 300);

        biGraphics.setColor(Color.red);
        for (Point2D p : f.points)
        {
            int x = (int) p.getX();
            int y = (int) p.getY();
            biGraphics.drawOval(x, y, 6, 6);
            biGraphics.fillOval(x, y, 6, 6);
        }
        drawBspline();
        label.repaint();
    }

    private void drawBspline()
    {
        if (f.points.size() < 4)
            return;
        biGraphics.setColor(Color.BLUE);

        Point2D prevPoint, curPoint;

        prevPoint = f.getPoint(0.0);

        for (double i = 0.1; i < f.points.size() - 3; i += 0.1)
        {
            curPoint = f.getPoint(i);
            biGraphics.drawLine((int)prevPoint.getX(), (int)prevPoint.getY(), (int)curPoint.getX(), (int)curPoint.getY());
            prevPoint = curPoint;
        }
    }
    public void setController(Controller c) {
        ok.addActionListener(c);
    }

    public Figure closeDialog() {
        this.dispose();
        this.setVisible(false);
        return f;
    }
}
