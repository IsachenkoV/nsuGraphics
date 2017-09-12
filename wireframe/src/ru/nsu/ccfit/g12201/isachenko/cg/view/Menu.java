package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Владимир on 25.04.2015.
 */
public class Menu extends JMenuBar {

    private Frame owner;
    private JMenu anotherMenu = new JMenu("Menu");
    private JMenuItem addNewModel = new JMenuItem("Add new model");
    private JMenuItem deletePreviousModel = new JMenuItem("Delete previous model");
    private JMenuItem parameters = new JMenuItem("Parameters");

    public AddingModelDialog addingModelDialog;
    public ParamsDialog paramsDialog;

    private int figuresCount = 1;

    Menu(Frame owner) {
        paramsDialog = new ParamsDialog(owner);

        this.owner = owner;
        anotherMenu.add(addNewModel);
        anotherMenu.add(deletePreviousModel);
        anotherMenu.add(parameters);

        add(anotherMenu);
    }

    public void setController(final Controller c) {
        addNewModel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (figuresCount == 10)
                {
                    JOptionPane.showMessageDialog(null,
                            "Count of figures must be less than 10!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    addingModelDialog = new AddingModelDialog(owner);
                    addingModelDialog.setController(c);
                    addingModelDialog.setVisible(true);
                }
            }
        });

        parameters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paramsDialog.setVisible(true);
                paramsDialog.setController(c);
            }
        });

        deletePreviousModel.addActionListener(c);
    }
}
