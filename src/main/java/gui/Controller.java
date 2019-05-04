package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.Bean;
import main.Config;
import main.Engine;

public class Controller {
	@FXML private VBox boxBuffer, boxList;
	@FXML private ListView<String>listView;
	@FXML private Label front, back, size, capacity, data, database, resizeLimit;
	@FXML private TextField initBufferSize, noOfConsumers, noOfData;
	@FXML private Button exit, start;
	@FXML ProgressBar consumerProgressBar, producerProgressBar;
	private double progressInterval;
	private double consumerProgress;
	private double producerProgress;
	private boolean isReady;
	
	public Controller() {
		isReady = true;
	}
	
	@FXML
	private void initialize() {
		initBufferSize.setText("6");
		noOfConsumers.setText("14");
		noOfData.setText("65");
		start.addEventHandler(ActionEvent.ACTION, e -> start());
		exit.addEventHandler(ActionEvent.ACTION, e -> System.exit(0));
	}
	
	private void start() {
		if (Engine.getInstance().getDbCheckOK()) {
			database.setText(Config.DBNAME);	
		} else {
			database.setText("not connected");
		}
		progressInterval = 1 / Double.parseDouble(noOfData.getText());
		switcher();
		Engine.getInstance().start(Integer.parseInt(initBufferSize.getText()), Integer.parseInt(noOfConsumers.getText()), Integer.parseInt(noOfData.getText()));
	}

	public void setInputsActive() {
		switcher();
	}
	
	private void switcher() {
		if (isReady) {
			boxList.getChildren().clear();
			boxBuffer.getChildren().clear();
			start.setDisable(true);
			initBufferSize.setDisable(true);
			noOfConsumers.setDisable(true);
			noOfData.setDisable(true);
			consumerProgressBar.setProgress(0);
			producerProgressBar.setProgress(0);
			consumerProgress = 0;
			producerProgress = 0;			
		} else {
			start.setDisable(false);
			initBufferSize.setDisable(false);
			noOfConsumers.setDisable(false);
			noOfData.setDisable(false);
		}
		isReady = !isReady;
	}
	
	public void setStatus(int front, int back, int bufferSize, String[] buffer, String data, double loadFactor) {
		this.front.setText(Integer.toString(front));
		this.back.setText(Integer.toString(back));
		this.size.setText(Integer.toString(bufferSize));
		this.capacity.setText(Integer.toString(buffer.length));
		this.resizeLimit.setText(Integer.toString((int)(buffer.length * loadFactor)));
		this.data.setText(data);
		rewriteBoxBuffer(buffer);
	}
	
	public void displayData(Bean bean) {
		String consumerID = Integer.toString(bean.getConsumerID());
		if (bean.getConsumerID() < 10) consumerID = "0" + consumerID;
		boxList.getChildren().add(new Label(Config.CONSUMERPREFIXNAME + consumerID + " -> " + bean.getDataNo()));
	}

	private void rewriteBoxBuffer(String[] buffer) {
		boxBuffer.getChildren().clear();
		for (int i = 0 ; i < buffer.length; i++) {
			String tempBuffer = buffer[i];
			if (tempBuffer == null) tempBuffer = Config.BUFFERNULL;
			boxBuffer.getChildren().add(new Label(Integer.toString(i) + ": " + tempBuffer));
		}
	}
	
	public void consumerProgress() {
		consumerProgressBar.setProgress(consumerProgress += progressInterval);
	}
	
	public void producerProgress() {
		producerProgressBar.setProgress(producerProgress += progressInterval);
	}
}
