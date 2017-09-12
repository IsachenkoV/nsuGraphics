package ru.nsu.ccfit.g12201.isachenko.cg.controller;

import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Point;
import ru.nsu.ccfit.g12201.isachenko.cg.view.View;

import javax.swing.*;
import java.awt.event.*;

public class Controller implements ComponentListener, ActionListener, MouseMotionListener, MouseListener{

    View view;
    Model model;

    public Controller(Model model)
    {
        this.model = model;
    }

    public void bindView(View v)
    {
        view = v;
        view.rePaintImg(model);
        view.rePaintLegend(model);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Point sz = view.getFrameSize();
        model.renderCanvas( new Point(sz.x * 5 / 6, sz.y * 5 / 6));
        model.renderLegend( new Point(sz.x / 12, sz.y * 4 / 6));

        view.rePaintLegend(model);
        view.rePaintImg(model);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK"))
        {
            int[] array = view.getParams();
            if (array[0] > array[1])
                return;
            if (array[2] > array[3])
                return;
            if (array[4] < 2 || array[5] < 2)
                return;
            model.setFunctionParams(array);
            view.rePaintImg(model);
            view.rePaintLegend(model);
        }

        if (e.getActionCommand().equals("Colored map"))
        {
            model.isInterpolationEnabled = false;
            model.isDitheringEnabled = false;
            model.clearUserIsolines();
            model.renderCanvas(new Point(model.canvas.getWidth(), model.canvas.getHeight()));
            model.renderLegend(new Point(model.legend.getWidth(), model.legend.getHeight()));
            view.rePaintImg(model);
            view.rePaintLegend(model);
        }

        if (e.getActionCommand().equals("Interpolation"))
        {
            model.isInterpolationEnabled = true;
            model.isDitheringEnabled = false;
            model.clearUserIsolines();
            model.renderCanvas(new Point(model.canvas.getWidth(), model.canvas.getHeight()));
            model.renderLegend(new Point(model.legend.getWidth(), model.legend.getHeight()));
            view.rePaintImg(model);
            view.rePaintLegend(model);
        }

        if (e.getActionCommand().equals("Dithering"))
        {
            model.isInterpolationEnabled = false;
            model.isDitheringEnabled = true;
            model.clearUserIsolines();
            model.renderCanvas(new Point(model.canvas.getWidth(), model.canvas.getHeight()));
            model.renderLegend(new Point(model.legend.getWidth(), model.legend.getHeight()));
            view.rePaintImg(model);
            view.rePaintLegend(model);
        }

        if (e.getActionCommand().equals("Isolines"))
        {
            model.isIsolinesEnabled = !model.isIsolinesEnabled;
            model.renderCanvas(new Point(model.canvas.getWidth(), model.canvas.getHeight()));
            view.rePaintImg(model);
        }

        if (e.getActionCommand().equals("Grid"))
        {
            model.isGridEnabled = !model.isGridEnabled;
            model.renderCanvas(new Point(model.canvas.getWidth(), model.canvas.getHeight()));
            view.rePaintImg(model);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        JLabel jl = (JLabel) e.getSource();
        int w = jl.getWidth();
        int h = jl.getHeight();
        int dw = (w - model.canvas.getWidth()) / 2;
        int dh = (h - model.canvas.getHeight()) / 2;

        view.setStatusBarText(e.getX() - dw, e.getY() - dh, model.getFunctionValue(e.getX() - dw, e.getY() - dh));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!model.isIsolinesEnabled)
            return;
        JLabel jl = (JLabel) e.getSource();
        int w = jl.getWidth();
        int h = jl.getHeight();
        int dw = (w - model.canvas.getWidth()) / 2;
        int dh = (h - model.canvas.getHeight()) / 2;
        model.addIsoline(model.getFunctionValue(e.getX() - dw, e.getY() - dh));
        view.rePaintImg(model);
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
}
