package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JPanel {

    private JButton mapMode = new JButton("Colored map");
    private JButton interpolationMode = new JButton("Interpolation");
    private JButton dither = new JButton("Dithering");
    private JButton isolines = new JButton("Isolines");
    private JButton grid = new JButton("Grid");
    private JButton setParams = new JButton("Parameters");

    private FunctionParameterDialog fpd;

    ToolBar(JFrame owner)
    {
        setLayout(new GridLayout(1, 5));
        add(mapMode);
        add(interpolationMode);
        add(dither);
        add(isolines);
        add(grid);
        add(setParams);

        fpd = new FunctionParameterDialog(owner, "Set your params");
        setParams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fpd.setVisible(true);
            }
        });
    }

    public void setController(Controller c)
    {
        mapMode.addActionListener(c);
        interpolationMode.addActionListener(c);
        dither.addActionListener(c);
        isolines.addActionListener(c);
        grid.addActionListener(c);

        fpd.setListener(c);
    }

    public int[] getParams() {
        return fpd.closeDialog();
    }
}
