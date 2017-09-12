package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Владимир on 09.03.2015.
 */
public class ControlPanel extends JPanel {
    private JRadioButton blackAndWhite = new JRadioButton("Черно-белый");

    private JRadioButton edge = new JRadioButton("Выделение контуров");
    private JSlider edgeSlider = new JSlider(0, 255, 85);

    private JRadioButton blur = new JRadioButton("Сглаживание");
    private JRadioButton sharpen = new JRadioButton("Резкость");
    private JRadioButton identical = new JRadioButton("Идентичное преобразование");
    private JRadioButton negative = new JRadioButton("Негатив");
    private JRadioButton emboss = new JRadioButton("Тиснение");
    private JRadioButton watercolor = new JRadioButton("Акварелизация");

    private JRadioButton gamma = new JRadioButton("Гамма-коррекция");
    private JSlider gammaSlider = new JSlider(1, 100, 1);

    private JRadioButton matrix = new JRadioButton("Произвольное матричное преобразование");
    private MatrixParameterDialog matrixDialog;

    private JButton savePic = new JButton("Сохранить");
    private JButton copySrcToDst = new JButton("Исходное → Текущее");
    private JButton copyDstToSrc = new JButton("Исходное ← Текущее");
    private JButton openFile = new JButton("Открыть файл");

    private ButtonGroup buttonGroup = new ButtonGroup();

    private JFileChooser chooser = new JFileChooser();

    ControlPanel(final JFrame owner)
    {
        setLayout(new GridLayout(16, 1));
        setBorder(new EtchedBorder());

        add(blackAndWhite);

        add(edge);
        edgeSlider.setEnabled(false);

        edge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gammaSlider.setEnabled(false);
                edgeSlider.setEnabled(true);
                edgeSlider.getChangeListeners()[0].stateChanged(new ChangeEvent(edgeSlider));
            }
        });

        add(edgeSlider);

        add(blur);
        add(sharpen);
        add(identical);
        add(negative);
        add(emboss);
        add(watercolor);

        add(gamma);
        gamma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edgeSlider.setEnabled(false);
                gammaSlider.setEnabled(true);
                gammaSlider.getChangeListeners()[0].stateChanged(new ChangeEvent(gammaSlider));
            }
        });

        gammaSlider.setEnabled(false);
        gammaSlider.setMajorTickSpacing(50);
        gammaSlider.setMinorTickSpacing(5);
        gammaSlider.setPaintTicks(true);
        add(gammaSlider);

        add(matrix);
        matrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edgeSlider.setEnabled(false);
                gammaSlider.setEnabled(false);
                matrixDialog.setVisible(true);
            }
        });
        matrixDialog = new MatrixParameterDialog(owner, "Set your parameters");

        add(copySrcToDst);
        add(copyDstToSrc);

        savePic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rVal = chooser.showSaveDialog(owner);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    chooser.getActionListeners()[0].actionPerformed(new ActionEvent(chooser, 0, "File saved"));
                }
            }
        });
        add(savePic);

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rVal = chooser.showOpenDialog(owner);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    chooser.getActionListeners()[0].actionPerformed(new ActionEvent(chooser, 0, "File opened"));
                }
            }
        });
        add(openFile);

        buttonGroup.add(blackAndWhite);
        buttonGroup.add(edge);
        buttonGroup.add(blur);
        buttonGroup.add(sharpen);
        buttonGroup.add(identical);
        buttonGroup.add(negative);
        buttonGroup.add(emboss);
        buttonGroup.add(watercolor);
        buttonGroup.add(gamma);
        buttonGroup.add(matrix);
    }

    public void setListener(Controller c)
    {
        blackAndWhite.addActionListener(c);
        edge.addActionListener(c);
        blur.addActionListener(c);
        sharpen.addActionListener(c);
        identical.addActionListener(c);
        negative.addActionListener(c);
        emboss.addActionListener(c);
        watercolor.addActionListener(c);
        gamma.addActionListener(c);
        matrixDialog.setListener(c);
        copyDstToSrc.addActionListener(c);
        copySrcToDst.addActionListener(c);

        chooser.addActionListener(c);

        gammaSlider.addChangeListener(c.gammaController);
        edgeSlider.addChangeListener(c.edgeController);
    }

    public void setSlidersDisable()
    {
        edgeSlider.setEnabled(false);
        gammaSlider.setEnabled(false);
    }

    public int[] getMatrixParameters() {
        return matrixDialog.closeDialog();
    }
}
