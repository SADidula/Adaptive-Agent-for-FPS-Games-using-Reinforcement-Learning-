package Univale.Tcc.RL.Pogamut.Actions;

/**
 * @author Didula
 */
public class ActionEngage extends Action {

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other instanceof ActionEngage) {
            return true;
        }
        return false;
    }
}
