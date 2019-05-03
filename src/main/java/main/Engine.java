//package main;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.concurrent.locks.ReentrantLock;
//
//import cli.CliPresentation;
//import producerConsumerDB.DBEngine;
//import producerConsumerDB.DBTemporaryDataStorage;
//
//public class Engine {
//	private static Engine instance;
//	private static int engineCallCount;
//	
//	public final String EOF;
//	public final int noOfConsumers;
//	public final int noOfData;
//	public final ReentrantLock bufferLock;
//	public final ReentrantLock ICLock;
//	public final DateFormat dateFormat;
//	public final DateFormat fullDateFormat;
//	
//	private boolean presentationDone;
//	private boolean consumerDone;
//	private Thread[] consumers;
//	private Buffer buffer;
//	private Thread producer;
//	private InfoCollector infoCollector;
//	private Thread cliPresentation;
//	
//	private Engine(
//			int noOfConsumers, 
//			int noOfData, 
//			String EOF, 
//			InfoCollector infoCollector, 
//			DBTemporaryDataStorage dbTemporaryDataStorege) {
//		this.dateFormat = new SimpleDateFormat("mm:ss:SS");
//		this.fullDateFormat = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
//		this.EOF = EOF;
//		this.noOfConsumers = noOfConsumers;
//		this.noOfData = noOfData;
//		this.buffer = new Buffer();
//		this.bufferLock = new ReentrantLock();
//		this.ICLock = DBEngine.getInstance().getICLock();
//		this.consumers = new Thread[noOfConsumers];
//		this.producer = new Thread(new MyProducer(buffer, noOfData, bufferLock));
//		this.infoCollector = infoCollector;
//		this.cliPresentation = new Thread(new CliPresentation(infoCollector, dbTemporaryDataStorege));
//		this.presentationDone = false;
//		this.consumerDone = false;
//		Engine.engineCallCount = 0;
//
//		for (int i = 0 ; i < consumers.length ; i++ ) {
//			consumers[i] = new Thread(new MyConsumer(buffer, i, bufferLock, ICLock, noOfConsumers, infoCollector));
//			this.infoCollector.addConsumerData(i, new Date());
//		}
//	}
//	
//	public static void setInstance(
//			int noOfConsumers,
//			int noOfData, 
//			String EOF, 
//			InfoCollector infoCollector,
//			DBTemporaryDataStorage dbTemporaryDataStorege) {
//		if (instance == null) {
//			instance = new Engine(noOfConsumers, noOfData, EOF, infoCollector, dbTemporaryDataStorege);
//		} else {
//			System.out.println("Instance already created");
//			return;
//		}
//	}
//	
//	public static Engine getInstance() {
//		if (instance == null) {
//			System.out.println("Instanciaton Engine by 'setInstance' method! Program terminated");
//			return null;
//		} else {
//			Engine.engineCallCount++;
//			return instance;
//		}
//	}
//
//	public void startConsumers() {
//		for (int i = 0 ; i < consumers.length ; i++ ) consumers[i].start();
//	}
//	public void startProducer() {
//		getProducer().start();
//	}
//	
//	public void 		 setPresentationDone()	{this.presentationDone = true;}
//	public void			 setConsumerDone()		{this.consumerDone = true;}
//	
//	public Thread[] 	 getConsumers()		 	{return consumers;}
//	public Buffer		 getBuffer() 	 		{return buffer;}
//	public Thread 		 getProducer() 			{return producer;}
//	public InfoCollector getInfoCollector() 	{return infoCollector;}
//	public boolean 		 getPresentationDone()	{return presentationDone;}
//	public int 			 getEngineCallCount() 	{return Engine.engineCallCount;}
//	public Thread 		 getCliPresentation() 	{return cliPresentation;}
//	public boolean		 getConsumerDone()		{return consumerDone;}
//}

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
	private boolean presentationDone;
	private boolean consumerDone;
	private boolean dataPreparationDone;
	private final ReentrantLock bufferLock;
	private final ReentrantLock ICLock;
	private Buffer buffer;
	private InfoCollector infoCollector;
	private Controller controller;
	private DBTemporaryDataStorage dbTemporaryDataStorege;
	private Thread dbDataPreparation;
	private Thread producer;
	private Thread[] consumers;
