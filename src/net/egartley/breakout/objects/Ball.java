package net.egartley.breakout.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import net.egartley.breakout.Debug;
import net.egartley.breakout.Game;
import net.egartley.breakout.logic.collision.EntityEntityCollision;
import net.egartley.breakout.logic.events.EntityEntityCollisionEvent;
import net.egartley.breakout.logic.interaction.EntityBoundary;

public class Ball extends Entity {

	private Color color = Color.WHITE;
	private Color borderColor = color.darker();

	private final byte VERTICAL_DIRECTION_UP = 98;
	private final byte VERTICAL_DIRECTION_DOWN = 99;
	private final byte HORIZONTAL_DIRECTION_LEFT = 97;
	private final byte HORIZONTAL_DIRECTION_RIGHT = 96;
	private final double APPARENT_VELOCITY = 2.875125D;
	private final double ANGLE_REFRACTION = 22.5D;

	public byte verticalDirection = -1;
	public byte horizontalDirection = -1;

	public static int diameter = 14;

	public double angle = 90;
	public double horizontalDelta = 0.0D;
	public double verticalDelta = APPARENT_VELOCITY;

	public Ball() {
		super(diameter, diameter);
		// start by going down
		verticalDirection = VERTICAL_DIRECTION_DOWN;

		x = Game.WINDOW_WIDTH / 2 - diameter / 2 - 12;
		y = 280;

		setBoundaries();
		setCollisions();
	}

	@Override
	public void render(Graphics graphics) {
		graphics.setColor(color);
		graphics.fillOval((int) x, (int) y, diameter, diameter);
		graphics.setColor(borderColor);
		graphics.drawOval((int) x, (int) y, diameter, diameter);
	}

	@Override
	public void tick() {
		boundaries.get(0).tick();
		for (EntityEntityCollision collision : collisions) {
			collision.tick();
		}
		move();
	}

	private void travelLeftwards() {
		horizontalDirection = HORIZONTAL_DIRECTION_LEFT;
	}

	private void travelRightwards() {
		horizontalDirection = HORIZONTAL_DIRECTION_RIGHT;
	}

	public void reflectHorizontal() {
		if (horizontalDirection == HORIZONTAL_DIRECTION_LEFT)
			travelRightwards();
		else
			travelLeftwards();
	}

	private void travelUpwards() {
		verticalDirection = VERTICAL_DIRECTION_UP;
	}

	private void travelDownwards() {
		verticalDirection = VERTICAL_DIRECTION_DOWN;
	}

	public void reflectVertical() {
		if (verticalDirection == VERTICAL_DIRECTION_DOWN)
			travelUpwards();
		else
			travelDownwards();
	}

	private void move() {
		// make sure ball will be within viewport (bounce off sides)
		if (y - APPARENT_VELOCITY <= 0 || y + APPARENT_VELOCITY + diameter >= Game.WINDOW_HEIGHT) {
			// hit top/bottom of window, reflect
			reflectVertical();
		}

		if (x + horizontalDelta + diameter >= Game.WINDOW_WIDTH || x - horizontalDelta <= 0) {
			// hit side of windows, reflect
			reflectHorizontal();
		}

		// update vertical position, which is the easiest to calculate
		if (verticalDirection == VERTICAL_DIRECTION_DOWN) {
			y += verticalDelta;
		} else if (verticalDirection == VERTICAL_DIRECTION_UP) {
			y -= verticalDelta;
		}
		// update horizontal position
		if (horizontalDirection == HORIZONTAL_DIRECTION_LEFT) {
			x -= horizontalDelta;
		} else if (horizontalDirection == HORIZONTAL_DIRECTION_RIGHT) {
			x += horizontalDelta;
		}
	}

