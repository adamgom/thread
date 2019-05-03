package producerConsumerDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import main.Config;
import main.Engine;

public class InsterDataToDatabase implements Runnable {
	private DBTemporaryDataStorage dbTDS;
	private ReentrantLock DBTDSLock;
	private String insertData = "Insert INTO " + Config.tableName +
			" (" + Config.col2name + ", " + Config.col3name + ", " + Config.col4name + ")" +
			" VALUES (?, ?, ?)";
	
	public InsterDataToDatabase(DBTemporaryDataStorage dbTemporaryDataStorage, ReentrantLock DBTDSLock) {
		this.dbTDS = dbTemporaryDataStorage;
		this.DBTDSLock = DBTDSLock;
	}
	
	public void run() {
		while (true) {
			if (Engine.getInstance().getDataPreparationDone() && dbTDS.getTempBDBuffer().size() == 0) break;
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
	}
	
	private void addDataToDatabase() {
		try (Connection connection = DriverManager.getConnection(Config.DBURL, Config.USER, Config.PASSWORD);) {
			connection.setCatalog(Config.DBNAME);
			try (PreparedStatement preparedStatement = connection.prepareStatement(insertData);){
	    		connection.setAutoCommit(false);
	    		int tempLoopEnd = dbTDS.getTempBDBuffer().size(); 
	    		for (int i = 0 ; i < tempLoopEnd ; i ++) {
	    			preparedStatement.setInt(1, dbTDS.getTempBDBuffer().get(0).getConsumerID());
	    			preparedStatement.setInt(2, (int)dbTDS.getTempBDBuffer().get(0).getConsumerWorkDate().getTime());
	    			preparedStatement.setString(3, dbTDS.getTempBDBuffer().get(0).getDataNo());
	    			preparedStatement.executeUpdate();
	    			connection.commit();
	    			dbTDS.getTempBDBuffer().remove(0);
	    		}
	    		System.out.println("... IDTD method - data passed");
			} catch (SQLException e) {
				System.out.println("IDTD method - commit error, rollback: ");
				connection.rollback();
				e.printStackTrace();
    		}
		} catch (SQLException e) {
			System.out.println("IDTD method - connection error: ");
			e.printStackTrace();
		} 
	}
}
