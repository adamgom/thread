//package main;
//
////import producerConsumerDB.DBEngine;
////import producerConsumerDB.DBTemporaryDataStorage;
//
//public class Main {
//
////	static InfoCollector infoCollector;
////	static DBTemporaryDataStorage dbTemporaryDataStorege;
//	
//	public static void main(String[] args) {
////		infoCollector = new InfoCollector();
////		dbTemporaryDataStorege = new DBTemporaryDataStorage();
//		
////		int noOfConsumers = 20;
////		int noOfData = 200;
////		String endOfFile = "End of file";
//		
////		DBEngine.setInstance(infoCollector, dbTemporaryDataStorege);
//		Engine.setInstance();
////		Engine.getInstance().startProducer();
////		Engine.getInstance().startConsumers();
//	}
//}
//


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