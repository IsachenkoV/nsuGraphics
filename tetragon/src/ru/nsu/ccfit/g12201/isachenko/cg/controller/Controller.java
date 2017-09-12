package ru.nsu.ccfit.g12201.isachenko.cg.controller;

import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Point;
import ru.nsu.ccfit.g12201.isachenko.cg.view.View;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by Владимир on 19.05.2015.
 */
public class Controller implements MouseListener, ActionListener, ComponentListener {
    View view;
    Model model;

    public Controller(Model model)
    {
        this.model = model;
    }

    public void bindView(View v)
    {
        view = v;
        view.drawImage(model.getImage());
        view.drawMiniImage(model.getInitialMiniImage());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() - 5;
        int y = e.getY() - 5;
        if (x < 0 || y < 0)
            return;
        Point p = new Point(x, y);
        if (model.addPointToCanvas(p))
        {
            view.drawImage(model.getImage());
        }
        else
        {
            JOptionPane.showMessageDialog(null,
                    "Четырехугольник без самопересечений, Карл!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand())
        {
            case "Добавить выпуклый":
                model.addConvexTetragon(view.getCanvasCurrentSize());
                break;
            case "Добавить невыпуклый":
                model.addNonconvexTetragon(view.getCanvasCurrentSize());
                break;
            case "Очистить":
                model.clearAll();
                break;
            case "Включить фильтрацию":
                model.filterOn();
                view.drawMiniImage(model.getMiniImage(null));
                break;
        }
        view.drawImage(model.getImage());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        view.drawMiniImage(model.getMiniImage(new Point(e.getComponent().getWidth(), e.getComponent().getHeight())));
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
