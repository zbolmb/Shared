import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;


public class scene extends Application {

	double m_dt = 1 / 60.0;
	int m_iterations = 5;
	ArrayList<Body> bodies = new ArrayList<>();
	ArrayList<Manifold> contacts = new ArrayList<>();
	double mouseX = 0;
	double mouseY = 0;
	int radius = 25;
	double gravity = 98;
	int time = 1000;

	public void IntegrateForces(Body b, double dt) {
		if (b.im == 0) {
			return;
		}

		b.vel = b.vel.add(((b.force.mult(b.im)).add(new Vector(0, gravity))).mult(dt / 2));
		b.angularVel += b.torque * b.iI * (dt / 2);
	}

	public void IntegrateVelocity(Body b, double dt) {
		if (b.im == 0) {
			return;
		}
		if (b.pos.y > 475) {
			b.setPos(new Vector(b.pos.x, 475));
			b.vel.y = b.vel.y * -.6;
		} else if (b.pos.y < 25) {
			b.setPos(new Vector(b.pos.x, 25));
			b.vel.y = b.vel.y * -.6;
		} else if (b.pos.x < 25) {
			b.setPos(new Vector(25, b.pos.y));
			b.vel.x = b.vel.x * -.6;
		} else if (b.pos.x > 475) {
			b.setPos(new Vector(475, b.pos.y));
			b.vel.x = b.vel.x * -.6;
		} else {
			b.setPos(b.pos.add(b.vel.mult(dt)));
			b.orient += b.angularVel * dt;
			b.setOrient(b.orient);
			IntegrateForces(b, dt);
		}
	}

	public void step() {
		contacts.clear();
		Body a;
		Body b;
		for (int i = 0; i < bodies.size(); i++) {
			a = bodies.get(i);

			for (int j = i + 1; j < bodies.size(); j++) {
				b = bodies.get(j);
				if (a.im == 0 && b.im == 0) {
					continue;
				}
				Manifold m = new Manifold(a, b);
				m.solve();
				contacts.add(m);
			}
		}

		for (int i = 0; i < bodies.size(); i++) {
			IntegrateForces(bodies.get(i), m_dt);
		}

		for (int i = 0; i < contacts.size(); i++) {
			contacts.get(i).init();
		}

		for (int j = 0; j < m_iterations; j++) {
			for (int i = 0; i < contacts.size(); i++) {
				contacts.get(i).applyImpulse();
			}
		}

		for (int i = 0; i < bodies.size(); i++) {
			IntegrateVelocity(bodies.get(i), m_dt);
		}

		//System.out.println(contacts.size());
		for (int i = 0; i < contacts.size(); i++) {
			if (contacts.get(i).contact_count != 0) {
				contacts.get(i).positionalCorrection();
			}
		}

		for (int i = 0; i < bodies.size(); i++) {
			b = bodies.get(i);
			b.force = new Vector(0, 0);
			b.torque = 0;
		}
	}

	public void start(Stage primary) {
		Group root = new Group();
		Scene scene = new Scene(root, 500, 500);
		Timeline update;
		Timeline mousePress;

		for (int i = 0; i < bodies.size(); i++) {
			root.getChildren().add(bodies.get(i));
		}

		update = new Timeline(new KeyFrame(
			Duration.millis(10),
			ae -> step()));
		update.setCycleCount(Animation.INDEFINITE);
		update.play();

		mousePress = new Timeline(new KeyFrame(
			Duration.millis(1000),
			ae -> drawCircle(root, mouseX, mouseY)));
		mousePress.setCycleCount(Animation.INDEFINITE);

		scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			mouseX = e.getX();
			mouseY = e.getY();
			drawCircle(root, mouseX, mouseY);
			//System.out.println(mouseX + " : " +  mouseY);
			mousePress.play();
		});

		scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
			mouseX = e.getX();
			mouseY = e.getY();
		});

		scene.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			mousePress.pause();
		});

		primary.setScene(scene);
		primary.show();
	}

	public void drawCircle(Group root, double x, double y) {
		bodies.add(new Body(x, y, radius));
		root.getChildren().add(bodies.get(bodies.size() - 1));
	}


	public static void main(String[] args) {
		launch(args);
	}
}
