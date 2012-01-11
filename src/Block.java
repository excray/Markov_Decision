/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author 
*/

public class Block<C> {
	private int x = 1;
	private int y = 1;
	private C content = null;

	
	public Block(int x, int y, C content) {
		this.x = x;
		this.y = y;
		this.content = content;
	}

	
	public int getX() {
		return x;
	}

	
	public int getY() {
		return y;
	}

	
	public C getContent() {
		return content;
	}

	
	public void setContent(C content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "<x=" + x + ", y=" + y + ", content=" + content + ">";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Block<?>) {
			Block<?> c = (Block<?>) o;
			return x == c.x && y == c.y && content.equals(c.content);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return x + 23 + y + 31 * content.hashCode();
	}
}
