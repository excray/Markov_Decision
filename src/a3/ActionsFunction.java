/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

/**
 *
 * @author 
 */
import java.util.Set;

public interface ActionsFunction<S, A extends Action> {
	/**
	 * Get the set of actions for state s.
	 * 
	 * @param s
	 *            the state.
	 * @return the set of actions for state s.
	 */
	Set<A> actions(S s);
}