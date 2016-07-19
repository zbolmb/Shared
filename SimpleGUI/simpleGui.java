import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class simpleGui extends Application {

	public void start(Stage primaryStage) throws Exception {
		Stage startScreen, gameScreen, gameOverScreen;

		//Start Scene
		Scene startScreenScene;
		GridPane startScreenLayout;

		startScreenLayout = new GridPane();
		startScreenScene = new Scene(startScreenLayout, 300, 200);
		startScreen = new Stage();
		startScreen.setScene(startScreenScene);

		Text title = new Text("A Simple Game");
		Text author = new Text("By Zhijian Li");
		title.getStyleClass().add("text");
		author.getStyleClass().add("text");

		startScreenLayout.add(title, 0, 0);
		startScreenLayout.add(author, 0, 1);

		//Game Scene
		Scene gameScreenScene;
		GridPane gameScreenLayout;

		gameScreenLayout = new GridPane();
		gameScreenScene = new Scene(gameScreenLayout, 300, 200);
		gameScreen = new Stage();
		gameScreen.setScene(gameScreenScene);

		Text gameScreenText = new Text("This is the gameScreen");
		gameScreenLayout.add(gameScreenText, 0, 0);

		Button btn = new Button();
		btn.setText("Switch to GameScreen");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				gameScreen.show();
				startScreen.hide();
			}
		});
		startScreenLayout.add(btn, 0, 2);

		//style
		// startScreenLayout.setHgap(10);
		// startScreenLayout.setVgap(10);
		// startScreenLayout.setPadding(new Insets(10, 10, 10, 10));
		// startScreenScene.getStylesheets().add("css/startScreenStyle.css");

		startScreenLayout.setGridLinesVisible(true);

		startScreen.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
