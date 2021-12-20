package ru.spbu.mt.stepyrev.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ru.spbu.mt.stepyrev.StateInstance;
import ru.spbu.mt.stepyrev.entity.AlgorithmAgent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** A class that realizes an agent behaviour.*/
public class AgentBehaviour extends TickerBehaviour {
    private static final double LOW_INTERFERENCE_BORDER = -1;
    private static final double UP_INTERFERENCE_BORDER = 1;
    private static final double CONNECTION_DROP_PROBABILITY = 0.15;
    private static final long MILLIS_TO_SET_CONNECTION = 50;

    private final AlgorithmAgent agent;
    private final List<AID> agentAidList;
    private final int currentAgentAid;

    public AgentBehaviour(AlgorithmAgent agent, List<AID> agentAidList, long period) {
        super(agent, period);
        this.agent = agent;
        this.agentAidList = agentAidList;
        this.currentAgentAid = Integer.parseInt(agent.getAID().getLocalName());
        this.setFixedPeriod(true);
    }

    @Override
    protected void onTick() {
        if (StateInstance.getInstance().isResultCounted()) {
            stop();
        }

        if (StateInstance.getInstance().isUpdatedByAid(currentAgentAid)) {
            return;
        }

        if (!StateInstance.getInstance().isSendByAid(currentAgentAid)) {
            for (AID agentAid : agentAidList) {
                simulateDisconnection();
                sendValueWithInterference(agentAid);
            }

            StateInstance.getInstance().setSentAgent(currentAgentAid);
        }

        receiveMessage();
        if (!StateInstance.getInstance().checkAllSentAgents()) {
            return;
        }

        updateAgentValue();

        if (StateInstance.getInstance().isAllUpdated()) {
            StateInstance.getInstance().resetSentList();
            StateInstance.getInstance().resetUS();
            StateInstance.getInstance().resetUpdatedList();
            StateInstance.getInstance().incrementCounter();
            System.out.println("");
        }

        double result = StateInstance.getInstance().getAgentValue(currentAgentAid);
        if (StateInstance.getInstance().counterFinished()) {
            System.out.printf("Result = %f%n", result);
            StateInstance.getInstance().setResultCounted(true);
            stop();
        }
    }

    /** A method that that simulates disconnection.*/
    private void simulateDisconnection() {
        if (Math.random() < CONNECTION_DROP_PROBABILITY) {
            // System.out.println(String.format("Agent %d connection dropped", currentAgentAid));
            agent.doWait(MILLIS_TO_SET_CONNECTION);
        }
    }

    /** A method that sends the message with interference.*/
    private void sendValueWithInterference(AID agentAid) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(agentAid);
        double agentValue = StateInstance.getInstance().getAgentValue(currentAgentAid);
        double interference = ThreadLocalRandom.current().nextDouble(LOW_INTERFERENCE_BORDER, UP_INTERFERENCE_BORDER);
        double sendValue = agentValue + interference;
        message.setContent(String.valueOf(sendValue));
        /**
        System.out.println(
                String.format(
                        "Agent %d send value to agent %d value %f with interference %f",
                        currentAgentAid,
                        Integer.parseInt(agentAid.getLocalName()),
                        sendValue,
                        interference));
         */
        agent.send(message);
    }

    /** A method that realizes receiving message from another agents.*/
    private void receiveMessage() {
        ACLMessage message = agent.receive();
        if (message == null) {
            return;
        }

        double curX = StateInstance.getInstance().getAgentValue(currentAgentAid);
        double receivedX = Double.parseDouble(message.getContent());
        double delta = StateInstance.getInstance().getBeta() * (receivedX - curX);
        double prevU = StateInstance.getInstance().getUValue(currentAgentAid);
        StateInstance.getInstance().setUValue(currentAgentAid,prevU + delta);
    }

    /** A method that updates the agent value.*/
    private void updateAgentValue() {
        double countedU = StateInstance.getInstance().getUValue(currentAgentAid);
        double alpha = StateInstance.getInstance().getAlpha();
        double prevValue = StateInstance.getInstance().getAgentValue(currentAgentAid);
        double nextValue = prevValue + alpha * countedU;
        System.out.println(String.format("Agent %d set new value: %f", currentAgentAid, nextValue));
        StateInstance.getInstance().setAgentValue(currentAgentAid, nextValue);
        StateInstance.getInstance().setUpdatedByAid(currentAgentAid);
    }
}
