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
import edu.cmu.sv.arinc838.builder.SoftwareDescriptionBuilder;

public class DataValidator {

	public static final long STR64K_MAX_LENGTH = 65535;

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
	public static String validateStr64kBinary(String value) {
		if (value == null) {
			throw new IllegalArgumentException("The input value cannot be null");
		} else if (value.length() > STR64K_MAX_LENGTH) {
			throw new IllegalArgumentException("The input value length of "
					+ value.length()
					+ " exceeds the maximum allowed characters of 65535");
		}

		return value;
	}

	/**
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public static String validateStr64kXml(String value) {
		return checkForEscapedXMLChars(validateStr64kBinary(value));
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

		if (!value.matches("0x.*")) {
			throw new IllegalArgumentException(
					"Integrity value not prefixed with 0x");
		}

		if (size != 6 && size != 10 && size != 18) {
			throw new IllegalArgumentException(
					"Incorrect number of characters for integrity value. Got "
							+ size + ", expected 6, 10, or 18");

		}

		if (value.substring(2).matches(".*[\\D&&[^a-fA-F]].*")) {
			throw new IllegalArgumentException(
					"Invalid characters found in integrity value! Got " + value
							+ ", expected 0-9, or A-F");
		}

		return value;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String validateSoftwarePartNumber(String value) {
		if (value == null) {
			throw new IllegalArgumentException(
					"Software part number cannot be null");
		}

		// Just check the basic format first: MMMCC-SSSS-SSSS
		if (!value.matches("\\w{5}-\\w{4}-\\w{4}")) {
			throw new IllegalArgumentException(
					"Software part number format was invalid. Got "
							+ value
							+ ", expected format to be "
							+ SoftwareDescriptionBuilder.SOFTWARE_PART_NUMBER_FORMAT);
		}

		checkForIllegalCharsInPartNumber(value);

		validateCheckCharacters(value);

		return value;
	}

	/**
	 * @param value
	 * @return
	 */
	public static String generateSoftwarePartNumber(String value) {
		if (value == null) {
			throw new IllegalArgumentException(
					"Software part number cannot be null");
		}

		checkForIllegalCharsInPartNumber(value);

		String check = generateCheckCharacters(value);
		String fullPart = value.substring(0, 3) + check + value.substring(5);

		return validateSoftwarePartNumber(fullPart);
	}

	private static String checkForIllegalCharsInPartNumber(String value) {
		// check for illegal characters in the SSSS-SSSS (part number) section
		if (value.matches("\\w{5}-.*[iIoOqQzZ].*")) {
			throw new IllegalArgumentException(
					"Software part number contains illegal characters I, O, Q, or Z. Got "
							+ value);
		}

		return value;
	}

	private static String validateCheckCharacters(String partNumber) {
		String check = partNumber.substring(3, 5);

		String checked = generateCheckCharacters(partNumber);
		if (!check.equals(checked)) {
			throw new IllegalArgumentException(
					"Software part number check characters did not validate. Got "
							+ checked + ", expected " + check);
		}
		return check;
	}

	/**
	 * <pre>
	 * Step 1: Establish the characters for the PN before the check characters are known:
	 * ACM??-1234-5678 (?? denoting unresolved CC values, not included in the
	 * calculation)
	 * Step 2: Exclude delimiters and the unresolved CC values, resulting in: ACM12345678
	 * Step 3: Convert the ASCII characters to binary
	 * Step 4: XOR all the binary characters
	 * Step 5: Express the resulting value in upper case hexadecimal characters:
	 * 0x47 => “47”
	 * Step 6: Construct the final PN, including delimiters:
	 * ACM47-1234-5678
	 * </pre>
	 * 
	 */
	private static String generateCheckCharacters(String partNumber) {
		String data = partNumber.substring(0, 3) + partNumber.substring(6, 10)
				+ partNumber.substring(11);

		int result = 0;
		for (char c : data.toCharArray()) {
			result = result ^ c;
		}

		return Integer.toHexString(result).toUpperCase();
	}
}
