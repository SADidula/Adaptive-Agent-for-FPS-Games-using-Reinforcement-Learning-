package Univale.Tcc.RL.Pogamut.Behaviors.Navigation;

import Univale.Tcc.RL.Pogamut.Behaviors.Abstract.Behavior;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.IUT2004Navigation;
import cz.cuni.amis.pogamut.ut2004.bot.command.ImprovedShooting;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

/**
 * @author Didula
 */
public class NavigationBehavior extends Behavior {

    public NavigationBehavior(ImprovedShooting shoot, IUT2004Navigation navigation) {
        super(null, shoot, navigation);
    }

    public void BehaviourDrivenMovement(NavPoint navPoint) {
        shoot.stopShooting();
        navigation.navigate(navPoint);
    }

}
