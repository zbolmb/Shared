import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GUI extends Application {

	private Piece[][] b;
	private AIPlayerMonteCarlo AI1;
	private AIPlayerMonteCarlo AI2;
	private Piece player1;
	private Piece player2;
	private Piece pColor;
	private Piece turn;
	private int gameOp;
	private Line l;
	private Timeline t;

	public void init() {
		b = new Piece[6][7];
		AI1 = new AIPlayerMonteCarlo(b, Piece.RED);
		AI2 = new AIPlayerMonteCarlo(b, Piece.YELLOW);
		turn = Piece.RED;
	}

	public void start(Stage primary) throws Exception {
		init();
		/**
		 * Intro Screen
		 * 1 Player : 1
		 * 2 Player : 2
		 * AI v AI : 3
		 */
		VBox gameOps = new VBox();
		gameOps.setAlignment(Pos.CENTER);
		gameOps.setSpacing(20);
		Scene introScreen = new Scene(gameOps, 500, 500);
		Button oneP = new Button("1 Player");
		Button twoP = new Button("2 Player");
		Button compVcomp = new Button("AI vs AI");

		/**
		 * Choosing a Color
		 */
		HBox chooseColor = new HBox();
		Scene chooseColorS = new Scene(chooseColor, 500, 500);
		chooseColor.setAlignment(Pos.CENTER);
		chooseColor.setSpacing(20);
		Button red = new Button();
		Button yellow = new Button();

		/**
		 * The game Screen
		 */
		Group board = new Group();
		Scene boardS = new Scene(board, 500, 500);

		// 1 Player Mode : 1
		oneP.setOnAction(e -> {
			primary.setScene(chooseColorS);
			gameOp = 1;
			t = new Timeline(new KeyFrame(
					Duration.millis(500),
					ae -> {
						if (AIPlayerMonteCarlo.hasWon(player1)) {
							board.getChildren().add(new Text(200, 250, player1 + " has won!"));
							t.pause();
						}
						if (turn == AI2.getColor()) {
							makeMove(board, AI2.moveG(), AI2.getColor());
							turn = turn.opPiece();
							t.pause();
						}
						if (AIPlayerMonteCarlo.hasWon(AI2.getColor())) {
							board.getChildren().add(new Text(200, 250, AI2.getColor() + " has won!"));
						}
					}));

			boardS.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				double x = event.getX();
				double y = event.getY();
				if (x > 40 && x < 460 && y < 90 && turn == player1) {
					makeMove(board, (int)(x - 40) / 60, turn);
					turn = turn.opPiece();
					t.play();
				}
			});
			t.setCycleCount(Animation.INDEFINITE);
			t.play();
		});
		addShadow(oneP);

		// 2 Player Mode : 2
		twoP.setOnAction(e -> {
			primary.setScene(chooseColorS);
			gameOp = 2;
			boardS.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				double x = event.getX();
				double y = event.getY();
				if (x > 40 && x < 460 && y < 90) {
					makeMove(board, (int)(x - 40) / 60, turn);
					turn = turn.opPiece();
				}
			});
		});
		addShadow(twoP);

		// AI vs AI mode : 3
		compVcomp.setOnAction(e -> {
			primary.setScene(boardS);
			gameOp = 3;
			Timeline t = new Timeline(new KeyFrame(
					Duration.millis(500),
					ae -> {
						if (!AI1.generateMoves().isEmpty()) {
							if (turn == Piece.RED) {
								makeMove(board, AI1.moveG(), Piece.RED);
								turn = turn.opPiece();
							} else {
								makeMove(board, AI2.moveG(), Piece.YELLOW);
								turn = turn.opPiece();
							}
						}
					}));
			t.setCycleCount(Animation.INDEFINITE);
			t.play();
		});
		addShadow(compVcomp);
		gameOps.getChildren().addAll(oneP, twoP, compVcomp);


		red.setStyle(
				"-fx-background-color: Red;" +
						"-fx-background-radius: 5em;" +
						"-fx-min-width: 60px; " +
						"-fx-min-height: 60px; " +
						"-fx-max-width: 60px; " +
						"-fx-max-height: 60px; "
				);
		red.setOnAction(e -> {
			player1 = Piece.RED;
			player2 = Piece.YELLOW;
			AI2.setColor(Piece.YELLOW);
			primary.setScene(boardS);
		});
		addShadow(red);

		yellow.setStyle(
				"-fx-background-color: Yellow;" +
						"-fx-background-radius: 5em;" +
						"-fx-min-width: 60px; " +
						"-fx-min-height: 60px; " +
						"-fx-max-width: 60px; " +
						"-fx-max-height: 60px; "
				);
		yellow.setOnAction(e -> {
			player1 = Piece.YELLOW;
			player2 = Piece.RED;
			AI2.setColor(Piece.RED);
			primary.setScene(boardS);
		});
		addShadow(yellow);
		Text chooseColor_Text = new Text(250, 200, "Choose a Color for Player 1");
		chooseColor.getChildren().addAll(red, yellow, chooseColor_Text);

		/**
		 * Creating the board
		 * dimensions are currently hard-coded
		 */
		board.getChildren().addAll(new Line(40, 90, 40, 450)
		, new Line(40, 90, 460, 90)
		, new Line(40, 450, 460, 450)
		, new Line(460, 90, 460, 450));
		Path p;
		MoveTo m;
		ArcTo a;
		p = new Path();

		/**
		 * to add a piece
		 * i = col
		 * j = rows
		 * col * 60 + 69.5
		 * row * 60 + 119.5
		 */
		for (int i = 0; i < b[0].length; i++) {
			for (int j = 0; j < b.length; j++) {
				p = new Path();
				m = new MoveTo();
				a = new ArcTo();
				m.setX(i * 60 + 40 + 43);
				m.setY(j * 60 + 90 + 15);
				a.setX(i * 60 + 1 + 40 + 43);
				a.setY(j * 60 + 1 + 90 + 15);
				a.setRadiusX(20);
				a.setRadiusY(20);
				a.setLargeArcFlag(true);
				p.getElements().add(m);
				p.getElements().add(a);
				board.getChildren().add(p);
			}
		}

		// Arrow showing where to place piece

		boardS.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
			double x = e.getX();
			double y = e.getY();
			//System.out.println(x + " : " + y);
			if (x > 40 && x < 460 && y < 90) {
				board.getChildren().remove(l);
				l = new Line((int)((x - 40) / 60) * 60 + 70
						, 60
						, (int)((x - 40) / 60) * 60 + 70
						, 80);
				board.getChildren().add(l);
			}
		});

		/**
		 * for making a move, have a timeline that starts
		 * moving the piece down the column. stops itself when hits 
		 * a piece or bottom
		 */

		primary.setScene(introScreen);
		primary.show();

	}


	public void addShadow(Button b) {
		b.addEventHandler(MouseEvent.MOUSE_ENTERED
				, e -> b.setEffect(new DropShadow()));
		b.addEventHandler(MouseEvent.MOUSE_EXITED
				, e -> b.setEffect(null));
	}

	public void makeMove(Group g, int m, Piece p) {
		int i;
		for (i = 0; i < b.length && b[i][m] == Piece.EMPTY; i++);
		i--;
		b[i][m] = p;
		g.getChildren().add(new Circle(m * 60 + 69.5
				, i * 60 + 119.5
				, 20
				, p.getColor()));
	}
	public static void main(String[] args) {
		launch(args);
	}

}
