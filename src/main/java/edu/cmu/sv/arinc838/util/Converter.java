package edu.cmu.sv.arinc838.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class Converter {
	/**
	 * Converts the hexadecimal input string into the binary, byte-array
	 * equivalent. The input string should not include the common "0x" prefix.
	 * 
	 * @param hexString
	 *            The hex string to be converted.
	 * @return The binary, byte-array representation.
	 */
	public static byte[] hexToBytes(String hexString) {
		return new HexBinaryAdapter().unmarshal(hexString);
	}

	/**
	 * Converts the byte array into a hexadecimal string representation. The
	 * output string will not include the common "0x" prefix.
	 * 
	 * @param bytes
	 *            The byte array to be converted
	 * @return The hexadecimal string representation
	 */
	public static String bytesToHex(byte[] bytes) {
		return new HexBinaryAdapter().marshal(bytes);
	}

	public static byte[] intToBytes(int i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);		
		return bb.array();
	}

	public static int bytesToInt(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		return bb.getInt();
	}
}