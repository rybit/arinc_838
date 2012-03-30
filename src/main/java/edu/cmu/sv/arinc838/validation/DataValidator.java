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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.cmu.sv.arinc838.dao.FileDefinitionDao;
import edu.cmu.sv.arinc838.dao.IntegrityDefinitionDao.IntegrityType;
import edu.cmu.sv.arinc838.dao.SoftwareDefinitionFileDao;
import edu.cmu.sv.arinc838.dao.SoftwareDescriptionDao;

/**
 * <p>
 * This class encapsulates the low-level validation of the different types
 * required to build the XDF and BDF files. The methods use the paradigm of
 * taking in value to validate, and then returning the value unchanged if the
 * validation passes or throwing an {@link IllegalArgumentException} if the
 * validation fails.
 * </p>
 * <p>
 * The reason for this design as opposed to returning a true/false is to allow
 * using this method in-line to validate the values without the need for an
 * "if/else" check. For example:
 * </p>
 * 
 * <pre>
 * public void setFileSize(long fileSize) {
 * 	this.fileSize = DataValidator.validateUint32(fileSize);
 * }
 * </pre>
 * 
 * vs.
 * 
 * <pre>
 * public void setFileSize(long fileSize) {
 *   if(DataValidator.validateUint32(fileSize)) {
 *     this.fileSize = fileSize
 *   } 
 *   else {
 *     throw new IllegalArgumentException("Some error message");
 *   }
 * }
 * </pre>
 * 
 * @author Mike Deats
 * 
 * 
 */
public class DataValidator {

	/**
	 * The maximum length of a STR64k. Value is {@value}
	 */
	public static final int STR64K_MAX_LENGTH = 65535;

	/**
	 * The maximum length (in bytes) of a HEXBIN64k. Value is {@value}
	 */
	public static final int HEXBIN64K_MAX_LENGTH = 32768;

	public static final String[] INVALID_DATA_FILE_EXTENSIONS = { "bdf", "crc",
			"dir", "hdr", "ldr", "lci", "lcl", "lcs", "lna", "lnd", "lnl",
			"lno", "lnr", "lns", "lub", "luh", "lui", "lum", "lur", "lus",
			"xdf" };

	/**
	 * Validates that the given value is an unsigned 32-bit integer.
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public long validateUint32(long value) {
		if ((value >= 0) && (value <= (long) Math.pow(2, 32))) {
			return value;
		} else {
			throw new IllegalArgumentException("The value '" + value
					+ "' is not an unsigned, 32-bit integer");
		}
	}

	/**
	 * Validates that the input is a STR64k for use in the binary. This is
	 * defined as a string that is a maximum of {@link STR64K_MAX_LENGTH}
	 * characters.
	 * 
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public String validateStr64kBinary(String value) {
		if (value == null) {
			throw new IllegalArgumentException("The input value cannot be null");
		}

		String checked = XmlFormatter
				.unescapeXmlSpecialChars(checkForEscapedXMLChars(value));

		if (checked.length() > STR64K_MAX_LENGTH) {
			throw new IllegalArgumentException("The input value length of "
					+ checked.length()
					+ " exceeds the maximum allowed characters of 65535");
		}

		return value;
	}

	/**
	 * Validates that the input is a STR64k for use in the XML. This is defined
	 * as a string that
	 * <ul>
	 * <li>Has all <, >, and & characters escaped as &lt, &gt, and &amp</li>
	 * <li>Is a maximum of {@link STR64K_MAX_LENGTH} characters (not including
	 * escape characters)</li>
	 * </ul>
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 * 
	 */
	public String validateStr64kXml(String value) {
		return checkForEscapedXMLChars(validateStr64kBinary(value));
	}

