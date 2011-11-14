/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

/**
 *
 * @author Vivek
 */
public interface RewardFunction<S> {
	
	/**
	 * Get the reward associated with being in state s.
	 * 
	 * @param s
	 *            the state whose award is sought.
	 * @return the reward associated with being in state s.
	 */
	double reward(S s);
}