/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 23, 2012
 */
package edu.cmu.sv.arinc838.validation;

public class XmlFormatter {

/**
	 * Returns a string with special XML characters escaped:
	 * <ol>
	 * <li>'<' becomes {@literal '&lt'}</li>
	 * <li>'>' becomes {@literal '&gt'}</li>
	 * <li>'&' becomes {@literal '&amp'}</li>
	 * </ol>
	 * @param value
	 * @return
	 */
	public static String escapeXmlSpecialChars(String value) {

		String unescaped = value;
		// if any chars are are already escaped, unescape them first
		// this will handle strings that are partially escaped
		try {
			new DataValidator().checkForEscapedXMLChars(value);
		} catch (IllegalArgumentException e) {
			unescaped = unescapeXmlSpecialChars(value);
		}
		
		return unescaped.replaceAll("&", "&amp").replaceAll(">", "&gt")
				.replaceAll("<", "&lt");
	}

/**
	 * Returns a string with escaped special XML characters reverted to plain characters:
	 * <ol>
	 * <li>{@literal '&lt'} becomes '<'</li>
	 * <li>{@literal '&gt'} becomes '>'</li>
	 * <li>{@literal '&amp'} becomes '&'</li>
	 * </ol>
	 * @param value
	 * @return
	 */
	public static String unescapeXmlSpecialChars(String value) {
		return value.replaceAll("&lt", "<").replaceAll("&gt", ">")
				.replaceAll("&amp", "&");
	}
}
