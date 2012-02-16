/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 12, 2012
 */
package edu.cmu.sv.arinc838.validation;

import java.util.List;

import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;
import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public class DataValidator {

	/**
	 * Validates that the given value is an unsigned 32-bit integer. If the
	 * input is valid, the same value is returned. If the input is invalid, an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public static long validateUint32(long value) {
		if ((value >= 0) && (value <= (long) Math.pow(2, 32))) {
			return value;
		} else {
			throw new IllegalArgumentException("The value '" + value
					+ "' is not an unsigned, 32-bit integer");
		}
	}

	/**
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public static String validateStr64k(String value) {
		if (value == null) {
			throw new IllegalArgumentException("The input value cannot be null");
		} else if (value.length() > 65535) {
			throw new IllegalArgumentException("The input value length of "
					+ value.length()
					+ " exceeds the maximum allowed characters of 65535");
		} else {
			return checkForEscapedXMLChars(value);
		}
	}

	/**
	 * Validates that the list has a least 1 element
	 * 
	 * @param value
	 * @return
	 */
	public static List<?> validateList1(List<?> value) {

		if (value == null) {
			throw new IllegalArgumentException("A LIST1 cannot be null");
		}

		if (value.size() < 1) {
			throw new IllegalArgumentException("A LIST1 must have a size >= 1.");

		}
		return value;
	}

	private static String checkForEscapedXMLChars(String value) {
		int idx = value.indexOf('<');
		if (idx != -1) {
			throw new IllegalArgumentException(
					"Found unescaped '<' char at index " + idx);
		}
		idx = value.indexOf('>');
		if (idx != -1) {
			throw new IllegalArgumentException(
					"Found unescaped '>' char at index " + idx);
		}

		idx = value.indexOf('&');

		if (idx != -1 && !value.matches(".*((&amp)|(&lt)|(&gt)).*")) {
			throw new IllegalArgumentException(
					"Found unescaped '&' char at index " + idx);
		}

		return value;
	}

	/**
	 * Validates the file format version value
	 * 
	 * @param version
	 * @return
	 */
	public static long validateFileFormatVersion(long version) {

		if (version != SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION) {
			throw new IllegalArgumentException(
					"File format version was set to "
							+ version
							+ ", expected "
							+ SoftwareDefinitionFileBuilder.DEFAULT_FILE_FORMAT_VERSION);
		}
		return version;
	}

	/**
	 * Validates the integrity type represents the CRC16, 32, or 64.
	 * 
	 * @param type
	 * @return
	 */
	public static long validateIntegrityType(long type) {
		if (IntegrityType.fromLong(type) == null) {
			throw new IllegalArgumentException(
					"Integrity type was invalid. Got " + type + ", expected "
							+ IntegrityType.asString());
		}
		return type;
	}

	/**
	 * Validates that the integrity value is a valid hexadecimal number that is
	 * either 4, 6, or 10 digits long (not including the '0x' prefix)
	 * 
	 * @param value
	 * @return
	 */
	public static String validateIntegrityValue(String value) {
		if (value == null) {
			throw new IllegalArgumentException("Integrity value cannot be null");
		}

		int size = value.length();

		if (size != 6 && size != 8 && size != 12) {
			throw new IllegalArgumentException(
					"Incorrect number of characters for integrity value. Got "
							+ size + ", expcted 4, 6, or 10");

		}

		if (!value.matches("0x.*")) {
			throw new IllegalArgumentException(
					"Integrity value not prefixed with 0x");
		}

		if (value.substring(2).matches(".*[^0-9&&[^a-fA-F]].*")) {
			throw new IllegalArgumentException(
					"Invalid characters found in integrity value! Got " + value
							+ ", expected 0-9, or A-F");
		}

		return value;
	}
}
