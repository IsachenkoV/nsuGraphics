package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.text.NumberFormat;

public class FunctionParameterDialog extends JDialog {

    private final int initialWidth = 250, initialHeight = 300;

    private JLabel fParamsLabel = new JLabel("Параметры функции:");
    private JFormattedTextField functionParams[] = new JFormattedTextField[4];
    private JPanel functionParamsPanel = new JPanel(new GridLayout(2, 4, 5, 5));

    private JLabel gridParamsLabel = new JLabel("Параметры сетки:");
    private JFormattedTextField gridParams[] = new JFormattedTextField[2];
    private JPanel gridParamsPanel = new JPanel(new GridLayout(2, 2, 5, 5));

    private JButton okButton = new JButton("OK");

    FunctionParameterDialog(JFrame owner, String title)
    {
        super(owner, title);
        setSize(initialWidth, initialHeight);
        setLayout(new GridLayout(5, 1));
        setAlwaysOnTop(true);
        setResizable(false);
        setLocation(100, 100);

        functionParamsPanel.add(new JLabel("a:"), 0);
        functionParamsPanel.add(new JLabel("b:"), 1);
        functionParamsPanel.add(new JLabel("c:"), 2);
        functionParamsPanel.add(new JLabel("d:"), 3);
        for (int i = 0; i <= 3; i++) {
            functionParams[i] = new JFormattedTextField(NumberFormat.getIntegerInstance());
            functionParamsPanel.add(functionParams[i], 4 + i);
        }
        functionParamsPanel.setBorder(new EtchedBorder());

        add(fParamsLabel);
        add(functionParamsPanel);

        gridParamsPanel.add(new JLabel("k:"), 0);
        gridParamsPanel.add(new JLabel("m:"), 1);
        gridParams[0] = new JFormattedTextField(NumberFormat.getIntegerInstance());
        gridParams[1] = new JFormattedTextField(NumberFormat.getIntegerInstance());
        gridParamsPanel.add(gridParams[0], 2);
        gridParamsPanel.add(gridParams[1], 3);
        gridParamsPanel.setBorder(new EtchedBorder());

        add(gridParamsLabel);
        add(gridParamsPanel);

        add(okButton);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public int[] closeDialog()
    {
        int[] array = new int[6];
        for (int i = 0; i <= 3; i++){
            if (!functionParams[i].getText().equals("")) {
                array[i] = Integer.parseInt(functionParams[i].getText().replaceAll(" ", ""));
            }
        }

        array[4] = Integer.parseInt(gridParams[0].getText().replaceAll(" ", ""));
        array[5] = Integer.parseInt(gridParams[1].getText().replaceAll(" ", ""));

        if (array[0] > array[1])
        {
            JOptionPane.showMessageDialog(null,
                    "b must be greater than a!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        if (array[2] > array[3])
        {
            JOptionPane.showMessageDialog(null,
                    "d must be greater than c!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        if (array[4] < 2 || array[5] < 2)
        {
            JOptionPane.showMessageDialog(null,
                    "k and m must be greater than 1!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        this.dispose();
        this.setVisible(false);
        return array;
    }

    public void setListener(Controller c)
    {
        okButton.addActionListener(c);
    }
}
