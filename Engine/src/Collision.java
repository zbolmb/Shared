
public class Collision {

	public static void CircleToCircle(Manifold m, Body a, Body b) {


		Vector normal = b.pos.subt(a.pos);

		double dist_sqr = normal.lengthSquared();
		double radius = a.radius * b.radius;
		double distance = Math.sqrt(distSq(a, b));

		if (!AABB(a, b) && distance > a.radius + b.radius) {
			m.contact_count = 0;
			return;
		} else {
			m.contact_count = 1;

			if (distance == 0) {
				m.penetration = a.radius;
				m.normal = new Vector(1, 0);
				m.contacts[0] = a.pos;
			} else {
				m.penetration = radius - distance;
				m.normal = normal.mult(1 / distance);
				m.contacts[0] = m.normal.mult(a.radius).add(a.pos);
			}
		}
	}

	public static boolean AABB(Body a, Body b) {
		return (a.pos.x + a.radius + b.radius > b.pos.x
			&& a.pos.x < b.pos.x + a.radius + b.radius
			&& a.pos.y + a.radius + b.radius > b.pos.y
			&& a.pos.y < b.pos.y + a.radius + b.radius);
	}

	public static double distSq(Body a, Body b) {
		return ((a.pos.x - b.pos.x) * (a.pos.x - b.pos.x))
		+ ((a.pos.y - b.pos.y) * (a.pos.y - b.pos.y));
	}
}
