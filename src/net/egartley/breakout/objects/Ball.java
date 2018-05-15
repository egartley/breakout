package net.egartley.breakout.objects;

import net.egartley.breakout.Debug;
import net.egartley.breakout.Game;
import net.egartley.breakout.logic.collision.EntityEntityCollision;
import net.egartley.breakout.logic.events.EntityEntityCollisionEvent;
import net.egartley.breakout.logic.interaction.EntityBoundary;
import net.egartley.breakout.logic.math.Calculate;

import java.awt.*;
import java.util.ArrayList;

public class Ball extends Entity {

    private Color color = Color.WHITE;
    private Color borderColor = color.darker();

    private final byte VERTICAL_DIRECTION_UP = 98;
    private final byte VERTICAL_DIRECTION_DOWN = 99;
    private final byte HORIZONTAL_DIRECTION_LEFT = 97;
    private final byte HORIZONTAL_DIRECTION_RIGHT = 96;
    private final double APPARENT_VELOCITY = 3.37255255D;
    private final double ANGLE_DIFFRACTION = 22.5D;
    private final double ANGLE_BASE = 90D;

    private byte verticalDirection;
    private byte horizontalDirection = -1;

    private static int diameter = 14;

    private double angle = 90;
    private double horizontalDelta = 0.0D;
    private double verticalDelta = APPARENT_VELOCITY;

    public Ball() {
        super(diameter, diameter);
        // start by going down
        verticalDirection = VERTICAL_DIRECTION_DOWN;

        x = Game.WINDOW_WIDTH / 2 - diameter / 2 - 12;
        y = 280;

        setBoundaries();
        setCollisions();

        calculateDeltas();
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

    void reflectHorizontal() {
        System.out.println("Reflect horizontal");
        if (horizontalDirection == HORIZONTAL_DIRECTION_LEFT)
            travelRightwards();
        else
            travelLeftwards();
    }

    void reflectVertical() {
        System.out.println("Reflect vertical");
        if (verticalDirection == VERTICAL_DIRECTION_DOWN)
            travelUpwards();
        else
            travelDownwards();
    }

    private void travelUpwards() {
        verticalDirection = VERTICAL_DIRECTION_UP;
    }

    private void travelDownwards() {
        verticalDirection = VERTICAL_DIRECTION_DOWN;
    }

    private void move() {
        // make sure ball will be within viewport (bounce off sides)
        if (0 >= y - APPARENT_VELOCITY || y + APPARENT_VELOCITY + diameter >= Game.WINDOW_HEIGHT) {
            // hit top/bottom of window, reflect
            reflectVertical();
        }

        if (x + horizontalDelta + diameter >= Game.WINDOW_WIDTH || 0 >= x - horizontalDelta) {
            // hit side of windows, reflect
            reflectHorizontal();
        }

        // update vertical position
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

    /**
     * Calculates the "angle", which will be used to calculate the horizontal and vertical deltas
     * <p>
     * The angle can be measured as follows:
     * <p>
     * 0 being moving to the left, with vertical delta of 0
     * <p>
     * 90 being moving up or down, with horizontal delta of 0
     * <p>
     * 180 being moving to the right, with vertical delta of 0
     * <p>
     * If the calculated angle happens to be greater than 180 or less than -180, it is left unchanged
     */
    private void calculateAngle() {
        System.out.println("Incoming angle: " + angle);

        // usually is the paddle, but could be a brick as well
        Entity paddle = lastCollisionEvent.invoker.entities.get(1);
        if (paddle instanceof Ball) {
            paddle = lastCollisionEvent.invoker.entities.get(0);
        }

        double halfWidth = paddle.width / 2;
        double fracpos = (x - paddle.x) / halfWidth;

        if (x - paddle.x > halfWidth) {
            // right half, but should be same proportion
            fracpos = (x - paddle.x) / paddle.width;
        }

        angle = fracpos * ANGLE_BASE;

        if (fracpos > 1) {
            // right half
            System.out.println("Reflecting " + ((fracpos - 1) * ANGLE_BASE));
            angle = Calculate.getReflectedAngle((fracpos - 1) * ANGLE_BASE);
        }
        // else left half, so just keep angle the same, don't reflect, or "convert", it

        System.out.println("New angle: " + angle + "\nfracpos = " + fracpos);
        calculateDeltas();
    }

    /**
     * Calculates horizontal and vertical deltas (change per tick)
     * <p>
     * The angle should have already been calculated
     */
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
                // reflect because the initial angle is 90, and horizontal delta is -1, and it needs to be set
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
                Debug.warning("Could not calculate an angle! (must be [-180, 180])" + "\n");
                return;
        }

        // shouldn't need a margin of error correction if angle calculation is working!
		/*double moe = (horizontalDelta + verticalDelta - APPARENT_VELOCITY) / APPARENT_VELOCITY;
		moe *= 100;

		System.out.println(angle + "\nx -> " + horizontalDelta + ", y -> " + verticalDelta);
		System.out.println("Margin of error: " + String.format("%.2f", Math.abs(moe)) + "% (" + moe + ")\n");
		if (Math.abs(moe) > 0.1) {
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
		}*/
        Debug.out("Calulation method: " + method + "\nh: " + horizontalDelta + ", v: " + verticalDelta + "\n");
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, diameter, diameter));
    }

    @Override
    protected void setCollisions() {
        collisions = new ArrayList<>();
        collisions.add(new EntityEntityCollision(boundaries.get(0), Entity.PADDLE.boundaries.get(0)) {
            @Override
            public void onCollide(EntityEntityCollisionEvent event) {
                travelUpwards();
                lastCollisionEvent = event;
                calculateAngle();
            }
        });
    }

    @Override
    public String toString() {
        return "Ball#" + super.toString();
    }

}
