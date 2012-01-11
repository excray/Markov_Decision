/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author 
 */

import java.util.LinkedHashSet;
import java.util.Set;


public enum BlockAction implements Action {
	Up, Down, Left, Right, None;

	private static final Set<BlockAction> actions = new LinkedHashSet<BlockAction>();
	static {
		actions.add(Up);
		actions.add(Down);
		actions.add(Left);
		actions.add(Right);
		actions.add(None);
	}
	
	public static final Set<BlockAction> actions() {
		return actions;
	}
	
	@Override
	public boolean isNoOp() {
		if (None == this) {
			return true;
		}
		return false;
	}
	
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

	public BlockAction getFirstRightAngledAction() {
		BlockAction a = null;

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

	
	public BlockAction getSecondRightAngledAction() {
		BlockAction a = null;

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
