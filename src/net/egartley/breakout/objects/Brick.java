package net.egartley.breakout.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.breakout.logic.collision.EntityEntityCollision;
import net.egartley.breakout.logic.events.EntityEntityCollisionEvent;
import net.egartley.breakout.logic.interaction.EntityBoundary;

public class Brick extends Entity {

	private Color color;
	private Color borderColor;

	public boolean isDead = false;

	public static final short WIDTH = 88;
	public static final short HEIGHT = 22;

	private static byte margin = 8;
	private static int horizontalOffset = 0;
	private static int verticalOffset = 0;
	private static int horizontalDelta = WIDTH + margin;
	private static int verticalDelta = HEIGHT + margin;

	public Brick(Color color) {
		super(WIDTH, HEIGHT);
		
		this.color = color;
		borderColor = color.darker();

		// x/y coords are calculated from left to right, up to down
		// incremented by the consecutive invocations of constructor

		if (horizontalOffset == Board.horizontalSize) {
			// reached horizontal limit, reset it and increment vertical
			horizontalOffset = 0;
			verticalOffset++;
		}
		x = margin + (horizontalOffset * horizontalDelta);
		y = margin + (verticalOffset * verticalDelta);
		horizontalOffset++;

		setBoundaries();
		setCollisions();
	}

	private void kill() {
		isDead = true;
	}

	@Override
	public void render(Graphics graphics) {
		if (isDead == false) {
			graphics.setColor(color);
			graphics.fillRect((int) x, (int) y, WIDTH, HEIGHT);
			graphics.setColor(borderColor);
			graphics.drawRect((int) x, (int) y, WIDTH, HEIGHT);
		}
	}

	@Override
	public void tick() {
		// don't call boundaries' ticks because they (shouldn't) move
		// boundaries.get(0).tick();
		if (isDead == false) {
			collisions.get(0).tick();
		}
	}

	@Override
	protected void setBoundaries() {
		boundaries.add(new EntityBoundary(this, WIDTH, HEIGHT));
	}

	@Override
	protected void setCollisions() {
		collisions = new ArrayList<EntityEntityCollision>();
		collisions.add(new EntityEntityCollision(boundaries.get(0), Entity.BALL.boundaries.get(0)) {
			@Override
			public void onCollide(EntityEntityCollisionEvent event) {
				kill();
				((Ball) Entity.BALL).reflectVertical();
			}
		});
	}

	@Override
	public String toString() {
		return "Brick#" + super.toString();
	}

}
