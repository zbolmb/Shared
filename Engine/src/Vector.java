import com.sun.javafx.geom.Vec2d;

/**
 * Basic Vector class, made only for basic usability
 * @author Zhijian
 * @version 1.0
 */
public class Vector extends Vec2d {

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// dot product of two vectors
	public double dot(Vector b) {
		return this.x * b.x + this.y * b.y;
	}

	//normL of vector
	public Vector normL() {
		return new Vector(this.y, -this.x);
	}

	//returns UnitVector of Vector
	public Vector normalize() {
		double len = Math.sqrt(x * x + y * y);
		return new Vector(x / len, y / len);
	}

	public Vector subt(Vector b) {
		return new Vector(x - b.x, y - b.y);
	}

	public Vector mult(double s) {
		return new Vector(x * s, y * s);
	}

	public Vector add(Vector b) {
		return new Vector(x + b.x, y + b.y);
	}

	public Vector add(double n) {
		return new Vector(x + n, y + n);
	}

	public Vector inverse() {
		return new Vector(y, x);
	}

	public double lengthSquared() {
		return (x * x) + (y * y);
	}

	public double length() {
		return Math.sqrt(lengthSquared());
	}

	public double cross(Vector v) {
		return this.x * v.y - this.y * v.x;
	}

	public Vector cross(double a) {
		return new Vector(-a * y, a * x);
	}

	public static Vector divide(double n, Vector v) {
		return new Vector(n / v.x, n / v.y);
	}
}
