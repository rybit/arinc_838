package edu.cmu.sv.arinc838.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;

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

	public static long checksumBytesToLong(IntegrityDefinitionDao integDef) {
		return checksumBytesToLong(integDef.getIntegrityValue(),
				integDef.getIntegrityType());
	}

	public static long checksumBytesToLong(byte[] crcValue, long type) {
		long value = new BigInteger(crcValue).longValue();

		switch (IntegrityType.fromLong(type)) {
		case CRC16:
			value &= 0xFFFFL;
			break;
		case CRC32:
			value &= 0xFFFFFFFFL;
			break;
		case CRC64:
			break;
		default:
			throw new IllegalArgumentException("Unknown CRC type: " + type);

		}

		return value;
	}

	public static byte[] longToBytes(long i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt((int) i);
		return bb.array();

	}

	public static long bytesToLong(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		return bb.getInt();
	}
}