package cli;

import main.Engine;
import main.InfoCollector;
import producerConsumerDB.DBTemporaryDataStorage;

public class CliPresentation implements Runnable {
	private InfoCollector infoCollector;
	private DBTemporaryDataStorage dbTDS;
	
	public CliPresentation(InfoCollector infoCollector, DBTemporaryDataStorage dbTemporaryDataStorege) {
		this.infoCollector = infoCollector;
		this.dbTDS = dbTemporaryDataStorege;
	}
	
	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) { }
		if (this.infoCollector.getConsumerBehaviour().size() != 0) ICShow();
		if (this.dbTDS.getTempBDBuffer().size() != 0) dbTDSShow();
//		Engine.getInstance().setPresentationDone();
	}
	
	private void ICShow() {
		System.out.println("========================");
		System.out.println("Info Collector presentation, date: " + 
			Engine.getInstance().fullDateFormat.format(this.infoCollector.getConsumerBehaviour().get(0).getConsumerWorkDate())
		);
		for (int i = 0 ; i < this.infoCollector.getConsumerBehaviour().size() ; i++ ) {
			System.out.println(
				setINumerator(i) + " - " +
				"Consumer ID: " + ((int)this.infoCollector.getConsumerBehaviour().get(i).getConsumerID()+1) +
				", work date: " + Engine.getInstance().dateFormat.format(this.infoCollector.getConsumerBehaviour().get(i).getConsumerWorkDate()) +
				", data processed: " + this.infoCollector.getConsumerBehaviour().get(i).getDataNo()
			);
		}		
	}
	
	private void dbTDSShow() {
		System.out.println("========================");
		System.out.println("DB Temporary Data Storage presentation, date: " + 
			Engine.getInstance().fullDateFormat.format(this.dbTDS.getTempBDBuffer().get(0).getConsumerWorkDate())
		);
		for (int i = 0 ; i < this.dbTDS.getTempBDBuffer().size() ; i++ ) {
			System.out.println(
				setINumerator(i) + " - " +
				"Consumer ID: " + ((int)this.dbTDS.getTempBDBuffer().get(i).getConsumerID()+1) +
				", work date: " + Engine.getInstance().dateFormat.format(this.dbTDS.getTempBDBuffer().get(i).getConsumerWorkDate()) +
				", data processed: " + this.dbTDS.getTempBDBuffer().get(i).getDataNo()
			);
		}
	}
	
	private String setINumerator(int j) {
		String tempI;
		if (j < 9) {tempI = "0" + ((int)j+1);
		} else {
			tempI = Integer.toString((int)j+1);
		}
		return tempI;
	}
}
