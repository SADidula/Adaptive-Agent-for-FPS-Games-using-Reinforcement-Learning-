package Univale.Tcc.RL.Pogamut.Behaviors.Agressive;

import Univale.Tcc.RL.Pogamut.Behaviors.Abstract.Behavior;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.Players;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.WeaponPrefs;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.IUT2004Navigation;
import cz.cuni.amis.pogamut.ut2004.bot.command.ImprovedShooting;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;

/**
 * @author Didula
 */
public class AssaultBehavior extends Behavior {

    private WeaponPrefs weaponPrefs;

    public AssaultBehavior(Players players, ImprovedShooting shoot, IUT2004Navigation navigation, WeaponPrefs weaponPrefs) {
        super(players, shoot, navigation);
        this.weaponPrefs = weaponPrefs;
    }

    public void BehaviourDrivenMovement() {
        Player target = getPlayers().getNearestVisiblePlayer(getPlayers().getVisibleEnemies().values());
        navigation.navigate(target);
        shoot.shoot(weaponPrefs, target);
    }

}
