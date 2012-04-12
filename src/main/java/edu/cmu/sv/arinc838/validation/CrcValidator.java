/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Apr 11, 2012
 */
package edu.cmu.sv.arinc838.validation;

import edu.cmu.sv.arinc838.crc.Crc16Generator;
import edu.cmu.sv.arinc838.crc.Crc32Generator;
import edu.cmu.sv.arinc838.crc.Crc64Generator;

public class CrcValidator {

	public static class ChecksumValidationException extends RuntimeException {
		public ChecksumValidationException(String message) {
			super(message);
		}
	}

	public static int validateCrc16(int crcValue, byte[] data) {
		if (data == null) {
			throw new IllegalArgumentException("Data parameter cannot be null");
		}

		int crcActual = Crc16Generator.calculateCrc(data);
		if (crcActual != crcValue) {
			throw new ChecksumValidationException(
					"CRC16 check failed! Expected 0x"
							+ new String(Integer.toHexString(crcValue))
									.toUpperCase()
							+ " but calcuated 0x"
							+ new String(Integer.toHexString(crcActual))
									.toUpperCase());
		}
		
		return crcActual;
	}

	public static long validateCrc32(long crcValue, byte[] data) {
		if (data == null) {
			throw new IllegalArgumentException("Data parameter cannot be null");
		}

		long crcActual = Crc32Generator.calculateCrc(data);
		if (crcActual != crcValue) {
			throw new ChecksumValidationException(
					"CRC32 check failed! Expected 0x"
							+ new String(Long.toHexString(crcValue))
									.toUpperCase()
							+ " but calcuated 0x"
							+ new String(Long.toHexString(crcActual))
									.toUpperCase());
		}
		
		return crcActual;
	}

	public static long validateCrc64(long crcValue, byte[] data) {
		if (data == null) {
			throw new IllegalArgumentException("Data parameter cannot be null");
		}

		long crcActual = Crc64Generator.calculateCrc(data);
		if (crcActual != crcValue) {
			throw new ChecksumValidationException(
					"CRC64 check failed! Expected 0x"
							+ new String(Long.toHexString(crcValue))
									.toUpperCase()
							+ " but calcuated 0x"
							+ new String(Long.toHexString(crcActual))
									.toUpperCase());
		}
		
		return crcActual;
	}

}
