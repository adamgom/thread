package subjects;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import buffer.Buffer;
import buffer.InfoCollector;
import main.Bean;
import main.Config;
import main.Engine;

public class MyConsumer implements Runnable {
	private boolean first;
	private int consumerID;
	private int noOfConsumers;
//	private String consumerName;
	private String tempBuffer;
	private Buffer buffer;
	private Random random;
	private ReentrantLock bufferLock;
	private ReentrantLock ICLock;
	private InfoCollector infoCollector;

	public MyConsumer(	int consumerID, int noOfConsumers, Buffer buffer, 
						ReentrantLock bufferLock, ReentrantLock ICLock, InfoCollector infoCollector) {
		this.first = true;
		this.consumerID = consumerID;
		this.noOfConsumers = noOfConsumers;
		this.tempBuffer = "";
//		this.consumerName = "CONS-" + (int)(consumerID+1);
		this.buffer = buffer;
		this.random = new Random();
		this.bufferLock = bufferLock;
		this.ICLock = ICLock;
		this.infoCollector = infoCollector;
	}
	
	public void run() {
		try {
			Thread.sleep(randomSleep());
		} catch (InterruptedException e) {}
//		int counter = 0;
		while(true) {
//			if (bufferLock.tryLock()) {
				bufferLock.lock();

				try {
					if (buffer.peek() == null) {
						continue;
					}
//					System.out.println("Counter: " + counter);
//					counter = 0;
					
					if (!buffer.peek().equals(Config.EOD)) {
						tempBuffer = buffer.get();
					} else {
						for (int i = 0 ; i < noOfConsumers ; i++) {
							if (i == consumerID) continue;
							Engine.getInstance().getConsumers()[i].interrupt();
						}
						Engine.getInstance().setConsumerDone();
						break;
					}
				} finally {
					bufferLock.unlock();
				}

				ICLock.lock();
				infoCollector.addConsumerBean(new Bean(consumerID, new Date(), tempBuffer));
				ICLock.unlock();
				
				try {
					Thread.sleep(randomSleep());
				} catch (InterruptedException e) { 
					break;
				}

//			} else {
//				counter++;
//			}
		}
	}
	
	private int randomSleep() {
		int a;
		int b;
		if (first) {
			a = 2000;
			b = 4000;
		} else {
			a = 1000;
			b = 2000;
		}
		first = false;
		return Math.max(a, random.nextInt(b));
	}
}
