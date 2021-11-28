package ru.spbu.mt.stepyrev;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/** A class that realizes application. */
public class Application {
    private static final int AGENT_NUMBER = 5;
    private static final int LOW_RANDOM_PARAM = -25;
    private static final int UP_RANDOM_PARAM = 25;

    /** A method that starts the application. */
    public static void main(String[] args) {
        Map<Integer, Double> parameters = generateAgentValues();
        printAgentValues(parameters);
        AlgorithmController controller = new AlgorithmController();
        controller.initAgents(parameters);
    }

    public static int getAgentNumber() {
        return AGENT_NUMBER;
    }

    /**
     * A method that generates agent values in range.
     * @return A Generated map with agent number and value.
     */
    private static Map<Integer, Double> generateAgentValues() {
        Map<Integer, Double> agentMap = new HashMap<>();

        for (int i = 0; i < AGENT_NUMBER; i++) {
            double agentValue = ThreadLocalRandom.current().nextDouble(LOW_RANDOM_PARAM, UP_RANDOM_PARAM);
            agentMap.put(i, agentValue);
        }

        return agentMap;
    }

    /** A method that prints agent values.*/
    private static void printAgentValues(Map<Integer, Double> parameters) {
        System.out.println("Generated agent values:");
        for (Double value : parameters.values()) {
            System.out.println(value);
        }
        double average = parameters.values().stream().mapToDouble(o -> o).average().getAsDouble();
        System.out.println(String.format("Average: %f", average));
    }
}
