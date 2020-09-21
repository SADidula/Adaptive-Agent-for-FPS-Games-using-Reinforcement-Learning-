package Univale.Tcc.RL.Pogamut.Services.Statistics;

/**
 * @author Didula
*/
public class RewardEvent {
    private Double Reward;
    private long elapsedTime;

    public Double getReward() {
        return Reward;
    }

    public void setReward(Double reward) {
        Reward = reward;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long time) {
        elapsedTime = time;
    }

    public RewardEvent(double reward, long time)
    {
        setReward(reward);
        setElapsedTime(time);
    }
}
