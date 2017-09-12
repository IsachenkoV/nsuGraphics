package ru.nsu.ccfit.g12201.isachenko.cg.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

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
}
