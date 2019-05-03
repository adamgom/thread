package producerConsumerDB;

import java.util.ArrayList;
import java.util.List;

import main.Bean;

public class DBTemporaryDataStorage {
	private List<Bean> tempDBBuffer;
	private boolean storageReady;
	
	public DBTemporaryDataStorage() {
		this.tempDBBuffer = new ArrayList<Bean>();
		this.storageReady = false;
	}

	public void setStorageReady(boolean b) {this.storageReady = b;}
	
	public List<Bean> 	getTempBDBuffer() {return tempDBBuffer;}
	public boolean 		getStorageReady() {return storageReady;}
}
