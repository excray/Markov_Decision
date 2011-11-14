/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

/**
 *
 * @author Vivek
 */

import java.util.Set;
        
public interface MarkovDecisionProcess<S, A extends Action> {

	/**
	 * Get the set of states associated with the Markov decision process.
	 * 
	 * @return the set of states associated with the Markov decision process.
	 */
	Set<S> states();

	/**
	 * Get the initial state s<sub>0</sub> for this instance of a Markov
	 * decision process.
	 * 
	 * @return the initial state s<sub>0</sub>.
	 */
	S getInitialState();

	/**
	 * Get the set of actions for state s.
	 * 
	 * @param s
	 *            the state.
	 * @return the set of actions for state s.
	 */
	Set<A> actions(S s);

	/**
	 * Return the probability of going from state s using action a to s' based
	 * on the underlying transition model P(s' | s, a).
	 * 
	 * @param sDelta
	 *            the state s' being transitioned to.
	 * @param s
	 *            the state s being transitions from.
	 * @param a
	 *            the action used to move from state s to s'.
	 * @return the probability of going from state s using action a to s'.
	 */
	double transitionProbability(S sDelta, S s, A a);

	/**
	 * Get the reward associated with being in state s.
	 * 
	 * @param s
	 *            the state whose award is sought.
	 * @return the reward associated with being in state s.
	 */
	double reward(S s);
}
