package ru.spbu.mt.stepyrev;

import ru.spbu.mt.stepyrev.util.NumberReader;

import java.util.List;

/** A class that realizes application. */
public class Application {
    /** A method that starts the application. */
    public static void main(String[] args) {
        List<Double> parameters = NumberReader.readInitParameters();
        AlgorithmController controller = new AlgorithmController();
        controller.initAgents(parameters);
    }
}
