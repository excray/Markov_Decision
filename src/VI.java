/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author 
 */
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashMap;

public class VI<S, A extends Action> {

    public VI(double gamma) {

        this.gamma = gamma;
    }
    private double gamma = 0;

    public Map<S, Double> valueIteration(MarkovProc<S, A> mp,
            double epsilon, Map<S, A> optimalAction) {

        Map<S, Double> util = init(mp.states(), new Double(0));
        Map<S, Double> utildelta = init(mp.states(), new Double(0));

        double delta = 0;
        double minDelta = epsilon * (1 - gamma) / gamma;

        do {
            delta = 0;

            util.putAll(utildelta);
            for (S state : mp.states()) 
            {
                Set<A> actions = mp.actions(state);
                double maxSum = 0;
                if (actions.size() > 0) 
                {
                    maxSum = Double.NEGATIVE_INFINITY;
                }
                for (A a : actions) 
                {
                    double cursum = 0;
                    for (S statedelta : mp.states()) 
                    {
                        cursum += mp.getProbability(statedelta, state, a) * util.get(statedelta);
                    }
                    if (cursum > maxSum) 
                    {
                        maxSum = cursum;
                        optimalAction.put(state, a);
                    }
                }
                Double utilval = mp.reward(state) + gamma * maxSum;
                utildelta.put(state, utilval);
                double diffval = Math.abs(utildelta.get(state) - util.get(state));
                if (diffval > delta) {
                    delta = diffval;
                }
            }
        } while (delta > minDelta);

        return util;
    }

    public static <K, V> Map<K, V> init(Collection<K> keys, V value) {
        Map<K, V> map = new LinkedHashMap<K, V>();

        for (K k : keys) {
            map.put(k, value);
        }

        return map;
    }
}
