
public class Manifold {
	protected Body a;
	protected Body b;

	protected double penetration;
	Vector normal;
	Vector[] contacts = new Vector[2];
	int contact_count;
	double e;
	double df;
	double sf;

	public Manifold(Body a, Body b) {
		this.a = a;
		this.b = b;
	}

	public void solve() {
		Collision.CircleToCircle(this, a, b);
	}

	public void init() {
		e = Math.min(a.restitution, b.restitution);

		sf = Math.sqrt(a.staticFriction * b.staticFriction);
		df = Math.sqrt(a.dynamicFriction * b.dynamicFriction);

		for (int i = 0; i < contact_count; i++) {
			Vector ra = contacts[i].subt(a.pos);
			Vector rb = contacts[i].subt(b.pos);

			Vector rv = b.vel.add(rb.cross(b.angularVel))
					.subt(a.vel.subt(ra.cross(a.angularVel)));
//			if (rv.lengthSquared() < (dt * gravity).lengthSquared() + EPSILON) {
//				e = 0;
//			}
		}
	}

	public void applyImpulse() {
		if (a.im + b.im == 0) {
			InfMassCorr();
			return;
		}

		for (int i = 0; i < contact_count; i++) {
			Vector ra = contacts[i].subt(a.pos);
			Vector rb = contacts[i].subt(b.pos);

			Vector rv = b.vel.add(rb.cross(b.angularVel))
					.subt(a.vel.subt(ra.cross(a.angularVel)));

			double contactVel = rv.dot(normal);

			if (contactVel > 0) return;

			double raCrossN = ra.cross(normal);
			double rbCrossN = rb.cross(normal);
			double invMassSum = a.im + b.im
					+ raCrossN * raCrossN * a.iI
					+ rbCrossN * rbCrossN * b.iI;

			double j = -(1 + e) * contactVel;
			j /= invMassSum;
			j /= 35;
			j /= contact_count;

			Vector impulse = normal.mult(j);
			a.applyImpulse(impulse.mult(-1), ra);
			b.applyImpulse(impulse, rb);

			rv = b.vel.add(rb.cross(b.angularVel))
					.subt(a.vel.subt(ra.cross(a.angularVel)));

			Vector t = rv.subt(normal.mult(rv.dot(normal)));
			t.normalize();

			double jt = -rv.dot(t);
			jt /= invMassSum;
			jt /= contact_count;

			if (jt == 0) {
				return;
			}

			Vector tangentImpulse;
			if (Math.abs(jt) < j * sf) {
				tangentImpulse = t.mult(jt);
			} else {
				tangentImpulse = t.mult(-j).mult(df);
			}

			a.applyImpulse(tangentImpulse.mult(-1), ra);
			b.applyImpulse(tangentImpulse, rb);
		}
	}

	public void positionalCorrection() {
		double k_slop = 0.05;
		double percent = 0.4;
		//System.out.println(normal);
		Vector correction = Vector.divide(Math.max(penetration - k_slop, 0), normal)
				.mult(a.im + b.im)
				.mult(percent);
		a.setPos(a.pos.subt(correction.mult(a.im)));
		b.setPos(b.pos.add(correction.mult(b.im)));
	}

	public void InfMassCorr() {
		a.vel = new Vector(0, 0);
		b.vel = new Vector(0, 0);
	}

}
