package net.egartley.breakout.logic.interaction;

import java.awt.Color;

import net.egartley.breakout.objects.Entity;

/**
 * Represents a {@link Boundary} that is specifically tailored for use with an
 * {@link Entity}
 */
public class EntityBoundary extends Boundary {

	/**
	 * The entity in which to base the boundary
	 */
	public Entity parent;
	/**
	 * Human-readable identifcation for this entity boundary
	 */
	public String name;

	/**
	 * Creates a new boundary for the given entity (with no padding)
	 * 
	 * @param entity
	 *            The entity to use
	 * @param width
	 *            Width of the boundary
	 * @param height
	 *            Height of the boundary
	 */
	public EntityBoundary(Entity entity, int width, int height) {
		this(entity, width, height, new BoundaryPadding(0), new BoundaryOffset(0, 0, 0, 0));
	}

	/**
	 * Creates a new boundary for the given entity
	 * 
	 * @param entity
	 *            The entity to use
	 * @param width
	 *            Width of the boundary (not including the left or right padding)
	 * @param height
	 *            Height of the boundary (not including the top or bottom padding)
	 * @param padding
	 *            The padding to apply
	 * @see Boundary
	 */
	public EntityBoundary(Entity entity, int width, int height, BoundaryPadding padding) {
		this(entity, width, height, padding, new BoundaryOffset(0, 0, 0, 0));
	}

	/**
	 * Creates a new boundary for the given entity
	 * 
	 * @param entity
	 *            The entity to use
	 * @param width
	 *            Width of the boundary (not including the left or right padding)
	 * @param height
	 *            Height of the boundary (not including the top or bottom padding)
	 * @param padding
	 *            The padding to apply
	 * @param offset
	 *            The offset to apply
	 * @see Boundary
	 */
	public EntityBoundary(Entity entity, int width, int height, BoundaryPadding padding, BoundaryOffset offset) {
		parent = entity;
		this.padding = padding;
		this.width = width + padding.left + padding.right;
		this.height = height + padding.top + padding.bottom;
		this.offset = offset;
		horizontalOffset = padding.left + this.offset.left - this.offset.right;
		verticalOffset = padding.top + this.offset.top - this.offset.bottom;
		name = "EntityBoundary#" + hashCode();
		tick();
		setColor();
	}

	private void setColor() {
		if (parent.isStatic) {
			drawColor = Color.BLACK;
		} else {
			drawColor = Color.YELLOW;
		}
	}

	@Override
	public void tick() {
		x = (int) parent.x - horizontalOffset;
		y = (int) parent.y - verticalOffset;
		super.tick();
	}

	public String toString() {
		return name;
	}

}
