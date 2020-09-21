package Univale.Tcc.RL.Pogamut.Actions;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;

import java.util.List;

/**
 * @author Didula
 */
public class ActionNavPoint extends Action {

    private String NavPoint;

    public String getNavPoint() {
        return NavPoint;
    }

    public void setNavPoint(String navPoint) {
        NavPoint = navPoint;
    }

    public ActionNavPoint(String navPoint) {
        NavPoint = navPoint;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof ActionNavPoint)) {
            return false;
        } else if ((((ActionNavPoint) other).getNavPoint()) == this.getNavPoint()) {
            return true;
        }
        return false;
    }
}
