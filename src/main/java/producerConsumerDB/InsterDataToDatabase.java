package producerConsumerDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import dbTempDataStorage.DBTempDataStorage;
import main.Bean;
import main.Config;
import main.Engine;

public class InsterDataToDatabase implements Runnable {
	private DBTempDataStorage dbTDS;
	private ReentrantLock DBTDSLock;
	private String insertData = "INSERT INTO " + Config.tableName +
			" (" + Config.col2name + ", " + Config.col3name + ", " + Config.col4name + ")" +
			" VALUES (?, ?, ?)";
	
	public InsterDataToDatabase(DBTempDataStorage dbTemporaryDataStorage, ReentrantLock DBTDSLock) {
		this.dbTDS = dbTemporaryDataStorage;
		this.DBTDSLock = DBTDSLock;
	}
	
	public void run() {
		while (true) {
			if (Engine.getInstance().getDataPreparationDone() && dbTDS.isEmpty()) break;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {}
			
			try {
				DBTDSLock.lock();
				if (!dbTDS.getStorageReady()) continue;
				addDataToDatabase();
				dbTDS.setStorageReady(false);
			} finally {
				DBTDSLock.unlock();
			}
		}
		Engine.getInstance().setInputActive();
	}
	
	private void addDataToDatabase() {
//		try (Connection connection = DriverManager.getConnection(Config.DBURL, Config.USER, Config.PASSWORD);) {
//			connection.setCatalog(Config.DBNAME);
//			try (PreparedStatement preparedStatement = connection.prepareStatement(insertData);){
//	    		connection.setAutoCommit(false);
	    		while (!dbTDS.isEmpty()) {
	    			Bean tempBean = dbTDS.remove();
//	    			preparedStatement.setInt(1, tempBean.getConsumerID());
//	    			preparedStatement.setInt(2, (int)tempBean.getConsumerWorkDate().getTime());
//	    			preparedStatement.setString(3, tempBean.getDataNo());
//	    			preparedStatement.executeUpdate();
//	    			connection.commit();
	    			Engine.getInstance().displayData(tempBean);
				}
//			} catch (SQLException e) {
//				System.out.print("IDTD method - commit error, rollback: ");
//				connection.rollback();
//				e.printStackTrace();
//    		}
//		} catch (SQLException e) {
//			System.out.print("IDTD method - connection error: ");
//			e.printStackTrace();
//		} 
	}
}
