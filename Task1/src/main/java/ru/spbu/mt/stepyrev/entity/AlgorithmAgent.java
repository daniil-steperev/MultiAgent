package ru.spbu.mt.stepyrev.entity;

import jade.core.Agent;
import lombok.Data;
import ru.spbu.mt.stepyrev.exception.InvalidArgumentException;

/** A class that realizes an agent used in the algorithm. */
@Data
public class AlgorithmAgent extends Agent {
    /** A field that shows if the agent is central. */
    private boolean isCentral = false;
    /** A field that shows if the agent send his message to the central one. */
    private boolean isSend = false;
    /** A field that stores the element conceived number. */
    private double number;

    /** A field that contains a number of the algorithm agent fields. */
    private final int AGENT_FIELD_NUMBER = 3;
    private final String INVALID_ARGUMENT_MESSAGE = "Invalid arguments found while setup an agent";

    public AlgorithmAgent(boolean isCentral, double number) {
        this.isCentral = isCentral;
        this.number = number;
    }

    @Override
    protected void setup() {
        Object[] arguments = getArguments();

        if (arguments == null || arguments.length != AGENT_FIELD_NUMBER) {
            throw new InvalidArgumentException(INVALID_ARGUMENT_MESSAGE);
        }

        System.out.println(String.format("Agent #%s with number = %f is created", getAID().getLocalName(), number));
    }

    public void sendMessage() {

    }
}
