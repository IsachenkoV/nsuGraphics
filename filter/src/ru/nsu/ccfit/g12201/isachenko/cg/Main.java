package ru.nsu.ccfit.g12201.isachenko.cg;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.view.View;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Владимир on 09.03.2015.
 */
public class Main {

    static private final String imagePath = "res/filter.jpg";

    static private boolean error = false;

    public static void main(String args[])
    {
        Model model = null;
        try {
            model = new Model(imagePath);
        } catch (IOException e) {
            error = true;
        }

        if (error)
        {
            JOptionPane.showMessageDialog(null,
                    "Can't find source file 'filter.jpg'.\n" +
                            "Try to add this file in folder 'res'.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Controller controller = new Controller(model);
        View view = new View(controller);
    }
}
