package ru.nsu.ccfit.g12201.isachenko.cg;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.view.View;

import java.io.IOException;

/**
 * Created by Владимир on 17.04.2015.
 */
public class Main {
    static private final String inputFilePath = "res/in.txt";

    public static void main(String args[])
    {
        Model model = null;
        try {
            model = new Model(inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Controller controller = new Controller(model);
        View view = new View(controller);
    }
}
