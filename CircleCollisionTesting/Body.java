import java.util.Random;

import javafx.scene.shape.Circle;

public class Body extends Circle {

	private vel;

	public Body(double x, double y, double r) {
		super(x, y, r);

	}

	public double getX() {
		return this.getCenterX();
	}

	public void setX(double x) {
		this.setCenterX(x);
	}

	public double getY() {
		return this.getCenterY();
	}

	public void setY(double y) {
		this.setCenterY(y);
	}

}
