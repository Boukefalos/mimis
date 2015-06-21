package sound.util;

public class Buffer {
	protected byte[] elements;
	public int capacity;
	protected int index;
	protected int size;

	public Buffer(int capacity) {
		elements = new byte[capacity];
		this.capacity = capacity;
		index = 0;
		size = 0;
	}

	public synchronized void add(byte[] elements) {
		for (byte element : elements) {
			elements[index++ % capacity] = element;
			if (size < capacity) {
				++size;
			}
		}
	}

	public synchronized void write(byte[] elements, int offset, int length) {
		for (int i = offset; i < length; ++i) {
			this.elements[(index++ % capacity)] = elements[i];
			if (size < capacity) {
				++size;
			}
		}
	}

	public synchronized byte[] get() {
		byte[] elements = new byte[size];
		for (int i = 0; i < size; i++) {
			elements[i] = elements[((index + i) % size)];
		}
		return elements;
	}
}