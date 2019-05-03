package subjects;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import main.Buffer;
import main.Config;

public class MyProducer implements Runnable {
	private Buffer buffer;
	private String[] data;
	private Random random;
	private ReentrantLock bufferLock;
	
	public MyProducer(Buffer buffer, int numsSize, ReentrantLock bufferLock) {
		this.buffer = buffer;
		this.bufferLock = bufferLock;
		this.random = new Random();
		this.data = new String[numsSize];
		for (int i = 0 ; i < numsSize ; i++) {
			this.data[i] = "data_" + Integer.toString(i+1);
		}
	}
	
	public void run() {
		for (String data : this.data) {
			try {
//				synchronized (buffer) {
				bufferLock.lock();
				try {
					buffer.add(data);
				} finally {
					bufferLock.unlock();
				}
//				}
				
				Thread.sleep(this.random.nextInt(200));
			} catch (InterruptedException e) {
				System.out.println("PRODUCER was interrupted");
			}
		}

//		synchronized (buffer) {
		bufferLock.lock();
		try {
			buffer.add(Config.EOD);
		} finally {
			bufferLock.unlock();
		}
//		}
	}
}
