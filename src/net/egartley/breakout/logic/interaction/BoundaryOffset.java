package net.egartley.breakout.logic.interaction;

/**
 * A numeric offset from the top left (relative origin) of a entity
 */
public class BoundaryOffset {

	public int top;
	public int bottom;
	public int left;
	public int right;

	/**
	 * Creates a new boundary offset ({@link #left} and {@link #right} will be set
	 * to 0)
	 * 
	 * @param top
	 *            Top offset
	 * @param bottom
	 *            Bottom offset
	 */
	public BoundaryOffset(int top, int bottom) {
		this(top, bottom, 0, 0);
	}

	/**
	 * Creates a new boundary offset
	 * 
	 * @param top
	 *            Top offset
	 * @param bottom
	 *            Bottom offset
	 * @param left
	 *            Left offset
	 * @param right
	 *            Right offset
	 */
	public BoundaryOffset(int top, int bottom, int left, int right) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}

}
