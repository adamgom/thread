package dbTempDataStorage;

import main.Bean;

public class BeanNode {
	private Bean bean;
	private BeanNode next;
	private BeanNode previous;
	
	public BeanNode(Bean bean) {
		this.bean = bean;
	}

	public Bean getBean() {
		return bean;
	}

	public BeanNode getNext() {
		return next;
	}

	public void setNext(BeanNode next) {
		this.next = next;
	}
	
	public BeanNode getPrevious() {
		return previous;
	}

	public void setPrevious(BeanNode previous) {
		this.previous = previous;
	}
}
