package main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.ReentrantLock;
import producerConsumerDB.DBCheck;
import producerConsumerDB.DBCreateTable;
import producerConsumerDB.DBDataPreparation;
//import producerConsumerDB.DBTempDataPresentation;
import producerConsumerDB.DBTemporaryDataStorage;
import producerConsumerDB.InsterDataToDatabase;
import subjects.MyConsumer;
import subjects.MyProducer;
import gui.Controller;
import javafx.application.Platform;

public class Engine {
	private static Engine instance;
	public final DateFormat dateFormat;
	public final DateFormat fullDateFormat;
	private boolean consumerDone;
	private boolean dataPreparationDone;
	private boolean dbCheckOK;
	private final ReentrantLock bufferLock;
	private final ReentrantLock ICLock;
	private final ReentrantLock DBTDSLock;
	private Buffer buffer;
	private InfoCollector infoCollector;
	private Controller controller;
	private DBTemporaryDataStorage dbTemporaryDataStorege;
	private Thread dbDataPreparation;
	private Thread producer;
	private Thread[] consumers;
	private Thread insertDataToDatabase;

	private Engine() {
		consumerDone = false;
		dataPreparationDone = false;
		dateFormat = new SimpleDateFormat(Config.dateFormatPattern);
		fullDateFormat = new SimpleDateFormat(Config.fullDateFormatPattern);
		infoCollector = new InfoCollector();
		dbTemporaryDataStorege = new DBTemporaryDataStorage();
		bufferLock = new ReentrantLock();
		DBTDSLock = new ReentrantLock();
		ICLock = new ReentrantLock();
		controller = new Controller();
		dbCheckOK = new DBCheck().dbCheck();
		dbCheckOK = new DBCreateTable().createTable();
	}
	
	public static Engine getInstance() {
		if (instance == null) {
			instance = new Engine();
			return instance;
		} else {
			return instance;
		}
	}
	
	public void start(int initBufferSize, int noOfConsumers, int noOfData) {
		consumerDone = false;
		dataPreparationDone = false;
		dbDataPreparation = new Thread(new DBDataPreparation(infoCollector, dbTemporaryDataStorege, ICLock, DBTDSLock));
		insertDataToDatabase = new Thread(new InsterDataToDatabase(dbTemporaryDataStorege, DBTDSLock));
		buffer = new Buffer(initBufferSize);
		consumers = new Thread[noOfConsumers];
		dbDataPreparation.start();
		insertDataToDatabase.start();
		producer = new Thread(new MyProducer(buffer, noOfData, bufferLock));
		for (int i = 0 ; i < consumers.length ; i++ ) {
			consumers[i] = new Thread(new MyConsumer(i, noOfConsumers, buffer, bufferLock, ICLock, infoCollector));
		}
		for (int i = 0 ; i < consumers.length ; i++ ) consumers[i].start();
		producer.start();
	}

	public void setBufferDataToGUI(int front, int back, String input, int bufferSize, String[] buffer, boolean insert) {
		Platform.runLater(new Runnable() {
			public void run() {
				controller.setStatus(front, back, bufferSize, buffer, input);
				if (insert) {
					controller.producerProgress();	
				} else {
					controller.consumerProgress();	
				}
			}
		});
	}
	
	public void displayData() {
		Platform.runLater(new Runnable() {
			public void run() {
				controller.displayData(dbTemporaryDataStorege);
			}
		});	
	}
	
	public void setStartButtonActive() {
		Platform.runLater(new Runnable() {
			public void run() {
				controller.setInputsActive();
			}
		});
	}
	
	public void setDataPreparationDone() {
		this.dataPreparationDone = true;
		displayData();
	}
	
	public void setConsumerDone() {
		this.consumerDone = true;
	}
	
	public boolean getDataPreparationDone()	{
		return dataPreparationDone;
	}
	
	public Thread[] getConsumers() {
		return consumers;
	}
	
	public Controller getController() {
		return controller;
	}
	
	public boolean getConsumerDone() {
		return consumerDone;
	}
	
	public boolean getDbCheckOK() {
		return dbCheckOK;
	}
}
