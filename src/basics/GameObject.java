package basics;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {

	protected Vector location;
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

		public void mult(Vector v) {
			x *= v.mag();
			y *= v.mag();
		}

		public Vector mult(Vector u, Vector v) {
			return new Vector(u.x * v.mag(), u.y * v.mag());
		}

		public void mult(float m) {
			x *= m;
			y *= m;
		}

		public Vector mult(Vector u, float m) {
			return new Vector(u.x * m, u.y * m);
		}

		public void div(Vector v) {
			x /= v.mag();
			y /= v.mag();
		}

		public void div(float m) {
			x /= m;
			y /= m;
		}

		public Vector div(Vector u, Vector v) {
			return new Vector(u.x / v.mag(), u.y + v.mag());
		}

		public float mag() {
			return (float) Math.sqrt(x * x + y * y);
		}

		public void normalize() {
			x /= mag();
			y /= mag();
		}

		public Vector normalize(Vector u) {
			u.normalize();
			return u;
		}

		public void limit(float limit) {
			this.normalize();
			x *= Math.sqrt(limit * limit);
			y *= Math.sqrt(limit * limit);
		}

		public float dist(Vector v) {
			return (float) Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2));
		}

		public float dist(Vector u, Vector v) {
			return (float) Math.sqrt(Math.pow(u.x - v.x, 2) + Math.pow(u.y - v.y, 2));
		}
	}

	protected GameObject(float x, float y, ID id) {

		this.location = new Vector(x, y);
		speed = new Vector();
		acceleration = new Vector();
		this.id = id;
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public abstract Rectangle getBounds();

	public Vector getLocation() {
		return location;
	}

	public void setLocation(Vector location) {
		this.location = location;
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
