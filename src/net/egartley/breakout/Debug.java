package net.egartley.breakout;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Stuff for debugging
 */
public class Debug {

	/**
	 * Font to use while rendering debug lines
	 */
	private static Font font = new Font("Consolas", Font.PLAIN, 12);
	/**
	 * Used for calculating width of strings so that each line's background will be
	 * sized correctly
	 */
	private static FontMetrics fontMetrics;
	/**
	 * Color for the background of debug lines
	 */
	private static Color backgroundColor = new Color(0, 0, 0, 72);
	/**
	 * Whether or not font metrics have been set
	 */
	private static boolean setFontMetrics;
	/**
	 * Initial line x-coordinate
	 */
	private static int lx = 24;
	/**
	 * Initial line y-coordinate
	 */
	private static int ly = 24;
	/**
	 * Offset for each line
	 */
	private static int rowOffset = 18;
	/**
	 * Number of pixels to add as padding around each line of text
	 */
	private static final byte TEXT_PADDING = 4;

	/**
	 * Prints the given object using {@link java.io.PrintStream#println(Object)
	 * System.out.println(object)}
	 * 
	 * @param object
	 *            The object to print out
	 */
	public static void out(Object object) {
		System.out.println(object);
	}

	/**
	 * Prints the given object using {@link java.io.PrintStream#println(Object)
	 * System.out.println(object)} with the tag "INFO"
	 * 
	 * @param object
	 *            The object to print out
	 */
	public static void info(Object object) {
		out("INFO: " + object);
	}

	/**
	 * Prints the given object using {@link java.io.PrintStream#println(Object)
	 * System.out.println(object)} with the tag "WARNING"
	 * 
	 * @param object
	 *            The object to print out
	 */
	public static void warning(Object object) {
		out("WARNING: " + object);
	}

	/**
	 * Prints the given object using {@link java.io.PrintStream#println(Object)
	 * System.out.println(object)} with the tag "ERROR"
	 * 
	 * @param object
	 *            The object to print out
	 */
	public static void error(Object object) {
		out("ERROR: " + object);
	}

	@SuppressWarnings("unused")
	private static void drawLine(String s, Graphics graphics, int row) {
		if (setFontMetrics == false) {
			fontMetrics = graphics.getFontMetrics();
			// don't do this every tick, only once
			setFontMetrics = true;
		}
		graphics.setColor(backgroundColor);
		// background (semi-transparent black)
		graphics.fillRect(lx - TEXT_PADDING, ly + (row * rowOffset) - font.getSize(), fontMetrics.stringWidth(s) + (TEXT_PADDING * 2), font.getSize() + TEXT_PADDING);
		graphics.setColor(Color.WHITE);
		// draw line text
		graphics.drawString(s, lx, ly + (row * rowOffset));
	}

	/**
	 * Render debug information
	 * 
	 * @param graphics
	 *            The {@link java.awt.Graphics Graphics} object to use
	 */
	public static void render(Graphics graphics) {
		if (Game.debug) {
			/*graphics.setFont(font);
			drawLine("Player (" + (int) Entities.PLAYER.x + ", " + (int) Entities.PLAYER.y + ")", graphics, 0);
			drawLine("isCollided = " + Entities.PLAYER.isCollided, graphics, 1);
			drawLine("Location: " + TestMap.currentSector, graphics, 2);
			if (Entities.PLAYER.lastCollision != null) {
				drawLine("Last collision: " + Entities.PLAYER.lastCollision.boundary1 + " and "
						+ Entities.PLAYER.lastCollision.boundary2.parent + " (collidedSide = "
						+ Entities.PLAYER.lastCollisionEvent.collidedSide + ")", graphics, 3);
			}*/
		}
	}

}
