//package main;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import ConsumerProducer.Engine;
//
//public class Buffer {
//	private List<String> buffer;
//	
//	public Buffer() {
//		this.buffer = new ArrayList<String>();
//	}
//	
//	public void addToBuffer(String data) {
//		this.buffer.add(data);
//	}
//	
//	public List<String> getBuffer() {
//		return buffer;
//	}
//}


package main;

//Circular Queue

public class Buffer {
	private String[] buffer;
	private int front;
	private int back;
	private final int initalCapacity;
	
	public Buffer(int capacity) {
		buffer = new String[this.initalCapacity = capacity];
		back = 0;
		front = 0;
	}
	
	public void add(String input) {
		buffer[back++] = input;
		if (bufferSize() == buffer.length - 1 || back == front) enlargeBuffer();
		if (back == buffer.length) back = 0;
		passDataToGUI(input);
	}
	
	public String get() {
		if (bufferSize() == 0) return null;
		String removed = buffer[front];
		buffer[front++] = null;
		if (front == buffer.length) front = 0;
		if (bufferSize() < (buffer.length / 4) && buffer.length > initalCapacity) decreaseBuffer();
		passDataToGUI(removed);
		return removed;
	}
	
	public String peek() {
		if (bufferSize() == 0 ) return null;
		return buffer[front];
	}
	
	private void enlargeBuffer() {
		int size = bufferSize();
		buffer = traverseBuffer(new String[buffer.length * 2]);
		front = 0;
		back = size;
	};
	
	private void decreaseBuffer() {
		int size = bufferSize();
		buffer = traverseBuffer(new String[Math.max(buffer.length / 2, initalCapacity)]);
		front = 0;
		back = size;
	}
	
	private String[] traverseBuffer(String[] tempArray) {
		if (front < back) {
			System.arraycopy(buffer, front, tempArray, 0, bufferSize());
		} else {
			System.arraycopy(buffer, front, tempArray, 0, buffer.length - front);
			System.arraycopy(buffer, 0,	 tempArray, buffer.length - front, back);
		}
		return tempArray;
	}
	
	private int bufferSize() {
		if (front < back) {
			return back - front;
		} else {
			return back + buffer.length - front;
		}
	}
	
	private void passDataToGUI(String data) {
		Engine.getInstance().setBufferDataToGUI(front, back, data, bufferSize(), buffer);		
	}
}
