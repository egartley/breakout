package net.egartley.breakout.gamestates;

import java.awt.Color;
import java.awt.Graphics;

import net.egartley.breakout.Game;
import net.egartley.breakout.objects.*;

public class InGameState extends GameState {

	public InGameState() {
		identificationNumber = GameState.IN_GAME;
		Entity.PADDLE = new Paddle();
		Entity.BALL = new Ball();
		Board.init();
	}

	@Override
	public void render(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);

		Board.render(graphics);

		Entity.PADDLE.render(graphics);
		Entity.BALL.render(graphics);
	}

	@Override
	public void tick() {
		Entity.PADDLE.tick();
		Entity.BALL.tick();
		Board.tick();
	}

}
