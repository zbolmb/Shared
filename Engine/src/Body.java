import java.util.Random;

import javafx.scene.shape.Circle;

public class Body extends Circle {
	
	protected Vector pos;
	protected Vector vel;
	protected double radius;
	
	protected double angularVel;
	protected double torque;
	protected double orient;
	
	protected Vector force;
	
	protected double I;
	protected double iI;
	protected double m;
	protected double im;
	
	protected double staticFriction;
	protected double dynamicFriction;
	protected double restitution;
	
	private Random rand = new Random();
	
	public Body(double x, double y, int radius) {
		super(x, y, radius);
		pos = new Vector(x, y);
		vel = new Vector(0, 0);
		angularVel = 0;
		torque = 0;
		orient = 3.14 - rand.nextDouble() * 6.28;
		force = new Vector(0, 0);
		staticFriction = 0.5;
		dynamicFriction = 0.3;
		restitution = 0.2;
		this.radius = radius;
		im = 1 / 6.0;
		
	}
	
	public void applyImpulse(Vector impulse, Vector contactVector) {
		vel = vel.add(impulse.mult(im));
		//System.out.println(vel);
		angularVel += iI * contactVector.cross(impulse);
	}
	
	public void setStatic() {
		I = 0;
		iI = 0;
		m = 0;
		im = 0;
	}	
	
	public void setOrient(double radians) {
		orient = radians;
	}
	
	public void computeMass(double density) {
		m = 3.14 * radius * radius * density;
		if (m != 0) {
			im = 1 / m;
		} else {
			im = 0;
		}
		I = m * radius * radius;
		if (I != 0) {
			iI = 1 / I;
		} else {
			I = 0;
		}
	}
	
	public void setPos(Vector v) {
		setPos(v.x, v.y);
	}
	
	public void setPos(double x, double y) {
		this.setCenterX(x);
		this.setCenterY(y);
		pos.x = x;
		pos.y = y;		
	}
	
	public void setX(double x) {
		this.setCenterX(x);
		pos.x = x;
	}
	
	public void setY(double y) {
		this.setCenterY(y);
		pos.y = y;
	}
	
	public String toString() {
		return pos + "";
	}
}
