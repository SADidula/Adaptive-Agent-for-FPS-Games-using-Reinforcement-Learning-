package Univale.Tcc.RL.Pogamut.DecisionMaking.Agent;

import Univale.Tcc.RL.Pogamut.DecisionMaking.GameState.GameState;
import com.thoughtworks.xstream.XStream;
import cz.cuni.amis.introspection.java.JProp;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;

import Univale.Tcc.RL.Pogamut.Actions.Action;

/**
 * @author Didula
 */
public class BaseAgent {

    @JProp
    protected double epsilon;
    //probability of exploitation

    @JProp
    protected double gamma;
    //discount factor, determined how concerned the agent will be with future rewards

    @JProp
    protected double alpha;
    //learning rate

    Random randomNumberGenerator;

    //list of all game states - knowledge base
    List<GameState> states;

    final XStream xstream = new XStream();

    public BaseAgent() {
        loadStates();

        //default values ​​for epsilon, gamma and alpha
        setEpsilon(0.1);
        setGamma(0.9);
        setAlpha(0.9);
    }

    public BaseAgent(double epsilon, double gamma, double alpha) {
        loadStates();
        setEpsilon(epsilon);
        setGamma(gamma);
        setAlpha(alpha);
    }

    private void loadStates() {
        FileInputStream file = null;
        try {
            file = new FileInputStream("db.xml");

            if (file == null) {
                states = new ArrayList<GameState>();
            } else {
                states = (List<GameState>) (xstream.fromXML(file));
                file.close();
                if (states == null) {
                    states = new ArrayList<GameState>();
                }
            }
        } catch (Exception e) {
            states = new ArrayList<GameState>();
        }
        randomNumberGenerator = new Random();
    }

    public int getCost() {
        return states.size();
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void Save() {
        XStream xstream = new XStream();
        String xml = xstream.toXML(states);

        try {
            PrintWriter writer = new PrintWriter("db.xml", "UTF-8");
            writer.write(xml);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.err.println("Cannot Save the states");
        }
    }
}
