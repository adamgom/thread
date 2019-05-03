//package main;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class InfoCollector {
//	private List<Integer> consumerNo;
//	private List<Date> consumerInstantiationDate;
//	private List<Bean> consumerBehaviour;
//	
//	public InfoCollector() {
//		this.consumerNo = new ArrayList<Integer>();
//		this.consumerInstantiationDate = new ArrayList<Date>();
//		this.consumerBehaviour = new ArrayList<Bean>();
//	}
//	
//	public void addConsumerData(int consumerNo, Date consumerInstantiationDate) {
//		this.consumerNo.add(consumerNo);
//		this.consumerInstantiationDate.add(consumerInstantiationDate);
//	}
//	
//	public void addConsumerBean (Bean bean) {
//		consumerBehaviour.add(bean);
//	}
//	public List<Bean> getConsumerBehaviour() {
//		return consumerBehaviour;
//	}
//	
//	public boolean checkICSize() {
//		if (this.consumerBehaviour.size() < 10) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//}
//


package main;

import java.util.ArrayList;
import java.util.List;

public class InfoCollector {
	private List<Bean> consumerBehaviour;
	
	public InfoCollector() {
		this.consumerBehaviour = new ArrayList<Bean>(20);
	}
	
	public void addConsumerBean (Bean bean) {
		consumerBehaviour.add(bean);
	}
	
	public List<Bean> getConsumerBehaviour() {
		return consumerBehaviour;
	}
	
	public boolean checkICSize() {
		if (consumerBehaviour.size() <= 9) {
			return false;
		} else {
			return true;
		}
	}
}
