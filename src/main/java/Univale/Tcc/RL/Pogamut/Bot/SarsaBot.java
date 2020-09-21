package Univale.Tcc.RL.Pogamut.Bot;

import Univale.Tcc.RL.Pogamut.DecisionMaking.Agent.SarsaAgent;

/**
 * @author Didula
*/
public class SarsaBot extends LearnerBot {
    public SarsaBot()
    {
        super(new SarsaAgent());
    }
}
