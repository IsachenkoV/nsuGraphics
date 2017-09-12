package ru.nsu.ccfit.g12201.isachenko.cg.control;

import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Point;
import ru.nsu.ccfit.g12201.isachenko.cg.view.View;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by Владимир on 25.02.2015.
 */
public class Controller implements ActionListener, ComponentListener, ChangeListener {

    private Model model;
    private View view;
    private Timer timer;
    private int curTime = 0;

    public Controller(Model model) {
        this.model = model;
    }

    public void bindView(View view)
    {
        this.view = view;
        view.rePaint(model);
        timer = new Timer(10, this);
        timer.setActionCommand("Timer");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Blend"))
        {
            model.blendIsOn = !model.blendIsOn;
            model.renderImage(new Point(model.canvas.getWidth(), model.canvas.getHeight()), curTime);
            view.rePaint(model);
        }
        if (e.getActionCommand().equals("Filter"))
        {
            model.filterIsOn = !model.filterIsOn;
            model.renderImage(new Point(model.canvas.getWidth(), model.canvas.getHeight()), curTime);
            view.rePaint(model);
        }
        if (e.getActionCommand().equals("Init"))
        {
            timer.stop();
            model.renderCanvas(new Point(model.canvas.getWidth(), model.canvas.getHeight()), 0);
            view.rePaint(model);
            view.setSliderState(0);
        }
        if (e.getActionCommand().equals("Start/Stop"))
        {
            if (timer.isRunning())
                timer.stop();
            else
                timer.start();
        }
        if (e.getActionCommand().equals("Timer"))
        {
            curTime = (curTime + 1) % 360;
            model.renderCanvas(new Point(model.canvas.getWidth(), model.canvas.getHeight()), curTime);
            view.rePaint(model);
            view.setSliderState(curTime);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        model.renderImage(view.getFrameSize(), curTime);
        view.rePaint(model);
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
    public void stateChanged(ChangeEvent e) {
        curTime = ((JSlider)e.getSource()).getValue();
        model.renderCanvas(new Point(model.canvas.getWidth(), model.canvas.getHeight()), curTime);
        view.rePaint(model);
    }
}
