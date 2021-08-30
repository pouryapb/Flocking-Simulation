package basics;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {

	protected Vector position;
	protected Vector speed;
	protected Vector acceleration;
	protected ID id;

	public class Vector {
		public float x;
		public float y;

		public Vector() {
			x = 0;
			y = 0;
		}

		public Vector(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public void add(Vector v) {
			x += v.x;
			y += v.y;
		}

		public void add(float m) {
			x += m;
			y += m;
		}

		public Vector add(Vector u, Vector v) {
			return new Vector(u.x + v.x, u.y + v.y);
		}

		public void sub(Vector v) {
			x -= v.x;
			y -= v.y;
		}

		public void sub(float m) {
			x -= m;
			y -= m;
		}

		public Vector sub(Vector u, Vector v) {
			return new Vector(u.x - v.x, u.y - v.y);
		}

		public Vector mult(Vector v) {
			x *= v.mag();
			y *= v.mag();

			return this;
		}

		public Vector mult(Vector u, Vector v) {
			return new Vector(u.x * v.mag(), u.y * v.mag());
		}

		public Vector mult(float m) {
			x *= m;
			y *= m;

			return this;
		}

		public Vector mult(Vector u, float m) {
			return new Vector(u.x * m, u.y * m);
		}

		public void div(Vector v) {
			x /= v.mag();
			y /= v.mag();
		}

		public Vector div(float m) {
			x /= m;
			y /= m;
			return this;
		}

		public Vector div(Vector u, Vector v) {
			return new Vector(u.x / v.mag(), u.y + v.mag());
		}

		public float mag() {
			return (float) Math.sqrt(x * x + y * y);
		}

		public Vector normalize() {
			var len = mag();
			if (len != 0)
				this.mult(1 / len);
			return this;
		}

		public Vector limit(float max) {
			var magSq = x * x + y * y;

			if (magSq > max * max) {
				this.div((float) Math.sqrt(magSq)).mult(max);
			}
			return this;
		}

		public Vector setMag(float n) {
			return this.normalize().mult(n);
		}

		public float dist(Vector v) {
			return (float) Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2));
		}

		public float dist(Vector u, Vector v) {
			return (float) Math.sqrt(Math.pow(u.x - v.x, 2) + Math.pow(u.y - v.y, 2));
		}
	}

	protected GameObject(float x, float y, ID id) {

		this.position = new Vector(x, y);
		speed = new Vector();
		acceleration = new Vector();
		this.id = id;
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public abstract Rectangle getBounds();

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector location) {
		this.position = location;
	}

	public Vector getSpeed() {
		return speed;
	}

	public void setSpeed(Vector speed) {
		this.speed = speed;
	}

	public Vector getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector acceleration) {
		this.acceleration = acceleration;
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}
}
