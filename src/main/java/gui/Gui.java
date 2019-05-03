package gui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Engine;

public class Gui {
	Stage primaryStage;
	Scene scene;
	FXMLLoader fxmlLoader;
	
	public Gui(Stage primaryStage) throws IOException {
		this.primaryStage = primaryStage;
		this.fxmlLoader = new FXMLLoader(this.getClass().getResource("Scene.fxml"));
		this.fxmlLoader.setController(Engine.getInstance().getController());
		this.scene = new Scene(fxmlLoader.load());
		this.primaryStage.setScene(scene);
		this.primaryStage.show();
	}
}
