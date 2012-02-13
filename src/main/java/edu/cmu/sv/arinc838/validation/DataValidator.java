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
}
