package dbTempDataStorage;

// Doubly linked list

import main.Bean;

public class DBTempDataStorage {
	private BeanNode head;
	private BeanNode tail;
	private int size;
	private boolean storageReady;
	
	public DBTempDataStorage() {
		storageReady = false;
	}
	
	public void add(Bean bean) {
		BeanNode node = new BeanNode(bean);
		node.setNext(head);
		if (head == null) {
			tail = node;
		} else {
			head.setPrevious(node);
		}
		head = node;
		size++;
	}
	
	public Bean remove() {
		if (tail == null) return null;
		BeanNode removed = tail;
		tail = tail.getPrevious();
		if (tail == null) {
			head = null;
		} else {
			tail.setNext(null);
		}
		size--;
		return removed.getBean();
	}
	
	public void setStorageReady(boolean storageReady) {
		this.storageReady = storageReady;
	}
	public boolean getStorageReady() {
		return storageReady;
	}
	
	public int getSize() {
		return size;
	}
	
	public boolean isEmpty() {
		if (head == null) return true;
		return false;
	}
}
