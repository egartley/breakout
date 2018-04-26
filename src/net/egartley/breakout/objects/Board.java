package net.egartley.breakout.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Board {

	public static byte horizontalSize = 10;
	public static byte verticalSize = 8;

	private static Color[] colors = new Color[] { Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, Color.YELLOW,
			Color.YELLOW, Color.GREEN, Color.GREEN };
	public static ArrayList<Brick> bricks = new ArrayList<Brick>();

	public static void init() {
		for (byte i = 0; i < verticalSize; i++) {
			for (byte k = 0; k < horizontalSize; k++) {
				bricks.add(new Brick(colors[i]));
			}
		}
	}

	public static void render(Graphics graphics) {
		for (Brick b : bricks) {
			b.render(graphics);
		}
	}

	public static void tick() {
		for (Brick b : bricks) {
			b.tick();
		}
	}

}
