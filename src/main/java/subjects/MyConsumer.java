//package subjects;
//
//import java.util.Date;
//import java.util.Random;
//import java.util.concurrent.locks.ReentrantLock;
//
//import main.Buffer;
//import main.InfoCollector;
//
//public class MyConsumer implements Runnable {
//	private Buffer buffer;
//	private int consumerID;
//	private ReentrantLock bufferLock;
//	private ReentrantLock ICLock;
//	private Random random;
//	private int noOfConsumers;
//	private String consumerName;
//	private InfoCollector infoCollector;
//	private String tempBuffer;
//
//	public MyConsumer(
//			Buffer buffer,
//			int consumerID, 
//			ReentrantLock bufferLock, 
//			ReentrantLock ICLock, 
//			int noOfConsumers, 
//			InfoCollector infoCollector) {
//		this.buffer = buffer;
//		this.consumerID = consumerID;
//		this.bufferLock = bufferLock;
//		this.ICLock = ICLock;
//		this.random = new Random();
//		this.noOfConsumers = noOfConsumers;
//		this.infoCollector = infoCollector;
//		this.consumerName = "CONS-" + (int)(consumerID+1);
//		this.tempBuffer = "";
//	}
//	
//	public void run() {
////		int counter = 0;
//		while(true) {
////			if (bufferLock.tryLock()) {
//				bufferLock.lock();
//
//				try {
//					if (this.buffer.getBuffer().isEmpty()) {
//						continue;
//					}
////					System.out.println("Counter: " + counter);
////					counter = 0;
//					
//					if (!this.buffer.getBuffer().get(0).equals(Engine.getInstance().EOF)) {
//						int tempRandom = Math.max(random.nextInt(this.buffer.getBuffer().size())-1, 0);
//						this.tempBuffer = this.buffer.getBuffer().get(tempRandom);
//						System.out.println(this.consumerName + " Removing... " + this.buffer.getBuffer().get(tempRandom));
//						this.buffer.getBuffer().remove(tempRandom);
//					} else {
//						for (int i = 0 ; i < this.noOfConsumers ; i++) {
//							if (i == this.consumerID) continue;
//							Engine.getInstance().getConsumers()[i].interrupt();
//						}
//						System.out.println("");
//						System.out.println(this.consumerName + " Exiting...");
//						this.infoCollector.addConsumerBean(new Bean(this.consumerID, new Date(), Engine.getInstance().EOF));
//						Engine.getInstance().setConsumerDone();
//						break;
//					}
//				} finally {
//					bufferLock.unlock();
//				}
//				
//				ICLock.lock();
//				this.infoCollector.addConsumerBean(new Bean(this.consumerID, new Date(), this.tempBuffer));
//				ICLock.unlock();
//				
//				try {
//					Thread.sleep(this.random.nextInt(5000));
//				} catch (InterruptedException e) { 
//					System.out.println(this.consumerName + " interrupted and Exiting...");
//					ICLock.lock();
//					if (!Engine.getInstance().getCliPresentation().isAlive()) Engine.getInstance().getCliPresentation().start();
//					ICLock.unlock();
//					break;
//				}
//
////			} else {
////				counter++;
////			}
//		}
//	}
//}



package subjects;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import main.Bean;
import main.Buffer;
import main.Config;
import main.Engine;
import main.InfoCollector;

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
		this.first = false;
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
		if (!first) {
			try {
				Thread.sleep(randomSleep());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			first = !first;
		}
		
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
			a = 1000;
			b = 2000;
		} else {
			a = 2000;
			b = 4000;
		}
		return Math.max(a, random.nextInt(b));
	}
}
