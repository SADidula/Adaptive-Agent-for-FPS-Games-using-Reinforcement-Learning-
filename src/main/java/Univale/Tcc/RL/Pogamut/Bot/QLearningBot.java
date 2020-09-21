package Univale.Tcc.RL.Pogamut.Bot;

import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.QLearningAgent;

/**
 * @author Didula
*/
public class QLearningBot extends LearnerBot {
    public QLearningBot()
    {
        super(new QLearningAgent());
    }
}
