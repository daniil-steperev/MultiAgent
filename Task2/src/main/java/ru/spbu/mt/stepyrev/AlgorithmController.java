package ru.spbu.mt.stepyrev;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.Map;

/** A class that realizes a controller of the algorithm. */
public class AlgorithmController {
    private static final String HOST = "localhost";
    private static final String PORT = "10098";
    private static final String GUI = "true";
    private static final String AGENT_PACKAGE_NAME = "ru.spbu.mt.stepyrev.entity.AlgorithmAgent";

    private ContainerController containerController;

    /**
     * A method that inits agents in the application.
     * @param agentValues --- passed arguments of the agents
     */
    public void initAgents(Map<Integer, Double> agentValues) {
        containerController = initMainContainerController();

        try {
            AgentController agentController;
            Object[] agentArguments;
            int agentsNumber = agentValues.size();
            for (int i = 0; i < agentsNumber; i++) {
                agentArguments = new Object[] {agentValues.get(i)};
                agentController = containerController.createNewAgent(String.valueOf(i), AGENT_PACKAGE_NAME, agentArguments);
                agentController.start();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that initializes main container controller.
     * @return --- initialized main container controller
     */
    private ContainerController initMainContainerController() {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();

        profile.setParameter(Profile.MAIN_HOST, HOST);
        profile.setParameter(Profile.MAIN_PORT, PORT);
        profile.setParameter(Profile.GUI, GUI);

        return runtime.createMainContainer(profile);
    }
}
