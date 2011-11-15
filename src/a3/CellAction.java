/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

/**
 *
 * @author 
 */

import java.util.LinkedHashSet;
import java.util.Set;


public enum CellAction implements Action {
	Up, Down, Left, Right, None;

	private static final Set<CellAction> _actions = new LinkedHashSet<CellAction>();
	static {
		_actions.add(Up);
		_actions.add(Down);
		_actions.add(Left);
		_actions.add(Right);
		_actions.add(None);
	}

	/**
	 * 
	 * @return a set of the actual actions.
	 */
	public static final Set<CellAction> actions() {
		return _actions;
	}
	
	//
	// START-Action
	@Override
	public boolean isNoOp() {
		if (None == this) {
			return true;
		}
		return false;
	}
	// END-Action
	//

	/**
	 * 
	 * @param curX
	 *            the current x position.
	 * @return the result on the x position of applying this action.
	 */
	public int getXResult(int curX) {
		int newX = curX;

		switch (this) {
		case Left:
			newX--;
			break;
		case Right:
			newX++;
			break;
		}

		return newX;
	}

	/**
	 * 
	 * @param curY
	 *            the current y position.
	 * @return the result on the y position of applying this action.
	 */
	public int getYResult(int curY) {
		int newY = curY;

		switch (this) {
		case Up:
			newY++;
			break;
		case Down:
			newY--;
			break;
		}

		return newY;
	}

	/**
	 * 
	 * @return the first right angled action related to this action.
	 */
	public CellAction getFirstRightAngledAction() {
		CellAction a = null;

		switch (this) {
		case Up:
		case Down:
			a = Left;
			break;
		case Left:
		case Right:
			a = Down;
			break;
		case None:
			a = None;
			break;
		}

		return a;
	}

	/**
	 * 
	 * @return the second right angled action related to this action.
	 */
	public CellAction getSecondRightAngledAction() {
		CellAction a = null;

		switch (this) {
		case Up:
		case Down:
			a = Right;
			break;
		case Left:
		case Right:
			a = Up;
			break;
		case None:
			a = None;
			break;
		}

		return a;
	}
}