//	private DBTempDataPresentation dbTDP; 
	
	public final ReentrantLock DBTDSLock;
	private boolean dbTDSPartReady;
	private boolean dbCheckOK;
	private Thread insertDataToDatabase;

	private Engine() {
		presentationDone = false;
		consumerDone = false;
		dataPreparationDone = false;
		dataPreparationDone = false;
		dbTDSPartReady = false;
		dateFormat = new SimpleDateFormat(Config.dateFormatPattern);
		fullDateFormat = new SimpleDateFormat(Config.fullDateFormatPattern);
		infoCollector = new InfoCollector();
		dbTemporaryDataStorege = new DBTemporaryDataStorage();
		bufferLock = new ReentrantLock();
		DBTDSLock = new ReentrantLock();
		ICLock = new ReentrantLock();
		controller = new Controller();
//		this.dbTDP = new DBTempDataPresentation();

		dbCheckOK = new DBCheck().dbCheck();
		dbCheckOK = new DBCreateTable().createTable();
		dbDataPreparation = new Thread(new DBDataPreparation(infoCollector, dbTemporaryDataStorege, ICLock, DBTDSLock));
		insertDataToDatabase = new Thread(new InsterDataToDatabase(dbTemporaryDataStorege, DBTDSLock));
	}
	
	public void start(int initBufferSize, int noOfConsumers, int noOfData) {
		dbDataPreparation.start();
		insertDataToDatabase.start();
		buffer = new Buffer(initBufferSize);
		consumers = new Thread[noOfConsumers];
		producer = new Thread(new MyProducer(buffer, noOfData, bufferLock));
		for (int i = 0 ; i < consumers.length ; i++ ) {
			consumers[i] = new Thread(new MyConsumer(i, noOfConsumers, buffer, bufferLock, ICLock, infoCollector));
		}
		for (int i = 0 ; i < consumers.length ; i++ ) consumers[i].start();
		producer.start();
	}
	
	public static Engine getInstance() {
		if (instance == null) {
			instance = new Engine();
			return instance;
		} else {
			return instance;
		}
	}
	
	public void setBufferDataToGUI(int front, int back, String input, int bufferSize, String[] buffer) {
		Platform.runLater(new Runnable() {
			public void run() {
				getController().setStatus(front, back, bufferSize, buffer, input);
			}
		});
	}
	
	public void displayData() {
//		if (!presentationDone) {
			Platform.runLater(new Runnable() {
				public void run() {
					getController().displayData(dbTemporaryDataStorege);
				}
			});	
//		} else {
//			return;
//		}
//		presentationDone = true;
	}
	
	public void setDataPreparationDone() {
		this.dataPreparationDone = true;
		displayData();
	}

	public ReentrantLock getICLock()			{return ICLock;}
//	public DBTempDataPresentation getDBTDP() 	{return dbTDP;}
	public boolean getDataPreparationDone()		{return dataPreparationDone;}
	public void			 setConsumerDone()		{this.consumerDone = true;}
	public Thread[] 	 getConsumers()		 	{return consumers;}
	public Buffer		 getBuffer() 	 		{return buffer;}
	public Controller	 getController()		{return controller;}
	public InfoCollector getInfoCollector() 	{return infoCollector;}
	public boolean 		 getPresentationDone()	{return presentationDone;}
	public boolean		 getConsumerDone()		{return consumerDone;}
	
	public void setDBTDSPartReady(boolean b)	{this.dbTDSPartReady = b;}
	public boolean getDbCheckOK() 				{return dbCheckOK;}
	public boolean getDbTDSPartReady() 			{return dbTDSPartReady;}
}