	/**
	 * Validates that the list has a least 1 element
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public List<?> validateList1(List<?> value) {

		if (value == null) {
			throw new IllegalArgumentException("A LIST1 cannot be null");
		}

		if (value.size() < 1) {
			throw new IllegalArgumentException("A LIST1 must have a size >= 1.");

		}
		return value;
	}

	public String checkForEscapedXMLChars(String value) {
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
	 * Validates the file format version value. Must be a byte[4], and have the
	 * value of {@link SoftwareDefinitionFileDao#DEFAULT_FILE_FORMAT_VERSION}
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public byte[] validateFileFormatVersion(byte[] version) {
		if (!Arrays.equals(version,
				SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION)) {
			throw new IllegalArgumentException(
					"File format version was set to "
							+ version
							+ ", expected "
							+ SoftwareDefinitionFileDao.DEFAULT_FILE_FORMAT_VERSION);
		}
		return version;
	}

	/**
	 * Validates the integrity type represents the CRC16, 32, or 64.
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public long validateIntegrityType(long type) {
		if (IntegrityType.fromLong(type) == null) {
			throw new IllegalArgumentException(
					"Integrity type was invalid. Got " + type + ", expected "
							+ IntegrityType.asString());
		}
		return type;
	}

	/**
	 * Validates that the integrity value is a valid byte array that is either
	 * 2, 4, or 8 bytes long
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public byte[] validateIntegrityValue(byte[] value) {
		if (value == null) {
			throw new IllegalArgumentException("Integrity value cannot be null");
		}

		int size = value.length;

		if (size != 2 && size != 4 && size != 8) {
			throw new IllegalArgumentException(
					"Incorrect number of bytes for integrity value. Got "
							+ size + ", expected 2, 4, or 8");

		}

		return value;
	}

	/**
	 * Validates that the input is a valid software part number. The format is
	 * MMMCC-SSSS-SSSS where
	 * <ul>
	 * <li>MMM = The manufacturer code</li>
	 * <li>CC = The check characters, calculated by XORing the binary
	 * representation of the other characters, not including the - delimiters</li>
	 * <li>SSSS-SSSS = The part number. Valid characters are any alphanumeric
	 * character except I, O, Q, and Z.</li>
	 * </ul>
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public String validateSoftwarePartNumber(String value) {
		if (value == null) {
			throw new IllegalArgumentException(
					"Software part number cannot be null");
		}

		// Just check the basic format first: MMMCC-SSSS-SSSS
		if (!value.matches("[A-Z0-9]{5}-[A-Z0-9]{4}-[A-Z0-9]{4}")) {
			throw new IllegalArgumentException(
					"Software part number format was invalid. Got "
							+ value
							+ ", expected format to be "
							+ SoftwareDescriptionDao.SOFTWARE_PART_NUMBER_FORMAT);
		}

		checkForIllegalCharsInPartNumber(value);

		validateCheckCharacters(value);

		return value;
	}

	/**
	 * <p>
	 * Takes a syntactically valid (put partial) software part number string and
	 * returns a fully valid software part number including the calculated check
	 * characters. The check characters must replaced with place holder
	 * characters, otherwise an IllegalArgumentException will be thrown. For
	 * example, the string ABC??-1234-5678 is a valid input but the string
	 * ABC-1234-5678 is not.
	 * </p>
	 * See {@link #validateSoftwarePartNumber(String)} for more information on a
	 * valid software part number string.
	 * 
	 * @param value
	 *            The input value
	 * @return The validated input value
	 * @throws IllegalArgumentException
	 *             if the input value does not validate.
	 */
	public String generateSoftwarePartNumber(String value) {
		if (value == null) {
			throw new IllegalArgumentException(
					"Software part number cannot be null");
		}

		checkForIllegalCharsInPartNumber(value);

		String check = generateCheckCharacters(value);
		String fullPart = value.substring(0, 3) + check + value.substring(5);

		return validateSoftwarePartNumber(fullPart);
	}

	private String checkForIllegalCharsInPartNumber(String value) {
		// check for illegal characters in the SSSS-SSSS (part number) section
		if (value.matches("\\w{5}-.*[iIoOqQzZ].*")) {
			throw new IllegalArgumentException(
					"Software part number contains illegal characters I, O, Q, or Z. Got "
							+ value);
		}

		return value;
	}

	private String validateCheckCharacters(String partNumber) {
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
	private String generateCheckCharacters(String partNumber) {
		String data = partNumber.substring(0, 3) + partNumber.substring(6, 10)
				+ partNumber.substring(11);

		int result = 0;
		for (char c : data.toCharArray()) {
			result = result ^ c;
		}

		return Integer.toHexString(result).toUpperCase();
	}

	public byte[] validateHexbin32(byte[] value) {
		if (value == null) {
			throw new IllegalArgumentException("Hexbin 32 type cannot be null");
		} else if (value.length != 4) {
			throw new IllegalArgumentException("Hexbin 32 type must be 4 bytes");
		}
		return value;
	}

	public byte[] validateHexbin64k(byte[] value) {
		if (value == null) {
			throw new IllegalArgumentException("Hexbin 64k type cannot be null");
		} else if (value.length > HEXBIN64K_MAX_LENGTH) {
			throw new IllegalArgumentException("Hexbin 64k type must be =< "
					+ HEXBIN64K_MAX_LENGTH + " bytes");
		}
		return value;
	}

	public List<Exception> validateDataFileName(String fileName) {
		ArrayList<Exception> errors = new ArrayList<Exception>();
		if (fileName == null) {
			errors.add(new IllegalArgumentException("File name cannot be null"));
			return errors;
		} else if (fileName.length() > 255) {
			errors.add(new IllegalArgumentException(
					"File name must be <= 255 characters. The length is "
							+ fileName.length()));
		}

		// check extension
		String name = fileName.substring(0, fileName.lastIndexOf("."));
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (Arrays.asList(INVALID_DATA_FILE_EXTENSIONS).contains(
				extension.toLowerCase())) {
			errors.add(new IllegalArgumentException(
					"File name contained an illegal extension '" + extension
							+ "'"));
		}

		// check file names
		if (name.matches(".*[\"'`*<>:;#?/\\\\|~!@$%^&+=,\\s]+.*")) {
			errors.add(new IllegalArgumentException(
					"File name contained an illegal character"));
		}

		return errors;
	}

	public List<Exception> validateDataFileNamesAreUnique(
			List<FileDefinitionDao> fileDefs) {
		ArrayList<Exception> errors = new ArrayList<Exception>();
		ArrayList<String> fileNames = new ArrayList<String>();
		for (FileDefinitionDao fileDef : fileDefs) {
			fileNames.add(fileDef.getFileName());
		}

		Iterator<String> iter = fileNames.iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			iter.remove();
			if (fileNames.contains(name)) {
				errors.add(new IllegalArgumentException(
						"Duplicate data file name '" + name + "'"));
			}
		}

		return errors;
	}
}
