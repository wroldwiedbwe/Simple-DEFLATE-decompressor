/* 
 * Simple DEFLATE decompressor
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/simple-deflate-decompressor
 * https://github.com/nayuki/Simple-DEFLATE-decompressor
 */

import java.io.EOFException;
import java.io.IOException;


/**
 * A bit input stream based on an ASCII string of 0s and 1s, useful for test vectors.
 * <p>Note that for {@code readByte()}, whole bytes are serialized unintuitively in reverse order.</p>
 */
final class StringBitInputStream implements BitInputStream {
	
	/*---- Fields ----*/
	
	private final String data;
	
	private int index;
	
	
	
	/*---- Constructor ----*/
	
	public StringBitInputStream(String str) {
		if (!str.matches("[01]*"))
			throw new IllegalArgumentException("Invalid string format");
		while (str.length() % 8 != 0)
			str += "0";  // Pad with '0' bits until a byte boundary
		data = str;
		index = 0;
	}
	
	
	
	/*---- Methods ----*/
	
	public int getBitPosition() {
		return index % 8;
	}
	
	
	public int readByte() throws IOException {
		index = (index + 7) / 8 * 8;
		if (data.length() - index < 8)
			return -1;
		
		int result = 0;
		for (int i = 0; i < 8; i++)
			result |= readNoEof() << i;
		return result;
	}
	
	
	public int read() {
		if (index >= data.length())
			return -1;
		else {
			int result = data.charAt(index) - '0';
			index++;
			return result;
		}
	}
	
	
	public int readNoEof() throws IOException {
		int result = read();
		if (result != -1)
			return result;
		else
			throw new EOFException();
	}
	
	
	public void close() {
		index = data.length();
	}
	
}
