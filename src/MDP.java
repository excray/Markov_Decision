/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author 
 */

import java.util.Set;


public class MDP<S, A extends Action> implements MarkovProc<S, A> {
	private Set<S> states = null;
	private S initialState = null;
	private ActionSet<S, A> actionsSet = null;
	private Probability<S, A> probFn = null;
	private Reward<S> rwFn = null;

	public MDP(Set<S> states, S initialState,
			ActionSet<S, A> actionsSet,
			Probability<S, A> probFn,
			Reward<S> rwFn) {
		this.states = states;
		this.initialState = initialState;
		this.actionsSet = actionsSet;
		this.probFn = probFn;
		this.rwFn = rwFn;
	}

	//
	// START-MarkovDecisionProcess
	@Override
	public Set<S> states() {
		return states;
	}

	@Override
	public S getInitialState() {
		return initialState;
	}

	@Override
	public Set<A> actions(S s) {
		return actionsSet.actions(s);
	}

	@Override
	public double getProbability(S sDelta, S s, A a) {
		return probFn.probability(sDelta, s, a);
	}

	@Override
	public double reward(S s) {
		return rwFn.reward(s);
	}

	// END-MarkovDecisionProcess
	//
}

