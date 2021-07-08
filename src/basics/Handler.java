package basics;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

public class Handler {

	public final List<GameObject> object = new LinkedList<>();

	public void tick() {
		for (var i = 0; i < object.size(); i++) {
			object.get(i).tick();
		}
	}

	public void render(Graphics g) {
		for (var i = 0; i < object.size(); i++) {
			object.get(i).render(g);
		}
	}

	public void addObject(GameObject object) {
		this.object.add(object);
	}

	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
}
