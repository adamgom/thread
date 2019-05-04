package buffer;

import java.util.ArrayList;
import java.util.List;

import main.Bean;

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
