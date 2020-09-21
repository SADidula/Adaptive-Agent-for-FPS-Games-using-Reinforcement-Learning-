package Univale.Tcc.RL.Pogamut.Bot;

//import com.thoughtworks.xstream.XStream;
import Univale.Tcc.RL.Pogamut.Actions.ActionEngage;
import Univale.Tcc.RL.Pogamut.Bot.LearnerBase;
import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.IAgent;
import cz.cuni.amis.pogamut.base.utils.guice.AgentScoped;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.*;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.utils.exception.PogamutException;

import java.util.logging.Level;

import Univale.Tcc.RL.Pogamut.Actions.Action;
import Univale.Tcc.RL.Pogamut.Actions.ActionNavPoint;
import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Rotate;

/**
 * @author Didula
 */
@AgentScoped
public class LearnerBot extends LearnerBase {

    private NavPoint newNavPoint = null;

    public LearnerBot(IAgent agent) {
        this.Agent = agent;
    }

    @Override
    public void logic() {
        GameState newState = GameStateFactory.getGameState(getWorldView());

        log.log(Level.INFO, "knowledge size is {0}", Agent.getCost());

        if (StuckDetector.check()) {
            reset();
        }

        if (!newState.equals(CurrentState)) {

            float newQValue = Agent.update(CurrentState, Action, newState, 0);

            log.info("qValue adjustment is " + newQValue);
            Action newAction = Agent.getAction(newState);

            CurrentState = newState;
            Action = newAction;

            if (newAction instanceof ActionNavPoint) {
                NavPoint navPoint = navDict.get(((ActionNavPoint) newAction).getNavPoint());
                //checking whether previous nav point and new nav point are same
                if (navPoint.equals(newNavPoint)) {
                    //generate random nav point
                    navPoint = navPoints.getRandomNavPoint();
                    navigationBehavior.BehaviourDrivenMovement(navPoint);
                    log.info("random nav generation");
                } else {
//                    use nav point got from map
                    navigationBehavior.BehaviourDrivenMovement(navPoint);
                    log.info("using map nav");
                }
                newNavPoint = navPoint;
            } else if (newAction instanceof ActionEngage) {
                //rotate bot
//                getAct().act(new Rotate().setAmount(32000));

                //throw weapon if current ammo is low
                if (agentInfo.getCurrentAmmo() < 1) {
                    System.out.println("Current Weapon: " + agentInfo.getCurrentWeapon() + " Current Ammo: " + agentInfo.getCurrentAmmo());
                    action.throwWeapon();
                }
                assaultbehavior.BehaviourDrivenMovement();
            }

        }

    }

    //inicializa o(s) bot(s)
//    public static void main(String args[]) throws PogamutException {
//
//        new UT2004BotRunner(LearnerBot.class, "Learner").setMain(true).setLogLevel(Level.INFO).startAgents(2);
//    }
}
