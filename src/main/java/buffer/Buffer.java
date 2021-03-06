package buffer;

import main.Engine;

// Array circular queue

public class Buffer {
	private String[] buffer;
	private int front;
	private int back;
	private final int initalCapacity;
	private double loadFactor;
	
	public Buffer(int capacity) {
		buffer = new String[this.initalCapacity = capacity];
		back = 0;
		front = 0;
		loadFactor = 0.8;
	}
	
	public void add(String input) {
		buffer[back++] = input;
		if (bufferSize() >= buffer.length * loadFactor) changeBuffer(true);;
		if (back == buffer.length) back = 0;
		Engine.getInstance().setBufferDataToGUI(front, back, input, bufferSize(), buffer, true, loadFactor);
	}
	
	public String get() {
		if (bufferSize() == 0) return null;
		String removed = buffer[front];
		buffer[front++] = null;
		if (front == buffer.length) front = 0;
		if (bufferSize() < buffer.length / 2 && buffer.length > initalCapacity) changeBuffer(false);
		Engine.getInstance().setBufferDataToGUI(front, back, removed, bufferSize(), buffer, false, loadFactor);
		return removed;
	}
	
	public String peek() {
		if (bufferSize() == 0 ) return null;
		return buffer[front];
	}

	private void changeBuffer(boolean enlarge) {
		int size = bufferSize();
		if (enlarge) {
			buffer = traverseBuffer(new String[buffer.length * 2]);
		} else {
			buffer = traverseBuffer(new String[Math.max((int)(buffer.length / 2), initalCapacity)]);
		}
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
}
