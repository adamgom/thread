//package producerConsumerDB;
//
//import java.util.concurrent.locks.ReentrantLock;
//
//import main.InfoCollector;
//
//public class DBEngine {
//	private static DBEngine instance;
//	
//	public final ReentrantLock ICLock;
//	public final ReentrantLock DBTDSLock;
//	
//	private boolean dataPreparationDone;
//	private boolean dbTDSPartReady;
//	private boolean dbCheckOK;
//	
//	private InfoCollector infoCollector;
//	private Thread dbDataPreparation;
//	private Thread insertDataToDatabase;
//	
//	private DBEngine(
//			InfoCollector infoCollector, 
//			DBTemporaryDataStorage dbTemporaryDataStorege) {
//		this.ICLock = new ReentrantLock();
//		this.DBTDSLock = new ReentrantLock();
//		
//		this.dataPreparationDone = false;
//		this.dbTDSPartReady = false;
//		this.dbCheckOK = new DBCheck().dbCheck();
//		this.dbCheckOK = new DBCreateTable().createTable();
//		
//		this.infoCollector = infoCollector;
//		this.dbDataPreparation = new Thread(new DBDataPreparation(infoCollector, dbTemporaryDataStorege, ICLock, DBTDSLock));
//		this.insertDataToDatabase = new Thread(new InsterDataToDatabase(dbTemporaryDataStorege, DBTDSLock));
//		dbDataPreparation.start();
//		insertDataToDatabase.start();
//	}
//	
//	public static void setInstance(
//			InfoCollector infoCollector, 
//			DBTemporaryDataStorage 
//			dbTemporaryDataStorege) {
//		if (instance == null) {
//			instance = new DBEngine(infoCollector, dbTemporaryDataStorege);
//		} else {
//			System.out.println("Instalce already created!");
//			return;
//		}
//	}
//	
//	public static DBEngine getInstance() {
//		if (instance == null) {
//			System.out.println("Instanciaton DBEngine by 'setInstance' method! Program terminated");
//			return null;
//		} else {
//			return instance;
//		}
//	}
//
//	public void setDataPreparationDone()		{this.dataPreparationDone = true;}
//	public void setDBTDSPartReady(boolean b)	{this.dbTDSPartReady = b;}
//
//	public InfoCollector getInfoCollector() 	{return infoCollector;}
//	public ReentrantLock getICLock()			{return ICLock;}
//	public boolean getDataPreparationDone()		{return dataPreparationDone;}
//	public boolean getDbCheckOK() 				{return dbCheckOK;}
//	public boolean getDbTDSPartReady() 			{return dbTDSPartReady;}
//}
