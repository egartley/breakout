package net.egartley.breakout.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseMotionListener {

	public static int x;
	public static int y;

	@Override
	public void mouseDragged(MouseEvent e) {
		// N/A
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

}
