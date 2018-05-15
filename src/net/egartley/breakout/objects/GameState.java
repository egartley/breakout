package net.egartley.breakout.objects;

import java.awt.*;

/**
 * One of the game's "states" that has unique render and tick methods
 */
public abstract class GameState {

	/**
	 * The ID number used while actually playing the game
	 * 
	 * @see GameState
	 */
	public static final int IN_GAME = 0;
	/**
	 * Unique integer used to identify different game states
	 */
	protected int identificationNumber;

	public abstract void render(Graphics graphics);

	public abstract void tick();

}
