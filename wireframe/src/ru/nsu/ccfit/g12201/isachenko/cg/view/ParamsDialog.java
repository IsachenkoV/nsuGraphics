package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class ParamsDialog extends JDialog {

    private final int paramsCount = 11;
    private final int initialWidth = 250, initialHeight = 400;
    private JPanel panel = new JPanel(new GridLayout(paramsCount, 2, 5, 5));
    private String[] ns = {"n:", "m:", "k:", "a:", "b:", "c:", "d:", "zb:", "zf:", "sw:", "sh:"};
    private JLabel[] labels = new JLabel[paramsCount];
    private JTextField[] textFields = new JFormattedTextField[paramsCount];

    private JButton ok = new JButton("Set params");

    ParamsDialog(Frame owner)
    {
        super(owner, "Add new model");

        setLayout(new BorderLayout());

        int c = 0;
        for (int i = 0; i < paramsCount; i++)
        {
            labels[i] = new JLabel(ns[i]);
            panel.add(labels[i], c);
            c++;
            textFields[i] = new JFormattedTextField(NumberFormat.getIntegerInstance());
            panel.add(textFields[i], c);
            c++;
        }

        add(panel);
        add(ok, BorderLayout.SOUTH);

        setSize(initialWidth, initialHeight);
        setAlwaysOnTop(true);
        setResizable(false);
        setLocation(100, 100);
    }

    int[] closeDialog()
    {
        int[] res = new int[paramsCount];
        for (int i = 0; i < paramsCount; i++){
            if (!textFields[i].getText().equals("")) {
                res[i] = Integer.parseInt(textFields[i].getText().replaceAll(" ", ""));
            }
        }

        this.dispose();
        this.setVisible(false);
        return res;
    }

    public void setController(Controller c) {
        ok.addActionListener(c);
    }
}
