

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author 
 */

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Table<C> {
	private Set<Block<C>> blocks = new LinkedHashSet<Block<C>>();
	private Map<Integer, Map<Integer, Block<C>>> blockLookup = new HashMap<Integer, Map<Integer, Block<C>>>();

	
	public Table(int xDimension, int yDimension, C defaultCellContent) {
		for (int x = 1; x <= xDimension; x++) {
			Map<Integer, Block<C>> xCol = new HashMap<Integer, Block<C>>();
			for (int y = 1; y <= yDimension; y++) {
				Block<C> c = new Block<C>(x, y, defaultCellContent);
				blocks.add(c);
				xCol.put(y, c);
			}
			blockLookup.put(x, xCol);
		}
	}

	public Set<Block<C>> getCells() {
		return blocks;
	}

	
	public Block<C> result(Block<C> s, BlockAction a) {
		Block<C> sDelta = getCellAt(a.getXResult(s.getX()), a.getYResult(s
				.getY()));
		if (null == sDelta) {
			
			sDelta = s;
		}

		return sDelta;
	}

	
	public void removeCell(int x, int y) {
		Map<Integer, Block<C>> xCol = blockLookup.get(x);
		if (null != xCol) {
			blocks.remove(xCol.remove(y));
		}
	}

	
	public Block<C> getCellAt(int x, int y) {
		Block<C> c = null;
		Map<Integer, Block<C>> xCol = blockLookup.get(x);
		if (null != xCol) {
			c = xCol.get(y);
		}

		return c;
	}
}
