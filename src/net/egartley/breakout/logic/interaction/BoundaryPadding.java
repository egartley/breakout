package net.egartley.breakout.logic.interaction;

/**
 * Extra spacing for boundaries, values can be negative or positive
 */
public class BoundaryPadding {

	/**
	 * Extra space added to the top side of the boundary
	 */
	public int top;
	/**
	 * Extra space added to the left side of the boundary
	 */
	public int left;
	/**
	 * Extra space added to the bottom side of the boundary
	 */
	public int bottom;
	/**
	 * Extra space added to the right side of the boundary
	 */
	public int right;

	/**
	 * Creates new padding with all sides having the same value ("square")
	 * 
	 * @param all
	 *            Value to set all sides to
	 */
	public BoundaryPadding(int all) {
		top = all;
		left = all;
		bottom = all;
		right = all;
	}

	/**
	 * Creates new padding with the specified values, in pixels, for each of the
	 * four sides
	 * 
	 * @param top
	 *            Value for {@link #top}
	 * @param left
	 *            Value for {@link #left}
	 * @param bottom
	 *            Value for {@link #bottom}
	 * @param right
	 *            Value for {@link #right}
	 */
	public BoundaryPadding(int top, int left, int bottom, int right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

}
