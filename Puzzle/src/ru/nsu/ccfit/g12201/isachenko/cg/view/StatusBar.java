package ru.nsu.ccfit.g12201.isachenko.cg.view;

import ru.nsu.ccfit.g12201.isachenko.cg.control.Controller;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by Владимир on 24.02.2015.
 */
public class StatusBar extends JPanel {

    private JLabel text;

    StatusBar()
    {
        super();
        setLayout(new BorderLayout());
        text = new JLabel("This is status bar");
        add(text, BorderLayout.CENTER);
        setBorder(new BevelBorder(BevelBorder.LOWERED));
    }

    void setText(String s)
    {
        text.setText(s);
    }

    public void registerListener() {
    }
}
