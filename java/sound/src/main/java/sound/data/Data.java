package sound.data;

public class Data {
	protected byte[] bytes;
	protected int length;

	public Data(byte[] bytes) {
		this(bytes, bytes.length);
	}

	public Data(byte[] bytes, int length) {
		this.bytes = bytes;
		this.length = length;
	}

	public byte[] get() {
		return this.bytes;
	}

	public int length() {
		return this.length;
	}
}