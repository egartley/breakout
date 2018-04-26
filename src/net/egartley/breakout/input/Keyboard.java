package net.egartley.breakout.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import net.egartley.breakout.Game;

public class Keyboard implements KeyListener {

	private static ArrayList<Integer> pressedKeyCodes = new ArrayList<Integer>();

	/**
	 * @param keycode
	 *            The key code from {@link java.awt.event.KeyEvent KeyEvent }
	 * @return Whether or not the provided key is currently being pressed down
	 */
	public static boolean isKeyPressed(int keycode) {
		return pressedKeyCodes.contains(keycode);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		if (!isKeyPressed(keycode)) {
			pressedKeyCodes.add(keycode);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (pressedKeyCodes.indexOf(e.getKeyCode()) != -1) {
			pressedKeyCodes.remove(pressedKeyCodes.indexOf(e.getKeyCode()));
		}
		if (e.getKeyCode() == KeyEvent.VK_F3) {
			Game.debug = !Game.debug;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
