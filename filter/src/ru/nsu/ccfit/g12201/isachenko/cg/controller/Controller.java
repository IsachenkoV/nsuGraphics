package ru.nsu.ccfit.g12201.isachenko.cg.controller;

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
import java.io.IOException;

/**
 * Created by Владимир on 09.03.2015.
 */
public class Controller implements ActionListener, ComponentListener {

    private View view;
    private Model model;

    public ChangeListener gammaController = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            double gamma = source.getValue() / 10.0;
            model.applyGammaCorrection(gamma);
            view.rePaint(model);
        }
    };

    public ChangeListener edgeController = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            int threshold = source.getValue();
            model.applyEdgeSecretion(threshold);
            view.rePaint(model);
        }
    };

    public Controller(Model model)
    {
        this.model = model;
    }

    public void bindView(View view)
    {
        this.view = view;
        view.rePaint(model);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Черно-белый"))
        {
            view.setSlidersDisable();
            model.applyFilter(Model.Filters.BLACK_AND_WHITE);
            view.rePaint(model);
        }

/*        if (e.getActionCommand().equals("Выделение контуров"))
        {
            model.applyFilter(Model.Filters.EDGE);
            view.rePaint(model);
        }
*/
        if (e.getActionCommand().equals("Сглаживание"))
        {
            view.setSlidersDisable();
            model.applyFilter(Model.Filters.BLUR);
            view.rePaint(model);
        }

        if (e.getActionCommand().equals("Резкость"))
        {
            view.setSlidersDisable();
            model.applyFilter(Model.Filters.SHARPEN);
            view.rePaint(model);
        }

        if (e.getActionCommand().equals("Идентичное преобразование"))
        {
            view.setSlidersDisable();
            model.applyFilter(Model.Filters.IDENTICAL);
            view.rePaint(model);
        }

        if (e.getActionCommand().equals("Негатив"))
        {
            view.setSlidersDisable();
            model.applyFilter(Model.Filters.NEGATIVE);
            view.rePaint(model);
        }

        if (e.getActionCommand().equals("Тиснение"))
        {
            view.setSlidersDisable();
            model.applyFilter(Model.Filters.EMBOSS);
            view.rePaint(model);
        }

        if (e.getActionCommand().equals("Акварелизация"))
        {
            view.setSlidersDisable();
            model.applyFilter(Model.Filters.WATERCOLOR);
            view.rePaint(model);
        }

        if (e.getActionCommand().equals("Произвольное матричное преобразование"))
        {
            view.setSlidersDisable();
        }

        /*        if (e.getActionCommand().equals("Гамма-коррекция"))
        {
            model.applyFilter(Model.Filters.GAMMA);
            view.rePaint(model);
        }
        */

        if (e.getActionCommand().equals("Исходное → Текущее"))
        {
            model.pixelCopy(true);
            view.rePaint(model);
        }

        if (e.getActionCommand().equals("Исходное ← Текущее"))
        {
            model.pixelCopy(false);
            view.rePaint(model);
        }

        if (e.getActionCommand().equals("OK"))
        {
            int[] array = view.getMatrixParameters();

            int[][] matrix = new int[3][3];
            for (int i = 0; i < 9; i++)
            {
                int x = i / 3;
                int y = i % 3;
                matrix[x][y] = array[i];
            }

            model.matrixTransform(matrix, array[9], array[10]);
            view.rePaint(model);
        }

        if (e.getActionCommand().equals("File saved"))
        {
            JFileChooser c = (JFileChooser)e.getSource();
            String s = c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName();
            try {
                model.saveFile(c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null,
                        "Can't find file " + s,
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        if (e.getActionCommand().equals("File opened"))
        {
            JFileChooser c = (JFileChooser)e.getSource();
            String s = c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName();
            try {
                model.openNewFile(s);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null,
                        "Can't find file " + s,
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
            }
            view.rePaint(model);
        }
    }

    //component ~ frame resized and jdialog closed
    @Override
    public void componentResized(ComponentEvent e) {
        Point sz = view.getDrawPanelSize();
        model.renderCanvas(sz);
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
}
