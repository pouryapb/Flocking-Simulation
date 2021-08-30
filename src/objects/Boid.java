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

		seperation.mult(1f);
		align.mult(1f);
		cohesion.mult(0.9f);

		acceleration.add(seperation);
		acceleration.add(align);
		acceleration.add(cohesion);

		speed.add(acceleration);
		speed.limit(maxSpeed);
		position.add(speed);
		acceleration.mult(0);

		border();
	}

	private void border() {
		if (position.x > Game.WIDTH)
			position.x = 0;
		else if (position.x < 0)
			position.x = Game.WIDTH;

		if (position.y > Game.HEIGHT)
			position.y = 0;
		else if (position.y < 0)
			position.y = Game.HEIGHT;

	}

	public void render(Graphics g) {

		g.setColor(Color.white);

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform transform = g2d.getTransform();

		double angle = Math.atan2(speed.y, speed.x) + Math.PI / 2;
		g2d.rotate(angle, position.x, position.y);

		g2d.fillPolygon(
				new int[] { (int) position.x, (int) (position.x - 3), (int) position.x, (int) (position.x + 3) },
				new int[] { (int) position.y, (int) (position.y + 12), (int) (position.y + 9),
						(int) (position.y + 12) },
				4);

		g2d.setTransform(transform);
	}

	public Rectangle getBounds() {
		return null;
	}

	private Vector align() {
		var neighborDist = 100;
		var steering = new Vector();
		var count = 0;

		for (GameObject other : handler.object) {
			if (other.getId() == ID.BOID) {
				float d = position.dist(other.getPosition());

				if (other != this && d <= neighborDist) {
					steering.add(other.getSpeed());
					count++;
				}
			}
		}

		if (count > 0) {
			steering.div(count);
			steering.setMag(maxSpeed);
			steering.sub(speed);
			steering.limit(maxForce);
		}

		return steering;
	}

	private Vector cohesion() {
		var neighborDist = 100;
		var steering = new Vector();
		var count = 0;

		for (GameObject other : handler.object) {
			if (other.getId() == ID.BOID) {
				float d = position.dist(other.getPosition());

				if (other != this && d <= neighborDist) {
					steering.add(other.getPosition());
					count++;
				}
			}
		}

		if (count > 0) {
			steering.div(count);
			steering.sub(position);
			steering.setMag(maxSpeed);
			steering.sub(speed);
			steering.limit(maxForce);
		}

		return steering;
	}

	private Vector seperation() {
		var neighborDist = 50;
		var steering = new Vector();
		var count = 0;

		for (GameObject other : handler.object) {
			if (other.getId() == ID.BOID) {
				float d = position.dist(other.getPosition());

				if (other != this && d <= neighborDist) {
					var diff = new Vector().sub(this.position, other.getPosition());
					diff.mult(1 / d);
					steering.add(diff);
					count++;
				}
			}
		}

		if (count > 0) {
			steering.div(count);
			steering.setMag(maxSpeed);
			steering.sub(speed);
			steering.limit(maxForce);
		}

		return steering;
	}

}
