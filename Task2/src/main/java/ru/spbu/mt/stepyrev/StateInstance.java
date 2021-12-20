package ru.spbu.mt.stepyrev;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class StateInstance {
    // Number of agents
    private static final int AGENT_NUMBER = Application.getAgentNumber();

    private double alpha = 1d / AGENT_NUMBER;

    // Beta from local voting protocol formula
    private double beta = 1;

    // Ticks counter
    private int counter = 0;

    // Maximum number of ticks
    private static final int MAX_COUNTER = 15;

    // Flags to check if agent has sent message on current tick
    private List<Boolean> sentAgentsList = new ArrayList<>(Collections.nCopies(AGENT_NUMBER, false));

    // Flags to check if agent has been updated on current tick
    private List<Boolean> updatedAgentsList = new ArrayList<>(Collections.nCopies(AGENT_NUMBER, false));

    private List<Double> agentValues = new ArrayList<>(Collections.nCopies(AGENT_NUMBER, 0.0));

    private List<Double> uList = new ArrayList<>(Collections.nCopies(AGENT_NUMBER, 0.0));

    private static StateInstance self = new StateInstance();

    private boolean isResultCounted = false;

    // Getter of the instance of object
    public static StateInstance getInstance() { return self; }

    /** A method that increments the counter. */
    public void incrementCounter() {
        counter++;
    }

    /** A method that checks if the counter has reached its maximum value maxCounter.*/
    public boolean counterFinished() {
        return counter >= MAX_COUNTER;
    }

    /** A method that checks if the agent has sent his value.*/
    public boolean isSendByAid(int id) {
        return sentAgentsList.get(id);
    }

    public boolean isUpdatedByAid(int aid) {
        return updatedAgentsList.get(aid);
    }

    public void setUpdatedByAid(int aid) {
        updatedAgentsList.set(aid, true);
    }

    public boolean isAllUpdated() {
        return !updatedAgentsList.contains(false);
    }

    public void resetUpdatedList() {
        updatedAgentsList = new ArrayList<>(Collections.nCopies(AGENT_NUMBER, false));
    }

    /** A method that checks if all agents sent their values.*/
    public boolean checkAllSentAgents() {
        return !sentAgentsList.contains(false);
    }

    /** A method that sets the sent agent.*/
    public void setSentAgent(int id) {
        sentAgentsList.set(id, true);
    }

    /** A method that gets the agent value.*/
    public double getAgentValue(int id) {
        return agentValues.get(id);
    }

    /** A method that sets the agent value.*/
    public void setAgentValue(int id, double val) {
        agentValues.set(id, val);
    }

    /** A method that gets the 'u' value. */
    public double getUValue(int id) {
        return uList.get(id);
    }

    /** A method that sets the 'u' value. */
    public void setUValue(int id, double val) {
        uList.set(id, val);
    }

    /** A method that resets the sent agents list. */
    public void resetSentList() {
        sentAgentsList = new ArrayList<>(Collections.nCopies(AGENT_NUMBER, false));
    }

    /** A method that resets the 'u' list. */
    public void resetUS() {
        uList = new ArrayList<>(Collections.nCopies(AGENT_NUMBER, 0.0));
    }
}
