package ru.spbu.mt.stepyrev.entity;

import jade.core.AID;
import jade.core.Agent;
import ru.spbu.mt.stepyrev.Application;
import ru.spbu.mt.stepyrev.StateInstance;
import ru.spbu.mt.stepyrev.behaviour.AgentBehaviour;
import ru.spbu.mt.stepyrev.exception.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** A class that realizes an agent used in the algorithm. */
public class AlgorithmAgent extends Agent {
    private static final int AGENT_NUMBER = Application.getAgentNumber();
    private static final String INVALID_ARGUMENT_MESSAGE = "Invalid arguments found while setup an agent";

    /**
     * A method that realises creating an agents.
     */
    @Override
    protected void setup() {
        Object[] arguments = getArguments();

        if (arguments == null || arguments.length != 1) {
            throw new InvalidArgumentException(INVALID_ARGUMENT_MESSAGE);
        }

        List<AID> agents = new ArrayList<>();
        try {
            int agentId = Integer.parseInt(String.valueOf(getAID().getLocalName()));
            double agentValue = Double.parseDouble(String.valueOf(arguments[0]));

            StateInstance.getInstance().setAgentValue(agentId, agentValue);
            for (int i = 0; i < AGENT_NUMBER; i++) {
                if (i == agentId) {
                    continue;
                }

                AID agentAid = new AID(String.valueOf(i), AID.ISLOCALNAME);
                agents.add(agentAid);
            }

        } catch (Exception e) {
            throw new InvalidArgumentException(INVALID_ARGUMENT_MESSAGE);
        }

        addBehaviour(new AgentBehaviour(this, agents, TimeUnit.SECONDS.toMillis(1)));
    }
}
