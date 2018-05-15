package net.egartley.breakout.objects;

import net.egartley.breakout.Game;
import net.egartley.breakout.Util;
import net.egartley.breakout.logic.collision.EntityEntityCollision;
import net.egartley.breakout.logic.events.EntityEntityCollisionEvent;
import net.egartley.breakout.logic.interaction.EntityBoundary;
import net.egartley.breakout.logic.math.Calculate;

import java.awt.*;
import java.util.ArrayList;

/**
 * Something that can rendered with a sprite and have a specific position
 */
public abstract class Entity {

	public static Entity PADDLE;
	public static Entity BALL;

	/**
	 * Collection of this entity's collisions
	 */
	ArrayList<EntityEntityCollision> collisions;
	/**
	 * Collection of this entity's boundaries
	 */
	public ArrayList<EntityBoundary> boundaries;
	/**
	 * The most recent collision that has occured for this entity. If no collisions
	 * have occured within this entity's lifetime, this will be null
	 */
	public EntityEntityCollision lastCollision = null;
	/**
	 * The most recent collision event to have occured. This will be null if no
	 * collision event has yet to take place
	 */
	public EntityEntityCollisionEvent lastCollisionEvent = null;
	/**
	 * The entity's x-axis coordinate (absolute)
	 */
	public double x;
	/**
	 * The entity's y-axis coordinate (absolute)
	 */
	public double y;
	public double width;
	public double height;
	/**
	 * The entity's unique identifacation number
	 */
	private int uuid;
	/**
	 * Whether or not this entity is static (no animation)
	 */
	public boolean isStatic;
	/**
	 * Whether ot not this entity is currently collided with another entity
	 */
	public boolean isCollided;

	private String name;
	private Font nameTagFont = new Font("Arial", Font.PLAIN, 11);
	private Color nameTagBackgroundColor = new Color(0, 0, 0, 128);
	private boolean setFontMetrics = false;

	private int nameTagWidth;
	private int entityWidth = 32;
	private int nameX;
	private int nameY;

	public Entity(double width, double height) {
		this.width = width;
		this.height = height;
		generateUUID();
		boundaries = new ArrayList<>();
		collisions = new ArrayList<>();
	}

	/**
	 * Renders the entity
	 * 
	 * @param graphics
	 *            Graphics object to use
	 */
	public abstract void render(Graphics graphics);

	/**
	 * Should be called 60 times per second within a tick thread
	 */
	public abstract void tick();

	/**
	 * Sets this entity's boundaries
	 */
	protected abstract void setBoundaries();

	/**
	 * Sets this entity's collisions
	 */
	protected abstract void setCollisions();

	/**
	 * Generates a new UUID
	 */
	public void generateUUID() {
		uuid = Util.randomInt(9999, 1000, true);
	}

	/**
	 * Renders debug information, such as the entity's boundaries and "name tag"
	 * 
	 * @param graphics
	 *            Graphics object to use
	 */
	public void drawDebug(Graphics graphics) {
		if (Game.debug) {
			drawBoundaries(graphics);
			drawNameTag(graphics);
		}
	}

	/**
	 * Draws the entity's "name tag"
	 * 
	 * @param graphics
	 *            Graphics object to use
	 */
	private void drawNameTag(Graphics graphics) {
		if (!setFontMetrics) {
			// init, only run once
			name = toString();
			nameTagWidth = graphics.getFontMetrics(nameTagFont).stringWidth(name) + 8; // 4px padding on both sides
			// don't initialize again, not needed
			setFontMetrics = true;
		}
		nameX = Calculate.horizontalCenter((int) x, entityWidth) - Calculate.horizontalCenter(0, nameTagWidth);
		nameY = (int) y - 18;

		graphics.setColor(nameTagBackgroundColor);
		graphics.setFont(nameTagFont);

		graphics.fillRect(nameX, nameY, nameTagWidth, 18);
		graphics.setColor(Color.WHITE);
		graphics.drawString(name, nameX + 5, nameY + 13);
	}

	/**
	 * Draws all of the entity's boundaries
	 * 
	 * @param graphics
	 *            Graphics object to use
	 */
	private void drawBoundaries(Graphics graphics) {
		for (EntityBoundary boundary : boundaries) {
			boundary.draw(graphics);
		}
	}

	public String toString() {
		return String.valueOf(uuid);
	}

}
