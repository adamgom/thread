package producerConsumerDB;

import java.util.concurrent.locks.ReentrantLock;

import buffer.InfoCollector;
import dbTempDataStorage.DBTempDataStorage;
import main.Engine;

public class DBDataPreparation implements Runnable {
	private InfoCollector infoCollector;
	private DBTempDataStorage dbTDS;
	private ReentrantLock ICLock;
	private ReentrantLock DBTDSLock;
	
	public DBDataPreparation(InfoCollector infoCollector, DBTempDataStorage dbTemporaryDataStorege, ReentrantLock ICLock, ReentrantLock DBTDSLock) {
		this.infoCollector = infoCollector;
		this.dbTDS = dbTemporaryDataStorege;
		this.ICLock = ICLock;
		this.DBTDSLock = DBTDSLock;
	}
	
	public void run() {
		while(true) {
			try {
				ICLock.lock();
				if (this.infoCollector.checkICSize()) {
					try {
						DBTDSLock.lock();
						passData(10);
					} finally {
						DBTDSLock.unlock();	
					}
				}
			} finally {
				ICLock.unlock();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			
			try {
				ICLock.lock();
				if (Engine.getInstance().getConsumerDone() && !infoCollector.checkICSize()) {
					break;
				}	
			} finally {
				ICLock.unlock();
			}
		}

		try {
			ICLock.lock();
			DBTDSLock.lock();
			passData(infoCollector.getConsumerBehaviour().size());
			Engine.getInstance().setDataPreparationDone();
		} finally {
			DBTDSLock.unlock();
			ICLock.unlock();
		}
		Engine.getInstance().setDataPreparationDone();
	}
	
	private void passData(int limit) {
		for (int i = 0 ; i < limit ; i++) {
			dbTDS.add(infoCollector.getConsumerBehaviour().get(0));
			infoCollector.getConsumerBehaviour().remove(0);
		}
		dbTDS.setStorageReady(true);
	}
}
