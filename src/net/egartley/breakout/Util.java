package net.egartley.breakout;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import net.egartley.breakout.logic.collision.EntityEntityCollision;
import net.egartley.breakout.logic.events.EntityEntityCollisionEvent;
import net.egartley.breakout.logic.interaction.EntityBoundary;
import net.egartley.breakout.objects.Entity;

/**
 * Miscellaneous methods that don't fit into a specific class or object
 */
public class Util {

	// https://stackoverflow.com/a/13605411
	private static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
	}

	/**
	 * Returns a resized image of the original with the supplied width and height
	 * 
	 * @param image
	 *            The original image to resize (won't be changed)
	 * @param w
	 *            New width to resize to
	 * @param h
	 *            New height to resize to
	 * @return A resized version of the given buffered image
	 */
	public static BufferedImage resized(BufferedImage image, int w, int h) {
		return toBufferedImage(image.getScaledInstance(w, h, Image.SCALE_DEFAULT));
	}

	/**
	 * Returns a random integer, using {@link java.util.concurrent.ThreadLocalRandom
	 * ThreadLocalRandom}, between the supplied maximum and minimum values
	 * 
	 * @param maximum
	 *            The maximum value the random integer could be
	 * @param minimum
	 *            The minimum value the random integer could be
	 * @return A randon integer between the given maximum and minimum
	 */
	public static int randomInt(int maximum, int minimum) {
		// this is using ThreadLocalRandom because that is apparently more efficient
		return ThreadLocalRandom.current().nextInt(minimum, maximum);
	}

	/**
	 * <p>
	 * Returns a random integer between the supplied maximum and minimum values
	 * (uses {@link #randomInt(int, int) randomInt(minimum, maximum)})
	 * </p>
	 * 
	 * <p>
	 * If inclusive, then {@link #randomInt(int, int) randomInt(minimum, maximum +
	 * 1)} will be used
	 * </p>
	 * 
	 * @param maximum
	 *            The maximum value the random integer could be
	 * @param minimum
	 *            The minimum value the random integer could be
	 * @param inclusive
	 *            Whether or not the include the maximum as a possible value
	 * @return A randon integer between the given maximum and minimum
	 */
	public static int randomInt(int maximum, int minimum, boolean inclusive) {
		if (inclusive) {
			return randomInt(maximum + 1, minimum);
		} else {
			return randomInt(maximum, minimum);
		}
	}

	/**
	 * Returns generated entity-to-entity collisions around the given parameters
	 * 
	 * @param baseEvent
	 *            The
	 *            {@link net.egartley.breakout.logic.collision.EntityEntityCollision
	 *            EntityEntityCollision} in which to base all of the returned ones
	 *            on
	 * @param entity
	 *            The {@link net.egartley.breakout.objects.Entity Entity} in
	 *            which to generate collisions around each of its boundaries
	 * @param baseBoundary
	 *            The other
	 *            {@link net.egartley.breakout.logic.interaction.EntityBoundary
	 *            EntityBounadry} in which to base the generated collisions around
	 * @return Generated entity-to-entity collisions based on the given parameters
	 */
	public static ArrayList<EntityEntityCollision> getAllBoundaryCollisions(EntityEntityCollision baseEvent,
			Entity entity, EntityBoundary baseBoundary) {
		ArrayList<EntityEntityCollision> collisions = new ArrayList<EntityEntityCollision>();
		for (EntityBoundary boundary : entity.boundaries) {
			collisions.add(new EntityEntityCollision(boundary, baseBoundary) {
				public void onCollide(EntityEntityCollisionEvent event) {
					baseEvent.onCollide(event);
				};

				public void onCollisionEnd(EntityEntityCollisionEvent event) {
					baseEvent.onCollisionEnd(event);
				};
			});
		}
		return collisions;
	}

}
