/*
 * Copyright (c) 2012 Chris Ellison, Mike Deats, Liron Yahdav, Ryan Neal,
 * Brandon Sutherlin, Scott Griffin
 * 
 * This software is released under the MIT license
 * (http://www.opensource.org/licenses/mit-license.php)
 * 
 * Created on Feb 28, 2012
 */
package edu.cmu.sv.arinc838.writer;

import edu.cmu.sv.arinc838.builder.SoftwareDefinitionFileBuilder;

public class BdfWriter implements SdfWriter {

	@Override
	public String write(String filename, SoftwareDefinitionFileBuilder builder)
			throws Exception {
		throw new UnsupportedOperationException(
				"This feature is not yet implemented");
	}
}
