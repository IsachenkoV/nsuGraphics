package ru.nsu.ccfit.g12201.isachenko.cg.controller;

import javafx.geometry.Point2D;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Figure;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.view.View;

import java.awt.event.*;

public class Controller implements MouseListener, MouseMotionListener, ActionListener {

    View view;
    Model model;
    private Point2D delta;

    public Controller(Model model)
    {
        this.model = model;
    }

    public void bindView(View v)
    {
        view = v;
        view.drawImage(model.getImage());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Set params"))
        {
            int[] res = view.closeParamsDialog();
            model.setParams(res);
            view.drawImage(model.getImage());
        }

        if (e.getActionCommand().equals("Ok"))
        {
            Figure figure = view.closeAddingModelDialog();
            model.addFigure(figure);
            view.drawImage(model.getImage());
        }

        if (e.getActionCommand().equals("Delete previous model"))
        {
            model.deletePrevModel();
            view.drawImage(model.getImage());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        delta = new Point2D(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point2D p = new Point2D(delta.getX() - e.getX(), delta.getY() - e.getY());
        model.rotate(p);
        view.drawImage(model.getImage());
        delta = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
