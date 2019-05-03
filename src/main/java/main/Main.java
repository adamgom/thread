package main;

import gui.Gui;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		Engine.getInstance();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Gui gui = new Gui(primaryStage);
	}
}