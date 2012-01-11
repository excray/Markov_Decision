/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author 
 */
import java.util.Set;

public interface ActionSet<S, A extends Action> {
	
	Set<A> actions(S s);
}