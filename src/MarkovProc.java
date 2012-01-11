/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author 
 */

import java.util.Set;
        
public interface MarkovProc<S, A extends Action> {

	Set<S> states();

	S getInitialState();

	
	Set<A> actions(S s);

	double getProbability(S sDelta, S s, A a);

	
	double reward(S s);
}
