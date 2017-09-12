package ru.nsu.ccfit.g12201.isachenko.cg;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import ru.nsu.ccfit.g12201.isachenko.cg.control.Controller;
import ru.nsu.ccfit.g12201.isachenko.cg.model.Model;
import ru.nsu.ccfit.g12201.isachenko.cg.view.View;


/**
 * Created by Владимир on 24.02.2015.
 */

public class Puzzle {
    static private final String imagePath = "res/puzzle.png";
    public static void main(String args[]) {
        System.out.println(1 + 2 + "3");
        System.out.println('a' + 1);
        System.out.println("1" + 2 + 3);
        System.out.println(5 / 4);
        System.out.println(4 + 1.0f);

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
