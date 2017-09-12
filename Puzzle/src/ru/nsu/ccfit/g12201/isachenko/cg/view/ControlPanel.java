package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.control.Controller;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by Владимир on 24.02.2015.
 */
public class ControlPanel extends JPanel {

    private JCheckBox isBlend = new JCheckBox("Blend");
    private JCheckBox isFilter = new JCheckBox("Filter");
    private JButton startButton = new JButton("Start/Stop");
    private JButton initButton = new JButton("Init");
    private JSlider timeSlider = new JSlider(0, 360, 0);

    ControlPanel()
    {
        setLayout(new GridLayout(5, 1));
        add(isBlend);
        add(isFilter);
        add(startButton);
        add(initButton);
        add(timeSlider);
        timeSlider.setMajorTickSpacing(180);
        timeSlider.setMinorTickSpacing(10);
        timeSlider.setPaintTicks(true);
        timeSlider.setAutoscrolls(true);
        setBorder(new EtchedBorder());
    }

    public void registerListener(Controller c) {
        isBlend.addActionListener(c);
        isFilter.addActionListener(c);
        startButton.addActionListener(c);
        initButton.addActionListener(c);
        timeSlider.addChangeListener(c);
    }

    public void setSliderState(int index)
    {
        timeSlider.setValue(index);
    }
}
