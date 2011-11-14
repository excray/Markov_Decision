/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

/**
 *
 * @author Vivek
 */

public interface Action {

	/**
	 * Indicates whether or not this Action is a 'No Operation'.<br>
	 * Note: AIMA3e - NoOp, or no operation, is the name of an assembly language
	 * instruction that does nothing.
	 * 
	 * @return true if this is a NoOp Action.
	 */
	boolean isNoOp();
}