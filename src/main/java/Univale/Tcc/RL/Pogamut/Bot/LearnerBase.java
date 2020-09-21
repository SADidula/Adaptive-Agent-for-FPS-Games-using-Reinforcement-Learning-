package Univale.Tcc.RL.Pogamut.Bot;

import Univale.Tcc.RL.Pogamut.Behaviors.Abstract.Behavior;
import Univale.Tcc.RL.Pogamut.Behaviors.Agressive.AssaultBehavior;
import Univale.Tcc.RL.Pogamut.Behaviors.Navigation.NavigationBehavior;
import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.IAgent;
import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.QLearningAgent;
import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;
import Univale.Tcc.RL.Pogamut.Services.Statistics.Statistics;
import Univale.Tcc.RL.Pogamut.WeaponSelection.WeaponSelector;
import cz.cuni.amis.introspection.java.JProp;
import cz.cuni.amis.pogamut.base.communication.worldview.event.IWorldEventListener;
import cz.cuni.amis.pogamut.base.utils.guice.AgentScoped;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensomotoric.Weapon;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.AgentInfo;
import cz.cuni.amis.pogamut.ut2004.bot.command.Action;
import cz.cuni.amis.pogamut.ut2004.bot.command.ImprovedShooting;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotModuleController;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Initialize;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Didula
*/
@AgentScoped
public class LearnerBase extends UT2004BotModuleController<UT2004Bot> {

    @JProp
    public int frags = 0;

    @JProp
    public int deaths = 0;

    public double damageDone = 0.0, damageRecieved = 0.0;

    protected IAgent Agent;

    protected AgentInfo agentInfo;

    protected WeaponSelector weaponSelector;

    protected ImprovedShooting shoot;
    
    protected Action action;

    protected Map<String, NavPoint> navDict;
    protected Univale.Tcc.RL.Pogamut.Actions.Action Action;
    protected GameState CurrentState;
    protected Univale.Tcc.RL.Pogamut.Services.StuckDetector.StuckDetector StuckDetector;
    protected Univale.Tcc.RL.Pogamut.Services.GameStateFactory.GameStateFactory GameStateFactory;
    protected Statistics statistics;

    //behaviors
//     protected Behavior assaultbehavior,navigationBehavior;
    AssaultBehavior assaultbehavior;
    NavigationBehavior navigationBehavior;

    IWorldEventListener<BotDamaged> botDamagedListener = new IWorldEventListener<BotDamaged>() {

        @Override
        public void notify(BotDamaged event) {
            log.info("bot has been damaged " + damageRecieved);
            log.info("\nbot has been damaged " + event);

            //penalty
            damageRecieved = event.getDamage();
            Double reward = 0 - (damageRecieved * 2);

            //Double reward = -1d;
            GameState state = GameStateFactory.getGameState(getWorldView());
            Agent.update(CurrentState, Action, state, reward);
            statistics.registerReward(reward);

        }
    };

    IWorldEventListener<ItemPickedUp> itemPickedUpListener = new IWorldEventListener<ItemPickedUp>() {

        @Override
        public void notify(ItemPickedUp event) {

            //reward
            if (navDict != null) {
                double reward;
                GameState state = GameStateFactory.getGameState(getWorldView());
                log.info("bot picked up an item " + event.getDescriptor().toString());
                //assigning rewards for each and every catagory of item type to increase reward
                switch (event.getType().getCategory()) {
                    case WEAPON:
                        reward = 270;
                        log.info("\nbot picked up an item " + event.getType().getGroup());
                        log.info("Reward for the weapon is " + reward);
                        try {
                            //finding the weapon catagory
                            shoot.changeWeapon(weaponSelector.weaponChecker(event.getType().getGroup().toString().split("_")[0] + "" + event.getType().getGroup().toString().split("_")[1]));
                        } catch (Exception e) {
                            shoot.changeWeapon(weaponSelector.weaponChecker(event.getType().getGroup().toString()));
                        } finally {
                            break;
                        }
                    case HEALTH:
                        reward = 260;
                        break;
                    case ADRENALINE:
                        reward = 250;
                        break;
                    case AMMO:
                        reward = 210;
                        break;
                    case ARMOR:
                        reward = 240;
                        break;
                    case DEPLOYABLE:
                        reward = 200;
                        break;
                    case PROJECTILE:
                        reward = 200;
                        break;
                    case SHIELD:
                        reward = 200;
                        break;
                    default:
                        reward = 200;
                        break;
                }
//                if (event.getType().getCategory().equals(ItemType.Category.WEAPON)) {
//                    reward = 5;
//                } else {
//                    reward = 1;
//                }
//                if (ItemType.Category.WEAPON.) {
//                    reward = 5;
//                } else {
//                    reward = 1;
//                }
                Agent.update(CurrentState, Action, state, reward);
                statistics.registerReward(reward);
            }
        }
    };

