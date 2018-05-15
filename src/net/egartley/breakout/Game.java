package net.egartley.breakout;

import net.egartley.breakout.gamestates.InGameState;
import net.egartley.breakout.input.Mouse;
import net.egartley.breakout.objects.GameState;
import net.egartley.breakout.threads.MasterTick;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * @author Evan Gartley
 */
public class Game extends Canvas implements Runnable {

	// SELF
	private static final long serialVersionUID = 7757649690453643797L;
	private static long startTime;
	private static short frames;
	private static short currentFrames;
	private static JFrame frame;
	private static Dimension windowDimension = new Dimension(974, 573);
	private Graphics graphics;
	private BufferStrategy bufferStrategy;

	// CONSTANTS
	public static final int WINDOW_WIDTH = windowDimension.width - 6;
	public static final int WINDOW_HEIGHT = windowDimension.height - 29;

	// THREADS
	private static Thread masterRenderThread;
	private static Thread masterTickThread;

	// THREAD OBJECTS
	private static MasterTick tick = new MasterTick();

	// FLAGS
	public static boolean running = false;
	/**
	 * Whether or not the main tick thread ({@link #tick}) is runnning
	 */
	public static boolean runTickThread = true;
	/**
	 * Whether or not to perform debug related operations
	 */
	public static boolean debug = true;

	private static Game self;

	// GAMESTATES
	public static GameState currentGameState;

	private void init() {
		currentGameState = new InGameState();

		Debug.out("Initializing the JFrame...");

		// initialize jframe with title
		frame = new JFrame("Breakout");
		// set window size
		frame.setSize(windowDimension.width, windowDimension.height);
		// normal close operation
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// enforce window size (for now)
		frame.setResizable(false);
		// add mouse input
		this.addMouseMotionListener(new Mouse());
		// add the "game" to the frame so things will actually display and update
		frame.add(self);
		// center the frame's window in the user's screen
		frame.setLocationRelativeTo(null);
		// actually show it
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		startTime = System.currentTimeMillis();
		self = new Game();
		self.setPreferredSize(windowDimension);
		self.setMaximumSize(windowDimension);
		self.setMinimumSize(windowDimension);

		// actually start the game
		self.start();
	}

	private synchronized void start() {
		if (running) {
			// already "running" so the render and tick threads should have already been
			// started
			return;
		}
		running = true;
		masterRenderThread = new Thread(this);
		masterRenderThread.setPriority(1);
		masterRenderThread.setName("Main-Render");

		// this actually starts the tick thread for the first time
		restartMainTickThread();
		// starts the rendering thread
		masterRenderThread.start();
	}

	private synchronized void stop() {
		if (!running) {
			return;
		}
		// stops the fps system, thus ending calls to render and tick methods
		running = false;
		try {
			masterRenderThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Debug.out("Starting render thread...");
		// load images, save data, etc.
		init();
		// enable double buffering
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
		// get graphics object to render to
		graphics = bufferStrategy.getDrawGraphics();
		// enable anti-aliasing for strings
		Graphics2D g2d = (Graphics2D) graphics;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// actually render (for the first time)
		Debug.out("Rendering for the first time...");
		render();
		// setup system for ensuring that the game runs at most 60 fps
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 16666666.666666666;
		double delta = 0.0D;
		// request user's operating system "focus", i.e. mouse and keyboard input
		requestFocus();
		Debug.out("Startup: " + ((System.currentTimeMillis() - startTime) / 1000.0) + " seconds\n");
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0D) {
				// render with the graphics object (tick is in a seperate thread)
				render();
				delta -= 1.0D;
				frames += 1;
				if (System.currentTimeMillis() - timer > 1000L) {
					timer += 1000L;
					currentFrames = frames;
					frames = 0;
				}
			}
			// this helps to stabilize the fps system
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Debug.out("Stopping render thread...");
		// end the game, thus terminating the process
		stop();
	}

	private synchronized void render() {
		currentGameState.render(graphics);
		bufferStrategy.show();
	}

	/**
	 * Stops all calls to tick methods, but render methods are still called
	 */
	public static void stopMainTickThread() {
		Debug.out("Stopping tick thread...");
		runTickThread = false;
	}

	/**
	 * Restarts the main tick thread, which enables calls to tick methods
	 */
	public static void restartMainTickThread() {
		Debug.out("Starting tick thread...");
		runTickThread = true;
		masterTickThread = new Thread(tick);
		masterTickThread.setPriority(2);
		masterTickThread.setName("Main-Tick");
		masterTickThread.start();
	}

	/**
	 * Returns the current FPS, which should always be between 60 and 57
	 * 
	 * @return The game's current frames per second
	 */
	public static short getFramesPerSecond() {
		return currentFrames;
	}

}