	private void calculateAngle(EntityEntityCollisionEvent event) {
		// usually paddle, but could be a brick (shouldn't matter)
		Entity paddle = event.invoker.entities.get(1);
		if (paddle instanceof Ball) {
			// if the second entity is for some the reason the ball, then the first entity
			// must be either the paddle or a brick
			paddle = event.invoker.entities.get(0);
		}
		double ballCenterX = this.x + (diameter / 2);
		double halfWidth = paddle.width / 2;

		if (ballCenterX < paddle.x + halfWidth) {
			// left half
			angle = 90 * ((ballCenterX - paddle.x) / halfWidth) + ANGLE_REFRACTION;
		} else {
			// right half
			angle = 180 - (90 * ((ballCenterX - (paddle.x + halfWidth)) / halfWidth) + ANGLE_REFRACTION);
		}
		if (horizontalDirection != HORIZONTAL_DIRECTION_LEFT && horizontalDirection != HORIZONTAL_DIRECTION_RIGHT) {
			reflectHorizontal();
		}

		calculateDeltas();
	}

	private void calculateDeltas() {
		byte method = -1;

		if (angle < 45) {
			method = 1;
		} else if (angle == 45) {
			method = 2;
		} else if (angle < 90) {
			method = 1;
		} else if (angle == 90) {
			method = 3;
		} else if (angle < 135) {
			method = 4;
		} else if (angle == 135) {
			method = 5;
		} else if (angle < 180) {
			method = 4;
		} else if (angle == 180) {
			method = 6;
		}

		switch (method) {
		case 1:
			verticalDelta = angle / 90;
			horizontalDelta = APPARENT_VELOCITY - verticalDelta;
			break;
		case 2:
			horizontalDelta = APPARENT_VELOCITY / 2;
			verticalDelta = horizontalDelta;
			break;
		case 3:
			horizontalDelta = 0;
			verticalDelta = APPARENT_VELOCITY;
			reflectHorizontal();
			break;
		case 4:
			horizontalDelta = (angle - 90) / -90;
			verticalDelta = APPARENT_VELOCITY - Math.abs(horizontalDelta);
			break;
		case 5:
			horizontalDelta = APPARENT_VELOCITY / -2;
			verticalDelta = Math.abs(horizontalDelta);
			break;
		case 6:
			verticalDelta = 0;
			horizontalDelta = APPARENT_VELOCITY * -1;
			break;
		default:
			Debug.warning("Could not calculate an angle! (must be [0, 180])");
			break;
		}

		double moe = (horizontalDelta + verticalDelta - APPARENT_VELOCITY) / APPARENT_VELOCITY;

		System.out.println(angle + "°\nx -> " + horizontalDelta + ", y -> " + verticalDelta);
		System.out.println("Margin of error: " + String.format("%.2f", Math.abs(moe) * 100) + "%\n");
		if (moe > 0.1D) {
			// MOE of anything less than 0.1% isn't significant
			double correction = (APPARENT_VELOCITY * moe) / 2;
			horizontalDelta -= correction;
			verticalDelta -= correction;
			System.out.println(
					"With correction of " + correction + "\nx -> " + horizontalDelta + ", y -> " + verticalDelta);
			moe = (horizontalDelta + verticalDelta - APPARENT_VELOCITY) / APPARENT_VELOCITY;
			System.out.println("Margin of error is now: " + String.format("%.2f", Math.abs(moe) * 100) + "%\n");
		} else {
			System.out.println("No correction was applied\n");
		}
		System.out.println("-------------------------------------------------\n");
	}

	@Override
	protected void setBoundaries() {
		boundaries.add(new EntityBoundary(this, diameter, diameter));
	}

	@Override
	protected void setCollisions() {
		collisions = new ArrayList<EntityEntityCollision>();
		collisions.add(new EntityEntityCollision(boundaries.get(0), Entity.PADDLE.boundaries.get(0)) {
			@Override
			public void onCollide(EntityEntityCollisionEvent event) {
				travelUpwards();
				calculateAngle(event);
			}
		});
	}

	@Override
	public String toString() {
		return "Ball#" + super.toString();
	}

}
