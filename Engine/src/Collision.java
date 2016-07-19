
public class Collision {
	
	public static void CircleToCircle(Manifold m, Body a, Body b) {
		Vector normal = b.pos.subt(a.pos);
		
		double dist_sqr = normal.lengthSquared();
		double radius = a.radius * b.radius;
		
		if (dist_sqr >= radius) {
			//System.out.println(a + "\n" + b + "\n" + dist_sqr + " : " + radius);
			m.contact_count = 0;
			return;
		}
		
		double distance = Math.sqrt(dist_sqr);
		
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