    IWorldEventListener<PlayerDamaged> playerDamagedListener = new IWorldEventListener<PlayerDamaged>() {

        @Override
        public void notify(PlayerDamaged event) {
            log.info("bot damaged an enemy " + damageDone);
//            log.info("\nbot damaged an enemy " + event.getDamageType());
//            log.info("\n\n\n\n\nbot damaged an enemy " + agentInfo.getCurrentWeaponName() + "\n\n\n\n\n");

            //reward
//            Double reward = 1d;
            damageDone = event.getDamage();
            if (damageDone != 0) {
                weaponSelector.weaponPref(agentInfo.getCurrentWeaponName(), (int) damageDone);
            }
            
            Double reward = damageDone * 2;

            GameState state = GameStateFactory.getGameState(getWorldView());
            Agent.update(CurrentState, Action, state, reward);
            statistics.registerReward(reward);
        }
    };

    IWorldEventListener<PlayerKilled> playerKilledListerner = new IWorldEventListener<PlayerKilled>() {

        @Override
        public void notify(PlayerKilled event) {
            if (event.getKiller() == info.getId()) {
                log.info("bot killed an enemy");
                frags++;
                //reward
//                Double reward = 5d;
                Double reward = (damageDone * 2) - damageRecieved;

                GameState state = GameStateFactory.getGameState(getWorldView());
                Agent.update(CurrentState, Action, state, reward);
                statistics.registerReward(reward);
            }
        }
    };

    //bot killed, must register the penalty
    @Override
    public void botKilled(BotKilled event) {
        log.info("bot was killed");
        deaths++;
        //penalty
//        Double reward = -5d;
        Double reward = (damageDone * 2) - damageRecieved;;

        GameState state = GameStateFactory.getGameState(getWorldView());
        Agent.update(CurrentState, Action, state, reward);
        statistics.registerReward(reward);
    }

    @Override
    public void prepareBot(UT2004Bot bot) {
        StuckDetector = new Univale.Tcc.RL.Pogamut.Services.StuckDetector.StuckDetector(4, navigation, info);

        CurrentState = null;

        Action = null;

        //register event listerners
        getWorldView().addEventListener(BotDamaged.class, botDamagedListener);
        getWorldView().addEventListener(ItemPickedUp.class, itemPickedUpListener);
        getWorldView().addEventListener(PlayerDamaged.class, playerDamagedListener);
        getWorldView().addEventListener(PlayerKilled.class, playerKilledListerner);

        statistics = new Statistics();

        shoot = new ImprovedShooting(weaponry, bot, log);
        
        action = new Action(bot, log);

        agentInfo = new AgentInfo(bot) {
            @Override
            public ItemType getCurrentWeaponType() {
                return null;
            }
        };

        weaponSelector = new WeaponSelector(weaponPrefs);

        assaultbehavior = new AssaultBehavior(players, shoot, navigation, weaponPrefs);
        navigationBehavior = new NavigationBehavior(shoot, navigation);

    }

    @Override
    public void beforeFirstLogic() {
        navDict = getWorldView().getAll(NavPoint.class).values().stream().collect(Collectors.toMap(nav -> nav.getId().toString(), nav -> nav));
        GameStateFactory = new Univale.Tcc.RL.Pogamut.Services.GameStateFactory.GameStateFactory(players, info);
    }

    @Override
    public Initialize getInitializeCommand() {
        // skill determines the shooting accuracy of the bot
        return new Initialize().setName("Adaptive AI").setSkin("HumanMaleA.MercMaleA").setDesiredSkill(7);

    }

    @Override
    public void botShutdown() {
        log.info("bot destroyed, saving knowledge");
        Agent.Save();
        statistics.SaveStatistics();
        weaponSelector.saveWeaponPref();
    }

    //resets the bot's situation
    protected void reset() {
        log.info(" warning - RESET");
        CurrentState = null;
    }
}
