package Univale.Tcc.RL.Pogamut.DecisionMaking.GameState;

import Univale.Tcc.RL.Pogamut.Actions.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Didula
 */
public final class GameState {

    private List<Action> availableActions;

    //enemy count (max 3)
    private int EnemiesCount;

    //Navpoint closest to the bot
    private String BotPosition;

    //id of previous navpoint
    //this is fundamental to determine the next movement
    //if the bot was coming from the south then he can conclude that he has already collected the items from that direction
    //and move to a less explored location
    private String PreviousPosition;

    public String getPreviousPosition() {
        return PreviousPosition;
    }

    public void setPreviousPosition(String previousPosition) {
        PreviousPosition = previousPosition;
    }

    //is health low?
    private boolean HealthLow;

    //currently running
    private String NearestEnemyPosition;

    public String getNearestEnemyPosition() {
        return NearestEnemyPosition;
    }

    public void setNearestEnemyPosition(String nearestEnemyPosition) {
        NearestEnemyPosition = nearestEnemyPosition;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof GameState)) {
            return false;
        }
        GameState otherState = (GameState) other;
        return this.getEnemiesCount() == otherState.getEnemiesCount()
                && this.getNearestEnemyPosition().equals(otherState.getNearestEnemyPosition())
                && this.isHealthLow() == otherState.isHealthLow()
                && this.getPreviousPosition().equals(otherState.getPreviousPosition())
                && this.getBotPosition().equals(otherState.getBotPosition());
    }

    /**
     * @return the enemiesCount
     */
    public int getEnemiesCount() {
        return EnemiesCount;
    }

    /**
     * @param enemiesCount the enemiesCount to set
     */
    public void setEnemiesCount(int enemiesCount) {
        this.EnemiesCount = (enemiesCount > 3 || enemiesCount < 0) ? 3 : enemiesCount;
    }

    /**
     * @return the healthLow
     */
    public boolean isHealthLow() {
        return HealthLow;
    }

    /**
     * @param healthLow the healthLow to set
     */
    public void setHealthLow(boolean healthLow) {
        this.HealthLow = healthLow;
    }

    /**
     * @return the action
     */
    public Action getAction(Action action) throws ActionNotFoundException {

        Optional<Action> result = availableActions.stream().filter(a -> a.equals(action)).findFirst();

        if (!result.isPresent()) {
            throw new ActionNotFoundException("action not found on the available actions list");
        }
        return result.get();
    }

    /**
     * @return the NavPoint
     */
    public String getBotPosition() {
        return BotPosition;
    }

    /**
     * @param BotPosition the NavPoint to set
     */
    public void setBotPosition(String botPosition) {
        this.BotPosition = botPosition;
    }

    public List<Action> getAvailableActions() {
        return availableActions;
    }

    public void setAvailableActions(List<Action> availableActions) {
        this.availableActions = availableActions;
    }

    /**
     * @return the qValue
     */
    public double getMaximunQValue() {
        return getAvailableActions().stream().map(action -> action.getQValue()).max(Double::compareTo).orElse(0d);
    }

    //public void updateActionQValue(Action action, float qValueAdjustment, double alpha) throws ActionNotFoundException
    public void updateActionQValue(Action action, float qValueAdjustment) throws ActionNotFoundException {
        if (!availableActions.contains(action)) {
            throw new ActionNotFoundException("action not found on the available actions list");
        }
        //availableActions.get(availableActions.indexOf(action)).updateQValue(qValueAdjustment, alpha);
        availableActions.get(availableActions.indexOf(action)).updateQValue(qValueAdjustment);
    }

    public class ActionNotFoundException extends Exception {
        public ActionNotFoundException(String message) {
            super(message);
        }
    }

    public GameState() {
        setNearestEnemyPosition("");
        setBotPosition("");
        setPreviousPosition("");
    }
}
