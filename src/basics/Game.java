package basics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import objects.Boid;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -5571995104017886412L;
	public static final int WIDTH = 1350;
	public static final int HEIGHT = WIDTH / 12 * 9;
	private transient Thread thread;
	private boolean running = false;
	private transient Handler handler;
	private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

	public Game() {
		handler = new Handler();

		// projects
		/////////////////////////////

		for (var i = 0; i < 500; i++) {
			handler.addObject(
					new Boid(new Random().nextInt(WIDTH), new Random().nextInt(HEIGHT), ID.BOID, handler, 4f, 0.5f));
		}

		/////////////////////////////

		new Window(WIDTH, HEIGHT, "Flocking Simulation", this);
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}

	}

	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		var amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		var frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if (running)
				render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				var log = "FPS: " + frames;
				LOGGER.log(Level.INFO, log);
				frames = 0;
			}
		}
		stop();
	}

	public void tick() {
		handler.tick();
	}

	public void render() {
		var bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		var g = bs.getDrawGraphics();
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		/////////////////////////////////////

		g.setColor(Color.decode("#212121"));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		handler.render(g);

		/////////////////////////////////////
		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		new Game();
	}
}
