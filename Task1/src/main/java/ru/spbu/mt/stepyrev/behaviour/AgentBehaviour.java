package ru.spbu.mt.stepyrev.behaviour;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ru.spbu.mt.stepyrev.entity.AlgorithmAgent;

/** A class that realizes an agent behaviour. */
public class AgentBehaviour extends TickerBehaviour {
    private AlgorithmAgent agent;

    public AgentBehaviour(AlgorithmAgent agent, long period) {
        super(agent, period);
        this.agent = agent;
        this.setFixedPeriod(true);
    }

    @Override
    protected void onTick() {
        if (!agent.isCentral()) {
            agent.sendNumberToCentral();
        }

        if (!agent.isCentral() && agent.isSend()) {
            stop();
        }

        ACLMessage message = agent.blockingReceive();
        if (message != null) {
            agent.receiveMessage(message);
        }

        if (agent.isResultCounted()) {
            System.out.println(
                    String.format("Result is counted, the average number of all elements = %f", agent.getResult()));
            stop();
        }
    }
}
