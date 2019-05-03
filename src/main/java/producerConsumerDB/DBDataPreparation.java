package producerConsumerDB;

import java.util.concurrent.locks.ReentrantLock;

import main.Engine;
import main.InfoCollector;

public class DBDataPreparation implements Runnable {
	private InfoCollector infoCollector;
	private DBTemporaryDataStorage dbTDS;
	private ReentrantLock ICLock;
	private ReentrantLock DBTDSLock;
	private boolean firstLoop;
	
	public DBDataPreparation(
			InfoCollector infoCollector,
			DBTemporaryDataStorage dbTemporaryDataStorege,
			ReentrantLock ICLock,
			ReentrantLock DBTDSLock) {
		this.infoCollector = infoCollector;
		this.dbTDS = dbTemporaryDataStorege;
		this.ICLock = ICLock;
		this.DBTDSLock = DBTDSLock;
		this.firstLoop = true;
	}
	
	public void run() {
		while(true) {
			try {
				ICLock.lock();
				if (this.infoCollector.checkICSize()) {
					wText("10 or more in Info Collector");
					try {
						DBTDSLock.lock();
						passData(10);
					} finally {
						DBTDSLock.unlock();	
					}
				}
				Engine.getInstance().displayData();
			} finally {
				ICLock.unlock();
			}

			try {
				if (this.firstLoop) {
					Thread.sleep(500);
					this.firstLoop = false;
				} else {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {}
			
			try {
				ICLock.lock();
				if (Engine.getInstance().getConsumerDone() && !infoCollector.checkICSize()) {
					System.out.println("DBDataPreparation final");
					break;
				}	
			} finally {
				ICLock.unlock();
			}
		}

		try {
			ICLock.lock();
			wText("Final rest of Info Collector data pass");
			DBTDSLock.lock();
			passData(infoCollector.getConsumerBehaviour().size()-1);
			Engine.getInstance().setDataPreparationDone();
		} finally {
			DBTDSLock.unlock();
			ICLock.unlock();
		}
		wText("Data preparation Done");
		Engine.getInstance().setDataPreparationDone();
	}
	
	private void passData(int limit) {
		for (int i = 0 ; i < limit ; i++ ) {
			dbTDS.getTempBDBuffer().add(infoCollector.getConsumerBehaviour().get(0));
			infoCollector.getConsumerBehaviour().remove(0);
		}
		dbTDS.setStorageReady(true);
	}
	
	private void wText(String text) {
		System.out.println(text + ", Info Collector Consumer Behaviour size: " + infoCollector.getConsumerBehaviour().size());
	}
}
