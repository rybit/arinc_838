package edu.cmu.sv.arinc838.util;

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
}
