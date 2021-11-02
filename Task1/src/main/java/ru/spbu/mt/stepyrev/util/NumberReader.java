package ru.spbu.mt.stepyrev.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** A class that realizes a number reader. */
public class NumberReader {
    private final static String INIT_FILE_NAME = "src/main/resources/initfile.txt";

    /**
     * A method that reads parameters from init file.
     * @return --- an array list with parameters
     */
    public static List<Double> readInitParameters() {
        List<Double> params = new ArrayList<>();

        File file = new File(INIT_FILE_NAME);
        System.out.println("ABS " + file.getAbsolutePath());
        try(BufferedReader reader = new BufferedReader(new FileReader(INIT_FILE_NAME))) {
            while (reader.ready()) {
                String param = reader.readLine();
                params.add(Double.parseDouble(param));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }
}
