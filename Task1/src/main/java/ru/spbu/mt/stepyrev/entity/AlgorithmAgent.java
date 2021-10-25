package ru.spbu.mt.stepyrev.entity;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import lombok.Data;
import ru.spbu.mt.stepyrev.behaviour.AgentBehaviour;
import ru.spbu.mt.stepyrev.exception.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** A class that realizes an agent used in the algorithm. */
@Data
public class AlgorithmAgent extends Agent {
    /** A field that shows if the agent is central. */
    private boolean isCentral = false;
    /** A field that stores the element conceived number. */
    private double number;
    /** A field that stores the element of all agents in the application. */
    private int agentsNumber;
    /** A field that stores the central agent aid of the algorithm. */
    private AID centralAgentAid;

    // This field is for simple agent
    /** A field that shows if the agent send his message to the central one. */
    private boolean isSend = false;

    // These fields are for central agent
    /** A field that stores all received numbers. */
    private List<Double> receivedNumbers = new ArrayList<>();
    /** A field that stores the quantity of all received numbers. */
    private int receivedNumberQuantity = 0;
    /** A field that stores the result. */
    private Double result = null;

    /** A field that contains a number of the algorithm agent fields. */
    private final int AGENT_FIELD_NUMBER = 4;
    private final String INVALID_ARGUMENT_MESSAGE = "Invalid arguments found while setup an agent";

    public AlgorithmAgent(boolean isCentral, double number) {
        this.isCentral = isCentral;
        this.number = number;
    }

    /**
     * A method that realises creating an agent.
     *
     * In arguments passed a several parameters: [isCentral, number, agentsNumber, centralAid].
     */
    @Override
    protected void setup() {
        Object[] arguments = getArguments();

        if (arguments == null || arguments.length != AGENT_FIELD_NUMBER) {
            throw new InvalidArgumentException(INVALID_ARGUMENT_MESSAGE);
        }

        try {
            this.isCentral = Boolean.parseBoolean((String) arguments[0]);
            this.number = Double.parseDouble((String) arguments[1]);
            this.agentsNumber = Integer.parseInt((String) arguments[2]);

            String centralAid = (String) arguments[3];
            this.centralAgentAid = new AID(centralAid, AID.ISLOCALNAME);
        } catch (Exception e) {
            throw new InvalidArgumentException(INVALID_ARGUMENT_MESSAGE);
        }

        addBehaviour(new AgentBehaviour(this, TimeUnit.SECONDS.toMillis(1)));
        System.out.println(String.format("Agent #%s with number = %f is created", getAID().getLocalName(), number));
    }

    /** A method that sends the agent number to the central one. */
    public void sendNumberToCentral() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(centralAgentAid);
        message.setContent(String.valueOf(number));

        try {
            send(message);
        } finally {
            isSend = true;
        }
    }

    /**
     * A method that realizes receiving messages.
     *
     * If a simple agent receives a message, than ignore it.
     * If a central agent received all numbers than set it to the calledResult.
     * @param message
     */
    public void receiveMessage(ACLMessage message) {
        if (!isCentral) {
            return;
        }

        try {
            double receivedNumber = Double.parseDouble(message.getContent());
            this.receivedNumbers.add(receivedNumber);
            this.receivedNumberQuantity += 1;
        } catch (Exception e) {
            throw new InvalidArgumentException(INVALID_ARGUMENT_MESSAGE);
        }

        // All numbers are received
        if (receivedNumberQuantity == agentsNumber) {
            double numbersSum = receivedNumbers
                    .stream()
                    .reduce(0d, Double::sum);
            result = numbersSum / receivedNumberQuantity;
        }
    }

    /**
     * A method that returns if the algorithm counted the result.
     * @return --- true if the agent is central and result is counted
     */
    public boolean isResultCounted() {
        return isCentral && result != null;
    }
}
