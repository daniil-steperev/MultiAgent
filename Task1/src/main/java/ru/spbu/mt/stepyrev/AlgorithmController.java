package ru.spbu.mt.stepyrev;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.List;

/** A class that realizes a controller of the algorithm. */
public class AlgorithmController {
    private static final String HOST = "localhost";
    private static final String PORT = "9090";
    private static final String GUI = "true";
    private static final String AGENT_PACKAGE_NAME = "ru.spbu.mt.stepyrev.entity";
    private static final String CENTRAL_NICKNAME = "Central";

    private ContainerController containerController;

    /**
     * A method that inits agents in the application.
     * @param arguments --- passed arguments of the agents
     */
    public void initAgents(List<Double> arguments) {
        containerController = initMainContainerController();

        try {
            int agentsNumber = arguments.size();
            initCentralAgent(agentsNumber);

            Object[] setupArguments;
            AgentController agentController;
            for (int i = 0; i < agentsNumber; i++) {
                setupArguments = new Object[] {false, arguments.get(i), agentsNumber, CENTRAL_NICKNAME};
                agentController = containerController.createNewAgent(String.valueOf(i), AGENT_PACKAGE_NAME, setupArguments);
                agentController.start();
            }
        } catch (StaleProxyException e) {
            // FIXME: add check here
        }
    }

    /**
     * A method that initializes a central agent.
     */
    private void initCentralAgent(int agentsNumber) {
        Object[] setupArguments = new Object[] {true, 0, agentsNumber, 0};

        try {
            AgentController centralAgent = containerController.createNewAgent(
                    CENTRAL_NICKNAME, AGENT_PACKAGE_NAME, setupArguments);
            centralAgent.start();
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
