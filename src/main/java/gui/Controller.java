package gui;

import producerConsumerDB.DBTemporaryDataStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.Config;
import main.Engine;

public class Controller {
	@FXML private VBox boxBuffer, boxList;
	@FXML private ListView<String>listView;
	@FXML private Label front, back, size, capacity, data, database;
	@FXML private TextField initBufferSize, noOfConsumers, noOfData;
	@FXML private Button exit, start;
	@FXML ProgressBar consumerProgressBar, producerProgressBar;
	private double progressInterval;
	private double consumerProgress;
	private double producerProgress;
	
	public Controller() {
	}
	
	@FXML
	private void initialize() {
		initBufferSize.setText("6");
		noOfConsumers.setText("15");
		noOfData.setText("20");
		start.addEventHandler(ActionEvent.ACTION, e -> start());
		exit.addEventHandler(ActionEvent.ACTION, e -> System.exit(0));
	}
	
	private void start() {
		boxList.getChildren().clear();
		dbCheck();
		progressInterval = 1 / Double.parseDouble(noOfData.getText());
		start.setDisable(true);
		initBufferSize.setDisable(true);
		noOfConsumers.setDisable(true);
		noOfData.setDisable(true);
		consumerProgressBar.setProgress(0);
		producerProgressBar.setProgress(0);
		consumerProgress = 0;
		producerProgress = 0;
		Engine.getInstance().start(Integer.parseInt(initBufferSize.getText()), Integer.parseInt(noOfConsumers.getText()), Integer.parseInt(noOfData.getText()));
	}

	public void setInputsActive() {
		start.setDisable(false);
		initBufferSize.setDisable(false);
		noOfConsumers.setDisable(false);
		noOfData.setDisable(false);
	}
	
	public void setStatus(int front, int back, int bufferSize, String[] buffer, String data) {
		this.front.setText(Integer.toString(front));
		this.back.setText(Integer.toString(back));
		this.size.setText(Integer.toString(bufferSize));
		this.capacity.setText(Integer.toString(buffer.length));
		this.data.setText(data);
		rewriteBoxBuffer(buffer);
	}
	
	public void displayData(DBTemporaryDataStorage dbTDS) {
		for (int i = 0 ; i < dbTDS.getTempBDBuffer().size(); i++) {
			boxList.getChildren().add(new Label("CONS-" + 
					dbTDS.getTempBDBuffer().get(i).getConsumerID() + ", " + 
					dbTDS.getTempBDBuffer().get(i).getDataNo()));
		}
	}

	private void rewriteBoxBuffer(String[] buffer) {
		boxBuffer.getChildren().clear();
		for (int i = 0 ; i < buffer.length; i++) {
			String tempBuffer;
			if (buffer[i] == null) {
				tempBuffer = "";
			} else {
				tempBuffer = buffer[i];
			}
			boxBuffer.getChildren().add(new Label(Integer.toString(i) + ": " + tempBuffer));
		}
	}
	
	public void dbCheck() {
		if (Engine.getInstance().getDbCheckOK()) {
			database.setText(Config.DBNAME);	
		} else {
			database.setText("not connected");
		}
	}
	
	public void consumerProgress() {
		consumerProgressBar.setProgress(consumerProgress += progressInterval);
	}
	
	public void producerProgress() {
		producerProgressBar.setProgress(producerProgress += progressInterval);
	}
}
