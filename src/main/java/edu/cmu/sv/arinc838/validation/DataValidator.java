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

import java.util.Arrays;
import java.util.List;

import edu.cmu.sv.arinc838.builder.IntegrityDefinitionBuilder.IntegrityType;
import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;
import edu.cmu.sv.arinc838.builder.SoftwareDescriptionBuilder;

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

		if (value.substring(2).matches(".*[\\D&&[^a-fA-F]].*")) {
			throw new IllegalArgumentException(
					"Invalid characters found in integrity value! Got " + value
							+ ", expected 0-9, or A-F");
		}

		return value;
	}

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

		valdateCheckCharacters(value);

		return value;
	}

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

	private static String valdateCheckCharacters(String partNumber) {
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
	 * Step 3: Convert the ASCII characters to hexadecimal and binary equivalent:
	 * “A” = 0x41 = 0100 0001
	 * “C” = 0x43 = 0100 0011
	 * “M” = 0x4D = 0100 1101
	 * “1” = 0x31 = 0011 0001
	 * “2” = 0x32 = 0011 0010
	 * “3” = 0x33 = 0011 0011
	 * “4” = 0x34 = 0011 0100
	 * “5” = 0x35 = 0011 0101
	 * “6” = 0x36 = 0011 0110
	 * “7” = 0x37 = 0011 0111
	 * “8” = 0x38 = 0011 1000
	 * Step 4: Add the binary equivalent characters using mod 2 addition rules (0+0=0, 0+1=1,
	 * 1+0=1, 1+1=0, No carry):
	 * sum = 0100 0111
	 * Step 5: Express the resulting value in upper case hexadecimal characters:
	 * 0x47 => “47”
	 * Step 6: Construct the final PN, including delimiters:
	 * ACM47-1234-5678
	 * </pre>
	 */
	private static String generateCheckCharacters(String partNumber) {
		String data = partNumber.substring(0, 3) + partNumber.substring(6, 10)
				+ partNumber.substring(11);

		int[] result = new int[8];
		Arrays.fill(result, -1);
		for (char c : data.toCharArray()) {
			String binary = Integer.toBinaryString(c);
			// pad with 0s to fill all 8 bits
			for (int i = 0; i <= 8 - binary.length(); i++) {
				binary = "0" + binary;
			}

			char[] charArr = binary.toCharArray();
			for (int i = 0; i < binary.length(); i++) {

				int b = Integer.valueOf(charArr[i] + "");
				int r = result[i];
				if (r == -1) {
					result[i] = b;
				} else {
					result[i] = (r + b) % 2;
				}
			}
		}
		int checked = 0;
		for (int i = 0; i < result.length; i++) {
			checked |= result[i] << (result.length - 1 - i);
		}

		return Integer.toHexString(checked);
	}
}
