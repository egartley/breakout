package net.egartley.breakout.logic.collision;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import net.egartley.breakout.logic.events.EntityEntityCollisionEvent;
import net.egartley.breakout.logic.interaction.EntityBoundary;
import net.egartley.breakout.objects.Entity;

/**
 * A collision between two different entities
 */
public class EntityEntityCollision {

	/**
	 * Whether or not the two boundaries are collided with one another
	 */
	public boolean isCollided;

	/**
	 * Forgot what this does...
	 */
	private boolean previouslyCollided;
	/**
	 * Whether or not the onCollide event has been fired
	 */
	private boolean firedEvent;

	private Rectangle rectangle1;
	private Rectangle rectangle2;
	private EntityEntityCollisionEvent event;

	public EntityBoundary boundary1;
	public EntityBoundary boundary2;

	/**
	 * One of the two entities that are to collide
	 */
	public ArrayList<Entity> entities;

	/**
	 * Creates a new collision between two entity boundaries
	 * 
	 * @param boundary1
	 *            First entity's boundary
	 * @param boundary2
	 *            Second entity's boundary
	 */
	public EntityEntityCollision(EntityBoundary boundary1, EntityBoundary boundary2) {
		this.boundary1 = boundary1;
		this.boundary2 = boundary2;
		rectangle1 = this.boundary1.asRectangle();
		rectangle2 = this.boundary2.asRectangle();
		rectangle1.width++;
		rectangle2.width++;
		entities = new ArrayList<Entity>();
		entities.add(boundary1.parent);
		entities.add(boundary2.parent);
	}

	/**
	 * Checks to see if the two boundaries are collided with one another, and calls
	 * {@link #onCollide(EntityEntityCollisionEvent)} when collided, then
	 * {@link #onCollisionEnd(EntityEntityCollisionEvent)} after it has ended
	 */
	public void tick() {
		rectangle1.x = boundary1.x - 1;
		rectangle2.x = boundary2.x - 1;
		rectangle1.y = boundary1.y - 1;
		rectangle2.y = boundary2.y - 1;
		isCollided = rectangle1.intersects(rectangle2);
		if (isCollided == true && firedEvent == false) {
			event = new EntityEntityCollisionEvent(this);
			onCollide_internal();
			onCollide(event);
			firedEvent = true;
		}
		if (isCollided == false && firedEvent == true) {
			onCollisionEnd_internal();
			onCollisionEnd(event);
			firedEvent = false;
		}
		if (previouslyCollided != isCollided) {
			setBoundaryColors();
		}

		previouslyCollided = isCollided;
	}

	/**
	 * This is called <b>once</b> after the collision occurs
	 * 
	 * @param event
	 *            The collision's event
	 */
	public void onCollide(EntityEntityCollisionEvent event) {

	}

	/**
	 * This is called <b>once</b> after the collision ends
	 * 
	 * @param event
	 *            The collision's event
	 */
	public void onCollisionEnd(EntityEntityCollisionEvent event) {

	}

	/**
	 * Sets or updates the colors for both boundaries
	 */
	public void setBoundaryColors() {
		determineBoundaryColors(entities.get(0).boundaries);
		determineBoundaryColors(entities.get(1).boundaries);
	}

	private void determineBoundaryColors(ArrayList<EntityBoundary> e) {
		for (EntityBoundary boundary : e) {
			determineBoundaryColor(boundary);
		}
	}

	private void determineBoundaryColor(EntityBoundary e) {
		if (e.isCollided == false) {
			if (e.parent.isStatic == true) {
				e.drawColor = Color.BLACK;
			} else {
				e.drawColor = Color.YELLOW;
			}
		} else {
			if (e.parent.isStatic == true) {
				e.drawColor = Color.YELLOW;
			} else {
				e.drawColor = Color.RED;
			}
		}
	}

	/**
	 * Called right before {@link #onCollide(EntityEntityCollisionEvent)}
	 */
	protected void onCollide_internal() {
		for (Entity entity : entities) {
			entity.lastCollision = this;
			entity.isCollided = true;
		}
		boundary1.isCollided = true;
		boundary2.isCollided = true;
	}

	/**
	 * Called right before {@link #onCollisionEnd(EntityEntityCollisionEvent)}
	 */
	protected void onCollisionEnd_internal() {
		boundary1.isCollided = false;
		boundary2.isCollided = false;
		// makes multiple entity-to-entity collision possible on one entity (hopefully)
		for (Entity entity : entities) {
			// for each entity
			for (EntityBoundary boundary : entity.boundaries) {
				// check all of its boundaries
				if (boundary.isCollided == true) {
					// if just one of them is collided, then the entity is still collided
					entity.isCollided = true;
					// break, because entity is still collided, don't set isCollided to false
					break;
				}
				// none of the entity's boundaries were collided, so isCollided set to false
				entity.isCollided = false;
			}
		}
	}

}