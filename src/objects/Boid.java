package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import basics.Game;
import basics.GameObject;
import basics.Handler;
import basics.ID;

public class Boid extends GameObject {

	private Handler handler;
	private float maxSpeed;
	private float maxForce;

	public Boid(float x, float y, ID id, Handler handler, float startSpeedX, float startSpeedY, float maxSpeed,
			float maxForce) {
		super(x, y, id);

		this.handler = handler;
		this.maxSpeed = maxSpeed;
		this.maxForce = maxForce;

		speed = new Vector(startSpeedX, startSpeedY);
	}

	public void tick() {

		Vector seperation = seperation();
		Vector align = align();
		Vector cohesion = cohesion();

		seperation.mult(0.6f);
		align.mult(0.5f);
		cohesion.mult(0.65f);

		acceleration.add(seperation);
		acceleration.add(align);
		acceleration.add(cohesion);

		speed.add(acceleration);
		speed.limit(maxSpeed);
		location.add(speed);
		acceleration.mult(0);

		border();
	}

	private void border() {
		if (location.x > Game.WIDTH)
			location.x = 0;
		else if (location.x < 0)
			location.x = Game.WIDTH;

		if (location.y > Game.HEIGHT)
			location.y = 0;
		else if (location.y < 0)
			location.y = Game.HEIGHT;

	}

	public void render(Graphics g) {

		g.setColor(Color.white);

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform transform = g2d.getTransform();

		double angle = Math.atan2(speed.y, speed.x) + Math.PI / 2;
		g2d.rotate(angle, location.x, location.y);

		g2d.fillPolygon(
				new int[] { (int) location.x, (int) (location.x - 3), (int) location.x, (int) (location.x + 3) },
				new int[] { (int) location.y, (int) (location.y + 12), (int) (location.y + 9),
						(int) (location.y + 12) },
				4);

		g2d.setTransform(transform);
	}

	public Rectangle getBounds() {
		return null;
	}

	private Vector seperation() {

		var desiredSeperation = 50;
		var steer = new Vector();
		var count = 0;

		for (GameObject other : handler.object) {
			if (other.getId() == ID.BOID) {
				float d = location.dist(other.getLocation());

				if (other != this && d <= desiredSeperation) {
					Vector diff = new Vector().sub(this.location, other.getLocation());
					diff.normalize();
					diff.div(d);
					steer.add(diff);
					count++;
				}
			}
		}

		if (count > 0)
			steer.div(count);

		if (steer.mag() > 0) {
			steer.normalize();
			steer.mult(maxSpeed);
			steer.sub(speed);
			steer.limit(maxForce);
		}

		return steer;
	}

	private Vector align() {

		var neighborDist = 25;
		var sum = new Vector();
		var count = 0;

		for (GameObject other : handler.object) {
			if (other.getId() == ID.BOID) {
				float d = location.dist(other.getLocation());

				if (other != this && d <= neighborDist) {
					sum.add(other.getSpeed());
					count++;
				}
			}
		}

		if (count > 0) {
			sum.div(count);
			sum.normalize();
			sum.mult(maxSpeed);
			Vector steer = new Vector().sub(sum, speed);
			steer.limit(maxForce);
			return steer;
		}

		return new Vector();
	}

	private Vector cohesion() {

		var neighborDist = 25;
		var sum = new Vector();
		var count = 0;

		for (GameObject other : handler.object) {
			if (other.getId() == ID.BOID) {
				float d = location.dist(other.getLocation());

				if (other != this && d <= neighborDist) {
					sum.add(other.getSpeed());
					count++;
				}
			}
		}

		if (count > 0) {
			sum.div(count);
			Vector desired = new Vector().sub(sum, location);
			desired.normalize();
			desired.mult(maxSpeed);
			desired.limit(maxForce);
			// steeting
			return new Vector().sub(desired, speed);
		}

		return new Vector();
	}

}
