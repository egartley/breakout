package net.egartley.breakout.objects;

import java.awt.Color;
import java.awt.Graphics;

import net.egartley.breakout.Game;
import net.egartley.breakout.input.Mouse;
import net.egartley.breakout.logic.interaction.EntityBoundary;

public class Paddle extends Entity {

	public static final short WIDTH = 200;
	public static final short HEIGHT = 12;

	private static final short HALF_WIDTH = WIDTH / 2;

	public Paddle() {
		super(WIDTH, HEIGHT);
		// y will always be the same
		y = Game.WINDOW_HEIGHT - 72;
		// x is set to the center horizontal center for now, but will update
		x = (Game.WINDOW_WIDTH / 2) - (WIDTH / 2);

		setBoundaries();
		setCollisions();
	}

	@Override
	public void render(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect((int) x, (int) y, WIDTH, HEIGHT);
	}

	@Override
	public void tick() {
		// update to horizontal mouse position
		x = Mouse.x - HALF_WIDTH;
		// enforce position to viewport
		if (x < 0) {
			x = 0;
		}
		if (x + WIDTH > Game.WINDOW_WIDTH) {
			x = Game.WINDOW_WIDTH - WIDTH;
		}
		boundaries.get(0).tick();
	}

	@Override
	protected void setBoundaries() {
		boundaries.add(new EntityBoundary(this, WIDTH, HEIGHT));
	}

	@Override
	protected void setCollisions() {

	}

	@Override
	public String toString() {
		return "Paddle#" + super.toString();
	}

}
