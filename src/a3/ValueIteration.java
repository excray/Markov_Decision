/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

/**
 *
 * @author 
 */

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashMap;

public class ValueIteration<S, A extends Action> {
	// discount &gamma; to be used.
	private double gamma = 0;

	/**
	 * Constructor.
	 * 
	 * @param gamma
	 *            discount &gamma; to be used.
	 */
	public ValueIteration(double gamma) {
		if (gamma > 1.0 || gamma <= 0.0) {
			throw new IllegalArgumentException("Gamma must be > 0 and <= 1.0");
		}
		this.gamma = gamma;
	}

	// function VALUE-ITERATION(mdp, &epsilon;) returns a utility function
	/**
	 * The value iteration algorithm for calculating the utility of states.
	 * 
	 * @param mdp
	 *            an MDP with states S, actions A(s), <br>
	 *            transition model P(s' | s, a), rewards R(s)
	 * @param epsilon
	 *            the maximum error allowed in the utility of any state
	 * @return a vector of utilities for states in S
	 */
	public Map<S, Double> valueIteration(MarkovDecisionProcess<S, A> mdp,
			double epsilon, Map<S, A> optimalAction ) {
		//
		// local variables: U, U', vectors of utilities for states in S,
		// initially zero
		Map<S, Double> U = create(mdp.states(), new Double(0));
		Map<S, Double> Udelta = create(mdp.states(), new Double(0));
		// &delta; the maximum change in the utility of any state in an
		// iteration
		double delta = 0;
		// Note: Just calculate this once for efficiency purposes:
		// &epsilon;(1 - &gamma;)/&gamma;
		double minDelta = epsilon * (1 - gamma) / gamma;

		// repeat
		do {
			// U <- U'; &delta; <- 0
			U.putAll(Udelta);
			delta = 0;
			// for each state s in S do
			for (S s : mdp.states()) {
				// max<sub>a &isin; A(s)</sub>
				Set<A> actions = mdp.actions(s);
				// Handle terminal states (i.e. no actions).
				double aMax = 0;
				if (actions.size() > 0) {
					aMax = Double.NEGATIVE_INFINITY;
				}
				for (A a : actions) {
					// &Sigma;<sub>s'</sub>P(s' | s, a) U[s']
					double aSum = 0;
					for (S sDelta : mdp.states()) {
						aSum += mdp.transitionProbability(sDelta, s, a)
								* U.get(sDelta);
					}
					if (aSum > aMax){
                                          //  aSum > aMax) {
						aMax = aSum;
                                                optimalAction.put(s, a);
					}
				}
				// U'[s] <- R(s) + &gamma;
				// max<sub>a &isin; A(s)</sub>
				Udelta.put(s, mdp.reward(s) + gamma * aMax);
				// if |U'[s] - U[s]| > &delta; then &delta; <- |U'[s] - U[s]|
				double aDiff = Math.abs(Udelta.get(s) - U.get(s));
				if (aDiff > delta) {
					delta = aDiff;
				}
			}
			// until &delta; < &epsilon;(1 - &gamma;)/&gamma;
		} while (delta > minDelta);

		// return U
		return U;
	}
        
        public static <K, V> Map<K, V> create(Collection<K> keys, V value) {
        Map<K, V> map = new LinkedHashMap<K, V>();

        for (K k : keys) {
            map.put(k, value);
        }

        return map;
    }
}

