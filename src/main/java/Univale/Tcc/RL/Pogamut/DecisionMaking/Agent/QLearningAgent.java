package Univale.Tcc.RL.Pogamut.DecisionMaking.Agent;

import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;

import java.util.*;

import Univale.Tcc.RL.Pogamut.Actions.Action;

/**
 * @author Didula
 */
public class QLearningAgent extends BaseAgent implements IAgent {

    public QLearningAgent() {
        super();
    }

    public QLearningAgent(double epsilon, double gamma, double alpha) {
        super(epsilon, gamma, alpha);
    }

    //returns qvalue of the action informed in the state
    //returns empty if the state is not known
    public Optional<Double> getQValue(GameState state, Action action) {
        Optional<Double> qValue = Optional.empty();
        try {
            Optional<GameState> state1 = states.stream().filter(st -> st.equals(state))
                    .findFirst();
            qValue = Optional.of(state1.get().getAction(action).getQValue());
        } catch (Exception e) {
            System.err.println("Cannot get Qvalue");
        }
        return qValue;
    }

    //returns maximum state qvalue
    Optional<Double> getMaxQValue(GameState state) {
        return states.stream().filter(s -> s.equals(state))
                .findFirst()
                .get().getAvailableActions().stream()
                .max((action1, action2) -> Double.compare(action1.getQValue(), action2.getQValue()))
                .map(action -> action.getQValue());
    }

    //returns the best action to be taken in the informed state
    Optional<Action> getBestAction(GameState state) {
        List<Action> actions = state.getAvailableActions();
        Collections.shuffle(actions);
        return actions.stream()
                .max((action1, action2) -> Double.compare(action1.getQValue(), action2.getQValue()));
    }

    //returns the best action or a random action with epsilon probability
    public Action getAction(GameState state) {
        Action result;
        List<Action> actions = state.getAvailableActions();
        // e-greedy - choose a random action with epsilon probability
        Double probResult = randomNumberGenerator.nextDouble();
        System.out.println("\n\n\n Action size " + actions.size() + " probResult " + probResult + "\n\n\n");
        if (probResult < epsilon) {
            result = actions.get(randomNumberGenerator.nextInt(actions.size()));
        } else {
            Optional<Action> action = getBestAction(state);
            result = action.get();
        }
        return result;
    }

    //updates QValue
    public float update(GameState oldState, Action chosenAction, GameState newState, double reward) {
        if (states == null) {
            return 0;
        }
        if (states.contains(newState)) {
            newState = states.get(states.indexOf(newState));
        } else {
            states.add(newState);
        }
        if (oldState == null || chosenAction == null) {
            return 0;
        }
        GameState targetState = states.get(states.indexOf(oldState));

        //calculation of the new qValue in the Q-learning algorithm
        //Q(s, a) = Q(s, a) + alpha(r + gamma(max(Q(s', a')) - Q(s, a))
        double newStateQValue = newState.getMaximunQValue();
        System.out.println("\n\n\n\n" + newStateQValue + "\n\n\n\n\n");
        double targetStateQValue = getAction(targetState).getQValue();
        float qValueAdjustment = 0;
        try {
            qValueAdjustment = (float) (alpha * (reward + (gamma * (newStateQValue - targetStateQValue))));
            targetState.updateActionQValue(chosenAction, qValueAdjustment);
        } catch (GameState.ActionNotFoundException e) {
            System.err.println("Cannot update Qvalue");
        }
        return qValueAdjustment;
    }

}
