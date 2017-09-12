package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Created by Владимир on 12.03.2015.
 */
public class MatrixParameterDialog extends JDialog {

    private final int initialWidth = 200, initialHeight = 200;
    private JFormattedTextField matrixField[][] = new JFormattedTextField[3][3];
    private JPanel matrixPanel = new JPanel(new GridLayout(3, 3));

    private JPanel additionalPanel = new JPanel(new GridLayout(5, 1, 5, 5));
    private JLabel divisorLabel = new JLabel("Делитель:");
    private JFormattedTextField divisorField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JButton autoDivisor = new JButton("Auto");
    private JLabel offsetLabel = new JLabel("Смещение:");
    private JFormattedTextField offsetField = new JFormattedTextField(NumberFormat.getIntegerInstance());

    private JButton okButton = new JButton("OK");


    MatrixParameterDialog(JFrame owner, String title)
    {
        super(owner, title);
        setSize(initialWidth, initialHeight);
        setLayout(new BorderLayout());
        setAlwaysOnTop(true);
        setResizable(false);
        setLocation(100, 100);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                matrixField[i][j] = new JFormattedTextField(NumberFormat.getIntegerInstance());
                matrixPanel.add(matrixField[i][j]);
            }

        matrixPanel.setBorder(new EtchedBorder());

        autoDivisor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sum = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int curValue = 0;
                        if (!matrixField[i][j].getText().equals(""))
                            curValue = Integer.parseInt(matrixField[i][j].getText());
                        sum += curValue;
                    }
                }
                divisorField.setText(sum + "");
            }
        });

        additionalPanel.add(divisorLabel);
        additionalPanel.add(divisorField);
        additionalPanel.add(autoDivisor);
        additionalPanel.add(offsetLabel);
        additionalPanel.add(offsetField);

        add(matrixPanel, BorderLayout.CENTER);
        add(additionalPanel, BorderLayout.WEST);
        add(okButton, BorderLayout.SOUTH);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public int[] closeDialog()
    {
        int[] array = new int[11];
        int sum = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
            {
                int curValue = 0;
                if (!matrixField[i][j].getText().equals(""))
                    curValue = Integer.parseInt(matrixField[i][j].getText());
                array[i * 3 + j] = curValue;
                sum += curValue;
            }

        if (!divisorField.getText().equals(""))
            array[9] = Integer.parseInt(divisorField.getText());
        else
            array[9] = sum;

        if (array[9] <= 0)
            array[9] = 1;

        if (!offsetField.getText().equals(""))
            array[10] = Integer.parseInt(offsetField.getText());
        else
            array[10] = 0;

        this.dispose();
        this.setVisible(false);

        return array;
    }

    public void setListener(Controller c)
    {
        okButton.addActionListener(c);
    }
}
