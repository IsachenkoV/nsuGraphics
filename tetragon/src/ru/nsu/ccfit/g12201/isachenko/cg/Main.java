package ru.nsu.ccfit.g12201.isachenko.cg;

import ru.nsu.ccfit.g12201.isachenko.cg.controller.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.view.View;

import java.io.IOException;

/**
 * Created by Владимир on 19.05.2015.
 */
public class Main {

    static private final String imagePath = "res/earth.png";

    public static void main(String[] args) {
        Model model = null;
        try {
            model = new Model(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Controller controller = new Controller(model);
        View view = new View(controller);
    }
}
